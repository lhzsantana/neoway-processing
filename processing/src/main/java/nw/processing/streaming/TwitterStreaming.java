package nw.processing.streaming;

import java.util.Arrays;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

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
		      sparkUrl, "Tutorial", new Duration(1000), sparkHome, new String[]{jarFile});
		
	public void getTweets(){
		
		JavaDStream<Status> tweets = TwitterUtils.createStream(ssc);
		
		JavaDStream<String> statuses = tweets.map(
			      new Function<Status, String>() {
			        public String call(Status status) { return status.getText(); }
			      }
			    );
			    statuses.print();
			    
			    ssc.checkpoint(checkpointDir);
			    
			    ssc.start();
			    ssc.awaitTermination();
			    
			    JavaDStream<String> words = statuses.flatMap(
			    	     new FlatMapFunction<String, String>() {
			    	       public Iterable<String> call(String in) {
			    	         return Arrays.asList(in.split(" "));
			    	       }
			    	     }
			    	   );

			    	   JavaDStream<String> hashTags = words.filter(
			    	     new Function<String, Boolean>() {
			    	       public Boolean call(String word) { return word.startsWith("#"); }
			    	     }
			    	   );
	}

}
