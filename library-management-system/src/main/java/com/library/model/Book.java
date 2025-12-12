package com.library.model;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * Represents a book in the library system Demonstrates encapsulation and
 * immutable design for core attributes
 */
public class Book {
	private static final Logger logger = Logger.getLogger(Book.class.getName());

	private final String isbn;
	private final String title;
	private final String author;
	private final int publicationYear;
	private BookStatus status;
	private String currentBranchId;

	public Book(String isbn, String title, String author, int publicationYear) {
		if (isbn == null || isbn.trim().isEmpty()) {
			throw new IllegalArgumentException("ISBN cannot be null or empty");
		}
		if (title == null || title.trim().isEmpty()) {
			throw new IllegalArgumentException("Title cannot be null or empty");
		}

		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.publicationYear = publicationYear;
		this.status = BookStatus.AVAILABLE;

		logger.info("Book created: " + title + " by " + author);
	}

	// Getters
	public String getIsbn() {
		return isbn;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public int getPublicationYear() {
		return publicationYear;
	}

	public BookStatus getStatus() {
		return status;
	}

	public String getCurrentBranchId() {
		return currentBranchId;
	}

	// Status management methods
	public void setStatus(BookStatus status) {
		BookStatus oldStatus = this.status;
		this.status = status;
		logger.info("Book " + isbn + " status changed from " + oldStatus + " to " + status);
	}

	public void setCurrentBranchId(String branchId) {
		this.currentBranchId = branchId;
	}

	public boolean isAvailable() {
		return status == BookStatus.AVAILABLE;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Book book = (Book) o;
		return isbn.equals(book.isbn);
	}

	@Override
	public int hashCode() {
		return Objects.hash(isbn);
	}

	@Override
	public String toString() {
		return "Book [isbn=" + isbn + ", title=" + title + ", author=" + author + ", publicationYear=" + publicationYear
				+ ", status=" + status + ", currentBranchId=" + currentBranchId + "]";
	}

}