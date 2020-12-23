package gitcurtain.database;

import java.sql.SQLException;

import java.text.ParseException;

import java.util.ArrayList;

import gitcurtain.utils.Commit;

/**
 * Gerencia todo o sistema de leitura e escrita de dados, permitindo que o desenvolvedor que crie um programa por meio do GitCURTAIN
 * defina onde guardar o banco SQLite que conterá os dados extraídos do repositório.
 * 
 * A princípio, o GitCURTAIN gerencia todas as chamadas necessárias ao banco de dados, não necessitando que a classe FileController seja
 * chamada por um sistema utilizando GitCURTAIN. Porém, caso seja necessário um acesso diferenciado ao banco de dados, toda a comunicação
 * com as classes que gerenciam o SQL diretamente deve ser feita por meio das funções da classe FileController.
 * 
 * @author Vinícius Soares
 *
 */
public class FileController {
	
    /**
     * Guarda a conexão com o banco de dados SQLite que contém os dados do repositório.
     */
    private static SQLiteConnection connection;

    /**
     * Prepara a conexão com o banco de dados SQLite por meio do <i>path</i> especificado. Este método é automaticamente invocado pelo
     * ExtractionController durante a inicialização do mesmo, e não deve ser invocado pelo sistema.
     * 
     * @param filePath Uma String que contém o <i>path</i> do arquivo do banco de dados SQLite. O <i>path</i> deve conter a extensão do
     * arquivo.
     */
    public static void setUpDatabase(String filePath) {
    	
        connection = SQLiteConnectionManager.getConnection(filePath);
    }

    /**
     * Escreve um conjunto de commits no banco de dados SQLite. Este método é automaticamente gerenciado pelo GitCURTAIN, e não precisa ser
     * utilizado por sistemas criados por meio deste <i>framework</i>.
     * 
     * @param commits Um ArrayList de Commits, que pode conter um número varíavel dos mesmos.
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
     * Remove um conjunto de commits do banco de dados SQLite. A princípio, este método não precisa ser utilizado, mas permite que sistemas
     * criados por meio do GitCURTAIN tenham a capacidade de remover elementos do banco de dados.
     * 
     * @param commitIDs Um ArrayList de Strings contendo hashes de commits (e.g., 29932f3915935d773dc8d52c292cadd81c81071d).
     * 
     * @return Um ArrayList de Commits contendo os elementos que foram removidos do banco de dados por meio dessa operação.
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
     * Obtém a lista de todos os commits que estão neste momento no banco de dados.
     * 
     * @return Um ArrayList de Commits contendo todos os elementos que atualmente estão no banco de dados.
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