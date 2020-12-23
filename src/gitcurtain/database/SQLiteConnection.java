package gitcurtain.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import gitcurtain.exceptions.InvalidEnumIDException;
import gitcurtain.utils.Commit;
import gitcurtain.utils.CommitVariables;

/**
 * Representa uma conexão com o SGBD SQLite. É um <i>singleton</i> e, portanto, somente pode haver uma cópia do mesmo no sistema.
 * 
 * O GitCURTAIN gerencia internamente todas as funcionalidades do banco de dados, já abstraídas, por meio de chamadas à classe
 * FileController. Portanto, a classe SQLiteConnection não deve ser chamada diretamente em sistemas que implementam GitCURTAIN. Além
 * disso, para manter seu status de <i>singleton</i>, a classe SQLiteConnection não pode ser criada diretamente, e deve ser criada
 * pelo método getConnection do SQLiteConnectionManager.
 * 
 * @author Vinícius Soares
 *
 */
public class SQLiteConnection {

    /**
     * Guarda a conexão com o SGBD SQLite.
     */
    private Connection connection;

    /**
     * Cria um SQLiteConnection, utilizando o <i>path</i> especificado. A criação desta classe é gerenciada pelo GitCURTAIN e, portanto, não
     * é necessário que o sistema diretamente chame esta função. Para criar uma conexão com o banco de dados, a função que deve ser utilizada
     * é a setUpDatabase, da classe FileController.
     * 
     * @param filePath Uma String que contém o <i>path</i> do arquivo do banco de dados SQLite. O <i>path</i> deve conter a extensão do
     * arquivo.
     */
    public SQLiteConnection(String filePath) {
    	
        String url = "jdbc:sqlite:" + filePath;
        
        try {
            connection = DriverManager.getConnection(url);
        }
        catch (SQLException e) {
            System.out.println("An unexpected error occurred while creating the database. Please try again and check for potential"
            		+ "permission-related errors in the database file.");
            e.printStackTrace();
        }
    }
    
    /**
     * Cria a tabela no banco de dados, caso não exista ainda.
     * 
     * @throws SQLException É lançada quando ocorre algum erro durante a execução das chamadas SQL.
     */
    public void prepareTable() throws SQLException{
    	Statement statement = connection.createStatement();
    	
    	String createSQL = "CREATE TABLE IF NOT EXISTS commits (\n"
                + "    hash text PRIMARY KEY,\n"
                + "    message text,\n"
                + "    author text,\n"
                + "    committer text,\n"
                + "    commitDate text,\n"
                + "    authoringDate text,\n"
                + "    modifiedFiles text\n"
                + ");";

    	statement.execute(createSQL);
    }

    /**
     * Insere um conjunto de commits ao banco de dados. O uso desta função é gerenciado pelo GitCURTAIN e, portanto, não é necessário que o
     * sistema diretamente chame essa função. Para inserir commits ao banco de dados, a função que deve ser utilizada é a writeCommits, da
     * classe FileController. 
     * 
     * @param commitList Um ArrayList de Commits, que pode conter um número varíavel dos mesmos.
     * 
     * @throws SQLException É lançada quando ocorre algum erro durante a execução das chamadas SQL.
     * 
     */
    public void insert(ArrayList<Commit> commitList) throws SQLException {
    	
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='commits'");

        if (!results.next()) {
            prepareTable();
        }
        
        results.close();
        statement.close();

        String insertSQL = "INSERT INTO commits(hash, message, author, committer, commitDate, authoringDate, modifiedFiles)"
        		+ " VALUES(?,?,?,?,?,?,?)";
        PreparedStatement prepStatement = connection.prepareStatement(insertSQL);

        for (Commit commit : commitList) {
        	insertCommit(prepStatement, commit);
        }
        
    }

    /**
     * Insere um commit individual ao banco de dados. Função privada, usada somente pela função insert.
     * 
     * @param prepStatement A chamada SQL, já preparada para receber os argumentos do commit a ser inserido.
     * @param commit O commit que está para ser inserido no banco de dados.
     * 
     * @throws SQLException É lançada quando ocorre algum erro durante a execução das chamadas SQL. 
     *
     */
    @SuppressWarnings("unchecked")
	private void insertCommit(PreparedStatement prepStatement, Commit commit) throws SQLException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
        	prepStatement.setString(1, commit.getVariableByEnum(CommitVariables.HASH).toString());
            prepStatement.setString(2, commit.getVariableByEnum(CommitVariables.MESSAGE).toString());
            prepStatement.setString(3, commit.getVariableByEnum(CommitVariables.AUTHOR).toString());
            prepStatement.setString(4, commit.getVariableByEnum(CommitVariables.COMMITTER).toString());
            prepStatement.setString(5, format.format(commit.getVariableByEnum(CommitVariables.COMMITDATE)));
            prepStatement.setString(6, format.format(commit.getVariableByEnum(CommitVariables.AUTHORINGDATE)));
            
            String stringFileList = "";
               
            for (String file : (ArrayList<String>) commit.getVariableByEnum(CommitVariables.MODIFIEDFILES)) {
            
            	stringFileList = stringFileList.concat(file + "|");
            }
            
            stringFileList = stringFileList.substring(0, stringFileList.length()-1);
            prepStatement.setString(7, stringFileList);
            
            prepStatement.executeUpdate();
        }
        catch (InvalidEnumIDException e) {
        	System.out.println("An unexpected error ocurred while accessing commit data. Please try again later, and double check the"
        			+ " Enum variables used for any access to commit data.");
        	e.printStackTrace();
        }
    }

    /**
     * Remove um conjunto de commits do banco de dados. O uso desta função é gerenciado pelo GitCURTAIN e, portanto, não é necessário que o
     * sistema diretamente chame essa função. Para remover commits do banco de dados, a função que deve ser utilizada é a removeCommits, da
     * classe FileController. 
     * 
     * @param IDs Um ArrayList de Strings contendo hashes de commits (e.g., 29932f3915935d773dc8d52c292cadd81c81071d).
     * 
     * @return Um ArrayList de Commits contendo os elementos que foram removidos do banco de dados por meio dessa operação.
     * 
     * @throws SQLException É lançada quando ocorre algum erro durante a execução das chamadas SQL. 
     * @throws ParseException É lançada quando ocorre algum erro na conversão da String do banco de dados contendo a data de um commit de
     * volta a um objeto Date.
     */
    public ArrayList<Commit> remove(ArrayList<String> IDs) throws SQLException, ParseException {
        ArrayList<Commit> commitList = new ArrayList<Commit>();
        
        commitList.addAll(getCommitsByHashList(IDs));

        String command = "DELETE FROM commits WHERE hash = ?";
        PreparedStatement statement = connection.prepareStatement(command);
        
        for (String id : IDs) {
        	statement.setString(1, id);
        	statement.executeUpdate();
        }

        return commitList;
    }

    /**
     * Retorna uma lista de commits com base em uma lista de hashes de commits. Função privada, usada somente pela função remove. 
     * 
     * @param IDs Um ArrayList de Strings contendo hashes de commits (e.g., 29932f3915935d773dc8d52c292cadd81c81071d).
     * 
     * @return Um ArrayList de Commits contendo os elementos que correspondem aos hashes da lista.
     * 
     * @throws SQLException É lançada quando ocorre algum erro durante a execução das chamadas SQL.
     * @throws ParseException É lançada quando ocorre algum erro na conversão da String do banco de dados contendo a data de um commit de
     * volta a um objeto Date.
     */
    private ArrayList<Commit> getCommitsByHashList(ArrayList<String> IDs) throws SQLException, ParseException {
    	
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String command = "SELECT hash, message, author, commiter, commitDate, authoringDate, modifiedFiles FROM commits WHERE hash=?";
    	PreparedStatement statement = connection.prepareStatement(command);
    	ArrayList<Commit> commitList = new ArrayList<Commit>();
    	
    	for (String id : IDs) {
    		
    		statement.setString(1, id);
    		ResultSet rs = statement.executeQuery();
    		
    		while (rs.next()) {
    			
    			Date commitDate = format.parse(rs.getString("commitDate"));
    			Date authoringDate = format.parse(rs.getString("authoringDate"));
    			ArrayList<String> modifiedFiles = new ArrayList<String>();
    			modifiedFiles.addAll(Arrays.asList(rs.getString("modifiedFiles").split("|")));
    			commitList.add(new Commit(rs.getString("message"), rs.getString("hash"), rs.getString("author"), rs.getString("committer"), commitDate, authoringDate, modifiedFiles));
    		}
    		
    	}
    	
    	return commitList;
    }
    
    /**
     * @return Obtém a lista de todos os commits que estão neste momento no banco de dados. O uso desta função é gerenciado pelo GitCURTAIN
     * e, portanto, não é necessário que o sistema diretamente chame essa função. Para obter a lista de commits no banco de dados, a função
     * que deve ser utilizada é a getAllCommits, da classe FileController. 
     * 
     * @throws SQLException É lançada quando ocorre algum erro durante a execução das chamadas SQL.
     * @throws ParseException É lançada quando ocorre algum erro na conversão da String do banco de dados contendo a data de um commit de
     * volta a um objeto Date.
     */
    public ArrayList<Commit> getAllCommits() throws SQLException, ParseException {
    	
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String command = "SELECT hash, message, author, committer, commitDate, authoringDate, modifiedFiles FROM commits";
    	Statement statement = connection.createStatement();
    	ArrayList<Commit> commitList = new ArrayList<Commit>();
    		
    	ResultSet rs = statement.executeQuery(command);
    		
    	while (rs.next()) {
    			
    		Date commitDate = format.parse(rs.getString("commitDate"));
    		Date authoringDate = format.parse(rs.getString("authoringDate"));
    		ArrayList<String> modifiedFiles = new ArrayList<String>();
    		modifiedFiles.addAll(Arrays.asList(rs.getString("modifiedFiles").split("|")));
    		commitList.add(new Commit(rs.getString("message"), rs.getString("hash"), rs.getString("author"), rs.getString("committer"), commitDate, authoringDate, modifiedFiles));
    		
    	}
    	
    	return commitList;
    }
    
}