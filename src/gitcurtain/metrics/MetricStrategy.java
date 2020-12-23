package gitcurtain.metrics;

import java.util.ArrayList;

import gitcurtain.utils.Commit;
import gitcurtain.utils.MetricResult;

/**
 * Uma interface que define como deve ser criada uma estrat�gia de c�lculo de m�tricas sobre os dados dos commits durante a execu��o
 * do GitCURTAIN. Para criar uma extens�o customizada do GitCURTAIN que fa�a estes c�lculos de outra forma, basta criar uma nova
 * classe implementando esta interface, e uma classe correspondente implementando o MetricResult que deve ser retornado pela fun��o
 * executeMetric.
 * 
 * @author Vin�cius Soares
 *
 */
public interface MetricStrategy {

    /**
     * Uma fun��o que deve ser implementada pela estrat�gia de prepara��o de metadados de um reposit�rio. Esta fun��o executa os c�lculos
     * da m�trica sobre a lista total de commits, e retorna ent�o um objeto da classe MetricResult que cont�m os resultados desta
     * execu��o, e quais commits realmente foram utilizados para o c�lculo da m�trica.
     * 
     * @param commits A lista total de commits.
     * 
     * @return O resultado da execu��o da m�trica, contendo os valores finais e a lista de commits que realmente foram utilizados para o
     * c�lculo destes valores.
     */
    public MetricResult executeMetric(ArrayList<Commit> commits);

}