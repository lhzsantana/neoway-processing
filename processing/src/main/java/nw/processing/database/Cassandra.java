package nw.processing.database;

import java.util.UUID;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;

public class Cassandra {
	
	private static Cluster cluster;
	private static Session session;

    public static Cluster connect(String node){
    	Builder clusterBuilder = Cluster.builder();
        clusterBuilder = clusterBuilder.addContactPoint(node);
        clusterBuilder = clusterBuilder.withPort(9160);
    	
    	return clusterBuilder.build();
   }

	public static void main(String [] args){
		Cassandra cassandra = new Cassandra();
		cassandra.insert("teste","teste");
	}
	
	public void insert(String userId, String text){
		
	    cluster =connect("localhost");
	    session = cluster.connect();
		
		Statement statement = QueryBuilder.insertInto("tweets", "tweet")
		        .value("userId", userId)
		        .value("text", text);
		session.execute(statement);
	}

}
