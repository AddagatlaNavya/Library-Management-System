package com.library.search;

import java.util.List;
import java.util.stream.Collectors;

import com.library.model.Book;

/**
 * Search books by author
 */
class AuthorSearchStrategy implements BookSearchStrategy {
	@Override
	public List<Book> search(List<Book> books, String query) {
		return books.stream().filter(book -> book.getAuthor().toLowerCase().contains(query.toLowerCase()))
				.collect(Collectors.toList());
	}
}
