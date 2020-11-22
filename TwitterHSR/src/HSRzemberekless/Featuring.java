package HSRzemberekless;

import java.math.BigDecimal;

public class Featuring {

	Integer sentences;
	Integer words;
	Integer syllables;
	Integer characters;

	public Featuring(String text) { 
		this.sentences = getNumberOfSentences(text + ".");
		this.words = getNumberOfWords(text);
		this.syllables = getNumberOfSyllables(text);
		this.characters = getNumberOfCharacters(text);
		if(text!=null){
			getFleschKincaidGradeLevel();
		}
	}
	
	int getCharacters() {
		return characters;
	}

	int getSentences() {
		return sentences;
	}

	int getSyllables() {
		return syllables;
	}

	int getWords() {
		return words;
	}

	/**
	 * Returns the number of letter characters in the text
	 * 
	 * @return
	 */
	private static int getNumberOfCharacters(String text) {
		String[] word = text.trim().split(" ");
		int characters = 0;
		for (String w : word) {
			characters += w.length();
		}
		return characters;
	}

	private static int getNumberOfSentences(String text) {
		if(text.length()<=1) return 1;
		int sentenceCount = 0;
		String delimiters = "?!.";
		for (int i = 1; ((text.length()>1)&&(i<text.length())); i++) {
	        if ((delimiters.indexOf(text.charAt(i))!=-1)&&(text.charAt(i-1)!='?')&&(text.charAt(i-1)!='!')&&(text.charAt(i-1)!='.')) {
	        	sentenceCount++;
	        }
	    }
		return sentenceCount;
	}

	private static int getNumberOfWords(String text) {
		//String cleanText = cleanLine(text);
		String[] word = text.trim().split("\\.|\\s+");
		int words = 0;
		for (String w : word) {
			if (w.length() > 0)
				words++;
		}
		return words;
	}

	/**
	 * Returns the total number of syllables in the words of the text
	 * 
	 * @param text
	 * @return the total number of syllables in the words of the text
	 */
	private static int getNumberOfSyllables(String text) {
		String[] word = text.trim().split("\\.|\\s+");
		int syllables = 0;
		for (String w : word) {
			if (w.length() > 0) {
				syllables += Syllabify.syllable(w);
			}
		}
		return syllables;
	}

	/**
	 * 
	 * http://en.wikipedia.org/wiki/Flesch-Kincaid_Readability_Test
	 * 
	 * @param text
	 * @return Returns the Flesch_Reading Ease value for the text
	 */
	double getFleschReadingEase() {
		double score = 206.835 - 1.015 * words / sentences - 84.6 * syllables
				/ words;
		return round(score, 3);
	}

	/**
	 * 
	 * http://en.wikipedia.org/wiki/Flesch-Kincaid_Readability_Test
	 * 
	 * @param text
	 * @return Returns the Flesch-Kincaid_Readability_Test value for the text
	 */
	 double getFleschKincaidGradeLevel() {
		double score = 0.39 * words / sentences + 11.8 * syllables / words - 15.59;
		//System.out.println(score + " ___ getFleschKincaidGradeLevel ___");
		return score;//round(score, 3);
	}

	/**
	 * 
	 * http://en.wikipedia.org/wiki/Automated_Readability_Index
	 * 
	 * @param text
	 * @return the Automated Readability Index for text
	 */
	private static Double round(double d, int decimalPlace) {
		// see the Javadoc about why we use a String in the constructor
		// http://java.sun.com/j2se/1.5.0/docs/api/java/math/BigDecimal.html#BigDecimal(double)
		BigDecimal bd = new BigDecimal(Double.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}
}
