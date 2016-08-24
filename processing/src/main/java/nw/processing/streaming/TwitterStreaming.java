package nw.processing.streaming;

import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.twitter.TwitterUtils;

import nw.processing.MyTweet;
import nw.processing.database.Cassandra;
import nw.processing.queue.KafkaProducerQueue;
import twitter4j.Status;
import twitter4j.auth.Authorization;
import twitter4j.auth.AuthorizationFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;

public class TwitterStreaming {

	// PROPRIEDADES
	// SPARK
	String sparkUrl = "spark://192.168.99.1:7077";
	String jarFile = "target/neoway-jar-with-dependencies.jar";
	String checkpointDir = "/checkpoint/";
	// PROPRIEDADES

	private static SparkConf conf;
	private static JavaStreamingContext ssc;

	public TwitterStreaming() {

		conf = new SparkConf().setAppName("Twitter Mirror").setMaster(sparkUrl).setJars(new String[] { jarFile })
				.set("spark.akka.frameSize", "20");

		ssc = new JavaStreamingContext(conf, new Duration(1000));
	}

	public void getTweets() throws InterruptedException {

		Configuration twitterConf = ConfigurationContext.getInstance();
		Authorization twitterAuth = AuthorizationFactory.getInstance(twitterConf);

		JavaDStream<Status> tweets = TwitterUtils.createStream(ssc, twitterAuth);

		JavaDStream<String> statuses = tweets.map(new Function<Status, String>() {
			public String call(Status status) {
				return status.getText();
			}
		});
		
		JavaDStream<String> words = statuses.flatMap(new FlatMapFunction<String, String>() {
			public Iterable<String> call(String in) {
				return Arrays.asList(in.split(" "));
			}
		});
		
		JavaDStream<String> hashTags = words.filter(new Function<String, Boolean>() {
			public Boolean call(String word) {
				return word.startsWith("#");
			}
		});

		statuses.print();

		ssc.checkpoint(checkpointDir);

		ssc.start();

		ssc.awaitTermination();

	}

	public JavaDStream<String> process(JavaDStream<String> input) {
		input.foreachRDD(rdd -> {
			rdd.foreachPartition(items -> {
				while (items.hasNext()) {
					System.out.println(items.next() + System.lineSeparator());
				}
			});
			return null;
		});
		return null;
	}

	private void insertToDataLake(MyTweet tweet) {
		Cassandra cassandra = new Cassandra();
		cassandra.insert(tweet);
	}

	private void insertToQueue(MyTweet tweet) {

		KafkaProducerQueue producer = new KafkaProducerQueue();

		try {
			producer.publishMesssage(tweet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {

		TwitterStreaming twitterStreaming = new TwitterStreaming();
		twitterStreaming.getTweets();
	}
}
