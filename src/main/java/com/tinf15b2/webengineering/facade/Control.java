package com.tinf15b2.webengineering.facade;

import com.tagcloud.persistence.TagcloudEntry;
import com.tinf15b2.webengineering.gateway.DatabaseGateway;
import com.tinf15b2.webengineering.gateway.TwitterGateway;
import com.tinf15b2.webengineering.model.ReceivedTweetListener;
import com.tinf15b2.webengineering.model.TwitterResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Control implements ReceivedTweetListener {

	private TwitterGateway twitterGateway;
	private DatabaseGateway databaseGateway;

	public Control() {
		twitterGateway = new TwitterGateway();
		databaseGateway = new DatabaseGateway();
	}

	@Override
	public void receivedNewTweet(TwitterResponse response) {
		log.info("New databaseEntry will be saved now");
		for (String hashtag : response.getHashtags()) {

			TagcloudEntry entry = TagcloudEntry.builder() //
					.tag(response.getKeyword()) //
					.tagWord(hashtag) //
					.timestamp(response.getDate()) //
					.build();
			
			databaseGateway.saveNewEntry(entry);

		}

	}

	public void startTweetListener() {
		this.twitterGateway.startTweetListenerFor(this);
	}

	public void stopTweetListener() {
		this.twitterGateway.stopTweetListener();
	}

	public void closeDatabaseConnection() {
		this.databaseGateway.closeConnection();
	}

}
