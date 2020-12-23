package gitcurtain.metrics;

import java.util.ArrayList;

import gitcurtain.utils.Commit;
import gitcurtain.utils.MetricResult;

/**
 * Representa a implementação de uma métrica no GitCURTAIN. Cada métrica customizada criada pelo desenvolvedor do sistema feito sobre
 * GitCURTAIN deve ser feita por uma classe que implementa a interface MetricStrategy, e deve ser criada por uma inicialização da classe
 * Metric antes da inicialização do MetricController.
 * 
 * @author Vinícius Soares
 *
 */
public class Metric {
	
    /**
     * O ID único da métrica.
     */
    private int id;
    /**
     * A estratégia customizada que determina o cálculo da métrica. Mais informações de como extender o GitCURTAIN com mais
     * estratégias de métricas estão na interface MetricStrategy. Por padrão, o GitCURTAIN contém a estratégia SelfAffirmedRefactoringMetric,
     * que calcula a percentagem de commits que contém um <i>Self-Affirmed Refactoring</i>, e a percentagem que não.
     */
    private MetricStrategy metricFunction;

    /**
     * Inicializa a métrica, determinando seu ID único, e a estratégia de cálculo a ser utilizada.
     * 
     * @param id O ID único da métrica.
     * @param metricFunction A estratégia customizada que determina o cálculo da métrica.
     */
    public Metric(int id, MetricStrategy metricFunction) {
        this.id = id;
        this.metricFunction = metricFunction;
    }

    /**
     * Executa a métrica, retornando um valor customizado MetricResult.
     * 
     * @param commits Um ArrayList de Commits que contém os commits a serem analisados pela métrica.
     * 
     * @return Um valor customizado MetricResult. Mais informações sobre MetricResult estão na documentação da interface MetricResult.
     */
    public MetricResult executeMetric(ArrayList<Commit> commits) {
        return metricFunction.executeMetric(commits);
    }

    /**
     * Obtém o ID único da métrica.
     * 
     * @return O valor do ID único da métrica.
     */
    public int getID() {
        return id;
    }

}