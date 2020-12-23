package gitcurtain.database;

import java.sql.SQLException;

/**
 * Gerencia a cria��o e a aloca��o da classe SQLiteConnection, permitindo que a mesma seja um <i>singleton</i>. Esta classe � gerenciada pelo
 * GitCURTAIN e, a princ�pio, n�o precisa ser utilizada pelo sistema.
 * 
 * @author Vin�cius Soares
 *
 */
public class SQLiteConnectionManager {

    /**
     * Cont�m o <i>singleton</i> da conex�o SQLite.
     */
    private static SQLiteConnection connection;

    /**
     * Cria, ou retorna, uma inst�ncia da classe SQLiteConnection. A cria��o da classe SQLiteConnection � gerenciada pelo GitCURTAIN e,
     * portanto, n�o � necess�rio que o sistema diretamente chame esta fun��o. Para criar uma conex�o com o banco de dados, a fun��o que
     * deve ser utilizada � a setUpDatabase, da classe FileController.
     * 
     * @param filePath Uma String que cont�m o <i>path</i> do arquivo do banco de dados SQLite. O <i>path</i> deve conter a extens�o do
     * arquivo.
     * 
     * @return Uma inst�ncia da classe SQLiteConnection.
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