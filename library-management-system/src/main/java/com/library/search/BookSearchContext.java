package com.library.search;

import java.util.List;

import com.library.model.Book;

/**
 * Context class for search operations
 */
public class BookSearchContext {
    private BookSearchStrategy strategy;
    
    public void setStrategy(BookSearchStrategy strategy) {
        this.strategy = strategy;
    }
    
    public List<Book> executeSearch(List<Book> books, String query) {
        if (strategy == null) {
            throw new IllegalStateException("Search strategy not set");
        }
        return strategy.search(books, query);
    }
}
