package nw.processing.database;

import java.util.UUID;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;

import nw.processing.MyTweet;

public class Cassandra {

	// PROPRIEDADES
	private final static String SERVER = "localhost";
	private final static Integer PORT = 9160;
	// PROPRIEDADES
	
	private static Cluster cluster;
	private static Session session;
	
	public Cassandra(){
		
		Builder clusterBuilder = Cluster.builder();
        clusterBuilder = clusterBuilder.addContactPoint(SERVER);
        clusterBuilder = clusterBuilder.withPort(9160);
        
	    cluster=clusterBuilder.build();
	    session=cluster.connect();
	}
	

	public static void main(String [] args){

		MyTweet tweet = new MyTweet();
		tweet.setId("1");
		tweet.setText("Texto de teste");
		tweet.setUsername("lhzsantana");		
		
		Cassandra cassandra = new Cassandra();
		cassandra.insert(tweet);
	}
	
	public void insert(MyTweet tweet){
		
		
		Statement statement = QueryBuilder.insertInto("tweets", "tweet")
		        .value("id", tweet.getId())
		        .value("username", tweet.getUsername())
		        .value("text", tweet.getText());
		session.execute(statement);
	}

}
