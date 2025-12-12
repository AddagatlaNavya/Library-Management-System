package com.library.search;

import java.util.List;
import java.util.stream.Collectors;

import com.library.model.Book;

/**
 * Search books by title
 */
class TitleSearchStrategy implements BookSearchStrategy {
	@Override
	public List<Book> search(List<Book> books, String query) {
		String lowerQuery = query.toLowerCase();
		return books.stream().filter(book -> book.getTitle().toLowerCase().contains(lowerQuery))
				.collect(Collectors.toList());
	}
}
