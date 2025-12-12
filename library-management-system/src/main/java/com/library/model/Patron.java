package com.library.model;

import java.util.*;
import java.util.logging.Logger;

/**
 * Represents a library patron Tracks borrowing history and current checkouts
 */
public class Patron {
	private static final Logger logger = Logger.getLogger(Patron.class.getName());
	private static final int MAX_BOOKS_ALLOWED = 5;

	private final String patronId;
	private String name;
	private String email;
	private String phoneNumber;
	private final List<BorrowingRecord> borrowingHistory;
	private final Set<String> currentCheckouts; // ISBNs of currently borrowed books

	public Patron(String patronId, String name, String email) {
		if (patronId == null || patronId.trim().isEmpty()) {
			throw new IllegalArgumentException("Patron ID cannot be null or empty");
		}
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Name cannot be null or empty");
		}

		this.patronId = patronId;
		this.name = name;
		this.email = email;
		this.borrowingHistory = new ArrayList<>();
		this.currentCheckouts = new HashSet<>();

		logger.info("Patron created: " + name + " (ID: " + patronId + ")");
	}

	// Getters
	public String getPatronId() {
		return patronId;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public List<BorrowingRecord> getBorrowingHistory() {
		return Collections.unmodifiableList(borrowingHistory);
	}

	public Set<String> getCurrentCheckouts() {
		return Collections.unmodifiableSet(currentCheckouts);
	}

	// Setters
	public void setName(String name) {
		this.name = name;
		logger.info("Patron " + patronId + " name updated to: " + name);
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	// Business logic
	public boolean canCheckoutMoreBooks() {
		return currentCheckouts.size() < MAX_BOOKS_ALLOWED;
	}

	public void addCheckout(String isbn) {
		if (!canCheckoutMoreBooks()) {
			throw new IllegalStateException("Patron has reached maximum checkout limit");
		}
		currentCheckouts.add(isbn);
		logger.info("Patron " + patronId + " checked out book " + isbn);
	}

	public void removeCheckout(String isbn) {
		currentCheckouts.remove(isbn);
		logger.info("Patron " + patronId + " returned book " + isbn);
	}

	public void addToBorrowingHistory(BorrowingRecord record) {
		borrowingHistory.add(record);
	}

	public int getCurrentCheckoutCount() {
		return currentCheckouts.size();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Patron patron = (Patron) o;
		return patronId.equals(patron.patronId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(patronId);
	}

	@Override
	public String toString() {
		return "Patron [patronId=" + patronId + ", name=" + name + ", email=" + email + ", phoneNumber=" + phoneNumber
				+ ", borrowingHistory=" + borrowingHistory + ", currentCheckouts=" + currentCheckouts + "]";
	}

}