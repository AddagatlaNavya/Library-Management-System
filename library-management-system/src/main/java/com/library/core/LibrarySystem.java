package com.library.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import com.library.model.Book;
import com.library.model.BookStatus;

/**
 * Main library system managing multiple branches Implements Singleton Pattern
 * to ensure single instance Demonstrates Open/Closed Principle - open for
 * extension, closed for modification
 */
public class LibrarySystem {
	private static final Logger logger = Logger.getLogger(LibrarySystem.class.getName());
	private static LibrarySystem instance;

	private final Map<String, LibraryBranch> branches;

	// Private constructor for Singleton pattern
	private LibrarySystem() {
		this.branches = new HashMap<>();
		logger.info("Library System initialized");
	}

	/**
	 * Thread-safe Singleton instance getter
	 */
	public static synchronized LibrarySystem getInstance() {
		if (instance == null) {
			instance = new LibrarySystem();
		}
		return instance;
	}

	// Branch Management
	public void addBranch(LibraryBranch branch) {
		if (branches.containsKey(branch.getBranchId())) {
			throw new IllegalArgumentException("Branch already exists: " + branch.getBranchId());
		}

		branches.put(branch.getBranchId(), branch);
		logger.info("Branch added to system: " + branch.getBranchName());
	}

	public LibraryBranch getBranch(String branchId) {
		LibraryBranch branch = branches.get(branchId);
		if (branch == null) {
			throw new NoSuchElementException("Branch not found: " + branchId);
		}
		return branch;
	}

	public List<LibraryBranch> getAllBranches() {
		return new ArrayList<>(branches.values());
	}

	/**
	 * Transfer a book from one branch to another Demonstrates inter-branch
	 * operations
	 */
	public void transferBook(String isbn, String fromBranchId, String toBranchId) {
		LibraryBranch fromBranch = getBranch(fromBranchId);
		LibraryBranch toBranch = getBranch(toBranchId);

		Book book = fromBranch.getBook(isbn);
		if (book == null) {
			throw new NoSuchElementException("Book not found in source branch: " + isbn);
		}

		if (book.getStatus() == BookStatus.CHECKED_OUT) {
			throw new IllegalStateException("Cannot transfer a checked-out book");
		}

		// Remove from source branch
		fromBranch.removeBook(isbn);

		// Update book's branch
		book.setCurrentBranchId(toBranchId);
		book.setStatus(BookStatus.IN_TRANSIT);

		// Add to destination branch
		toBranch.addBook(book);
		book.setStatus(BookStatus.AVAILABLE);

		logger.info(String.format("Book " + isbn + " transferred from " + fromBranch.getBranchName() + " to "
				+ toBranch.getBranchName()));
	}

	/**
	 * Search for a book across all branches
	 */
	public Map<String, Book> findBookAcrossBranches(String isbn) {
		Map<String, Book> results = new HashMap<>();

		for (LibraryBranch branch : branches.values()) {
			Book book = branch.getBook(isbn);
			if (book != null) {
				results.put(branch.getBranchId(), book);
			}
		}

		return results;
	}

	/**
	 * Get system-wide statistics
	 */
	public SystemStatistics getStatistics() {
		int totalBooks = 0;
		int availableBooks = 0;
		int totalPatrons = 0;
		int totalTransactions = 0;

		for (LibraryBranch branch : branches.values()) {
			totalBooks += branch.getAllBooks().size();
			availableBooks += branch.getAvailableBooks().size();
			totalPatrons += branch.getAllPatrons().size();
			totalTransactions += branch.getTransactions().size();
		}

		return new SystemStatistics(branches.size(), totalBooks, availableBooks, totalPatrons, totalTransactions);
	}

}