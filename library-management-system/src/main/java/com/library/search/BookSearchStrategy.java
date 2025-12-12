package com.library.search;

import java.util.List;

import com.library.model.Book;

public interface BookSearchStrategy {

	List<Book> search(List<Book> books, String query);
}
