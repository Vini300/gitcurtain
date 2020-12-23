package gitcurtain.metrics;

import java.util.ArrayList;

import gitcurtain.utils.Commit;
import gitcurtain.utils.MetricResult;

/**
 * Representa a implementa��o de uma m�trica no GitCURTAIN. Cada m�trica customizada criada pelo desenvolvedor do sistema feito sobre
 * GitCURTAIN deve ser feita por uma classe que implementa a interface MetricStrategy, e deve ser criada por uma inicializa��o da classe
 * Metric antes da inicializa��o do MetricController.
 * 
 * @author Vin�cius Soares
 *
 */
public class Metric {
	
    /**
     * O ID �nico da m�trica.
     */
    private int id;
    /**
     * A estrat�gia customizada que determina o c�lculo da m�trica. Mais informa��es de como extender o GitCURTAIN com mais
     * estrat�gias de m�tricas est�o na interface MetricStrategy. Por padr�o, o GitCURTAIN cont�m a estrat�gia SelfAffirmedRefactoringMetric,
     * que calcula a percentagem de commits que cont�m um <i>Self-Affirmed Refactoring</i>, e a percentagem que n�o.
     */
    private MetricStrategy metricFunction;

    /**
     * Inicializa a m�trica, determinando seu ID �nico, e a estrat�gia de c�lculo a ser utilizada.
     * 
     * @param id O ID �nico da m�trica.
     * @param metricFunction A estrat�gia customizada que determina o c�lculo da m�trica.
     */
    public Metric(int id, MetricStrategy metricFunction) {
        this.id = id;
        this.metricFunction = metricFunction;
    }

    /**
     * Executa a m�trica, retornando um valor customizado MetricResult.
     * 
     * @param commits Um ArrayList de Commits que cont�m os commits a serem analisados pela m�trica.
     * 
     * @return Um valor customizado MetricResult. Mais informa��es sobre MetricResult est�o na documenta��o da interface MetricResult.
     */
    public MetricResult executeMetric(ArrayList<Commit> commits) {
        return metricFunction.executeMetric(commits);
    }

    /**
     * Obt�m o ID �nico da m�trica.
     * 
     * @return O valor do ID �nico da m�trica.
     */
    public int getID() {
        return id;
    }

}