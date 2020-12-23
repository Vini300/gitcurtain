package gitcurtain.metrics;

import java.util.ArrayList;

import gitcurtain.utils.Commit;
import gitcurtain.utils.MetricResult;

/**
 * Gerencia todo o sistema de c�lculo de m�tricas, permitindo que o desenvolvedor determine como e quais m�tricas devem ser executadas
 * sobre os dados para gerar as informa��es que formar�o a visualiza��o final futuramente.
 * 
 * Toda a comunica��o com as classes que gerenciam a execu��o e c�lculo de m�tricas diretamente deve ser feita por meio das fun��es da classe
 * MetricController.
 * 
 * @author Vin�cius Soares
 *
 */
public class MetricController {
	
    /**
     * A lista de m�tricas que devem ser calculadas sobre os dados dos commits.
     */
    private static ArrayList<Metric> metricList = new ArrayList<Metric>();
    
    /**
     * Uma das formas de preparar a lista de m�tricas. Substitui a lista de m�tricas atual do MetricController por uma lista passada como
     * par�metro para a fun��o. A lista de m�tricas deve ser preparada durante a inicializa��o em qualquer sistema que utiliza o GitCURTAIN,
     * ou por meio da fun��o setUpMetricArray, ou por meio da fun��o addMetric.
     * 
     * @param list A lista de m�tricas que devem ser calculadas.
     */
    public static void setUpMetricArray(ArrayList<Metric> list) {
    	
    	metricList = list;
    }

    /**
     * Uma das formas de preparar a lista de m�tricas. Adiciona uma m�trica � lista de m�tricas do MetricController. A lista de m�tricas
     * deve ser preparada durante a inicializa��o em qualquer sistema que utiliza o GitCURTAIN, ou por meio da fun��o setUpMetricArray,
     * ou por meio da fun��o addMetric.
     * 
     * @param metric A m�trica a ser adicionada � lista de m�tricas.
     */
    public static void addMetric(Metric metric) {
    	
        metricList.add(metric);
    }

    /**
     * Remove uma m�trica da lista de m�tricas do MetricController, que corresponde ao ID passado como par�metro.
     * 
     * @param metricID Um valor inteiro representando o ID da m�trica a ser removida.
     * 
     * @return A m�trica removida.
     */
    public static Metric removeMetric(int metricID) {
    	
        Metric removedMetric = null;

        for (Metric metric : metricList) {
            if (metric.getID() == metricID) {
                removedMetric = metric;
            }
        }

        if (removedMetric != null) {
            metricList.remove(removedMetric);
            return removedMetric;
        }
        
        return null;
    }
    
    /**
     * Remove todas as m�tricas do MetricController, retornando o array com todas as m�tricas que foram removidas.
     * 
     * @return Um ArrayList de Metrics contendo a lista de m�tricas que foram removidas.
     */
    public static ArrayList<Metric> removeAllMetrics() {
    	ArrayList<Metric> oldList = metricList;
    	metricList = new ArrayList<Metric>();
    	return oldList;
    }

    /**
     * Ordena todas as m�tricas da lista de m�tricas a serem executadas, em ordem FIFO. Esta fun��o � gerenciada pelo GitCURTAIN e, portanto,
     * n�o deve ser utilizada pelo sistema. Para executar as m�tricas, simplesmente execute a thread de m�tricas e elas ser�o executadas
     * automaticamente uma vez que novos dados sejam coletados para estas.
     * 
     * @param commitList Um ArrayList de Commits, que cont�m os commits que ser�o utilizados para c�lculo de m�tricas.
     * 
     * @return Um ArrayList de MetricResults, com os resultados de cada uma das m�tricas.
     */
    public static ArrayList<MetricResult> executeMetrics(ArrayList<Commit> commitList) {
    	
        ArrayList<MetricResult> resultList = new ArrayList<MetricResult>();
        MetricResult newResult;

        for (Metric metric : metricList) {
            newResult = metric.executeMetric(commitList);
            resultList.add(newResult);
        }

        return resultList;
    }
    
    /**
     * Inicia a thread de c�lculo de m�tricas do reposit�rio. Esta fun��o deve ser chamada por todo sistema que utiliza GitCURTAIN,
     * durante o processo de inicializa��o.
     * 
     */
    public static void beginMetricCalculation() {
    	MetricThread metricsThread = new MetricThread();
    	metricsThread.start();
    }
    
}