package com.library.recommendation;

import java.util.List;

import com.library.model.Book;
import com.library.model.Patron;

/**
 * Book recommendation system based on patron borrowing history Uses
 * collaborative filtering approach Demonstrates Interface Segregation Principle
 */
public interface RecommendationEngine {
	List<Book> getRecommendations(Patron patron, List<Book> availableBooks, int limit);
}
