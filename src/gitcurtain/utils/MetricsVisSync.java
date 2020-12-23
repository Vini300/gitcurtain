package gitcurtain.utils;

import java.util.ArrayList;

/**
 * Objeto que permite a sincronização entre a MetricThread e a VisualizationThread. Serve simultaneamente como um <i>lock</i> para garantir
 * sincronia assim como a forma de transmissão de dados entre as threads. Para permitir a passagem de dados, o objeto é um <i>singleton</i>.
 * 
 * @author Vinícius Soares
 *
 */
public class MetricsVisSync {
	
	/**
	 * O <i>singleton</i> que representa a cópia do objeto MetricsVisSync.
	 */
	private static MetricsVisSync syncLock;
	
	/**
	 * A lista de resultados de métricas que será transferida entre a MetricThread e a VisualizationThread.
	 */
	private ArrayList<MetricResult> metricList;
	/**
	 * Uma variável que representa quando uma nova lista de commits está disponível.
	 */
	private boolean ready;
	/**
	 * O <i>lock</i> que garante a sincronização entre a MetricThread e a VisualizationThread.
	 */
	private final Object LOCK = new Object();
	
	/**
	 * Cria uma instância do MetricsVisSync. Somente pode ser invocado pelo método getInstance.
	 */
	private MetricsVisSync() {
		metricList = null;
		ready = false;
	}
	
	/**
	 * Cria uma instância do MetricsVisSync, se não houver ainda. Se já houver, então retorna a instância existente.
	 * 
	 * @return O objeto <i>singleton</i> do MetricsVisSync.
	 */
	public static MetricsVisSync getInstance() {
		if (syncLock == null) {
			syncLock = new MetricsVisSync();
		}
		return syncLock;
	}
	
	/**
	 * Atualiza a lista de resultados compartilhada e manda um sinal de início ao VisualizationThread.
	 * 
	 * @param newList Um ArrayList de MetricResults com os novos resultados de métricas a serem enviados ao VisualizationThread.
	 */
	public void setResultList(ArrayList<MetricResult> newList) {
		synchronized(LOCK) {
			metricList = newList;
			ready = true;
			LOCK.notify();
		}
	}
	
	/**
	 * Obtém a lista mais recentemente atualizada de resultados de métricas.
	 * 
	 * @return Um ArrayList de MetricResults com a lista mais atualizada de resultados de métricas calculados.
	 */
	public ArrayList<MetricResult> getResultList() {
		synchronized(LOCK) {
			if (ready) {
				ready = false;
				return metricList;
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
