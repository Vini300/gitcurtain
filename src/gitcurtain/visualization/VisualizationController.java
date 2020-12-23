package gitcurtain.visualization;

import java.util.ArrayList;

import javax.swing.JFrame;

import gitcurtain.utils.MetricResult;

/**
 * Gerencia todo o sistema de visualiza��o, permitindo que o desenvolvedor determine quais e como as visualiza��es s�o geradas com base
 * nos resultados das m�tricas originais.
 * 
 * Toda a comunica��o com as classes que gerenciam a visualiza��o diretamente deve ser feita por meio das fun��es da classe
 * VisualizationController.
 * 
 * @author Vin�cius Soares
 *
 */
public class VisualizationController {
	
    /**
     * A visualiza��o a ser gerada. Mais informa��es de como extender o GitCURTAIN com uma visualiza��o customizada est�o na interface
     * Visualization. Por padr�o, o GitCURTAIN cont�m a visualiza��o SelfAffirmedRefactoringPieChart, que representa a percentagem de
     * commits que cont�m um <i>Self-Affirmed Refactoring</i>, e a percentagem que n�o, por meio de um Pie Chart.
     */
    private static Visualization vis;
    /**
     * Um JFrame que cont�m a visualiza��o final.
     */
    private static JFrame frame;
    
    /**
     * Determina qual visualiza��o deve ser gerada pelo GitCURTAIN. Este m�todo deve ser chamado uma vez, durante a incializa��o, em
     * qualquer sistema que seja criado por meio do GitCURTAIN.
     * 
     * @param newVis A visualiza��o a ser gerada pelo GitCURTAIN.
     */
    public static void setVis(Visualization newVis) {
    	
    	vis = newVis;
    }

    /**
     * Desenha a visualiza��o pela primeira vez, abrindo uma nova janela que cont�m a mesma. Esta fun��o � gerenciada pelo GitCURTAIN e,
     * portanto, n�o deve ser utilizada pelo sistema.
     * 
     * @param metricList A lista de resultados das m�tricas executadas no passo anterior.
     */
    public static void renderVis(ArrayList<MetricResult> metricList) {
    	
    	frame = new JFrame();
    	frame.setSize(800,600);
    	frame.setVisible(true);
    	
        vis.draw(frame, metricList);
    }

    /**
     * Desenha a visualiza��o por cima de uma j� existente, mantendo a janela aberta. Esta fun��o � gerenciada pelo GitCURTAIN e, portanto,
     * n�o deve ser utilizada pelo sistema.
     * 
     * @param metricList A lista de resultados das m�tricas executadas no passo anterior.
     */
    public static void updateVis(ArrayList<MetricResult> metricList) {
    	
        vis.draw(frame, metricList);
    }
    
    /**
     * Inicia a thread de desenho da visualiza��o dos resultados de m�tricas mais recentes. Esta fun��o deve ser chamada por todo
     * sistema que utiliza GitCURTAIN, durante o processo de inicializa��o.
     * 
     */
    public static void startVisualization() {
    	VisualizationThread visThread = new VisualizationThread();
    	visThread.start();
    }

}