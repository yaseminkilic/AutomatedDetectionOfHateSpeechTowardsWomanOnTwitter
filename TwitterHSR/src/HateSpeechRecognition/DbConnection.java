package HateSpeechRecognition;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

/* 
 *   This class is directly related to connect Twitter.
 * We use that class for the process such as closing database, accessing twitter connection and getting the list of terms in the database.
 */
class DbConnection {
	
	/* Some useful variables to control class' operations and interaction with other classes. */ 
	private static Connection conn = null;
	private ArrayList<String> list = null;

	DbConnection(){}
	
	
	/* 
	 * Close database connection 
	 */
	void closeDb() throws SQLException, ClassNotFoundException {
		if (conn != null) {
			conn.close();
		}
	}
	
	/*
	 * Get List of Terms
	 */
	public ArrayList<String> getList() {
		return list;
	}
	
	/* 
	 * Get - Set Connection 
	 */
	public Connection getConn() { return conn; }
	void setConn() throws ClassNotFoundException, SQLException {
		if(conn == null) {
			Class.forName("com.mysql.jdbc.Driver");
			conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/twitter_schema?useUnicode=true&useSSL=false","root","1234");
		}
	}
}
