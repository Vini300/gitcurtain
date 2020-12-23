package gitcurtain.exceptions;

/**
 * Uma exce��o que representa um acesso mal-sucedido aos dados de uma inst�ncia da classe Commit por causa de um valor de Enum que n�o
 * corresponde a nenhum dado contido por aquele Commit.
 * 
 * @author Vin�cius Soares
 *
 */
@SuppressWarnings("serial")
public class InvalidEnumIDException extends Exception {

	/**
	 * Cria uma inst�ncia da exce��o.
	 * 
	 * @param message A mensagem de erro que deve ser lan�ada ao usu�rio do sistema caso a exce��o seja lan�ada.
	 */
	public InvalidEnumIDException(String message) {
		super(message);
	}
}
