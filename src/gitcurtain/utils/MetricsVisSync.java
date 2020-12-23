package gitcurtain.utils;

import java.util.ArrayList;

/**
 * Objeto que permite a sincroniza��o entre a MetricThread e a VisualizationThread. Serve simultaneamente como um <i>lock</i> para garantir
 * sincronia assim como a forma de transmiss�o de dados entre as threads. Para permitir a passagem de dados, o objeto � um <i>singleton</i>.
 * 
 * @author Vin�cius Soares
 *
 */
public class MetricsVisSync {
	
	/**
	 * O <i>singleton</i> que representa a c�pia do objeto MetricsVisSync.
	 */
	private static MetricsVisSync syncLock;
	
	/**
	 * A lista de resultados de m�tricas que ser� transferida entre a MetricThread e a VisualizationThread.
	 */
	private ArrayList<MetricResult> metricList;
	/**
	 * Uma vari�vel que representa quando uma nova lista de commits est� dispon�vel.
	 */
	private boolean ready;
	/**
	 * O <i>lock</i> que garante a sincroniza��o entre a MetricThread e a VisualizationThread.
	 */
	private final Object LOCK = new Object();
	
	/**
	 * Cria uma inst�ncia do MetricsVisSync. Somente pode ser invocado pelo m�todo getInstance.
	 */
	private MetricsVisSync() {
		metricList = null;
		ready = false;
	}
	
	/**
	 * Cria uma inst�ncia do MetricsVisSync, se n�o houver ainda. Se j� houver, ent�o retorna a inst�ncia existente.
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
	 * Atualiza a lista de resultados compartilhada e manda um sinal de in�cio ao VisualizationThread.
	 * 
	 * @param newList Um ArrayList de MetricResults com os novos resultados de m�tricas a serem enviados ao VisualizationThread.
	 */
	public void setResultList(ArrayList<MetricResult> newList) {
		synchronized(LOCK) {
			metricList = newList;
			ready = true;
			LOCK.notify();
		}
	}
	
	/**
	 * Obt�m a lista mais recentemente atualizada de resultados de m�tricas.
	 * 
	 * @return Um ArrayList de MetricResults com a lista mais atualizada de resultados de m�tricas calculados.
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
	 * Retorna o <i>lock</i>, para sincroniza��o.
	 * 
	 * @return O <i>lock</i>.
	 */
	public Object getLock() {
		return LOCK;
	}
	
}
