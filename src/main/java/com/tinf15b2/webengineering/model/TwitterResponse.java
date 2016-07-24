package com.tinf15b2.webengineering.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TwitterResponse {
	private String keyword;
	private List<String> hashtags;
	private long date;
}
