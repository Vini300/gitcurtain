package gitcurtain.defaults;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.general.DefaultPieDataset;

import gitcurtain.utils.MetricResult;
import gitcurtain.visualization.Visualization;

import java.awt.Color;
import java.awt.Font;

/**
 * Uma implementação padrão de uma extensão da interface Visualization. Essa implementação apresenta um Pie Chart com a informação de qual
 * a percentagem de commits que contém SARs, e qual a percentagem de commits que não contém SARs.
 * 
 * @author Vinícius Soares
 *
 */
public class SelfAffirmedRefactoringPieChart implements Visualization {

	/**
	 * Descompacta os resultados das métricas, e então desenha a visualização final dos resultados no JFrame.
	 * 
	 * @param frame O JFrame que receberá a visualização
	 * @param metricList A lista de resultados de métricas a serem utilizados para gerar a visualização final.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public void draw(JFrame frame, ArrayList<MetricResult> metricList) {
		
		System.out.println("Drawing - Self-Affirmed Refactoring Pie Chart...");
		
		DefaultPieDataset dataset = new DefaultPieDataset();
        
		MetricResult first = metricList.get(0);
		HashMap<String, ArrayList<String>> map = (HashMap<String, ArrayList<String>>) first.getValues();
		
		for (String key : map.keySet()) {
			
			int localTotal = 0;
			
			for (String str : map.get(key)) {
				localTotal += 1;
			}
			
			dataset.setValue(key, localTotal);
			
		}
		
		JFreeChart chart = ChartFactory.createPieChart("", dataset, true, true, false);
		chart.getLegend().setFrame(BlockBorder.NONE);
		
		PiePlot plot = (PiePlot) chart.getPlot();
		PieSectionLabelGenerator generator = new StandardPieSectionLabelGenerator("{2}");
		Color invisible = new Color(0.0f, 0.0f, 0.0f, 0.0f);
		Random randGen = new Random();
		
		plot.setSimpleLabels(true);
		plot.setLabelGenerator(generator);
		plot.setBackgroundPaint(Color.WHITE);
		plot.setOutlineVisible(false);
		plot.setLabelFont(new Font("Arial", Font.BOLD, 16));
		plot.setLabelPaint(Color.WHITE);
		plot.setLabelBackgroundPaint(invisible);
		plot.setLabelOutlinePaint(invisible);
		plot.setLabelShadowPaint(invisible);
		plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1}"));
		
		for (String key : map.keySet()) {
			plot.setSectionPaint(key, new Color(randGen.nextFloat(), randGen.nextFloat(), randGen.nextFloat()));
		}
		
		plot.setSectionOutlinesVisible(true);
		
		TextTitle legendText = new TextTitle("Number of SARs VS non-SARs");
		legendText.setPosition(RectangleEdge.BOTTOM);
		chart.addSubtitle(legendText);
		
		LegendTitle legend = chart.getLegend();
		legend.setPosition(RectangleEdge.RIGHT);
		
		ChartPanel chartPanel = new ChartPanel(chart);
		
		frame.getContentPane().removeAll();
	    frame.getContentPane().add(chartPanel);
	    frame.getContentPane().revalidate();
	    frame.repaint();
	    frame.setVisible(true);
	}

}
