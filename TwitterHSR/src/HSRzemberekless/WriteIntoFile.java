package HSRzemberekless;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class WriteIntoFile {
	private BufferedWriter bw = null;
	private FileWriter fw = null;
	private String file = "";
	
	public WriteIntoFile(String name) {
		this.file = name;
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void WriteIntoFile(String input){
		try{
			if(input!= null)
				bw.write(input + "\n");
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void CloseFile(){
		try {
			if (bw != null)
				bw.close();
			if (fw != null)
				fw.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
