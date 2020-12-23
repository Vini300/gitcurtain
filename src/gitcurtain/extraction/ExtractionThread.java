package gitcurtain.extraction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.util.io.DisabledOutputStream;

import gitcurtain.exceptions.InvalidEnumIDException;
import gitcurtain.utils.Commit;
import gitcurtain.utils.CommitVariables;
import gitcurtain.utils.ExtractionMetricsSync;

/**
 * Uma thread responsável por, a cada "duration" milisegundos de espera (padrão 3 horas), checar se houveram novos commits no repositório e,
 * assim, atualizar os dados do sistema. Essa thread é completamente gerenciada pelo GitCURTAIN e, portanto, não deve ser chamada pelo
 * sistema. Para iniciar a thread corretamente, é necessário usar o método beginExtraction da classe ExtractionController.
 * 
 * @author Vinícius Soares
 *
 */
public class ExtractionThread extends Thread {
	
    /**
     * O repositório Git de onde os dados estão sendo extraídos.
     */
    private Git git;
    /**
     * O commit mais recentemente extraído do repositório.
     */
    private Commit lastCommit;
    /**
     * O token de autenticação do usuário.
     */
    private String token;
    /**
     * O nome da branch principal do repositório.
     */
    private String branchName;
    /**
     * A duração de espera entre duas checagens de novos commits no repositório.
     */
    private long duration;
    /**
     * O objeto de sincronização entre o ExtractionThread e o MetricThread.
     */
    private ExtractionMetricsSync syncLock;
    
    /**
     * Constrói um ExtractionThread. Recebe somente o repositório como parâmetro, e usa um tempo de espera padrão (3 horas). É possível
     * também suprir uma duração customizada por uma variação deste método
     * 
     * @param git O repositório Git de onde os dados serão extraídos
     */
    public ExtractionThread(Git git, String token, String branchName) {
    	this.git = git;
    	this.token = token;
    	this.branchName = branchName;
    	duration = 12000000; //3 horas
    	lastCommit = ExtractionController.getLatestCommit();
    	syncLock = ExtractionMetricsSync.getInstance();
    }
    
    /**
     * Constrói um ExtractionThread. Recebe o repositório e um tempo de espera customizados como parâmetro. É possível também não suprir
     * uma duração customizada por uma variação deste método, que usa um tempo de espera padrão (3 horas).
     * 
     * @param git O repositório Git de onde os dados serão extraídos
     * @param duration O tempo de espera entre duas execuções da thread.
     */
    public ExtractionThread(Git git, String token, String branchName, long duration) {
    	this.git = git;
    	this.duration = duration;
    	this.token = token;
    	this.branchName = branchName;
    	lastCommit = ExtractionController.getLatestCommit();
    	syncLock = ExtractionMetricsSync.getInstance();
    }
    
	/**
	 * Obtém a lista de arquivos que foram modificados no commit em questão.
	 * 
	 * @param commit O commit no qual as mudanças foram feitas.
	 * @param repo O repositório no qual o commit foi feito.
	 * 
	 * @return Um ArrayList de Strings com a lista de arquivos que foram modificados no commit.
	 * 
	 * @throws MissingObjectException É lançado quando não foi possível encontrar um repositório.
	 * @throws IncorrectObjectTypeException É lançado quando houve um erro na coleta de dados do repositório.
	 * @throws IOException É lançado quando ocorre um erro durante alguma operação de entrada e saída de dados.
	 */
	private ArrayList<String> getModifiedFiles(RevCommit commit, Repository repo) throws MissingObjectException, IncorrectObjectTypeException, IOException {
		
		ArrayList<String> modifiedFiles = new ArrayList<String>();
		
		RevWalk rw = new RevWalk(repo);
		RevCommit parent = null;
		
		if (commit.getParents().length != 0) {
			parent = rw.parseCommit(commit.getParent(0).getId());
		}
		
		DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
		df.setRepository(repo);
		df.setDiffComparator(RawTextComparator.DEFAULT);
		df.setDetectRenames(true);
		List<DiffEntry> diffs = null;
		
		if (parent != null) {
			diffs = df.scan(parent.getTree(), commit.getTree());
		}
		else {
			diffs = df.scan(null, commit.getTree());
		}
		
		for (DiffEntry diff : diffs) {
			modifiedFiles.add(diff.getNewPath());
		}
		
		rw.close();
		df.close();
		
		return modifiedFiles;
	}
	
	/**
	 * Extrai todos os commits a partir de um commit específico (até o commit mais recente).
	 * 
	 * @param commit O commit mais antigo a ser extraído neste conjunto.
	 * @param repo O repositório no qual os commits estão sendo extraídos de.
	 * 
	 * @throws MissingObjectException É lançado quando não foi possível encontrar um repositório.
	 * @throws IncorrectObjectTypeException É lançado quando houve um erro na coleta de dados do repositório.
	 * @throws IOException É lançado quando ocorre um erro durante alguma operação de entrada e saída de dados.
	 */
	private void extractCommitsFrom(RevCommit commit, Repository repo) throws MissingObjectException, IncorrectObjectTypeException, IOException {
		
		Commit newLast = setupCommit(commit, repo);
		
		ArrayList<Commit> newCommits = setupNewCommits(lastCommit, commit, repo);
		
		lastCommit = newLast;
		
		ExtractionController.addCommits(newCommits);
		synchronizeCommits();
	}
	
	/**
	 * Recebe um objeto RevCommit do JGit, e cria um objeto Commit do GitCURTAIN equivalente.
	 * 
	 * @param commit O commit a ser convertido para a classe Commit.
	 * @param repo O repositório no qual os commits estão sendo extraídos de.
	 * 
	 * @throws MissingObjectException É lançado quando não foi possível encontrar um repositório.
	 * @throws IncorrectObjectTypeException É lançado quando houve um erro na coleta de dados do repositório.
	 * @throws IOException É lançado quando ocorre um erro durante alguma operação de entrada e saída de dados.
	 */
	private Commit setupCommit(RevCommit commit, Repository repo) throws MissingObjectException, IncorrectObjectTypeException, IOException {
		
		String message = commit.getFullMessage();
		String hash = commit.getName();
		PersonIdent author = commit.getAuthorIdent();
		String authorName = author.getName();
		Date authoringDate = author.getWhen();
		PersonIdent committer = commit.getCommitterIdent();
		String committerName = commit.getName();
		Date commitDate = committer.getWhen();
		ArrayList<String> modifiedFiles = getModifiedFiles(commit, repo);
		
		return new Commit(message, hash, authorName, committerName, commitDate, authoringDate, modifiedFiles);
	}

	/**
	 * Prepara um conjunto de commits para serem adicionados ao banco de dados pelo ExtractionController.
	 * 
	 * @param lastAnalyzed O commit analisado por último.
	 * @param commit O commit que inicia a lista de commits sendo extraídos.
	 * @param repo O repositório de onde os commits estão sendo extraídos.
	 * 
	 * @return Um ArrayList de Commits com os commits novos desde o commit lastAnalyzed.
	 * 
	 * @throws MissingObjectException É lançado quando não foi possível encontrar um repositório.
	 * @throws IncorrectObjectTypeException É lançado quando houve um erro na coleta de dados do repositório.
	 * @throws IOException É lançado quando ocorre um erro durante alguma operação de entrada e saída de dados.
	 */
	private ArrayList<Commit> setupNewCommits(Commit lastAnalyzed, RevCommit commit, Repository repo) throws MissingObjectException, IncorrectObjectTypeException, IOException {
		
		RevWalk rw = new RevWalk(repo);
		rw.markStart(commit);
		ArrayList<Commit> commitList = new ArrayList<Commit>();

		for (RevCommit current : rw) {
			
			try {
				
				if (lastAnalyzed != null) {
					if (current.getName().equals((String) lastAnalyzed.getVariableByEnum(CommitVariables.HASH))) {
						break;
					}
				}
			
				Commit addedCommit = setupCommit(current, repo);
				commitList.add(addedCommit);
			}
			catch (InvalidEnumIDException e) {
				System.out.println("An unexpected error ocurred while accessing commit data. Please try again later, and double check the"
	        			+ " Enum variables used for any access to commit data.");
				e.printStackTrace();
			}
		}
		
		rw.close();
		
		return commitList;
	}

	/**
	 * Sincroniza os commits entre o ExtractionThread e o MetricThread, fazendo com que as métricas sejam calculadas novamente.
	 */
	private void synchronizeCommits() {
		syncLock.setCommitList(ExtractionController.getAllCommits());
	}
    
    /**
     * Inicia a thread, fazendo com que os commits sejam extraídos sempre que houver uma nova atualização no repositório.
     */
    public void run() {
    	
    	PullCommand pull = git.pull();
    	pull.setCredentialsProvider(new UsernamePasswordCredentialsProvider(token, ""));
		pull.setRemote("origin");
		pull.setRemoteBranchName(branchName);
		
		try {
			pull.call();
		} catch (Exception e) {
			System.out.println("Unexpected error in extraction process. Please terminate the program.");
			throw new RuntimeException(e);
		}
		
		Repository repo = git.getRepository();
		
		while (true) {
			
			try {
				
				RevWalk revWalk = new RevWalk(repo);
				Ref head = repo.exactRef("refs/heads/" + branchName);
				RevCommit commit = revWalk.parseCommit(head.getObjectId());
				
				if (lastCommit != null) {
					if (!commit.getName().contentEquals((String) lastCommit.getVariableByEnum(CommitVariables.HASH))) {
						extractCommitsFrom(commit, repo);
					}
					else {
						synchronizeCommits();
					}
				}
				else {
					extractCommitsFrom(commit, repo);
				}
				
				revWalk.close();
				sleep(duration);
			}
			catch (IOException e) {
				System.out.println("Unexpected I/O error. Retrying...");
				e.printStackTrace();
			}
			catch (InvalidEnumIDException e) {
				System.out.println("An unexpected error ocurred while accessing commit data. Please try again later, and double check the"
	        			+ " Enum variables used for any access to commit data. Retrying...");
				e.printStackTrace();
			}
			catch (InterruptedException e) {
				System.out.println("Unexpected interruption from thread. Retrying...");
				e.printStackTrace();
			}
		}
    }
}
