package com.library.search;

import java.util.List;
import java.util.stream.Collectors;

import com.library.model.Book;

/**
 * Search books by publication year
 */
class YearSearchStrategy implements BookSearchStrategy {
    @Override
    public List<Book> search(List<Book> books, String query) {
        try {
            int year = Integer.parseInt(query);
            return books.stream()
                .filter(book -> book.getPublicationYear() == year)
                .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            return List.of();
        }
    }
}
