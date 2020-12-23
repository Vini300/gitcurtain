package gitcurtain.visualization;

import java.util.ArrayList;

import javax.swing.JFrame;

import gitcurtain.utils.MetricResult;

/**
 * Uma interface que define como deve ser gerada a visualização final com base nos dados das métricas. Esta visualização pode ser tanto
 * uma única visualização quanto um dashboard completo, desde que esteja contida dentro de um JFrame. Para criar uma extensão customizada
 * do GitCURTAIN que crie a visualização de forma diferente, basta criar uma nova classe implementando esta interface.
 * 
 * @author Vinícius Soares
 *
 */
public interface Visualization {

	/**
	 * Uma função que deve ser implementada pela estratégia de preparação de metadados de um repositório. Desenha a visualização no
	 * JFrame especificado. Este desenho substitui completamente uma visualização anterior, se existir, com a nova visualização,
	 * permitindo que os dados sejam atualizados dinamicamente.
	 * 
	 * @param frame O JFrame onde deve ser desenhada a visualização.
	 * @param metricList Um ArrayList de MetricResults contendo os resultados das aplicações das métricas sobre os commits.
	 */
	public void draw(JFrame frame, ArrayList<MetricResult> metricList);

}