package com.library.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.model.Patron;
import com.library.observer.PatronNotifier;
import com.library.observer.ReservationManager;
import com.library.search.BookSearchContext;
import com.library.search.BookSearchStrategy;
import com.library.search.SearchStrategyFactory;
import com.library.search.SearchType;
import com.library.transaction.CheckoutCommand;
import com.library.transaction.ReturnCommand;
import com.library.transaction.Transaction;

/**
 * Represents a library branch
 * Manages books, patrons, and transactions for a specific branch
 * Demonstrates Single Responsibility Principle
 */
public class LibraryBranch {
    private static final Logger logger = Logger.getLogger(LibraryBranch.class.getName());
    
    private final String branchId;
    private final String branchName;
    private final String address;
    
    // Inventory management
    private final Map<String, Book> inventory; // ISBN -> Book
    private final Map<String, Patron> patrons; // PatronId -> Patron
    private final List<Transaction> transactions;
    private final Map<String, ReservationManager> reservations; // ISBN -> ReservationManager
    
    // Search context
    private final BookSearchContext searchContext;
    
    public LibraryBranch(String branchId, String branchName, String address) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.address = address;
        this.inventory = new HashMap<>();
        this.patrons = new HashMap<>();
        this.transactions = new ArrayList<>();
        this.reservations = new HashMap<>();
        this.searchContext = new BookSearchContext();
        
        logger.info("Library branch created: " + branchName + " (ID: " + branchId + ")");
    }
    
    // Book Management
    public void addBook(Book book) {
        if (inventory.containsKey(book.getIsbn())) {
            logger.warning("Book with ISBN " + book.getIsbn() + " already exists");
            throw new IllegalArgumentException("Book already exists in inventory");
        }
        
        book.setCurrentBranchId(branchId);
        inventory.put(book.getIsbn(), book);
        logger.info("Book added to branch " + branchName + ": " + book.getTitle());
    }
    
    public void removeBook(String isbn) {
        Book book = inventory.get(isbn);
        if (book == null) {
            throw new NoSuchElementException("Book not found: " + isbn);
        }
        
        if (book.getStatus() == BookStatus.CHECKED_OUT) {
            throw new IllegalStateException("Cannot remove a checked-out book");
        }
        
        inventory.remove(isbn);
        logger.info("Book removed from branch " + branchName + ": " + isbn);
    }
    
    public void updateBook(String isbn, Book updatedBook) {
        if (!inventory.containsKey(isbn)) {
            throw new NoSuchElementException("Book not found: " + isbn);
        }
        
        // Keep the same status and branch
        BookStatus currentStatus = inventory.get(isbn).getStatus();
        updatedBook.setStatus(currentStatus);
        updatedBook.setCurrentBranchId(branchId);
        
        inventory.put(isbn, updatedBook);
        logger.info("Book updated in branch " + branchName + ": " + isbn);
    }
    
    public Book getBook(String isbn) {
        return inventory.get(isbn);
    }
    
    public List<Book> getAllBooks() {
        return new ArrayList<>(inventory.values());
    }
    
    public List<Book> getAvailableBooks() {
        return inventory.values().stream()
            .filter(Book::isAvailable)
            .collect(Collectors.toList());
    }
    
    // Search functionality using Strategy Pattern
    public List<Book> searchBooks(SearchType searchType, String query) {
        BookSearchStrategy strategy = SearchStrategyFactory.createStrategy(searchType);
        searchContext.setStrategy(strategy);
        return searchContext.executeSearch(new ArrayList<>(inventory.values()), query);
    }
    
    // Patron Management
    public void addPatron(Patron patron) {
        if (patrons.containsKey(patron.getPatronId())) {
            throw new IllegalArgumentException("Patron already exists: " + patron.getPatronId());
        }
        
        patrons.put(patron.getPatronId(), patron);
        logger.info("Patron added to branch " + branchName + ": " + patron.getName());
    }
    
    public void updatePatron(Patron patron) {
        if (!patrons.containsKey(patron.getPatronId())) {
            throw new NoSuchElementException("Patron not found: " + patron.getPatronId());
        }
        
        patrons.put(patron.getPatronId(), patron);
        logger.info("Patron updated in branch " + branchName + ": " + patron.getPatronId());
    }
    
    public Patron getPatron(String patronId) {
        return patrons.get(patronId);
    }
    
    public List<Patron> getAllPatrons() {
        return new ArrayList<>(patrons.values());
    }
    
    // Transaction operations using Command Pattern
    public Transaction checkoutBook(String isbn, String patronId) {
        Book book = inventory.get(isbn);
        Patron patron = patrons.get(patronId);
        
        if (book == null) {
            throw new NoSuchElementException("Book not found: " + isbn);
        }
        if (patron == null) {
            throw new NoSuchElementException("Patron not found: " + patronId);
        }
        
        CheckoutCommand command = new CheckoutCommand(book, patron);
        command.execute();
        
        Transaction transaction = command.getTransaction();
        transactions.add(transaction);
        
        logger.info(String.format("Book %s checked out by patron %s at branch %s", 
            isbn, patronId, branchName));
        
        return transaction;
    }
    
    public Transaction returnBook(String isbn, String patronId) {
        Book book = inventory.get(isbn);
        Patron patron = patrons.get(patronId);
        
        if (book == null) {
            throw new NoSuchElementException("Book not found: " + isbn);
        }
        if (patron == null) {
            throw new NoSuchElementException("Patron not found: " + patronId);
        }
        
        ReturnCommand command = new ReturnCommand(book, patron);
        command.execute();
        
        Transaction transaction = command.getTransaction();
        transactions.add(transaction);
        
        // Check if there are reservations and notify
        if (reservations.containsKey(isbn)) {
            ReservationManager manager = reservations.get(isbn);
            manager.bookReturned();
        }
        
        logger.info(String.format("Book %s returned by patron %s at branch %s", 
            isbn, patronId, branchName));
        
        return transaction;
    }
    
    // Reservation system using Observer Pattern
    public void reserveBook(String isbn, String patronId) {
        Book book = inventory.get(isbn);
        Patron patron = patrons.get(patronId);
        
        if (book == null) {
            throw new NoSuchElementException("Book not found: " + isbn);
        }
        if (patron == null) {
            throw new NoSuchElementException("Patron not found: " + patronId);
        }
        
        if (book.isAvailable()) {
            throw new IllegalStateException("Book is available, no need to reserve");
        }
        
        ReservationManager manager = reservations.computeIfAbsent(
            isbn, k -> new ReservationManager(book)
        );
        
        PatronNotifier notifier = new PatronNotifier(patron);
        manager.attach(notifier);
        
        logger.info(String.format("Patron %s reserved book %s at branch %s", 
            patronId, isbn, branchName));
    }
    
    public void cancelReservation(String isbn, String patronId) {
        if (!reservations.containsKey(isbn)) {
            throw new IllegalStateException("No reservations for this book");
        }
        
        Patron patron = patrons.get(patronId);
        if (patron == null) {
            throw new NoSuchElementException("Patron not found: " + patronId);
        }
        
        ReservationManager manager = reservations.get(isbn);
        PatronNotifier notifier = new PatronNotifier(patron);
        manager.detach(notifier);
        
        logger.info(String.format("Patron %s cancelled reservation for book %s", 
            patronId, isbn));
    }
    
    public int getReservationWaitlistSize(String isbn) {
        ReservationManager manager = reservations.get(isbn);
        return manager != null ? manager.getWaitlistSize() : 0;
    }
    
    // Getters
    public String getBranchId() {
        return branchId;
    }
    
    public String getBranchName() {
        return branchName;
    }
    
    public String getAddress() {
        return address;
    }
    
    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }
    
    public Map<String, Book> getInventory() {
        return Collections.unmodifiableMap(inventory);
    }
}