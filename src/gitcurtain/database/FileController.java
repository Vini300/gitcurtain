package gitcurtain.database;

import java.sql.SQLException;

import java.text.ParseException;

import java.util.ArrayList;

import gitcurtain.utils.Commit;

/**
 * Gerencia todo o sistema de leitura e escrita de dados, permitindo que o desenvolvedor que crie um programa por meio do GitCURTAIN
 * defina onde guardar o banco SQLite que conter� os dados extra�dos do reposit�rio.
 * 
 * A princ�pio, o GitCURTAIN gerencia todas as chamadas necess�rias ao banco de dados, n�o necessitando que a classe FileController seja
 * chamada por um sistema utilizando GitCURTAIN. Por�m, caso seja necess�rio um acesso diferenciado ao banco de dados, toda a comunica��o
 * com as classes que gerenciam o SQL diretamente deve ser feita por meio das fun��es da classe FileController.
 * 
 * @author Vin�cius Soares
 *
 */
public class FileController {
	
    /**
     * Guarda a conex�o com o banco de dados SQLite que cont�m os dados do reposit�rio.
     */
    private static SQLiteConnection connection;

    /**
     * Prepara a conex�o com o banco de dados SQLite por meio do <i>path</i> especificado. Este m�todo � automaticamente invocado pelo
     * ExtractionController durante a inicializa��o do mesmo, e n�o deve ser invocado pelo sistema.
     * 
     * @param filePath Uma String que cont�m o <i>path</i> do arquivo do banco de dados SQLite. O <i>path</i> deve conter a extens�o do
     * arquivo.
     */
    public static void setUpDatabase(String filePath) {
    	
        connection = SQLiteConnectionManager.getConnection(filePath);
    }

    /**
     * Escreve um conjunto de commits no banco de dados SQLite. Este m�todo � automaticamente gerenciado pelo GitCURTAIN, e n�o precisa ser
     * utilizado por sistemas criados por meio deste <i>framework</i>.
     * 
     * @param commits Um ArrayList de Commits, que pode conter um n�mero var�avel dos mesmos.
     */
	public static void writeCommits(ArrayList<Commit> commits) {
    	try {
    		connection.insert(commits);
    	}
    	catch (SQLException e) {
    		System.out.println("Error during SQL operation. Please try again, and check for potentially invalid commit data in"
    				+ " the repository.");
    		e.printStackTrace();
    	}
    }
    
    /**
     * Remove um conjunto de commits do banco de dados SQLite. A princ�pio, este m�todo n�o precisa ser utilizado, mas permite que sistemas
     * criados por meio do GitCURTAIN tenham a capacidade de remover elementos do banco de dados.
     * 
     * @param commitIDs Um ArrayList de Strings contendo hashes de commits (e.g., 29932f3915935d773dc8d52c292cadd81c81071d).
     * 
     * @return Um ArrayList de Commits contendo os elementos que foram removidos do banco de dados por meio dessa opera��o.
     */
    public static ArrayList<Commit> removeCommits(ArrayList<String> commitIDs) {
    	
    	boolean done = false;
    	ArrayList<Commit> removedCommits = null;
    	
    	while (!done) {
    		try {
				removedCommits = connection.remove(commitIDs);
				done = true;
			}
    		catch (SQLException e) {
    			System.out.println("Error during SQL operation. Please try again, and check if the commit IDs used correspond to"
    					+ " commits in the database.");
				e.printStackTrace();
				return null;
			}
    		catch (ParseException e) {
    			System.out.println("Unexpected error during parsing. Retrying...");
				e.printStackTrace();
			}
    	}
    	
    	return removedCommits;
    }

    /**
     * Obt�m a lista de todos os commits que est�o neste momento no banco de dados.
     * 
     * @return Um ArrayList de Commits contendo todos os elementos que atualmente est�o no banco de dados.
     */
    public static ArrayList<Commit> getAllCommits() {
    	
    	boolean done = false;
    	ArrayList<Commit> commitList = null;
    	
    	while (!done) {
    		try {
    			commitList = connection.getAllCommits();
    			done = true;
    		}
    		catch (SQLException e) {
    			System.out.println("Error during SQL operation. Please try again, and check for potential corrupted data in the database.");
    			e.printStackTrace();
    			return null;
    		}
    		catch (ParseException e) {
    			System.out.println("Unexpected error during parsing. Retrying...");
    			e.printStackTrace();
    		}
    	}
        
        return commitList;
    }
}