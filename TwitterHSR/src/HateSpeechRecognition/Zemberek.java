package HateSpeechRecognition;

import zemberek.morphology.analysis.WordAnalysis;
import zemberek.morphology.analysis.tr.*;
import java.io.IOException;
import java.util.List;

public class Zemberek {
    private TurkishMorphology morphology;
    private static List<String> stems;
	private static List<String> lemmas;
    
	public Zemberek(TurkishMorphology morphology) {
        this.morphology = morphology;
    }

    public TurkishMorphology getMorphology() {
		return morphology;
	}

    public static List<String> getStems() {
		return stems;
	}

	public static void setStems(List<String> stems) {
		Zemberek.stems = stems;
	}

	public static List<String> getLemmas() {
		return lemmas;
	}

	public static void setLemmas(List<String> lemmas) {
		Zemberek.lemmas = lemmas;
	}

    public void analyze(String word) {
      //  System.out.println("Word = " + word);

        //System.out.println("Parses: ");
        List<WordAnalysis> results = morphology.analyze(word);
        for (WordAnalysis result : results) {
           // System.out.println(result.formatLong());
            setStems(result.getStems());
            setLemmas(result.getLemmas());
            
        }
    }
}