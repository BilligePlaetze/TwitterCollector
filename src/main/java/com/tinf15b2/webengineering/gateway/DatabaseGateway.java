package com.tinf15b2.webengineering.gateway;

import com.tagcloud.persistence.RequestHandler;
import com.tagcloud.persistence.TagcloudEntry;

public class DatabaseGateway {

	private RequestHandler requestHandler;
	
	public DatabaseGateway(){
		requestHandler = RequestHandler.getInstance();
	}

	public void saveNewEntry(TagcloudEntry entry) {
		requestHandler.insert(entry);
	}

	public void closeConnection() {
		requestHandler.close();
	}

}
