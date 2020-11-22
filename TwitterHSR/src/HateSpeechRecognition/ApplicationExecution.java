package HateSpeechRecognition;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import twitter4j.Twitter;
import zemberek.morphology.analysis.tr.TurkishMorphology;

public class ApplicationExecution extends Thread {

	/* 
	 *  This class is directly related to authenticate Twitter.
	 * We use that class for the process such as twitter authentication, configuration, and accessing our list of terms in the database.
	 */
	protected static TwitterProccess twProcess = null;
	
	/* 
	 * This class is directly related to connect Twitter.
	 * We use that class for the process such as closing database, accessing twitter connection and getting the list of terms in the database.
	 */
	protected static DbConnection dbConn = null;
	
	/* 
	 * This class is directly related to create the query for accessing database' data.
	 * We use that class for the process such as getting list of terms, finding term' id and inserting any Twitter data for our tables, 
	 */
	protected static DbProcess dbprocess = null;
	
	/*
	 * This class is directly related to format Twitter data, and purify them from stopWords.
	 * We use that class for the process such as getting list of terms, finding term' id and inserting any Twitter data for our tables, 
	 */
	protected static DataFormat dataFormat = null;
	
	/* 
	 * twitter4j.Twitter interface is that has some available functions such as searching, posting, getting a Tweet, OAuth etc. 
	 */
	protected static Twitter twitter = null;

    private static TurkishMorphology morphology;
	private static TFIDFCalculator calculator = null;
	private static N_Gram ngram;
	private static HashMap<Integer, String> hateTweets, nonHateTweets, dataSet;
	private static HashMap<Integer, String> trainingSet, testSet, hateTrainingData, nonHateTrainingData;
	private static Featuring featuring;
	private static String locTrain = "", locTest = "";
	private static WriteIntoFile writeIntoFile;
	/* hateTrain, nonhateTrain, hateTest, nonHateTest */
	private static List<HashMap<Integer, String>> data = new ArrayList<HashMap<Integer, String>>();
	
	protected ApplicationExecution(){
		dbprocess = new DbProcess();
		dataFormat = new DataFormat();
		ngram = new N_Gram();
		
		trainingSet = new HashMap<Integer, String>();
		testSet = new HashMap<Integer, String>();
		hateTrainingData = new HashMap<Integer, String>();
		nonHateTrainingData = new HashMap<Integer, String>();
	}
	
	public static void execution(String[] args) {
		
		List<List<String>> trainWords = new ArrayList<List<String>>();
		List<List<String>> documents = new ArrayList<List<String>>();
		List<Double[]> counter = new ArrayList<Double[]>();
		List<String> doc = null, wordsList = null;
		try {
			initialize(args);
			for (int i = 0; i < 2; i++) { /* HateTrain and NonhateTrain */ 
				//writeIntoFile = new WriteIntoFile("ZemberekStems" + i + ".txt");
				wordsList = new ArrayList<String>();
				Set<?> set = data.get(i).entrySet(); /* hateTraining and nonHateTrain Data Set*/
				Iterator<?> iterator = set.iterator();
				while(iterator.hasNext()) {
					Map.Entry tweetMap = (Map.Entry)iterator.next();
					int tweetid = (int) tweetMap.getKey();
					String text = ((String) tweetMap.getValue()).toLowerCase();
					String[] words = text.trim().split("[^\\p{L}0-9#@']+");
					String stems = "";

		            int hateOrNot = (i==0)? 1 : 0; /* 1=hate 0=nonhate */ 
					Double[] tmpCounter = createFeatures(text, hateOrNot);
					for(String word : words){
						/* if its start with @ or #, delete it */
						word = dataFormat.formatData(word.trim()).trim();
						if(!word.isEmpty() & word.length()>1){
							//new Zemberek(morphology).analyze(word);
							//String stem = Zemberek.getStems().get(0); /* String lemma = Zemberek.getLemmas().get(0); */
							//System.out.println(Zemberek.getStems());
							//writeIntoFile.WriteIntoFile(Zemberek.getStems().toString());
							stems += word.trim() + " ";
						}
					}
					doc = ngram.ngrams(1, stems.trim());
					if(doc.size()>0){
						for (int n = 2;doc.size()>=n && n<=3; n++) {
							List<String> tmp = ngram.ngrams(n, stems.trim());
							if(tmp != null){ doc.addAll(tmp); }
				        }
						doc.removeAll(Arrays.asList(null,""));
						wordsList.addAll(doc);
						documents.addAll(Arrays.asList(doc));
						counter.add(tmpCounter);
					}
				}
				trainWords.add(wordsList);
			}
			// writeIntoFile.CloseFile();
			wordsList.removeAll(Arrays.asList(null,""));
			calculator = new TFIDFCalculator(trainWords, documents, counter, 0);
			calculator.calculate();
			Double[][] arr = calculator.getFbaseArr();
			// feature = "Fselection";
			// calculator.calculateFbase(feature);
			// featuring.Readability(FeaturingArray);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("HATA !!! Exception : " + e.getMessage());
		}
	}
	
	/* initialize Twitter
	 * create dataSet that has a equal size for hateTweets and nonHateTweets such as Hate: 159 & Nonhate: 159 
	 * set dataSet randomly
	 */ 
	private static void initialize(String[] args) throws ClassNotFoundException, SQLException, IOException {
		morphology = TurkishMorphology.createWithDefaults();
		/* Authenticate Twitter to access its data by using the information of consumer and accessToken as a arg parameter. Set the Configuration for twitter by using these arg parameters */
		twProcess = TwitterProccess.authenticate(args);
		/* Get some details such as connection, twitter object and our term list */
		dbConn = TwitterProccess.conn;
		twitter = twProcess.getTwitter();
		
		dbprocess.separateTweets();
		hateTweets = dbprocess.getHateTweets();
		nonHateTweets = dbprocess.getNonHateTweets();
		createTrainingTestData(); 
		createTraining_TestingData();

		locTrain = args[2];
		locTest = args[3];
	}
	
	private static Double[] createFeatures(String text, int hateOrNot){
		Double[] features = new Double[7];
		featuring = new Featuring(text);
		features[0] = (double) hateOrNot;
		features[1] = (double) featuring.getSentences();
		features[2] = (double) featuring.getWords();
		features[3] = (double) featuring.getSyllables();
		features[4] = (double) featuring.getCharacters();
		features[5] = featuring.getFleschKincaidGradeLevel();
		features[6] = featuring.getFleschReadingEase();
		return features;
	}
	
	private static void createTrainingTestData(){
		int hateSize = hateTweets.size(), nonHateSize=nonHateTweets.size(); 
		//System.out.print(hateTweets.size()+"_hatanonhate_"+nonHateSize+"=");
		if( hateSize != nonHateSize){
			HashMap<Integer, String> tweets = new HashMap<Integer, String>();
			Set<?> set = (hateSize < nonHateSize) ? nonHateTweets.entrySet() : hateTweets.entrySet();
			Iterator<?> iterator = set.iterator();
			
			for (int i=0; i<((hateSize<nonHateSize)?hateSize:nonHateSize)&&iterator.hasNext(); i++) {
				Map.Entry tweetMap = (Map.Entry)iterator.next();
				tweets.put((Integer)tweetMap.getKey(), tweetMap.getValue().toString());
			}
			
			dataSet = (hateSize<nonHateSize) ? concateMap(tweets, hateTweets) : concateMap(tweets, nonHateTweets);
		} else{
			dataSet = concateMap(nonHateTweets, hateTweets);
		}
		dataSet = getRandomFromHashMap(dataSet);
	}
	
	private static void createTraining_TestingData(){
		Set<?> set = dataSet.entrySet();
		Iterator<?> iterator = set.iterator();
		for (int i = 0; iterator.hasNext(); i++) {
			Map.Entry tweetMap = (Map.Entry)iterator.next();
			if(i < dataSet.size() * 3 / 4){
				trainingSet.put((Integer)tweetMap.getKey(), tweetMap.getValue().toString());
			} else{
				testSet.put((Integer)tweetMap.getKey(), tweetMap.getValue().toString());
			}
		}
		data = divideHateNonhateFromTraining(trainingSet, testSet, hateTrainingData, nonHateTrainingData);
	}
	
	private static List<HashMap<Integer, String>> divideHateNonhateFromTraining (HashMap<Integer, String> train, HashMap<Integer, String> test, HashMap<Integer, String> hate, HashMap<Integer, String> nonHate){
		List<HashMap<Integer, String>> list = new ArrayList<HashMap<Integer, String>>();
		for (int ite = 0; ite < 2; ite++) {
			Set<?> set = (ite==0)?train.entrySet():test.entrySet();
			Iterator<?> iterator = set.iterator();
			for (int i = 0; iterator.hasNext(); i++) {
				Map.Entry dataIte = (Map.Entry)iterator.next();
				if(hateTweets.containsKey(dataIte.getKey())){
					hate.put(Integer.valueOf(dataIte.getKey().toString()), dataIte.getValue().toString());
				} else{
					nonHate.put(Integer.valueOf(dataIte.getKey().toString()), dataIte.getValue().toString());
				}
			}
			list.add(hate);
			list.add(nonHate);
		}
		return list;
	}
	
	private static HashMap<Integer, String> concateMap(HashMap<Integer, String> h1, HashMap<Integer, String> h2){
		return (HashMap<Integer, String>) Stream.of(h1, h2).flatMap(m -> m.entrySet().stream())
			       .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}
	
	/* Get a random entry from the HashMap. */
	private static HashMap<Integer, String> getRandomFromHashMap(HashMap<Integer, String> map){
		HashMap<Integer, String> tmp = new HashMap<Integer, String>();
		List<Map.Entry<Integer, String>> list = new ArrayList<Map.Entry<Integer, String>>((Collection<? extends Entry<Integer, String>>) map.entrySet());
		Collections.shuffle(list);
		for (Map.Entry<Integer, String> entry : list) {
			tmp.put(entry.getKey(), entry.getValue());
		}
		return tmp;
		 
	}
}
