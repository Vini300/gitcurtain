package gitcurtain.exceptions;

/**
 * Uma exceção que representa um valor inválido para o timer do ExtractionThread.
 * 
 * @author Vinícius Soares
 *
 */
@SuppressWarnings("serial")
public class InvalidTimerValueException extends Exception {
	/**
	 * Cria uma instância da exceção.
	 * 
	 * @param message A mensagem de erro que deve ser lançada ao usuário do sistema caso a exceção seja lançada.
	 */
	public InvalidTimerValueException(String message) {
		super(message);
	}
}
