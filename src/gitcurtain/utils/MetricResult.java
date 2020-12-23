package gitcurtain.utils;

import java.io.Serializable;

import java.util.ArrayList;

/**
 * Uma interface que define um objeto que contém os resultados de alguma estratégia de cálculo de métrica. Cada estratégia customizada
 * adicionada ao GitCURTAIN deve possuir um MetricResult equivalente, para que os resultados possam ser descompactados corretamente
 * durante o processo de criação da visualização.
 * 
 * @author Vinícius Soares
 *
 */
public interface MetricResult extends Serializable {

    /**
     * Uma função que deve ser implementada pela estratégia de preparação de metadados de um repositório. Esta função retorna um único
     * objeto que será tratado futuramente por uma visualização customizada.
     * 
     * @return Um objeto qualquer, que contém os dados relevantes para a criação da visualização.
     */
    public Object getValues();

    /**
     * Uma função que deve ser implementada pela estratégia de preparação de metadados de um repositório. Esta função retorna a lista de
     * hashes dos commits que estão relacionados a esta métrica.
     * 
     * @return Um ArrayList de Strings, contendo a lista de hashes de commits relacionados à métrica em questão.
     */
    public ArrayList<String> getCommitHashes();

}