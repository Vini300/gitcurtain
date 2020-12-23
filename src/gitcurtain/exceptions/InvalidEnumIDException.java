package gitcurtain.exceptions;

/**
 * Uma exceção que representa um acesso mal-sucedido aos dados de uma instância da classe Commit por causa de um valor de Enum que não
 * corresponde a nenhum dado contido por aquele Commit.
 * 
 * @author Vinícius Soares
 *
 */
@SuppressWarnings("serial")
public class InvalidEnumIDException extends Exception {

	/**
	 * Cria uma instância da exceção.
	 * 
	 * @param message A mensagem de erro que deve ser lançada ao usuário do sistema caso a exceção seja lançada.
	 */
	public InvalidEnumIDException(String message) {
		super(message);
	}
}
