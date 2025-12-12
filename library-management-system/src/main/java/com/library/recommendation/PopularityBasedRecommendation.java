package com.library.recommendation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.library.model.Book;
import com.library.model.BorrowingRecord;
import com.library.model.Patron;

/**
 * Popularity-based recommendation strategy Recommends most frequently borrowed
 * books
 */
class PopularityBasedRecommendation implements RecommendationEngine {
	private static final Logger logger = Logger.getLogger(PopularityBasedRecommendation.class.getName());
	private final Map<String, Integer> borrowCounts;

	public PopularityBasedRecommendation() {
		this.borrowCounts = new HashMap<>();
	}

	public void recordBorrow(String isbn) {
		borrowCounts.merge(isbn, 1, Integer::sum);
	}

	@Override
	public List<Book> getRecommendations(Patron patron, List<Book> availableBooks, int limit) {
		// Filter out books patron has already borrowed
		Set<String> borrowedIsbns = patron.getBorrowingHistory().stream().map(BorrowingRecord::getIsbn)
				.collect(Collectors.toSet());

		// Sort available books by popularity
		List<Book> recommendations = availableBooks.stream().filter(book -> !borrowedIsbns.contains(book.getIsbn()))
				.sorted((b1, b2) -> {
					int count1 = borrowCounts.getOrDefault(b1.getIsbn(), 0);
					int count2 = borrowCounts.getOrDefault(b2.getIsbn(), 0);
					return Integer.compare(count2, count1);
				}).limit(limit).collect(Collectors.toList());

		logger.info(String.format("Generated %d popularity-based recommendations for patron %s", recommendations.size(),
				patron.getPatronId()));

		return recommendations;
	}
}
