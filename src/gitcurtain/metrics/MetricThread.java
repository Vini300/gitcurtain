package gitcurtain.metrics;

import java.util.ArrayList;

import gitcurtain.utils.Commit;
import gitcurtain.utils.ExtractionMetricsSync;
import gitcurtain.utils.MetricResult;
import gitcurtain.utils.MetricsVisSync;

/**
 * Uma thread respons�vel por, uma vez que novos commits sejam encontrados pelo ExtractionThread, calcule um novo conjunto de resultados de
 * m�tricas incluindo estes novos commits. Essa thread � completamente gerenciada pelo GitCURTAIN e, portanto, n�o deve ser chamada pelo
 * sistema. Para iniciar a thread corretamente, � necess�rio usar o m�todo beginMetricCalculation da classe MetricController.
 * 
 * @author Vin�cius Soares
 *
 */
public class MetricThread extends Thread {
	
	/**
	 * A lista de commits a serem analisados.
	 */
	private ArrayList<Commit> commits;
	/**
	 * A lista de resultados que ser� gerada ap�s a execu��o das m�tricas.
	 */
	private ArrayList<MetricResult> metricResults;
	
	/**
	 * A dura��o de espera caso a thread acorde sem necessariamente terem novos dados.
	 */
	private final long DURATION = 300;
	/**
     * O objeto de sincroniza��o entre o ExtractionThread e o MetricThread.
     */
	private ExtractionMetricsSync emSyncLock;
	/**
     * O objeto de sincroniza��o entre o MetricThread e o VisualizationThread.
     */
	private MetricsVisSync mvSyncLock;
	
	
	/**
	 * Constr�i um MetricThread.
	 */
	public MetricThread() {
		emSyncLock = ExtractionMetricsSync.getInstance();
		mvSyncLock = MetricsVisSync.getInstance();
	}
	
	/**
	 * Sincroniza os resultados das m�tricas entre o MetricThread e o VisualizationThread, fazendo com que a visualiza��o seja gerada e/ou
	 * atualizada.
	 */
	private void synchronizeMetrics() {
		mvSyncLock.setResultList(metricResults);
	}
	
	/**
     * Inicia a thread, fazendo com que as m�tricas sejam recalculadas uma vez que o reposit�rio seja atualizado.
     */
	public void run() {
		
		while (true) {
			
			commits = null;
			Object lock = emSyncLock.getLock();
			try {
				synchronized(lock) {
					lock.wait();
				}
			
				while (commits == null) {
					commits = emSyncLock.getCommitList();
					if (commits == null) {
						sleep(DURATION);
					}
				}
			
				metricResults = MetricController.executeMetrics(commits);
				synchronizeMetrics();
			}
			catch (InterruptedException e) {
				System.out.println("Unexpected interruption from thread. Retrying...");
				e.printStackTrace();
			}
		}
		
	}
    
}
