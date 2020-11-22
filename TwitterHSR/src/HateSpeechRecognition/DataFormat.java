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
    String[] stopWordsArray = {"rt", "bana", "bazen", "bazý", "bazýlarý", "bazýsý", "belki", "ben", 
    		"beni", "benim", "beþ", "bile", "bir", "birçoðu", "birçok", "birçoklarý", "biri", 
    		"birisi", "birkaç", "birkaçý", "birþey", "birþeyi", "biz", "bize", "bizi", "bizim", 
    		"böyle", "böylece", "bu", "buna", "bunda", "bundan", "bunu", "bunun", "burada", 
    		"bütün", "çoðu", "çoðuna", "çoðunu", "çok", "çünkü", "da", "daha", "de", "deðil", 
    		"demek", "diðer", "diðeri", "diðerleri", "diye", "dolayý", "elbette", "en", "fakat", 
    		"falan", "felan", "filan", "gene", "gibi", "hangi", "hangisi", "hani", "hatta", 
    		"hem", "henüz", "hep", "hepsi", "hepsine", "hepsini", "her", "her biri", "herkes", 
    		"herkese", "herkesi", "hiç", "hiç kimse", "hiçbiri", "hiçbirine", "hiçbirini", 
    		"için", "içinde", "ile", "ise", "iþte", "kaç", "kadar", "kendi", "kendine", 
    		"kendini", "ki", "kim", "kime", "kimi", "kimin", "kimisi", "madem", "mý", "mi", 
    		"mu", "mü", "mýsýn", "misin", "musun", "müsün", "nasýl", "ne", "ne", "kadar", "ne zaman", "neden", "nedir", "nerde", 
    		"nerede", "nereden", "nereye", "nesi", "neyse", "niçin", "niye", "ona", "ondan", 
    		"onlar", "onlara", "onlardan", "onlarýn", "onu", "onun", "orada", "oysa", "oysaki", 
    		"öbürü", "ön", "önce", "ötürü", "öyle", "sana", "sen", "senden", "seni", "senin", 
    		"siz", "sizden", "size", "sizi", "sizin", "son", "sonra", "seobilog", "þayet", 
    		"þey", "þimdi", "þöyle", "þu", "þuna", "þunda", "þundan", "þunlar", "þunu", "þunun",
    		"tabi", "tamam", "tüm", "tümü", "üzere", "var", "ve", "veya", "veyahut", "ya", 
    		"ya da", "yani", "yerine", "yine", "yoksa", "zaten", "zira"};
    

    private String formatToTokens(String word) {
        return word.trim().toLowerCase(Locale.ENGLISH);
    }
    
    /*
     * Divide each text using delimiters provided in the “delimiters” variable.
     * Make all the tokens lowercase and remove any tailing and leading spaces.
     * Ignore all common words provided in the “stopWordsArray” variable.
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
    		// Ignore all common words provided in the “stopWordsArray” variable.
    		if(!Arrays.asList(stopWordsArray).contains(word) && !Arrays.asList(deletedArray).contains(word.trim().substring(0, 1))){
    			newText += word + " ";
    		}
		}
		return newText.trim();
    }
}
