package com.tinf15b2.webengineering.gateway;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.tinf15b2.webengineering.model.ReceivedTweetListener;
import com.tinf15b2.webengineering.model.TwitterResponse;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import twitter4j.FilterQuery;
import twitter4j.HashtagEntity;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

@NoArgsConstructor
@Slf4j
public class TwitterGateway {

	private TwitterStream twitterStream;
	private ReceivedTweetListener receivedTweetListener;

	private final static List<String> SEARCHED_KEY_WORD = new LinkedList<String>(Arrays.asList("Karlsruhe"));

	public void startTweetListenerFor(ReceivedTweetListener receivedTweetListener) {
		this.receivedTweetListener = receivedTweetListener;
		initializeStream();
		FilterQuery hashtagFilter = createFilterFor(SEARCHED_KEY_WORD);
		twitterStream.filter(hashtagFilter); // Starts the stream & listens for
												// the hashtag
		log.info("Twitterstream started");
	}

	public void stopTweetListener() {
		twitterStream.cleanUp();
		twitterStream.shutdown();
		log.info("Twitterstream closed");
	}

	private void initializeStream() {
		Configuration twitterConfig = createConfig();
		twitterStream = new TwitterStreamFactory(twitterConfig).getInstance();
		StatusListener tweetListener = createTweetListener();
		twitterStream.addListener(tweetListener);
		log.info("TwitterStream was initialized");
	}

	private Configuration createConfig() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(false);
		cb.setOAuthConsumerKey("YcqihL0MMZESnDOoZslladoxj");
		cb.setOAuthConsumerSecret("pL7h1v6G1AEogXjXcN4dGTTJkrAMacU3FOeAP6dZwu8YRuo24i");
		cb.setOAuthAccessToken("735033050376118274-Obeq94eWoqFYm4gxP8VMwVWcIK7BkbB");
		cb.setOAuthAccessTokenSecret("cjf2i0UqB90a0nPxiG06RNcfJtJnZOXx1OCvmWA3ZwUbL");
		return cb.build();
	}

	private StatusListener createTweetListener() {

		StatusListener tweet_Listener = new StatusListener() {

			@Override
			public void onStatus(Status status) {
				List<String> hashTagsInStatus = getHashtagsFrom(status);

				if (!hashTagsInStatus.isEmpty()) {

					for (String keyWord : SEARCHED_KEY_WORD) {
						if (status.getText().contains(keyWord)) {
							log.debug("New status: Contains " + hashTagsInStatus.size()
									+ " hashtags -> will be saved in database");
							TwitterResponse twitterResponse = TwitterResponse.builder() //
									.keyword(keyWord) //
									.hashtags(hashTagsInStatus) //
									.date(status.getCreatedAt().getTime()) //
									.build();

							receivedTweetListener.receivedNewTweet(twitterResponse);
							return;
						}
					}
				}
				log.debug("New status: Contains 0 hashtags -> won't be saved");
			}

			@Override
			public void onException(Exception arg0) {
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {
			}

			@Override
			public void onScrubGeo(long arg0, long arg1) {
			}

			@Override
			public void onTrackLimitationNotice(int arg0) {
			}

			@Override
			public void onStallWarning(StallWarning arg0) {
			}
		};
		return tweet_Listener;
	}

	private FilterQuery createFilterFor(List<String> hashTags) {
		FilterQuery fq = new FilterQuery();
		fq.track(hashTags.toArray(new String[0]));
		return fq;
	}

	private List<String> getHashtagsFrom(Status status) {
		List<String> hashTagsInTweet = new LinkedList<>();
		for (HashtagEntity hashtag : status.getHashtagEntities()) {
			String hashTagAsString = hashtag.getText();

			if (!(isKeyword(hashTagAsString))) {
				hashTagsInTweet.add(hashTagAsString);
			}
		}
		return hashTagsInTweet;
	}

	private boolean isKeyword(String hashtag) {
		
		for (String keyWord : SEARCHED_KEY_WORD) {
			if (hashtag.toLowerCase().equals(keyWord.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
}