package HSRzemberekless;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

public class WekaFunctions {
	
	public void crossValidateModel(String locTrain, int fold, int classIndex){
		try {
			System.out.println("locTrain => \t" + locTrain);
			BufferedReader rb = new BufferedReader(new FileReader(locTrain));
			Instances train = new Instances(rb);
			train.setClassIndex(train.numAttributes()-1);
			rb.close();
			
			NaiveBayes naive = new NaiveBayes();
			naive.buildClassifier(train);
			
			Evaluation ev = new Evaluation(train);
			ev.crossValidateModel(naive, train, fold, new Random(1));
			System.out.println(ev.toSummaryString("Result =>\t", true));
			System.out.println("fMeasure=>"+ ev.fMeasure(classIndex)
				+"\nprecision=>"+ ev.precision(classIndex) 
				+"\trecall=>"+ ev.recall(classIndex)
				+"\nTP=>"+ ev.truePositiveRate(classIndex)
				+"\tTN=>"+ ev.trueNegativeRate(classIndex)
				+"\nFP=>"+ ev.falsePositiveRate(classIndex)
				+"\tFN=>"+ ev.falseNegativeRate(classIndex));
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
