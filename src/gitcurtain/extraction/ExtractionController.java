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
 * Gerencia todo o sistema de extra��o de dados, permitindo que o desenvolvedor determine qual a forma de obten��o dos metadados necess�rios
 * para a extra��o dos dados do reposit�rio a ser analisado.
 * 
 * Toda a comunica��o com as classes que gerenciam a extra��o de dados diretamente deve ser feita por meio das fun��es da classe
 * ExtractionController.
 * 
 * @author Vin�cius Soares
 *
 */
public class ExtractionController {
	
    /**
     * A lista de commits que j� existem no banco de dados (para que n�o sejam re-coletados sempre que o banco de dados seja atualizado)
     */
    private static ArrayList<Commit> commits;
    /**
     * O objeto que representa um reposit�rio Git.
     */
    private static Git git;
    /**
     * A URI do reposit�rio.
     */
    private static String repositoryURI;
    /**
     * O Token de autentica��o do usu�rio.
     */
    private static String repositoryToken;
    /**
     * O <i>path</i> para a pasta do reposit�rio.
     */
    private static String repositoryPath;
    /**
     * A <i>branch</i> principal do reposit�rio.
     */
    private static String repositoryBranch;

    /**
     * Prepara o reposit�rio para extra��o por meio da estrat�gia especificada. Este m�todo deve ser chamado uma vez, durante a
     * incializa��o, em qualquer sistema que seja criado por meio do GitCURTAIN.
     * 
     * A documenta��o de como extender o sistema GitCURTAIN com mais estrat�gias de setup de reposit�rio est� na interface
     * RepoSetupStrategy. Por padr�o, o sistema GitCURTAIN vem com a estrat�gia RepositoryByTerminal, que obt�m o reposit�rio por meio
     * da entrada de dados pelo terminal.
     * 
     * @param repositorySetup Um objeto que extende a interface RepoSetupStrategy, e cont�m a estrat�gia que deve ser utilizada para a
     * obten��o dos metadados do reposit�rio.
     * @param databaseFilePath Uma String que cont�m o <i>path</i> do arquivo do banco de dados SQLite. O <i>path</i> deve conter a
     * extens�o do arquivo.
     * 
     * @throws InvalidRemoteException Quando a URI do reposit�rio � inv�lida.
     * @throws TransportException Quando o token de acesso ao reposit�rio � inv�lido.
     * @throws InvalidBranchException Quando o nome da branch � inv�lido.
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
     * Carrega os commits do banco de dados, se um j� estiver dispon�vel. Se n�o, cria uma lista em branco.
     * 
     * @param filePath Uma String que cont�m o <i>path</i> do arquivo do banco de dados SQLite. O <i>path</i> deve conter a extens�o
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
     * Adiciona um conjunto de commits � vari�vel que cont�m os Commits j� coletados, e tamb�m ao banco de dados. Essa fun��o � gerenciada
     * pelo ExtractionThread, e n�o precisa ser chamada pelo sistema.
     * 
     * @param commitList Um ArrayList de Commits que cont�m os commits a serem adicionados � lista de commits j� coletados e ao banco de
     * dados.
     */
    public static void addCommits(ArrayList<Commit> commitList) {
    	
        commits.addAll(commitList);
		FileController.writeCommits(commitList);
    }
    
    /**
     * Obt�m a lista atual de commits que est� guardada no ExtractionController. Essa fun��o � gerenciada pelo ExtractionThread, e n�o
     * precisa ser chamada pelo sistema.
     * 
     * @return Um ArrayList de Commits que cont�m os commits j� guardados no ExtractionController.
     */
    public static ArrayList<Commit> getAllCommits() {
    	return commits;
    }
    
    /**
     * Inicia a thread de extra��o de commits do reposit�rio. Esta fun��o deve ser chamada por todo sistema que utiliza GitCURTAIN, durante
     * o processo de inicializa��o. Esta varia��o da fun��o usa o tempo padr�o de espera entre coletas (3 horas). Caso seja necess�rio
     * customizar este tempo de espera, � poss�vel passar como par�metro um tempo diferente na outra varia��o desta fun��o.
     */
    public static void beginExtraction() {
	    ExtractionThread extractionThread = new ExtractionThread(git, repositoryToken, repositoryBranch);
	    extractionThread.start();
    }
    
    /**
     * Inicia a thread de extra��o de commits do reposit�rio. Esta fun��o deve ser chamada por todo sistema que utiliza GitCURTAIN, durante
     * o processo de inicializa��o. Esta varia��o da fun��o usa um tempo de espera customizado entre coletas do reposit�rio. Caso n�o seja
     * necess�rio usar um tempo customizado, � poss�vel n�o passar nenhum par�metro para usar o tempo padr�o (3 horas).
     * 
     * @param timer O tempo, em milisegundos, de espera entre duas coletas do reposit�rio.
     * @throws InvalidTimerValueException Quando o valor do timer � inv�lido.
     */
    public static void beginExtraction(long timer) throws InvalidTimerValueException {
    	if (timer < 0) {
    		throw new InvalidTimerValueException("Timer cannot be below zero.");
    	}
    	ExtractionThread extractionThread = new ExtractionThread(git, repositoryToken, repositoryBranch, timer);
	    extractionThread.start();
    }
    
    /**
     * Retorna o commit mais recentemente coletado, para o caso do banco de dados j� ter dados antes da primeira execu��o.
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