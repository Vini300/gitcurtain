package gitcurtain.exceptions;

/**
 * Uma exce��o que representa uma tentativa de trabalhar com o GitCURTAIN utilizando uma branch inexistente do reposit�rio.
 * 
 * @author Vin�cius Soares
 *
 */
@SuppressWarnings("serial")
public class InvalidBranchException extends Exception {

	/**
	 * Cria uma inst�ncia da exce��o.
	 * 
	 * @param message A mensagem de erro que deve ser lan�ada ao usu�rio do sistema caso a exce��o seja lan�ada.
	 */
	public InvalidBranchException(String message) {
		super(message);
	}
}
