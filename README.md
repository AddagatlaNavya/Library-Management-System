# Library Management System

A comprehensive Library Management System implemented in Java, demonstrating Object-Oriented Programming principles, SOLID design patterns, and advanced software architecture concepts.

## 📋 Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Design Patterns](#design-patterns)
- [SOLID Principles](#solid-principles)
- [Class Diagram](#class-diagram)
- [Project Structure](#project-structure)
- [Setup and Installation](#setup-and-installation)
- [Usage](#usage)
- [Testing](#testing)
- [Future Enhancements](#future-enhancements)

## ✨ Features

### Core Features
- ✅ **Book Management**: Add, remove, update, and search books
- ✅ **Patron Management**: Register patrons and track borrowing history
- ✅ **Transaction System**: Checkout and return books with transaction logging
- ✅ **Inventory Management**: Real-time tracking of book availability

### Advanced Features
- ✅ **Multi-branch Support**: Manage multiple library branches
- ✅ **Book Transfer**: Transfer books between branches
- ✅ **Reservation System**: Reserve checked-out books with automatic notifications
- ✅ **Recommendation Engine**: Personalized book recommendations based on borrowing history
- ✅ **Search Strategies**: Multiple search options (title, author, ISBN, year)

## 🏗️ Architecture

The system follows a layered architecture:

```
┌─────────────────────────────────────┐
│     Presentation Layer              │
│   (Demo/CLI Interface)              │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│     Business Logic Layer            │
│   (LibrarySystem, LibraryBranch)    │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│     Domain Model Layer              │
│   (Book, Patron, Transaction)       │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│     Infrastructure Layer            │
│   (Search, Observer, Recommendation)│
└─────────────────────────────────────┘
```

## 🎨 Design Patterns

### 1. **Singleton Pattern**
- **Class**: `LibrarySystem`
- **Purpose**: Ensures single instance of the library management system
- **Benefits**: Centralized control, prevents multiple system instances

### 2. **Strategy Pattern**
- **Classes**: `BookSearchStrategy`, `TitleSearchStrategy`, `AuthorSearchStrategy`, etc.
- **Purpose**: Encapsulates different search algorithms
- **Benefits**: Easy to add new search strategies without modifying existing code

### 3. **Observer Pattern**
- **Classes**: `BookObserver`, `BookSubject`, `ReservationManager`, `PatronNotifier`
- **Purpose**: Notifies patrons when reserved books become available
- **Benefits**: Loose coupling between reservation system and notification mechanism

### 4. **Command Pattern**
- **Classes**: `TransactionCommand`, `CheckoutCommand`, `ReturnCommand`
- **Purpose**: Encapsulates transaction operations as objects
- **Benefits**: Supports undo operations, transaction logging, and auditing

### 5. **Factory Pattern**
- **Classes**: `SearchStrategyFactory`, `RecommendationEngineFactory`
- **Purpose**: Creates instances of strategies without exposing creation logic
- **Benefits**: Centralized object creation, easy to extend

## 🎯 SOLID Principles

### Single Responsibility Principle (SRP)
- Each class has one reason to change
- `Book` manages book data, `Patron` manages patron data
- `LibraryBranch` handles branch operations, `LibrarySystem` manages multiple branches

### Open/Closed Principle (OCP)
- System is open for extension, closed for modification
- New search strategies can be added without modifying existing search code
- New recommendation algorithms can be plugged in easily

### Liskov Substitution Principle (LSP)
- All `BookSearchStrategy` implementations are interchangeable
- All `RecommendationEngine` implementations can substitute each other

### Interface Segregation Principle (ISP)
- Focused interfaces: `BookObserver`, `BookSubject`, `RecommendationEngine`
- Clients depend only on methods they use

### Dependency Inversion Principle (DIP)
- High-level modules depend on abstractions (interfaces)
- `LibraryBranch` depends on `BookSearchStrategy` interface, not concrete implementations

## 📊 Class Diagram

```
┌──────────────────────────────────────────────────────────────────┐
│                         LibrarySystem                            │
│                         <<Singleton>>                            │
├──────────────────────────────────────────────────────────────────┤
│ - instance: LibrarySystem                                        │
│ - branches: Map<String, LibraryBranch>                          │
├──────────────────────────────────────────────────────────────────┤
│ + getInstance(): LibrarySystem                                   │
│ + addBranch(branch: LibraryBranch): void                        │
│ + getBranch(branchId: String): LibraryBranch                    │
│ + transferBook(isbn, fromBranch, toBranch): void                │
│ + getStatistics(): SystemStatistics                              │
└──────────────────────────────────────────────────────────────────┘
                              │
                              │ manages
                              ▼
┌──────────────────────────────────────────────────────────────────┐
│                       LibraryBranch                              │
├──────────────────────────────────────────────────────────────────┤
│ - branchId: String                                               │
│ - branchName: String                                             │
│ - inventory: Map<String, Book>                                   │
│ - patrons: Map<String, Patron>                                   │
│ - transactions: List<Transaction>                                │
│ - reservations: Map<String, ReservationManager>                  │
│ - searchContext: BookSearchContext                               │
├──────────────────────────────────────────────────────────────────┤
│ + addBook(book: Book): void                                      │
│ + removeBook(isbn: String): void                                 │
│ + searchBooks(type: SearchType, query: String): List<Book>       │
│ + addPatron(patron: Patron): void                                │
│ + checkoutBook(isbn, patronId): Transaction                      │
│ + returnBook(isbn, patronId): Transaction                        │
│ + reserveBook(isbn, patronId): void                              │
└──────────────────────────────────────────────────────────────────┘
           │                    │                    │
           │ has-a              │ has-a              │ has-a
           ▼                    ▼                    ▼
┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐
│      Book       │  │     Patron      │  │  Transaction    │
├─────────────────┤  ├─────────────────┤  ├─────────────────┤
│ - isbn: String  │  │ - patronId: Str │  │ - txnId: String │
│ - title: String │  │ - name: String  │  │ - isbn: String  │
│ - author: String│  │ - email: String │  │ - patronId: Str │
│ - year: int     │  │ - history: List │  │ - date: Date    │
│ - status: Enum  │  │ - checkouts: Set│  │ - type: Enum    │
├─────────────────┤  ├─────────────────┤  ├─────────────────┤
│ + getters()     │  │ + canCheckout() │  │ + isOverdue()   │
│ + setStatus()   │  │ + addCheckout() │  │ + getters()     │
│ + isAvailable() │  │ + removeCheck() │  └─────────────────┘
└─────────────────┘  └─────────────────┘

┌──────────────────────────────────────────────────────────────────┐
│                  <<interface>>                                   │
│                BookSearchStrategy                                │
├──────────────────────────────────────────────────────────────────┤
│ + search(books: List<Book>, query: String): List<Book>          │
└──────────────────────────────────────────────────────────────────┘
                              △
                              │ implements
              ┌───────────────┼───────────────┬──────────────┐
              │               │               │              │
   ┌──────────────┐ ┌─────────────┐ ┌────────────┐ ┌──────────────┐
   │TitleSearch   │ │AuthorSearch │ │ISBNSearch  │ │YearSearch    │
   │Strategy      │ │Strategy     │ │Strategy    │ │Strategy      │
   └──────────────┘ └─────────────┘ └────────────┘ └──────────────┘

┌──────────────────────────────────────────────────────────────────┐
│                  <<interface>>                                   │
│                   BookObserver                                   │
├──────────────────────────────────────────────────────────────────┤
│ + notify(book: Book): void                                       │
└──────────────────────────────────────────────────────────────────┘
                              △
                              │ implements
                     ┌────────────────┐
                     │PatronNotifier  │
                     ├────────────────┤
                     │- patron: Patron│
                     └────────────────┘

┌──────────────────────────────────────────────────────────────────┐
│                  <<interface>>                                   │
│                 BookSubject                                      │
├──────────────────────────────────────────────────────────────────┤
│ + attach(observer: BookObserver): void                           │
│ + detach(observer: BookObserver): void                           │
│ + notifyObservers(): void                                        │
└──────────────────────────────────────────────────────────────────┘
                              △
                              │ implements
                  ┌───────────────────────┐
                  │ReservationManager     │
                  ├───────────────────────┤
                  │- book: Book           │
                  │- waitlist: Queue      │
                  │+ bookReturned(): void │
                  └───────────────────────┘

┌──────────────────────────────────────────────────────────────────┐
│                  <<interface>>                                   │
│              RecommendationEngine                                │
├──────────────────────────────────────────────────────────────────┤
│ + getRecommendations(patron, books, limit): List<Book>           │
└──────────────────────────────────────────────────────────────────┘
                              △
                              │ implements
              ┌───────────────┼───────────────────┐
              │               │                   │
   ┌──────────────┐ ┌─────────────────┐ ┌────────────────┐
   │ContentBased  │ │PopularityBased  │ │Hybrid          │
   │Recommendation│ │Recommendation   │ │Recommendation  │
   └──────────────┘ └─────────────────┘ └────────────────┘
```

## 📁 Project Structure

```
library-management-system/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── library/
│                   ├── LibraryManagementDemo.java
│                   ├── core/
│                   │   ├── LibrarySystem.java
│                   │   └── LibraryBranch.java
│                   ├── model/
│                   │   ├── Book.java
│                   │   └── Patron.java
│                   ├── transaction/
│                   │   └── Transaction.java
│                   ├── search/
│                   │   └── BookSearchStrategy.java
│                   ├── observer/
│                   │   └── ReservationObserver.java
│                   └── recommendation/
│                       └── RecommendationSystem.java
├── README.md
└── pom.xml (if using Maven)
```

## 🚀 Setup and Installation

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Maven or Gradle (optional, for dependency management)
- IDE (IntelliJ IDEA, Eclipse, or VS Code recommended)

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/library-management-system.git
   cd library-management-system
   ```

2. **Compile the project**
   ```bash
   javac -d bin src/main/java/com/library/**/*.java
   ```

3. **Run the demo**
   ```bash
   java -cp bin com.library.LibraryManagementSystemApplication
   ```

### Using Maven (Optional)

If you prefer using Maven, create a `pom.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>com.library</groupId>
    <artifactId>library-management-system</artifactId>
    <version>1.0-SNAPSHOT</version>
    
    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
    </properties>
    
    <dependencies>
        <!-- JUnit for testing -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

Then compile and run:
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.library.LibraryManagementSystemApplication"
```

