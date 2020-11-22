package HateSpeechRecognition;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.Twitter;

/* 
 *  This class is directly related to authenticate Twitter.
 * We use that class for the process such as twitter authentication, configuration, and accessing our list of terms in the database.
 */
public class TwitterProccess {
	
	/* Some useful variables to control class' operations and interaction with other classes. */ 
	private static TwitterProccess twProcess = null;
	protected static DbConnection conn = new DbConnection();
	protected static DbProcess dbprocess = new DbProcess();
	private Twitter twitter = null;
	
	/* 
	 * Application can access the user account without userid/password combination given.
	 * Authenticate for Twitter by using consumerKey, and consumerSecret, accessTokenKey and accessTokenSecret.
	 */
	private TwitterProccess(String[] args) throws ClassNotFoundException, SQLException{
		/*
		if (args.length < 2) {
			System.out.println("Error! There isn't an usuable OAuthConsumerKey/Secret!!!");
			System.exit(0);
		}

		String accessToken = "", accessSecret = "";
		AuthenticationData oauth = new AuthenticationData(args[0], args[1]);

		if (args.length <= 2) { // There isn't a OAuthConsumerSecret and secret
			String[] access = oauth.getAccessToken();
			if (access.length < 2) {
				System.out.println("Error! There isn't an usuable OAuthAccessToken/Secret!!!");
				System.exit(0);
			}
			accessToken = access[0];
			accessSecret = access[1];
		} else {
			accessToken = args[2];
			accessSecret = args[3];
		} */
		conn.setConn();
		// setTwitter(args[0], args[1], accessToken, accessSecret);
	}
	
	/*
	 * Authenticate the user before getting data from Twitter is started.
	 * This authentication operation is called only for once because of using Singleton Pattern.
	 * 
	 * @return TwitterProccess
	 */
	public static TwitterProccess authenticate(String[] args) throws ClassNotFoundException, SQLException{
		if(twProcess == null) twProcess = new TwitterProccess(args);
		return twProcess;
	}

	public twitter4j.Twitter getTwitter() {  return twitter;  }
	/* 
	 * Set configuration data for Twitter.
	 * 
	 * @return Twitter
	 */
	void setTwitter(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) throws ClassNotFoundException, SQLException {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(consumerKey).setOAuthConsumerSecret(consumerSecret)
				.setOAuthAccessToken(accessToken).setOAuthAccessTokenSecret(accessTokenSecret);

		twitter = new TwitterFactory(cb.build()).getInstance();
	}
}