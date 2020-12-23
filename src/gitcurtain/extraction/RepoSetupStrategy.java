package gitcurtain.extraction;

/**
 * Uma interface que define como deve ser criada uma estratégia de preparação de metadados de um repositório Git que será analisado durante a
 * execução do GitCURTAIN. Para criar uma extensão customizada do GitCURTAIN que obtenha e prepare estes metadados de outra forma, basta criar
 * uma nova classe implementando esta interface.
 * 
 * @author Vinícius Soares
 *
 */
public interface RepoSetupStrategy {
	
	/**
	 * Uma função que deve ser implementada pela estratégia de preparação de metadados de um repositório. Esta função retorna a <i>path</i>,
	 * ou seja, o caminho no sistema de arquivos onde o repositório está ou será armazenado.
	 * 
	 * @return Uma String com o <i>path</i> do repositório.
	 */
	public String getRepositoryPath();
	
	/**
	 * Uma função que deve ser implementada pela estratégia de preparação de metadados de um repositório. Esta função retorna a URI, ou seja,
	 * o link especificado pelo Git, para que seja executado um <i>clone</i> no repositório.
	 * 
	 * @return Uma String com a URI do repositório.
	 */
	public String getRepositoryURI();
	
	/**
	 * Uma função que deve ser implementada pela estratégia de preparação de metadados de um repositório. Esta função retorna o Token de
	 * identificação do usuário Git que fará a extração dos dados. Isto é necessário para que seja feita a extração de repositórios que
	 * necessitam de autenticação.
	 * 
	 * @return Uma String com o Token de identificação do usuário Git.
	 */
	public String getToken();

	/**
	 * Uma função que deve ser implementada pela estratégia de preparação de metadados de um repositório. Esta função retorna o nome da
	 * branch deste repositório Git na qual será feita a análise dos dados de commit.
	 * 
	 * @return Uma String com o nome da branch do repositório Git.
	 */
	public String getBranch();

}