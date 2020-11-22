package HateSpeechRecognition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.StringTokenizer;

/* 
 * This class is directly related to format the Twitter data before it is recorded in database.
 */
public class DataFormat {

	
	private static DbProcess dbprocess = new DbProcess();
	
    String delimiters = "\t,;.?!-:[](){}_*/\'\"";
    String[] deletedArray = {"@", "#"};
    String[] stopWordsArray = {"rt", "bana", "bazen", "baz�", "baz�lar�", "baz�s�", "belki", "ben", 
    		"beni", "benim", "be�", "bile", "bir", "bir�o�u", "bir�ok", "bir�oklar�", "biri", 
    		"birisi", "birka�", "birka��", "bir�ey", "bir�eyi", "biz", "bize", "bizi", "bizim", 
    		"b�yle", "b�ylece", "bu", "buna", "bunda", "bundan", "bunu", "bunun", "burada", 
    		"b�t�n", "�o�u", "�o�una", "�o�unu", "�ok", "��nk�", "da", "daha", "de", "de�il", 
    		"demek", "di�er", "di�eri", "di�erleri", "diye", "dolay�", "elbette", "en", "fakat", 
    		"falan", "felan", "filan", "gene", "gibi", "hangi", "hangisi", "hani", "hatta", 
    		"hem", "hen�z", "hep", "hepsi", "hepsine", "hepsini", "her", "her biri", "herkes", 
    		"herkese", "herkesi", "hi�", "hi� kimse", "hi�biri", "hi�birine", "hi�birini", 
    		"i�in", "i�inde", "ile", "ise", "i�te", "ka�", "kadar", "kendi", "kendine", 
    		"kendini", "ki", "kim", "kime", "kimi", "kimin", "kimisi", "madem", "m�", "mi", 
    		"mu", "m�", "m�s�n", "misin", "musun", "m�s�n", "nas�l", "ne", "ne", "kadar", "ne zaman", "neden", "nedir", "nerde", 
    		"nerede", "nereden", "nereye", "nesi", "neyse", "ni�in", "niye", "ona", "ondan", 
    		"onlar", "onlara", "onlardan", "onlar�n", "onu", "onun", "orada", "oysa", "oysaki", 
    		"�b�r�", "�n", "�nce", "�t�r�", "�yle", "sana", "sen", "senden", "seni", "senin", 
    		"siz", "sizden", "size", "sizi", "sizin", "son", "sonra", "seobilog", "�ayet", 
    		"�ey", "�imdi", "��yle", "�u", "�una", "�unda", "�undan", "�unlar", "�unu", "�unun",
    		"tabi", "tamam", "t�m", "t�m�", "�zere", "var", "ve", "veya", "veyahut", "ya", 
    		"ya da", "yani", "yerine", "yine", "yoksa", "zaten", "zira"};
    

    private String formatToTokens(String word) {
        return word.trim().toLowerCase(Locale.ENGLISH);
    }
    
    /*
     * Divide each text using delimiters provided in the �delimiters� variable.
     * Make all the tokens lowercase and remove any tailing and leading spaces.
     * Ignore all common words provided in the �stopWordsArray� variable.
     * 
     * @parameters text:String
     * @return String
     */
	public String formatData(String text) throws Exception {
    	String word = "", newText = "";
    	
    	String[] splitedText = text.trim().split("http");
        StringTokenizer tokenizer = new StringTokenizer(splitedText[0].trim(),delimiters);
    	while (tokenizer.hasMoreElements()) {
    		// Make all the tokens lowercase and remove any tailing and leading spaces.
    		word = formatToTokens(tokenizer.nextElement().toString());
    		// Ignore all common words provided in the �stopWordsArray� variable.
    		if(!Arrays.asList(stopWordsArray).contains(word) && !Arrays.asList(deletedArray).contains(word.trim().substring(0, 1))){
    			newText += word + " ";
    		}
		}
		return newText.trim();
    }
}
