package gitcurtain.visualization;

import java.util.ArrayList;

import javax.swing.JFrame;

import gitcurtain.utils.MetricResult;

/**
 * Gerencia todo o sistema de visualização, permitindo que o desenvolvedor determine quais e como as visualizações são geradas com base
 * nos resultados das métricas originais.
 * 
 * Toda a comunicação com as classes que gerenciam a visualização diretamente deve ser feita por meio das funções da classe
 * VisualizationController.
 * 
 * @author Vinícius Soares
 *
 */
public class VisualizationController {
	
    /**
     * A visualização a ser gerada. Mais informações de como extender o GitCURTAIN com uma visualização customizada estão na interface
     * Visualization. Por padrão, o GitCURTAIN contém a visualização SelfAffirmedRefactoringPieChart, que representa a percentagem de
     * commits que contém um <i>Self-Affirmed Refactoring</i>, e a percentagem que não, por meio de um Pie Chart.
     */
    private static Visualization vis;
    /**
     * Um JFrame que contém a visualização final.
     */
    private static JFrame frame;
    
    /**
     * Determina qual visualização deve ser gerada pelo GitCURTAIN. Este método deve ser chamado uma vez, durante a incialização, em
     * qualquer sistema que seja criado por meio do GitCURTAIN.
     * 
     * @param newVis A visualização a ser gerada pelo GitCURTAIN.
     */
    public static void setVis(Visualization newVis) {
    	
    	vis = newVis;
    }

    /**
     * Desenha a visualização pela primeira vez, abrindo uma nova janela que contém a mesma. Esta função é gerenciada pelo GitCURTAIN e,
     * portanto, não deve ser utilizada pelo sistema.
     * 
     * @param metricList A lista de resultados das métricas executadas no passo anterior.
     */
    public static void renderVis(ArrayList<MetricResult> metricList) {
    	
    	frame = new JFrame();
    	frame.setSize(800,600);
    	frame.setVisible(true);
    	
        vis.draw(frame, metricList);
    }

    /**
     * Desenha a visualização por cima de uma já existente, mantendo a janela aberta. Esta função é gerenciada pelo GitCURTAIN e, portanto,
     * não deve ser utilizada pelo sistema.
     * 
     * @param metricList A lista de resultados das métricas executadas no passo anterior.
     */
    public static void updateVis(ArrayList<MetricResult> metricList) {
    	
        vis.draw(frame, metricList);
    }
    
    /**
     * Inicia a thread de desenho da visualização dos resultados de métricas mais recentes. Esta função deve ser chamada por todo
     * sistema que utiliza GitCURTAIN, durante o processo de inicialização.
     * 
     */
    public static void startVisualization() {
    	VisualizationThread visThread = new VisualizationThread();
    	visThread.start();
    }

}