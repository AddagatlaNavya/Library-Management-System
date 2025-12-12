package com.library.transaction;

import java.util.Date;
import java.util.UUID;

/**
 * Represents a transaction in the library system Implements Command Pattern for
 * transaction operations
 */
public class Transaction {

	private final String transactionId;
	private final String isbn;
	private final String patronId;
	private final Date transactionDate;
	private final TransactionType type;
	private Date dueDate;
	private Date returnDate;

	public Transaction(String isbn, String patronId, TransactionType type) {
		this.transactionId = UUID.randomUUID().toString();
		this.isbn = isbn;
		this.patronId = patronId;
		this.transactionDate = new Date();
		this.type = type;

		if (type == TransactionType.CHECKOUT) {
			// Set due date to 14 days from now
			this.dueDate = new Date(System.currentTimeMillis() + 14L * 24 * 60 * 60 * 1000);
		}
	}

	public String getTransactionId() {
		return transactionId;
	}

	public String getIsbn() {
		return isbn;
	}

	public String getPatronId() {
		return patronId;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public TransactionType getType() {
		return type;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public boolean isOverdue() {
		if (dueDate == null || returnDate != null) {
			return false;
		}
		return new Date().after(dueDate);
	}

	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", isbn=" + isbn + ", patronId=" + patronId
				+ ", transactionDate=" + transactionDate + ", type=" + type + ", dueDate=" + dueDate + ", returnDate="
				+ returnDate + "]";
	}
}
