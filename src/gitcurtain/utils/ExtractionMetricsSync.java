package gitcurtain.utils;

import java.util.ArrayList;

/**
 * Objeto que permite a sincronização entre a ExtractionThread e a MetricThread. Serve simultaneamente como um <i>lock</i> para garantir
 * sincronia assim como a forma de transmissão de dados entre as threads. Para permitir a passagem de dados, o objeto é um <i>singleton</i>.
 * 
 * @author Vinícius Soares
 *
 */
public class ExtractionMetricsSync {
	
	/**
	 * O <i>singleton</i> que representa a cópia do objeto ExtractionMetricsSync.
	 */
	private static ExtractionMetricsSync syncLock;
	
	/**
	 * A lista de commits que será transferida entre a ExtractionThread e a MetricThread.
	 */
	private ArrayList<Commit> commitList;
	/**
	 * Uma variável que representa quando uma nova lista de commits está disponível.
	 */
	private boolean ready;
	/**
	 * O <i>lock</i> que garante a sincronização entre a ExtractionThread e a MetricThread.
	 */
	private final Object LOCK = new Object();
	
	/**
	 * Cria uma instância do ExtractionMetricsSync. Somente pode ser invocado pelo método getInstance.
	 */
	private ExtractionMetricsSync() {
		commitList = null;
		ready = false;
	}
	
	/**
	 * Cria uma instância do ExtractionMetricsSync, se não houver ainda. Se já houver, então retorna a instância existente.
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
	 * Atualiza a lista de commits compartilhada e manda um sinal de início ao MetricThread.
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
	 * Obtém a lista mais recentemente atualizada de commits.
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
	 * Retorna o <i>lock</i>, para sincronização.
	 * 
	 * @return O <i>lock</i>.
	 */
	public Object getLock() {
		return LOCK;
	}
	
}
