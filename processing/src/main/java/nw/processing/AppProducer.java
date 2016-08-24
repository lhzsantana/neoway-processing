package nw.processing;

import nw.processing.streaming.TwitterStreaming;

public class AppProducer 
{
    public static void main(String[] args) throws Exception {
    	
    	produce();
    	
    }
    
    private static void produce(){

    	TwitterStreaming twitterStreaming = new TwitterStreaming();
    	
    	try {
			twitterStreaming.getTweets();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
   
}
