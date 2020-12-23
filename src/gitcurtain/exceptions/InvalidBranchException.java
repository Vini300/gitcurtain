package gitcurtain.exceptions;

/**
 * Uma exceção que representa uma tentativa de trabalhar com o GitCURTAIN utilizando uma branch inexistente do repositório.
 * 
 * @author Vinícius Soares
 *
 */
@SuppressWarnings("serial")
public class InvalidBranchException extends Exception {

	/**
	 * Cria uma instância da exceção.
	 * 
	 * @param message A mensagem de erro que deve ser lançada ao usuário do sistema caso a exceção seja lançada.
	 */
	public InvalidBranchException(String message) {
		super(message);
	}
}
