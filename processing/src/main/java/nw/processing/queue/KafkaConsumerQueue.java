package nw.processing.queue;

import java.util.Properties;
import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.*;
import nw.processing.MyTweet;
import nw.processing.memory.IgniteSharedMemory;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class KafkaConsumerQueue {

	private KafkaConsumer<String, byte[]> consumer = null;
	
	private final String topic = "myTweetTopic";

	public KafkaConsumerQueue() {
		Properties properties = new Properties();
		properties.put("bootstrap.servers", "localhost:9092");
		properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
		properties.put("group.id", "test");
		properties.put("enable.auto.commit", "true");
		properties.put("max.partition.fetch.bytes", "2097152");				
		
		consumer = new KafkaConsumer<String, byte[]>(properties);		
	}

	public void consume() {
		
		consumer.subscribe(Arrays.asList(topic));

		while (true) {
			ConsumerRecords<String, byte[]> records = consumer.poll(200);
			
			for (ConsumerRecord<String, byte[]> record : records) {
				
				MyTweet myTweet = convertFromByteArray(record.value());

				System.out.println("---NEW TWEET---");
				System.out.println(myTweet.getText());
				System.out.println(myTweet.getText());
				System.out.println("---------------");
							
				IgniteSharedMemory sharedMemory = new IgniteSharedMemory();
				sharedMemory.insert(myTweet);
			}
		}
	}

    public MyTweet convertFromByteArray(byte[] bytes){
		
		if(bytes == null) return null;
    	
    	try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)){
    		
        	ObjectInput in = new ObjectInputStream(bis);
        	
        	return (MyTweet) in.readObject();
        	
        } catch (Exception e) {
			e.printStackTrace();
		}
    	
		return null;
    }
    

	public static void main(String[] args) throws Exception {
		
		KafkaConsumerQueue consumer = new KafkaConsumerQueue();
		consumer.consume();
	}

}
