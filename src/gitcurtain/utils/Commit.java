package gitcurtain.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import gitcurtain.exceptions.InvalidEnumIDException;

import java.io.Serializable;

/**
 * Uma classe que representa um commit no GitCURTAIN. � uma classe utilit�ria que pode, e deve, ser utilizada por extens�es customizadas do
 * GitCURTAIN.
 * 
 * @author Vin�cius Soares
 *
 */
@SuppressWarnings("serial")
public class Commit implements Serializable {
    /**
     * A mensagem do commit.
     */
    private String message;
    /**
     * O hash identificador do commit.
     */
    private String hash;
    /**
     * O autor do commit (i.e., a pessoa que fez as mudan�as).
     */
    private String author;
    /**
     * A pessoa que executou o commit (i.e., a pessoa que enviou as mudan�as ao reposit�rio).
     */
    private String committer;
    /**
     * A data em que o commit foi executado (i.e., a data em que as mudan�as foram enviadas ao reposit�rio).
     */
    private Date commitDate;
    /**
     * A data de autoria do commit (i.e., a data em que as mudan�as foram feitas).
     */
    private Date authoringDate;
    /**
     * A lista de arquivos que foram modificados pelo commit.
     */
    private ArrayList<String> modifiedFiles;

    /**
     * Cria um novo commit com os dados especificados. Este construtor � gerenciado pelo GitCURTAIN e, portanto, n�o deve ser invocado pelo
     * sistema.
     * 
     * @param message A mensagem do commit.
     * @param hash O hash identificador do commit.
     * @param author O autor do commit (i.e., a pessoa que fez as mudan�as).
     * @param committer A pessoa que executou o commit (i.e., a pessoa que enviou as mudan�as ao reposit�rio).
     * @param commitDate A data em que o commit foi executado (i.e., a data em que as mudan�as foram enviadas ao reposit�rio).
     * @param authoringDate A data de autoria do commit (i.e., a data em que as mudan�as foram feitas).
     * @param modifiedFiles A lista de arquivos que foram modificados pelo commit.
     */
    public Commit(String message, String hash, String author, String committer, Date commitDate, Date authoringDate, ArrayList<String> modifiedFiles) {
        this.message = message;
        this.hash = hash;
        this.author = author;
        this.committer = committer;
        this.commitDate = commitDate;
        this.authoringDate = authoringDate;
        this.modifiedFiles = modifiedFiles;
    }

    /**
     * Retorna um dado espec�fico do commit, com base no valor do Enum enviado como par�metro. O dado � retornado como Object, e deve ser
     * ent�o convertido ao tipo correto para ser tratado futuramente.
     * 
     * @param enumID O valor do Enum que corresponde ao dado que deve ser obtido do commit.
     * 
     * @return Um objeto da classe Object com o dado que corresponde ao valor do Enum.
     * 
     * @throws InvalidEnumIDException � lan�ado quando o valor do Enum n�o corresponde a nenhum dos dados contidos no commit.
     */
    public Object getVariableByEnum(CommitVariables enumID) throws InvalidEnumIDException {
        switch (enumID) {
            case MESSAGE:
                return message;
            case HASH:
                return hash;
            case AUTHOR:
                return author;
            case COMMITTER:
                return committer;
            case COMMITDATE:
                return commitDate;
            case AUTHORINGDATE:
                return authoringDate;
            case MODIFIEDFILES:
                return modifiedFiles;
            default:
                throw new InvalidEnumIDException("Variable " + enumID + " does not correlate to a Commit variable.");
        }
    }
    
    /**
     * Comparator para permitir ordena��o por data de commit.
     */
    public static Comparator<Commit> CommitComparatorByCommitDate = new Comparator<Commit>() {
    	
    	public int compare(Commit c1, Commit c2) {
    		try {
    			Date c1Date = (Date) c1.getVariableByEnum(CommitVariables.AUTHORINGDATE);
				Date c2Date = (Date) c2.getVariableByEnum(CommitVariables.AUTHORINGDATE);
				if (c1Date.after(c2Date)) {
					return 1;
				}
				else {
					return -1;
				}
			} catch (InvalidEnumIDException e) {
				System.out.println("An unexpected error ocurred while accessing commit data. Please try again later, and double check"
						+ " the Enum variables used for any access to commit data.");
				e.printStackTrace();
			}
			return 0;
    	}
    };

}