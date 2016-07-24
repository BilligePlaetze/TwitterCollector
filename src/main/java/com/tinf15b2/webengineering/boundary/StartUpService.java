package com.tinf15b2.webengineering.boundary;

import com.tinf15b2.webengineering.facade.Control;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StartUpService  {

	public static void main(String[] args) {
		
		Control control = new Control();
		control.startTweetListener();
		log.info("Twittercollector startet");
		
		Runtime.getRuntime().addShutdownHook(
			    new Thread() {
			        public void run() {
			            control.stopTweetListener();
			            control.closeDatabaseConnection();
			            log.info("Listener & databaseconnection were closed -> Service closed");
			        }
			    }
			);
	}
}
