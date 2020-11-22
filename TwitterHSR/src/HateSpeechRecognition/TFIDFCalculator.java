package HateSpeechRecognition;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.Ordering;

public class TFIDFCalculator {

	protected static DbProcess dbprocess;
	private static List<List<String>> documents = null, trainWords = null;
	private static List<Double[]> counter = null;
	private static List<String> topWords = new ArrayList<String>();
	private static List<Double> topWordsTfIdf = new ArrayList<Double>();
	private static Double[][] FbaseArr = null;
	private static int docsSize = 0, trainingOrTestSet = 0;
	private static WriteIntoFile writeIntoFile = null;
	private static int topSize = 100;
	
	TFIDFCalculator(List<List<String>> tWords, List<List<String>> docs, List<Double[]> counter2, int trainingOrTest){
		dbprocess = new DbProcess();
		new WekaFunctions();
		trainWords = new ArrayList<List<String>>(); trainWords = tWords;
		TFIDFCalculator.counter = counter2; 
		trainingOrTestSet = trainingOrTest;
		documents = docs;
		docsSize = documents.size();
		createTFarray();
		calculateTfIdf(trainWords, docsSize);
	}
	
	private void createTFarray(){
		FbaseArr = new Double[docsSize][docsSize+7];
		for (int i = 0; i < FbaseArr.length; i++) {
			for (int j = 0; j < FbaseArr[i].length; j++) {
				FbaseArr[i][j] = Double.valueOf("0");
			}
		}
	}
	
	private void calculateTfIdf(List<List<String>> words, int topSize){
		HashMap<String, Double> valueOfTF_IDF = new HashMap<String, Double>();
    	//for (int d = 0; d < words.size(); d++) {
    		List<String> dataList = words.get(0); /* Hate Training */
            for (int i=0; i<dataList.size(); i++){
            	String word = dataList.get(i);
            	valueOfTF_IDF.put(word, tfIdf(dataList, words, word));
            }
    	//}
    	Map<String, Double> sortedMap = sortByComparator(valueOfTF_IDF, false);
    	sortedMap.remove(Arrays.asList(null,""));
    	printMap(sortedMap);
    	if(topSize<sortedMap.size()){
	    	topWords = getTopKeys(sortedMap, topSize);
	    	topWordsTfIdf = new ArrayList(sortedMap.values()).subList(0,topSize);

	    	writeIntoFile = new WriteIntoFile("Top"+topSize+".txt");
	    	for(int i=0; i<topWords.size(); i++) {
	    		String write = (i+". word : "+topWords.get(i) + " => " + topWordsTfIdf.get(i));
	    		System.out.println(write);
	    		writeIntoFile.WriteIntoFile(write);
    		   //System.out.println("Umut = > " + sortedMap.get("umut") + " = 0.0685079865417749" );
    		   //System.out.println("keþke un => " + sortedMap.get("keþke un") + " = 0.022835995513924963");
        	}
	    	writeIntoFile.CloseFile();
    	}
    }
    
    protected void calculate() throws ClassNotFoundException, SQLException{
    	writeIntoFile = new WriteIntoFile("WordsFromZemberek.txt");
    	
    	for (int d = 0; d < documents.size(); d++) {
    		List<String> doc = documents.get(d);
            for (int i=0; i<topWords.size(); i++){
            	String word = topWords.get(i);
            	if(doc.contains(word)){
                	writeIntoFile.WriteIntoFile(word);
                	//System.out.println(word);
                	double value = count(doc, word);
                	FbaseArr[d][i] = value;
            	}
            }
            // Tag : Hate or not?
            FbaseArr[d][FbaseArr[d].length-1] = counter.get(d)[0].doubleValue();
            // sentencesCount
        	FbaseArr[d][FbaseArr[d].length-2] = counter.get(d)[1].doubleValue();
            // wordsCount
        	FbaseArr[d][FbaseArr[d].length-3] = counter.get(d)[2].doubleValue();
            //syllablesCount
        	FbaseArr[d][FbaseArr[d].length-4] = counter.get(d)[3].doubleValue();
            // charactersCount
        	FbaseArr[d][FbaseArr[d].length-5] = counter.get(d)[4].doubleValue();
        	// FleschKincaidGradeLevel
        	FbaseArr[d][FbaseArr[d].length-6] = counter.get(d)[5].doubleValue();
        	// FleschReadingEase
        	FbaseArr[d][FbaseArr[d].length-7] = counter.get(d)[6].doubleValue();
        }
    	writeIntoFile.CloseFile();
    	String nameOfFile = (trainingOrTestSet==0)?"Training_":"Testing_";
    	WriteCSVfile file = new WriteCSVfile(nameOfFile, topWords, FbaseArr);
    	file.writeArray();
    	file.writeArffFile();
    	
    	// weka.crossValidateModel(locTrain, 4, 1);
	}
	
	/**
     * @param doc  count of words in a document
     * @param term String represents a term
     * @return term frequency of term in document
     */
	private int count(List<String> doc, String term) {
        int result = 0;
        for (String word : doc) {
            if (term.equalsIgnoreCase(word))
                result++;
        }
        return result;
    }
	
	/**
     * @param doc  list of strings
     * @param term String represents a term
     * @return term frequency of term in document
     */
    public double tf(List<String> doc, String term) {
        double result = 0;
        for (String word : doc) {
            if (term.equalsIgnoreCase(word))
                result++;
        }
        return result * 1.0 / doc.size();
    }

    /**
     * @param docs list of list of strings represents the dataset
     * @param term String represents a term
     * @return the inverse term frequency of term in documents
     */
    public double idf(List<List<String>> docs, String term) {
        double n = 0;
        for (List<String> doc : docs) {
            for (String word : doc) {
                if (term.equalsIgnoreCase(word)) {
                    n++;
                    break;
                }
            }
        }
        return Math.log(docs.size() / n);
    }

    /**
     * @param doc  a text document
     * @param docs all documents
     * @param term term
     * @return the TF-IDF of term
     */
    public double tfIdf(List<String> doc, List<List<String>> docs, String term) {
        return tf(doc, term) * idf(docs, term);

    }

    public static Double[][] getFbaseArr() { return FbaseArr; }
    
    public static <K, V extends Comparable<?>> List<K> getTopKeys(Map<K, V> map, int n) {
	  Function<K, V> getVal = Functions.forMap(map);
	  Ordering<K> ordering = Ordering.natural().onResultOf(getVal);
	  return ordering.greatestOf(map.keySet(), n);
	}
    
    private static Map<String, Double> sortByComparator(Map<String, Double> unsortMap, boolean order){
    	List<Entry<String, Double>> list = new LinkedList<Entry<String, Double>>(unsortMap.entrySet());
    	// Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Double>>(){
            public int compare(Entry<String, Double> o1, Entry<String, Double> o2){
                if (order){ return o1.getValue().compareTo(o2.getValue()); }
                else { return o2.getValue().compareTo(o1.getValue()); }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        for (Entry<String, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
    
    public <K, V> void printMap(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey() 
				+ " Value : " + entry.getValue());
        }
    }
}
