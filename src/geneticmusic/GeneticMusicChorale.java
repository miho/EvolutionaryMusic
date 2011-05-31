/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticmusic;

import geneticmusic.fitness.ChoraleFitnessFunction;
import geneticmusic.genes.ChoraleGene;
import geneticmusic.genes.NoteGenerator;
import geneticmusic.jmusic.bridge.ConverterUtil;
import javax.swing.JFrame;
import jm.JMC;
import jm.util.Write;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYBarDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.UnsupportedRepresentationException;
import org.jgap.impl.DefaultConfiguration;

/**
 *
 * @author daviden
 */
public class GeneticMusicChorale implements JMC {
    private static final int FIELD1 = 1;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InvalidConfigurationException, UnsupportedOperationException, UnsupportedRepresentationException {

        //configuration object
        Configuration cfg = new DefaultConfiguration();
        FitnessFunction fitnessF = new ChoraleFitnessFunction();
        cfg.setFitnessFunction(fitnessF);


        
        //**************create a sample cromossome************************
        
        Gene[] sampleGenes = new Gene[8];
        for(int i=0; i< sampleGenes.length; i++)
            sampleGenes[i] = new ChoraleGene(cfg);

      
        
        
        Chromosome sampleChromosome = new Chromosome(cfg, sampleGenes);

        cfg.setSampleChromosome(sampleChromosome);
        //***************************************************************
        
        
        System.out.println("Percentage selected from previous generations:"+cfg.getSelectFromPrevGen());
        //set population size
        cfg.setPopulationSize( 200 );
        
        //set note generator
        cfg.setRandomGenerator(new NoteGenerator());
        
        
        //construct a population genotype
        Genotype population = Genotype.randomInitialGenotype( cfg );
        
        
        
        
        
        //// evolve and evaluate
        double currentFitness = 0.0;
        
        int i = 0;
        double lastFitness = 0.0;
        
        /***********************PRESENT DATA ON A CHART************************/
        XYSeries fitnessSeries = new XYSeries("Fittest Fitness");
        
       
         XYSeriesCollection dataset = new XYSeriesCollection(fitnessSeries);
        
         
         JFreeChart fitnessChart = ChartFactory.createXYLineChart("Fittest Fitness Evolution", 
                                                                "Generation", 
                                                                "Fitness", dataset, 
                                                                PlotOrientation.VERTICAL,
                                                                true, //legend
                                                                true, //tooltips
                                                                false); //url
        
        //add chart to panel
       ChartPanel chartPanel = new ChartPanel(fitnessChart);
       JFrame chartFrame = new JFrame("Fittests Fitness");
       chartFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       chartFrame.setContentPane(chartPanel);
       chartFrame.pack();
       chartFrame.setVisible(true);
        
        do{
            lastFitness = currentFitness;
            population.evolve();
            currentFitness = population.getFittestChromosome().getFitnessValue();
            
            fitnessSeries.add(i, currentFitness);//update series
            //System.out.println("Current fitness: "+currentFitness);
            i++;
            
            //System.out.println(i);
        }while(i<100);
        
       
       
        
       
        IChromosome chm = population.getFittestChromosome();
        
        
        System.out.println(chm.toString());
        Write.midi(ConverterUtil.getChoraleScore(chm), "test_chorale.mid");
        


    }
}