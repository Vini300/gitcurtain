package gitcurtain.visualization;

import java.util.ArrayList;

import gitcurtain.utils.MetricResult;
import gitcurtain.utils.MetricsVisSync;

/**
 * Uma thread responsável por, uma vez que novos resultados de métricas sejam calculados pelo MetricThread, crie ou atualize a visualização
 * destes resultados. Essa thread é completamente gerenciada pelo GitCURTAIN e, portanto, não deve ser chamada pelo sistema. Para iniciar
 * a thread corretamente, é necessário usar o método beginMetricCalculation da classe MetricController.
 * 
 * @author Vinícius Soares
 *
 */
public class VisualizationThread extends Thread {

	/**
	 * Uma variável que determina se a visualização precisa ser criada do zero ou simplesmente atualizada.
	 */
	private boolean visCreated;
	/**
	 * A lista de resultados que foi gerada pela execução das métricas.
	 */
	private ArrayList<MetricResult> results;
	
	/**
	 * A duração de espera caso a thread acorde sem necessariamente terem novos dados.
	 */
	private final long DURATION = 300;
	/**
     * O objeto de sincronização entre o MetricThread e o VisualizationThread.
     */
	private MetricsVisSync syncLock;
	
	/**
	 * Constrói um VisualizationThread.
	 */
	public VisualizationThread() {
		syncLock = MetricsVisSync.getInstance();
	}
	
	/**
     * Inicia a thread, fazendo com que a visualização seja criada ou atualizada assim que a lista de resultados de métricas seja atualizada.
     */
	public void run() {
		
		while (true) {
			
			results = null;
			Object lock = syncLock.getLock();
			
			try {
				synchronized(lock) {
					lock.wait();
				}
			
				while (results == null) {
					results = syncLock.getResultList();
					if (results == null) {
						sleep(DURATION);
					}
				}
			
				if (!visCreated) {
					VisualizationController.renderVis(results);
					visCreated = true;
				}
				else {
					VisualizationController.updateVis(results);
				}
			}
			catch (InterruptedException e) {
				System.out.println("Unexpected interruption from thread. Retrying...");
				e.printStackTrace();
			}
		}
		
	}
}
