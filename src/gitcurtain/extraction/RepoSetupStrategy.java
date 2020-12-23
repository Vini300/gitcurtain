package gitcurtain.extraction;

/**
 * Uma interface que define como deve ser criada uma estrat�gia de prepara��o de metadados de um reposit�rio Git que ser� analisado durante a
 * execu��o do GitCURTAIN. Para criar uma extens�o customizada do GitCURTAIN que obtenha e prepare estes metadados de outra forma, basta criar
 * uma nova classe implementando esta interface.
 * 
 * @author Vin�cius Soares
 *
 */
public interface RepoSetupStrategy {
	
	/**
	 * Uma fun��o que deve ser implementada pela estrat�gia de prepara��o de metadados de um reposit�rio. Esta fun��o retorna a <i>path</i>,
	 * ou seja, o caminho no sistema de arquivos onde o reposit�rio est� ou ser� armazenado.
	 * 
	 * @return Uma String com o <i>path</i> do reposit�rio.
	 */
	public String getRepositoryPath();
	
	/**
	 * Uma fun��o que deve ser implementada pela estrat�gia de prepara��o de metadados de um reposit�rio. Esta fun��o retorna a URI, ou seja,
	 * o link especificado pelo Git, para que seja executado um <i>clone</i> no reposit�rio.
	 * 
	 * @return Uma String com a URI do reposit�rio.
	 */
	public String getRepositoryURI();
	
	/**
	 * Uma fun��o que deve ser implementada pela estrat�gia de prepara��o de metadados de um reposit�rio. Esta fun��o retorna o Token de
	 * identifica��o do usu�rio Git que far� a extra��o dos dados. Isto � necess�rio para que seja feita a extra��o de reposit�rios que
	 * necessitam de autentica��o.
	 * 
	 * @return Uma String com o Token de identifica��o do usu�rio Git.
	 */
	public String getToken();

	/**
	 * Uma fun��o que deve ser implementada pela estrat�gia de prepara��o de metadados de um reposit�rio. Esta fun��o retorna o nome da
	 * branch deste reposit�rio Git na qual ser� feita a an�lise dos dados de commit.
	 * 
	 * @return Uma String com o nome da branch do reposit�rio Git.
	 */
	public String getBranch();

}