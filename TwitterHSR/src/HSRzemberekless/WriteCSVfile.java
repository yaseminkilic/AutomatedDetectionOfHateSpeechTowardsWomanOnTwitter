package HSRzemberekless;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class WriteCSVfile {
	
	private static List<String> columnNames;
	private static Double[][] fBaseArr;
	private static String preNameOfFile = "";
	protected WriteCSVfile(String nameOfFile, List<String> topWords, Double[][] arr){
		columnNames = topWords;
		fBaseArr = arr;
		preNameOfFile = nameOfFile;
	}
	
	protected void writeArffFile() {
		PrintWriter pw = null;
		String att = "";
		try {
		    pw = new PrintWriter(new File(preNameOfFile + "FbaseArr.arff"));
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		}
		StringBuilder builder = new StringBuilder();
		builder.append("@relation fBase_arffFile"); builder.append("\n");
		for (int i = 0; i < columnNames.size(); i++) {
			String[] col = columnNames.get(i).trim().split(" ");
			//if(col.length>0 && !col[0].isEmpty()){
				att = col[0];
				for (int j = 1; j < col.length; j++) {
					att += "-" + col[j];
				}
				builder.append("@attribute " + att + " numeric");
				builder.append("\n");
			//}
		}
		builder.append("@attribute fleschReadingEase numeric"); builder.append("\n");
		builder.append("@attribute FleschKincaidGrade numeric"); builder.append("\n");
		builder.append("@attribute characters numeric"); builder.append("\n");
		builder.append("@attribute syllables numeric"); builder.append("\n");
		builder.append("@attribute words numeric"); builder.append("\n");
		builder.append("@attribute sentences numeric"); builder.append("\n");
		builder.append("@attribute class {hate, nonhate}"); builder.append("\n");
		builder.append("\n");
		builder.append("@data"); builder.append("\n");
		for (int i = 0; i < fBaseArr.length; i++) {
			builder.append(fBaseArr[i][0]);
			for (int j = 1; j < fBaseArr[i].length; j++) {
				if(j==(fBaseArr[i].length-1)){
					builder.append("," + (fBaseArr[i][j]==1?"hate":"nonhate"));
				} else {
					builder.append("," + fBaseArr[i][j]);
				}
			}
			builder.append("\n");
		}
		pw.write(builder.toString());
		pw.close();
		System.out.println(preNameOfFile + " Writing ArffFile is done!");
	}
	
	protected void writeArray(){
		PrintWriter pw = null;
		try {
		    pw = new PrintWriter(new File(preNameOfFile + "FbaseArr.csv"));
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		}
		StringBuilder builder = new StringBuilder();
		builder.append(getUniqWords(columnNames) +"\n");
		for (int i = 0; i < fBaseArr.length; i++) {
			for (int j = 0; j < fBaseArr[i].length; j++) {
				builder.append(fBaseArr[i][j] + ", ");
			}
			builder.append('\n');
		}
		pw.write(builder.toString());
		pw.close();
		System.out.println(preNameOfFile + " Writing csvFile is done!");
	}
	
	private String getUniqWords(List<String> listOfWords){
		String word = "";
		for(String w : listOfWords){
			word += w + ", ";
		}
		return (word + "#FleschReadingEase, #FleschKincaidGrade, #Characters, #Syllables, #Words, #Sentences, #Tag").trim();
	}
}