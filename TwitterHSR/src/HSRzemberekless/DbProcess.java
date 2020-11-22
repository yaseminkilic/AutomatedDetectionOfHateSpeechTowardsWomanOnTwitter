package HSRzemberekless;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import twitter4j.Status;

/* 
 *  This class is directly related to create the query for accessing database' data.
 * We use that class for the process such as getting list of terms, finding term' id and inserting any Twitter data for our tables, 
 */
public class DbProcess extends DbConnection{
	
	/* Some useful variables to control class' operations and interaction with other classes. */ 
	private ArrayList<String> list;
	private HashMap<Integer, String> hateTweets;
	private HashMap<Integer, String> nonHateTweets;
	
	public void separateTweets() {
		hateTweets = new HashMap<Integer, String>();
		nonHateTweets = new HashMap<Integer, String>();
		try {
			Statement st = getConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM processed_tweets");

			while (rs.next()) {
				if(rs.getInt("hatenonhate") == 0){
					nonHateTweets.put(rs.getInt("tweetid"), rs.getString("text"));
				} else if(rs.getInt("hatenonhate") == 1){
					hateTweets.put(rs.getInt("tweetid"), rs.getString("text"));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public HashMap<Integer, String> getHateTweets() { return hateTweets; }
	public HashMap<Integer, String> getNonHateTweets() { return nonHateTweets; }

	/*  
	 * Insert tweet into table that is given as a parameter
	 * @parameters  table:String, userid:integer, tweetid:integer, text:String, date:Date
	 * 
	 * @return boolean
	 */
	boolean insertTweet(String table, int userid, int tweetid, String text, Date date)
			throws ClassNotFoundException, SQLException {
		try {
			PreparedStatement preparedStatement = getConn()
					.prepareStatement("INSERT INTO " + table + " (userid, tweetid, text, date) values (?, ?, ?, ?)");
			preparedStatement.setLong(1, userid);
			preparedStatement.setLong(2, tweetid);
			preparedStatement.setString(3, text);
			preparedStatement.setDate(4, (java.sql.Date) date);
			return preparedStatement.executeUpdate() > 0 ? true : false;

		} catch (Exception e) {
			// System.err.println("Hata in insertTweet ! " + e.getMessage());
		}

		return false;
	}

	boolean addColumn(String table, String column)throws ClassNotFoundException, SQLException {
		try {
			Statement st = getConn().createStatement();
		    int n = st.executeUpdate("ALTER TABLE " + table + " ADD COLUMN " + column + " int NOT NULL");
		    System.out.println("Query OK, " + n + " rows affected");
		    
		} catch (SQLException  e) {
			if(e.getSQLState().equals("23505")){}//Found duplicate from database view
	         
		}
	
		return false;
	}

	/*  
	 * Find Hatenonhate from processed_tweets table
	 * @parameters  tweetid
	 * @return integer
	 */
	int findHatenonhate(String id) throws ClassNotFoundException, SQLException {
		try {
			Statement st = getConn().createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM processed_tweets");

			while (rs.next()) {
				if (rs.getString("tweetid").equals(id)) {
					return rs.getInt("hatenonhate");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 2;
	}
	
	boolean tf_idf(int tweetid, String table, String column)throws ClassNotFoundException, SQLException {
		try {

		} catch (Exception e) {
			// System.err.println("Hata in insertTweet ! " + e.getMessage());
		}
	
		return false;
	}
}
