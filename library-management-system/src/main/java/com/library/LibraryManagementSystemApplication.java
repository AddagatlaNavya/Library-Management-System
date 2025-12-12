package com.library;

import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.library.core.LibraryBranch;
import com.library.core.LibrarySystem;
import com.library.core.SystemStatistics;
import com.library.model.Book;
import com.library.model.Patron;
import com.library.recommendation.RecommendationEngine;
import com.library.recommendation.RecommendationEngineFactory;
import com.library.recommendation.RecommendationType;
import com.library.search.SearchType;

/**
 * Demo class to showcase Library Management System functionality
 */
@SpringBootApplication
public class LibraryManagementSystemApplication {
	private static final Logger logger = Logger.getLogger(LibraryManagementSystemApplication.class.getName());

	public static void main(String[] args) {
		SpringApplication.run(LibraryManagementSystemApplication.class, args);

		// Configure logging
		configureLogging();

		logger.info("=== Library Management System Demo ===\n");

		// Get singleton instance
		LibrarySystem system = LibrarySystem.getInstance();

		// Demo 1: Create branches
		demoBranchCreation(system);

		// Demo 2: Book management
		demoBookManagement(system);

		// Demo 3: Patron management
		demoPatronManagement(system);

		// Demo 4: Checkout and return
		demoTransactions(system);

		// Demo 5: Search functionality
		demoSearch(system);

		// Demo 6: Reservation system
		demoReservations(system);

		// Demo 7: Book transfer between branches
		demoBookTransfer(system);

		// Demo 8: Recommendations
		demoRecommendations(system);

		// Demo 9: System statistics
		demoStatistics(system);

		logger.info("\n=== Demo Complete ===");

	}

	private static void configureLogging() {
		Logger rootLogger = Logger.getLogger("");
		rootLogger.setLevel(Level.INFO);

		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.INFO);
		handler.setFormatter(new SimpleFormatter());

		rootLogger.addHandler(handler);
	}

	private static void demoBranchCreation(LibrarySystem system) {
		logger.info("\n--- Demo 1: Creating Library Branches ---");

		LibraryBranch centralBranch = new LibraryBranch("BR001", "Central Library", "123 Main St");
		LibraryBranch eastBranch = new LibraryBranch("BR002", "East Branch", "456 East Ave");

		system.addBranch(centralBranch);
		system.addBranch(eastBranch);

		logger.info("Created " + system.getAllBranches().size() + " branches");
	}

	private static void demoBookManagement(LibrarySystem system) {
		logger.info("\n--- Demo 2: Book Management ---");

		LibraryBranch centralBranch = system.getBranch("BR001");

		// Add books
		Book book1 = new Book("978-0-13-468599-1", "Clean Code", "Robert Martin", 2008);
		Book book2 = new Book("978-0-201-63361-0", "Design Patterns", "Gang of Four", 1994);
		Book book3 = new Book("978-0-13-235088-4", "Clean Architecture", "Robert Martin", 2017);
		Book book4 = new Book("978-0-132-35088-8", "Effective Java", "Joshua Bloch", 2018);

		centralBranch.addBook(book1);
		centralBranch.addBook(book2);
		centralBranch.addBook(book3);
		centralBranch.addBook(book4);

		// Add books to east branch
		LibraryBranch eastBranch = system.getBranch("BR002");
		Book book5 = new Book("978-0-596-52068-7", "Head First Java", "Kathy Sierra", 2005);
		eastBranch.addBook(book5);

		logger.info("Total books in Central: " + centralBranch.getAllBooks().size());
		logger.info("Total books in East: " + eastBranch.getAllBooks().size());
	}

	private static void demoPatronManagement(LibrarySystem system) {
		logger.info("\n--- Demo 3: Patron Management ---");

		LibraryBranch centralBranch = system.getBranch("BR001");

		Patron patron1 = new Patron("P001", "Alice Johnson", "alice@email.com");
		Patron patron2 = new Patron("P002", "Bob Smith", "bob@email.com");
		Patron patron3 = new Patron("P003", "Carol Williams", "carol@email.com");

		patron1.setPhoneNumber("555-0101");
		patron2.setPhoneNumber("555-0102");

		centralBranch.addPatron(patron1);
		centralBranch.addPatron(patron2);
		centralBranch.addPatron(patron3);

		logger.info("Total patrons registered: " + centralBranch.getAllPatrons().size());
	}

	private static void demoTransactions(LibrarySystem system) {
		logger.info("\n--- Demo 4: Checkout and Return Transactions ---");

		LibraryBranch centralBranch = system.getBranch("BR001");

		// Checkout books
		centralBranch.checkoutBook("978-0-13-468599-1", "P001");
		centralBranch.checkoutBook("978-0-201-63361-0", "P001");
		centralBranch.checkoutBook("978-0-13-235088-4", "P002");

		logger.info("Books checked out successfully");

		// Return a book
		centralBranch.returnBook("978-0-13-468599-1", "P001");
		logger.info("Book returned successfully");

		// Check patron status
		Patron patron1 = centralBranch.getPatron("P001");
		logger.info("Patron P001 current checkouts: " + patron1.getCurrentCheckoutCount());
		logger.info("Patron P001 borrowing history: " + patron1.getBorrowingHistory().size());
	}

	private static void demoSearch(LibrarySystem system) {
		logger.info("\n--- Demo 5: Search Functionality ---");

		LibraryBranch centralBranch = system.getBranch("BR001");

		// Search by title
		List<Book> titleResults = centralBranch.searchBooks(SearchType.TITLE, "Clean");
		logger.info("Search by title 'Clean': " + titleResults.size() + " results");

		// Search by author
		List<Book> authorResults = centralBranch.searchBooks(SearchType.AUTHOR, "Robert Martin");
		logger.info("Search by author 'Robert Martin': " + authorResults.size() + " results");

		// Search by ISBN
		List<Book> isbnResults = centralBranch.searchBooks(SearchType.ISBN, "978-0-201-63361-0");
		logger.info("Search by ISBN: " + isbnResults.size() + " results");
	}

	private static void demoReservations(LibrarySystem system) {
		logger.info("\n--- Demo 6: Reservation System ---");

		LibraryBranch centralBranch = system.getBranch("BR001");

		// Book is checked out, so patron3 reserves it
		try {
			centralBranch.reserveBook("978-0-201-63361-0", "P003");
			logger.info("Book reserved successfully");

			int waitlistSize = centralBranch.getReservationWaitlistSize("978-0-201-63361-0");
			logger.info("Current waitlist size: " + waitlistSize);

			// When the book is returned, patron3 will be notified
			centralBranch.returnBook("978-0-201-63361-0", "P001");
			logger.info("Book returned - reservation notification sent");

		} catch (Exception e) {
			logger.warning("Reservation error: " + e.getMessage());
		}
	}

	private static void demoBookTransfer(LibrarySystem system) {
		logger.info("\n--- Demo 7: Book Transfer Between Branches ---");

		// Transfer a book from Central to East branch
		try {
			system.transferBook("978-0-132-35088-8", "BR001", "BR002");
			logger.info("Book transferred successfully");

			// Verify transfer
			Book book = system.getBranch("BR002").getBook("978-0-132-35088-8");
			logger.info("Book now at branch: " + book.getCurrentBranchId());

		} catch (Exception e) {
			logger.warning("Transfer error: " + e.getMessage());
		}
	}

	private static void demoRecommendations(LibrarySystem system) {
		logger.info("\n--- Demo 8: Book Recommendations ---");

		LibraryBranch centralBranch = system.getBranch("BR001");
		Patron patron1 = centralBranch.getPatron("P001");

		// Get recommendations
		RecommendationEngine engine = RecommendationEngineFactory
				.createEngine(RecommendationType.CONTENT_BASED);

		List<Book> recommendations = engine.getRecommendations(patron1, centralBranch.getAvailableBooks(), 3);

		logger.info("Generated " + recommendations.size() + " recommendations for patron P001");
		for (Book book : recommendations) {
			logger.info("  - " + book.getTitle() + " by " + book.getAuthor());
		}
	}

	private static void demoStatistics(LibrarySystem system) {
		logger.info("\n--- Demo 9: System Statistics ---");

		SystemStatistics stats = system.getStatistics();
		logger.info("\n" + stats.toString());
	}
}
