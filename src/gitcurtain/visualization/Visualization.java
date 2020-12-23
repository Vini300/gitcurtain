package gitcurtain.visualization;

import java.util.ArrayList;

import javax.swing.JFrame;

import gitcurtain.utils.MetricResult;

/**
 * Uma interface que define como deve ser gerada a visualiza��o final com base nos dados das m�tricas. Esta visualiza��o pode ser tanto
 * uma �nica visualiza��o quanto um dashboard completo, desde que esteja contida dentro de um JFrame. Para criar uma extens�o customizada
 * do GitCURTAIN que crie a visualiza��o de forma diferente, basta criar uma nova classe implementando esta interface.
 * 
 * @author Vin�cius Soares
 *
 */
public interface Visualization {

	/**
	 * Uma fun��o que deve ser implementada pela estrat�gia de prepara��o de metadados de um reposit�rio. Desenha a visualiza��o no
	 * JFrame especificado. Este desenho substitui completamente uma visualiza��o anterior, se existir, com a nova visualiza��o,
	 * permitindo que os dados sejam atualizados dinamicamente.
	 * 
	 * @param frame O JFrame onde deve ser desenhada a visualiza��o.
	 * @param metricList Um ArrayList de MetricResults contendo os resultados das aplica��es das m�tricas sobre os commits.
	 */
	public void draw(JFrame frame, ArrayList<MetricResult> metricList);

}