package nw.processing.streaming;

import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.twitter.TwitterUtils;

import nw.processing.MyTweet;
import nw.processing.database.Cassandra;
import nw.processing.queue.KafkaProducerQueue;
import twitter4j.Status;

public class TwitterStreaming {

	// Location of the Spark directory
    String sparkHome = "/root/spark";
    // URL of the Spark cluster
    String sparkUrl = "";
    // Location of the required JAR files
    String jarFile = "target/scala-2.9.3/tutorial_2.9.3-0.1-SNAPSHOT.jar";
    // HDFS directory for checkpointing
    String checkpointDir = "/checkpoint/";

	JavaStreamingContext ssc = new JavaStreamingContext(
		      sparkUrl, "TwitterMirror", new Duration(1000), sparkHome, new String[]{jarFile});    

	public void getTweets() throws InterruptedException{
		
		JavaDStream<Status> tweets = TwitterUtils.createStream(ssc);
		
		JavaDStream<String> statuses = tweets.map(new Function<Status, String>() {
				public String call(Status status) {
					return status.getText(); 
				}
			}
		);
		
		statuses.print();

		ssc.checkpoint(checkpointDir);

		ssc.start();

		ssc.awaitTermination();
			    
	}
	
	private void insertToDataLake(MyTweet tweet){
		Cassandra cassandra = new Cassandra();
		
		cassandra.insert("", "");
	}

	private void insertToQueue(MyTweet tweet){
		
		KafkaProducerQueue producer = new KafkaProducerQueue();
		
		try {
			producer.publishMesssage(tweet);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
