package com.library.search;

/**
 * Factory for creating search strategies
 */
public class SearchStrategyFactory {

	public static BookSearchStrategy createStrategy(SearchType type) {
		switch (type) {
		case TITLE:
			return new TitleSearchStrategy();
		case AUTHOR:
			return new AuthorSearchStrategy();
		case ISBN:
			return new ISBNSearchStrategy();
		case YEAR:
			return new YearSearchStrategy();
		default:
			throw new IllegalArgumentException("Unknown search type: " + type);
		}
	}
}
