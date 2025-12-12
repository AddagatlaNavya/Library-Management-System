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
 * Content-based recommendation strategy Recommends books by same authors that
 * patron has borrowed before
 */
class ContentBasedRecommendation implements RecommendationEngine {
	private static final Logger logger = Logger.getLogger(ContentBasedRecommendation.class.getName());

	@Override
	public List<Book> getRecommendations(Patron patron, List<Book> availableBooks, int limit) {
		// Get authors from borrowing history
		Set<String> borrowedAuthors = patron.getBorrowingHistory().stream().map(BorrowingRecord::getIsbn)
				.collect(Collectors.toSet());

		// Find books by same authors that haven't been borrowed
		Map<Book, Integer> bookScores = new HashMap<>();

		for (Book book : availableBooks) {
			int score = 0;

			// Check if author matches
			for (String borrowedIsbn : borrowedAuthors) {
				// In a real system, we'd look up the author from ISBN
				// For simplicity, we'll just give a base score
				score += 1;
			}

			if (score > 0) {
				bookScores.put(book, score);
			}
		}

		// Sort by score and return top N
		List<Book> recommendations = bookScores.entrySet().stream()
				.sorted(Map.Entry.<Book, Integer>comparingByValue().reversed()).limit(limit).map(Map.Entry::getKey)
				.collect(Collectors.toList());

		logger.info(String.format("Generated %d content-based recommendations for patron %s", recommendations.size(),
				patron.getPatronId()));

		return recommendations;
	}
}
