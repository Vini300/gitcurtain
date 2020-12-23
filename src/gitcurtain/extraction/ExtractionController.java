package gitcurtain.extraction;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import gitcurtain.database.FileController;
import gitcurtain.exceptions.InvalidBranchException;
import gitcurtain.exceptions.InvalidTimerValueException;
import gitcurtain.utils.Commit;

/**
 * Gerencia todo o sistema de extração de dados, permitindo que o desenvolvedor determine qual a forma de obtenção dos metadados necessários
 * para a extração dos dados do repositório a ser analisado.
 * 
 * Toda a comunicação com as classes que gerenciam a extração de dados diretamente deve ser feita por meio das funções da classe
 * ExtractionController.
 * 
 * @author Vinícius Soares
 *
 */
public class ExtractionController {
	
    /**
     * A lista de commits que já existem no banco de dados (para que não sejam re-coletados sempre que o banco de dados seja atualizado)
     */
    private static ArrayList<Commit> commits;
    /**
     * O objeto que representa um repositório Git.
     */
    private static Git git;
    /**
     * A URI do repositório.
     */
    private static String repositoryURI;
    /**
     * O Token de autenticação do usuário.
     */
    private static String repositoryToken;
    /**
     * O <i>path</i> para a pasta do repositório.
     */
    private static String repositoryPath;
    /**
     * A <i>branch</i> principal do repositório.
     */
    private static String repositoryBranch;

    /**
     * Prepara o repositório para extração por meio da estratégia especificada. Este método deve ser chamado uma vez, durante a
     * incialização, em qualquer sistema que seja criado por meio do GitCURTAIN.
     * 
     * A documentação de como extender o sistema GitCURTAIN com mais estratégias de setup de repositório está na interface
     * RepoSetupStrategy. Por padrão, o sistema GitCURTAIN vem com a estratégia RepositoryByTerminal, que obtém o repositório por meio
     * da entrada de dados pelo terminal.
     * 
     * @param repositorySetup Um objeto que extende a interface RepoSetupStrategy, e contém a estratégia que deve ser utilizada para a
     * obtenção dos metadados do repositório.
     * @param databaseFilePath Uma String que contém o <i>path</i> do arquivo do banco de dados SQLite. O <i>path</i> deve conter a
     * extensão do arquivo.
     * 
     * @throws InvalidRemoteException Quando a URI do repositório é inválida.
     * @throws TransportException Quando o token de acesso ao repositório é inválido.
     * @throws InvalidBranchException Quando o nome da branch é inválido.
     */
    public static void setUpRepository(RepoSetupStrategy repositorySetup, String databaseFilePath) throws InvalidRemoteException, TransportException, InvalidBranchException {
    	
    	boolean done = false;
    	commits = getCommitsFromDatabase(databaseFilePath);
        
        while (!done) {
        	try {
        		repositoryURI = repositorySetup.getRepositoryURI();
            	repositoryToken = repositorySetup.getToken();
            	repositoryPath = repositorySetup.getRepositoryPath();
            	repositoryBranch = repositorySetup.getBranch();
        		File repositoryFile = new File(repositoryPath);
        		
        		CloneCommand clone = Git.cloneRepository();
        		clone.setURI(repositoryURI);
        		clone.setCredentialsProvider(new UsernamePasswordCredentialsProvider(repositoryToken, "" ));
        		clone.setDirectory(repositoryFile);
        		clone.call();
			
        		git = Git.open(repositoryFile);
        		
        		List<String> branchList = git.branchList()
        									.call()
        									.stream()
        									.map(Ref::getName)
        									.collect(Collectors.toList());
        		
        		
        		if (!branchList.contains("refs/heads/" + repositoryBranch)) {
        			throw new InvalidBranchException("Branch name does not exist in the remote repository.");
        		}
        		
        		StoredConfig config = git.getRepository().getConfig();
        		config.setString("branch", "master", "merge", "refs/heads/" + repositoryBranch);
        		config.setString("branch", "master", "remote", "origin");
        		config.setString("remote", "origin", "fetch", "+refs/heads/*:refs/remotes/origin/*");
        		config.setString("remote", "origin", "url", repositoryURI);
        		config.save();
        		
        		done = true;
        	}
        	catch (InvalidRemoteException e) {
        		throw e;
        	}
        	catch (TransportException e) {
        		throw e;
        	}
        	catch (GitAPIException e) {
        		System.out.println("Unexpected error occured in the Git API. Retrying...");
        		e.printStackTrace();
        	}
        	catch (IOException e) {
        		System.out.println("Unexpected I/O error. Retrying...");
        		e.printStackTrace();
        	}
        }

    }
    
    /**
     * Carrega os commits do banco de dados, se um já estiver disponível. Se não, cria uma lista em branco.
     * 
     * @param filePath Uma String que contém o <i>path</i> do arquivo do banco de dados SQLite. O <i>path</i> deve conter a extensão
     * do arquivo.
     * 
     * @return Um ArrayList de Commits com os commits que estavam guardados no banco de dados (se houver), ou um ArrayList de Commits
     * vazio.
     */
    private static ArrayList<Commit> getCommitsFromDatabase(String filePath) {
    	
    	FileController.setUpDatabase(filePath);
    	ArrayList<Commit> commits = FileController.getAllCommits();
    	
    	if (commits != null) {
    		return commits;
    	}
    	return new ArrayList<Commit>();
    }


    /**
     * Adiciona um conjunto de commits à variável que contém os Commits já coletados, e também ao banco de dados. Essa função é gerenciada
     * pelo ExtractionThread, e não precisa ser chamada pelo sistema.
     * 
     * @param commitList Um ArrayList de Commits que contém os commits a serem adicionados à lista de commits já coletados e ao banco de
     * dados.
     */
    public static void addCommits(ArrayList<Commit> commitList) {
    	
        commits.addAll(commitList);
		FileController.writeCommits(commitList);
    }
    
    /**
     * Obtém a lista atual de commits que está guardada no ExtractionController. Essa função é gerenciada pelo ExtractionThread, e não
     * precisa ser chamada pelo sistema.
     * 
     * @return Um ArrayList de Commits que contém os commits já guardados no ExtractionController.
     */
    public static ArrayList<Commit> getAllCommits() {
    	return commits;
    }
    
    /**
     * Inicia a thread de extração de commits do repositório. Esta função deve ser chamada por todo sistema que utiliza GitCURTAIN, durante
     * o processo de inicialização. Esta variação da função usa o tempo padrão de espera entre coletas (3 horas). Caso seja necessário
     * customizar este tempo de espera, é possível passar como parâmetro um tempo diferente na outra variação desta função.
     */
    public static void beginExtraction() {
	    ExtractionThread extractionThread = new ExtractionThread(git, repositoryToken, repositoryBranch);
	    extractionThread.start();
    }
    
    /**
     * Inicia a thread de extração de commits do repositório. Esta função deve ser chamada por todo sistema que utiliza GitCURTAIN, durante
     * o processo de inicialização. Esta variação da função usa um tempo de espera customizado entre coletas do repositório. Caso não seja
     * necessário usar um tempo customizado, é possível não passar nenhum parâmetro para usar o tempo padrão (3 horas).
     * 
     * @param timer O tempo, em milisegundos, de espera entre duas coletas do repositório.
     * @throws InvalidTimerValueException Quando o valor do timer é inválido.
     */
    public static void beginExtraction(long timer) throws InvalidTimerValueException {
    	if (timer < 0) {
    		throw new InvalidTimerValueException("Timer cannot be below zero.");
    	}
    	ExtractionThread extractionThread = new ExtractionThread(git, repositoryToken, repositoryBranch, timer);
	    extractionThread.start();
    }
    
    /**
     * Retorna o commit mais recentemente coletado, para o caso do banco de dados já ter dados antes da primeira execução.
     * 
     * @return O commit mais recentemente coletado.
     */
    public static Commit getLatestCommit() {
    	if (commits.size() >= 1) {
    		ArrayList<Commit> sorted = commits;
    		sorted.sort(Commit.CommitComparatorByCommitDate);
    		return sorted.get(sorted.size()-1);
    	}
    	else {
    		return null;
    	}
    }
}