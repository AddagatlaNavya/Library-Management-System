package com.library.model;

import java.util.Date;

/**
 * Represents a single borrowing record
 */
public class BorrowingRecord {
	private final String isbn;
	private final Date checkoutDate;
	private Date returnDate;

	public BorrowingRecord(String isbn, Date checkoutDate) {
		this.isbn = isbn;
		this.checkoutDate = checkoutDate;
	}

	public String getIsbn() {
		return isbn;
	}

	public Date getCheckoutDate() {
		return checkoutDate;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public boolean isReturned() {
		return returnDate != null;
	}
}