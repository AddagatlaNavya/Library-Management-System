package com.library.core;
/**
 * Statistics class
 */
public class SystemStatistics {
	private final int totalBranches;
	private final int totalBooks;
	private final int availableBooks;
	private final int totalPatrons;
	private final int totalTransactions;

	public SystemStatistics(int totalBranches, int totalBooks, int availableBooks, int totalPatrons,
			int totalTransactions) {
		this.totalBranches = totalBranches;
		this.totalBooks = totalBooks;
		this.availableBooks = availableBooks;
		this.totalPatrons = totalPatrons;
		this.totalTransactions = totalTransactions;
	}

	public int getTotalBranches() {
		return totalBranches;
	}

	public int getTotalBooks() {
		return totalBooks;
	}

	public int getAvailableBooks() {
		return availableBooks;
	}

	public int getTotalPatrons() {
		return totalPatrons;
	}

	public int getTotalTransactions() {
		return totalTransactions;
	}

	public String toString() {
		return "SystemStatistics [totalBranches=" + totalBranches + ", totalBooks=" + totalBooks
				+ ", availableBooks=" + availableBooks + ", totalPatrons=" + totalPatrons + ", totalTransactions="
				+ totalTransactions + "]";
	}
	
	
	
}
