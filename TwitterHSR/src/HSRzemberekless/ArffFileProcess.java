package HSRzemberekless;

import weka.core.converters.ArffLoader;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.unsupervised.attribute.Remove;

import java.io.File;
import java.io.IOException;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;

public class ArffFileProcess {
	
	private static Instances train, test;
	private static String trainLocation = "", testLocation = "";
	
	protected ArffFileProcess(String trainDataLocation, String testDataLocation){
		try {
			trainLocation = trainDataLocation;
			testLocation = testDataLocation;
			train= new DataSource(trainDataLocation).getDataSet();
			test = new DataSource(testDataLocation).getDataSet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void trainWithNaiveBayes(){
		try {
			// load data
			ArffLoader loader = new ArffLoader();
			loader.setFile(new File(trainLocation));
			Instances structure = loader.getStructure();
			structure.setClassIndex(structure.numAttributes() - 1);
			 
			// train NaiveBayes
			NaiveBayesUpdateable nb = new NaiveBayesUpdateable();
			nb.buildClassifier(structure);
			Instance current;
			while ((current = loader.getNextInstance(structure)) != null)
				nb.updateClassifier(current);
			
			trainThenEvaluate(nb);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void avaliarPerfomance(Instances ins, Classifier nb) throws Exception{
        
        Evaluation evaluation = new Evaluation(ins);
        evaluation.evaluateModel(nb, ins);
        
        String result = evaluation.toSummaryString();
        System.out.println("Result (toSummaryString):\n" + result);
        
        String confusionMatrix = evaluation.toMatrixString();
        System.out.println("Result (toMatrixString):\n" + confusionMatrix);

        String accuracy = evaluation.toClassDetailsString();
        System.out.println("Result (toClassDetailsString):\n" + accuracy);
        
    }

	/*
	 * a J48 is instantiated, trained and then evaluated
	 */
	protected void trainThenEvaluate(Classifier method){
		String[] options = new String[2];
		options[0] = "-t";
		options[1] = trainLocation;
		try {
			System.out.println(Evaluation.evaluateModel(method, options));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
