package gitcurtain.metrics;

import java.util.ArrayList;

import gitcurtain.utils.Commit;
import gitcurtain.utils.MetricResult;

/**
 * Uma interface que define como deve ser criada uma estratégia de cálculo de métricas sobre os dados dos commits durante a execução
 * do GitCURTAIN. Para criar uma extensão customizada do GitCURTAIN que faça estes cálculos de outra forma, basta criar uma nova
 * classe implementando esta interface, e uma classe correspondente implementando o MetricResult que deve ser retornado pela função
 * executeMetric.
 * 
 * @author Vinícius Soares
 *
 */
public interface MetricStrategy {

    /**
     * Uma função que deve ser implementada pela estratégia de preparação de metadados de um repositório. Esta função executa os cálculos
     * da métrica sobre a lista total de commits, e retorna então um objeto da classe MetricResult que contém os resultados desta
     * execução, e quais commits realmente foram utilizados para o cálculo da métrica.
     * 
     * @param commits A lista total de commits.
     * 
     * @return O resultado da execução da métrica, contendo os valores finais e a lista de commits que realmente foram utilizados para o
     * cálculo destes valores.
     */
    public MetricResult executeMetric(ArrayList<Commit> commits);

}