package gitcurtain.metrics;

import java.util.ArrayList;

import gitcurtain.utils.Commit;
import gitcurtain.utils.MetricResult;

/**
 * Gerencia todo o sistema de cálculo de métricas, permitindo que o desenvolvedor determine como e quais métricas devem ser executadas
 * sobre os dados para gerar as informações que formarão a visualização final futuramente.
 * 
 * Toda a comunicação com as classes que gerenciam a execução e cálculo de métricas diretamente deve ser feita por meio das funções da classe
 * MetricController.
 * 
 * @author Vinícius Soares
 *
 */
public class MetricController {
	
    /**
     * A lista de métricas que devem ser calculadas sobre os dados dos commits.
     */
    private static ArrayList<Metric> metricList = new ArrayList<Metric>();
    
    /**
     * Uma das formas de preparar a lista de métricas. Substitui a lista de métricas atual do MetricController por uma lista passada como
     * parâmetro para a função. A lista de métricas deve ser preparada durante a inicialização em qualquer sistema que utiliza o GitCURTAIN,
     * ou por meio da função setUpMetricArray, ou por meio da função addMetric.
     * 
     * @param list A lista de métricas que devem ser calculadas.
     */
    public static void setUpMetricArray(ArrayList<Metric> list) {
    	
    	metricList = list;
    }

    /**
     * Uma das formas de preparar a lista de métricas. Adiciona uma métrica à lista de métricas do MetricController. A lista de métricas
     * deve ser preparada durante a inicialização em qualquer sistema que utiliza o GitCURTAIN, ou por meio da função setUpMetricArray,
     * ou por meio da função addMetric.
     * 
     * @param metric A métrica a ser adicionada à lista de métricas.
     */
    public static void addMetric(Metric metric) {
    	
        metricList.add(metric);
    }

    /**
     * Remove uma métrica da lista de métricas do MetricController, que corresponde ao ID passado como parâmetro.
     * 
     * @param metricID Um valor inteiro representando o ID da métrica a ser removida.
     * 
     * @return A métrica removida.
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
     * Remove todas as métricas do MetricController, retornando o array com todas as métricas que foram removidas.
     * 
     * @return Um ArrayList de Metrics contendo a lista de métricas que foram removidas.
     */
    public static ArrayList<Metric> removeAllMetrics() {
    	ArrayList<Metric> oldList = metricList;
    	metricList = new ArrayList<Metric>();
    	return oldList;
    }

    /**
     * Ordena todas as métricas da lista de métricas a serem executadas, em ordem FIFO. Esta função é gerenciada pelo GitCURTAIN e, portanto,
     * não deve ser utilizada pelo sistema. Para executar as métricas, simplesmente execute a thread de métricas e elas serão executadas
     * automaticamente uma vez que novos dados sejam coletados para estas.
     * 
     * @param commitList Um ArrayList de Commits, que contém os commits que serão utilizados para cálculo de métricas.
     * 
     * @return Um ArrayList de MetricResults, com os resultados de cada uma das métricas.
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
     * Inicia a thread de cálculo de métricas do repositório. Esta função deve ser chamada por todo sistema que utiliza GitCURTAIN,
     * durante o processo de inicialização.
     * 
     */
    public static void beginMetricCalculation() {
    	MetricThread metricsThread = new MetricThread();
    	metricsThread.start();
    }
    
}