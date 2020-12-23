package gitcurtain.metrics;

import java.util.ArrayList;

import gitcurtain.utils.Commit;
import gitcurtain.utils.ExtractionMetricsSync;
import gitcurtain.utils.MetricResult;
import gitcurtain.utils.MetricsVisSync;

/**
 * Uma thread responsável por, uma vez que novos commits sejam encontrados pelo ExtractionThread, calcule um novo conjunto de resultados de
 * métricas incluindo estes novos commits. Essa thread é completamente gerenciada pelo GitCURTAIN e, portanto, não deve ser chamada pelo
 * sistema. Para iniciar a thread corretamente, é necessário usar o método beginMetricCalculation da classe MetricController.
 * 
 * @author Vinícius Soares
 *
 */
public class MetricThread extends Thread {
	
	/**
	 * A lista de commits a serem analisados.
	 */
	private ArrayList<Commit> commits;
	/**
	 * A lista de resultados que será gerada após a execução das métricas.
	 */
	private ArrayList<MetricResult> metricResults;
	
	/**
	 * A duração de espera caso a thread acorde sem necessariamente terem novos dados.
	 */
	private final long DURATION = 300;
	/**
     * O objeto de sincronização entre o ExtractionThread e o MetricThread.
     */
	private ExtractionMetricsSync emSyncLock;
	/**
     * O objeto de sincronização entre o MetricThread e o VisualizationThread.
     */
	private MetricsVisSync mvSyncLock;
	
	
	/**
	 * Constrói um MetricThread.
	 */
	public MetricThread() {
		emSyncLock = ExtractionMetricsSync.getInstance();
		mvSyncLock = MetricsVisSync.getInstance();
	}
	
	/**
	 * Sincroniza os resultados das métricas entre o MetricThread e o VisualizationThread, fazendo com que a visualização seja gerada e/ou
	 * atualizada.
	 */
	private void synchronizeMetrics() {
		mvSyncLock.setResultList(metricResults);
	}
	
	/**
     * Inicia a thread, fazendo com que as métricas sejam recalculadas uma vez que o repositório seja atualizado.
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
