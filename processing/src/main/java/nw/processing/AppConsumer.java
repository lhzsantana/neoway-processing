package nw.processing;

import nw.processing.memory.IgniteSharedMemory;
import nw.processing.queue.KafkaConsumerQueue;

public class AppConsumer 
{
    public static void main(String[] args) throws Exception {

    	consume();
    	
    }
    
    private static void consume(){
    	
    	KafkaConsumerQueue kafkaConsumer = new KafkaConsumerQueue();
    	
    	kafkaConsumer.consume();
    	
    	IgniteSharedMemory ignite = new IgniteSharedMemory();
    	
    	ignite.search("trump");
    }
   
}
