package gitcurtain.visualization;

import java.util.ArrayList;

import gitcurtain.utils.MetricResult;
import gitcurtain.utils.MetricsVisSync;

/**
 * Uma thread respons�vel por, uma vez que novos resultados de m�tricas sejam calculados pelo MetricThread, crie ou atualize a visualiza��o
 * destes resultados. Essa thread � completamente gerenciada pelo GitCURTAIN e, portanto, n�o deve ser chamada pelo sistema. Para iniciar
 * a thread corretamente, � necess�rio usar o m�todo beginMetricCalculation da classe MetricController.
 * 
 * @author Vin�cius Soares
 *
 */
public class VisualizationThread extends Thread {

	/**
	 * Uma vari�vel que determina se a visualiza��o precisa ser criada do zero ou simplesmente atualizada.
	 */
	private boolean visCreated;
	/**
	 * A lista de resultados que foi gerada pela execu��o das m�tricas.
	 */
	private ArrayList<MetricResult> results;
	
	/**
	 * A dura��o de espera caso a thread acorde sem necessariamente terem novos dados.
	 */
	private final long DURATION = 300;
	/**
     * O objeto de sincroniza��o entre o MetricThread e o VisualizationThread.
     */
	private MetricsVisSync syncLock;
	
	/**
	 * Constr�i um VisualizationThread.
	 */
	public VisualizationThread() {
		syncLock = MetricsVisSync.getInstance();
	}
	
	/**
     * Inicia a thread, fazendo com que a visualiza��o seja criada ou atualizada assim que a lista de resultados de m�tricas seja atualizada.
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
