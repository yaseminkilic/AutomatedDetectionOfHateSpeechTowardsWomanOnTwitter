package HateSpeechRecognition;

/* 
 * This class is directly related to use consumerKey and consumerSecret variables for OAuth support.
 */
class Oauth {
	
	/*  Some useful variables to control class' operations and interaction with other classes. */ 
	private String consumerKey;
	private String consumerSecret;
	
	Oauth(String key, String secret){
		consumerKey = key;
		consumerSecret = secret;
	}
	
	public String getConsumerKey() {
		return consumerKey;
	}
	
	void setConsumerKey(String key) {
		consumerKey = key;
	}
	
	public String getConsumerSecret() {
		return consumerSecret;
	}
	
	void setConsumerSecret(String secret) {
		consumerSecret = secret;
	}
}
