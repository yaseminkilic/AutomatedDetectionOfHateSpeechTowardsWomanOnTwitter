package HateSpeechRecognition;

public class Main {

	public static void main(String[] args) {
		
		try {
			ApplicationExecution app = new ApplicationExecution();
			if(args.length<2){ System.err.println(); }
			else if(args.length<3){
				/* Location of Train Data */ 
				String locTrain = "C://Users//Yasemin//Desktop//Twitter//Twitter//Training_FbaseArr.arff";
				/* Location of Test Data */ 
				String lovTest = "C://Users//Yasemin//Desktop//Twitter//Twitter//Testing_FbaseArr.arff"; 
				String[] argsN = {args[0], args[1], locTrain, lovTest};

				app.execution(argsN);
			}
			else 
				app.execution(args);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("HATA !!! Exception : " + e.getMessage());
		}
	}
}
