package gitcurtain.utils;

import java.io.Serializable;

import java.util.ArrayList;

/**
 * Uma interface que define um objeto que cont�m os resultados de alguma estrat�gia de c�lculo de m�trica. Cada estrat�gia customizada
 * adicionada ao GitCURTAIN deve possuir um MetricResult equivalente, para que os resultados possam ser descompactados corretamente
 * durante o processo de cria��o da visualiza��o.
 * 
 * @author Vin�cius Soares
 *
 */
public interface MetricResult extends Serializable {

    /**
     * Uma fun��o que deve ser implementada pela estrat�gia de prepara��o de metadados de um reposit�rio. Esta fun��o retorna um �nico
     * objeto que ser� tratado futuramente por uma visualiza��o customizada.
     * 
     * @return Um objeto qualquer, que cont�m os dados relevantes para a cria��o da visualiza��o.
     */
    public Object getValues();

    /**
     * Uma fun��o que deve ser implementada pela estrat�gia de prepara��o de metadados de um reposit�rio. Esta fun��o retorna a lista de
     * hashes dos commits que est�o relacionados a esta m�trica.
     * 
     * @return Um ArrayList de Strings, contendo a lista de hashes de commits relacionados � m�trica em quest�o.
     */
    public ArrayList<String> getCommitHashes();

}