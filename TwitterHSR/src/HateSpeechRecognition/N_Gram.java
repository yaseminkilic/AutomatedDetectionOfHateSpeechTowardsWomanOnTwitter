package HateSpeechRecognition;

import java.util.ArrayList;
import java.util.List;

public class N_Gram {
	public List<String> ngrams(int n, String str) {
		List<String> ngrams = new ArrayList<String>();
	    String[] words = str.trim().split(" ");
        for (int i = 0; i < words.length - n + 1; i++)
            ngrams.add(concat(words, i, i+n));
        return ngrams.size()>0 ? ngrams : null;
	}
	
	private String concat(String[] words, int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++)
            sb.append((i > start ? " " : "") + words[i]);
        return sb.toString();
	}
}
