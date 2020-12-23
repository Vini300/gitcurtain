package gitcurtain.database;

import java.sql.SQLException;

/**
 * Gerencia a criação e a alocação da classe SQLiteConnection, permitindo que a mesma seja um <i>singleton</i>. Esta classe é gerenciada pelo
 * GitCURTAIN e, a princípio, não precisa ser utilizada pelo sistema.
 * 
 * @author Vinícius Soares
 *
 */
public class SQLiteConnectionManager {

    /**
     * Contém o <i>singleton</i> da conexão SQLite.
     */
    private static SQLiteConnection connection;

    /**
     * Cria, ou retorna, uma instância da classe SQLiteConnection. A criação da classe SQLiteConnection é gerenciada pelo GitCURTAIN e,
     * portanto, não é necessário que o sistema diretamente chame esta função. Para criar uma conexão com o banco de dados, a função que
     * deve ser utilizada é a setUpDatabase, da classe FileController.
     * 
     * @param filePath Uma String que contém o <i>path</i> do arquivo do banco de dados SQLite. O <i>path</i> deve conter a extensão do
     * arquivo.
     * 
     * @return Uma instância da classe SQLiteConnection.
     */
    public static SQLiteConnection getConnection(String filePath) {
    	
        if (connection == null) {
            connection = new SQLiteConnection(filePath);
            try {
				connection.prepareTable();
			} catch (SQLException e) {
				System.out.println("Error creating database.");
				e.printStackTrace();
			}
        }
        return connection;
    }

}