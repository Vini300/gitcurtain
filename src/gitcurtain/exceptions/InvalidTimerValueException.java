package gitcurtain.exceptions;

/**
 * Uma exce��o que representa um valor inv�lido para o timer do ExtractionThread.
 * 
 * @author Vin�cius Soares
 *
 */
@SuppressWarnings("serial")
public class InvalidTimerValueException extends Exception {
	/**
	 * Cria uma inst�ncia da exce��o.
	 * 
	 * @param message A mensagem de erro que deve ser lan�ada ao usu�rio do sistema caso a exce��o seja lan�ada.
	 */
	public InvalidTimerValueException(String message) {
		super(message);
	}
}
