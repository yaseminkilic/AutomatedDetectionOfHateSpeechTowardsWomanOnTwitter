package HateSpeechRecognition;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/* 
 *   This class is directly related to get OAuthConsumerKey and its secret from Twitter when there isn't a OAuthConsumerKey and its secret.
 * ConsumerKey and its secret are required.
 * Access token is obtained from an authorization URL that use the user's account and its password.
 */
class AuthenticationData {
	
	/* Some useful variables to control class' operations and interaction with other classes. */ 
	private Oauth oauth;
	
	/*
	 * Create a Oauth Object by using consumerKey and its secret.
	 */
	AuthenticationData (String key, String secret){
		oauth = new Oauth(key, secret);
	}
	
	/*
	 * Get ConsumerKey and ConsumerSecret from Oauth class.
	 * Create a AccessTokenKey and AccessTokenSecret by using them.
	 * 
	 * @return String[] that holds the AccessTokenKey and AccessTokenSecret
	 */
	public String[] getAccessToken() {
		
		String key = oauth.getConsumerKey();
		String secret = oauth.getConsumerSecret();
		
        Properties prop = new Properties();
        InputStream is = null;
        OutputStream os = null;
        try {
            if (!key.isEmpty() && !secret.isEmpty()) {
                prop.setProperty("oauth.consumerKey", key);
                prop.setProperty("oauth.consumerSecret", secret);
                os = new FileOutputStream("twitter4j.properties");
                prop.store(os, "twitter4j.properties");
            }
            else
            	return null;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(-1);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignore) {
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ignore) {
                }
            }
        }
        
        try {
            twitter4j.Twitter twitter = new TwitterFactory().getInstance();
            RequestToken requestToken = twitter.getOAuthRequestToken();
            //System.out.println("Got request token.\nRequest token: " + requestToken.getToken() + " secret: " + requestToken.getTokenSecret());
            AccessToken accessToken = null;

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (null == accessToken) {
                System.out.println("Open the following URL and grant access to your account:" + requestToken.getAuthorizationURL());
                try {
                    Desktop.getDesktop().browse(new URI(requestToken.getAuthorizationURL()));
                } catch (UnsupportedOperationException ignore) {
                } catch (IOException ignore) {
                } catch (URISyntaxException e) {
                    throw new AssertionError(e);
                }
                System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");
                String pin = br.readLine();
                try {
                    if (pin.length() > 0) {
                        accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                    } else {
                        accessToken = twitter.getOAuthAccessToken(requestToken);
                    }
                } catch (TwitterException te) {
                    if (401 == te.getStatusCode()) {
                        System.out.println("Unable to get the access token.");
                    } else {
                        te.printStackTrace();
                    }
                }
            }
            
            String[] access = new String[2];
            
            access[0] = accessToken.getToken();
            access[1] = accessToken.getTokenSecret();
        
            System.out.println("Got access token.");
            System.out.println("Access token: " + accessToken.getToken());
            System.out.println("Access token secret: " + accessToken.getTokenSecret());
            
            return access;
            
        } catch (TwitterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return null;
    }
	
}
