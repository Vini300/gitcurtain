package gitcurtain.utils;

import java.util.ArrayList;

/**
 * Objeto que permite a sincroniza��o entre a ExtractionThread e a MetricThread. Serve simultaneamente como um <i>lock</i> para garantir
 * sincronia assim como a forma de transmiss�o de dados entre as threads. Para permitir a passagem de dados, o objeto � um <i>singleton</i>.
 * 
 * @author Vin�cius Soares
 *
 */
public class ExtractionMetricsSync {
	
	/**
	 * O <i>singleton</i> que representa a c�pia do objeto ExtractionMetricsSync.
	 */
	private static ExtractionMetricsSync syncLock;
	
	/**
	 * A lista de commits que ser� transferida entre a ExtractionThread e a MetricThread.
	 */
	private ArrayList<Commit> commitList;
	/**
	 * Uma vari�vel que representa quando uma nova lista de commits est� dispon�vel.
	 */
	private boolean ready;
	/**
	 * O <i>lock</i> que garante a sincroniza��o entre a ExtractionThread e a MetricThread.
	 */
	private final Object LOCK = new Object();
	
	/**
	 * Cria uma inst�ncia do ExtractionMetricsSync. Somente pode ser invocado pelo m�todo getInstance.
	 */
	private ExtractionMetricsSync() {
		commitList = null;
		ready = false;
	}
	
	/**
	 * Cria uma inst�ncia do ExtractionMetricsSync, se n�o houver ainda. Se j� houver, ent�o retorna a inst�ncia existente.
	 * 
	 * @return O objeto <i>singleton</i> do ExtractionMetricsSync.
	 */
	public static ExtractionMetricsSync getInstance() {
		if (syncLock == null) {
			syncLock = new ExtractionMetricsSync();
		}
		return syncLock;
	}
	
	/**
	 * Atualiza a lista de commits compartilhada e manda um sinal de in�cio ao MetricThread.
	 * 
	 * @param newList Um ArrayList de Commits com os novos commits a serem enviados ao MetricThread.
	 */
	public void setCommitList(ArrayList<Commit> newList) {
		synchronized(LOCK) {
			commitList = newList;
			ready = true;
			LOCK.notify();
		}
	}
	
	/**
	 * Obt�m a lista mais recentemente atualizada de commits.
	 * 
	 * @return Um ArrayList de Commits com a lista mais atualizada de commits coletados.
	 */
	public ArrayList<Commit> getCommitList() {
		synchronized(LOCK) {
			if (ready) {
				ready = false;
				return commitList;
			}
			else {
				return null;
			}
		}
	}
	
	/**
	 * Retorna o <i>lock</i>, para sincroniza��o.
	 * 
	 * @return O <i>lock</i>.
	 */
	public Object getLock() {
		return LOCK;
	}
	
}
