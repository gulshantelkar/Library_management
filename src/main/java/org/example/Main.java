package org.example;

import java.sql.*;
import java.util.Scanner;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:library.db";

        try (Connection connection = DriverManager.getConnection(url)) {
            System.out.println("Connected to the database.");

            Database.createBookTable(connection);
            Database.createPatronTable(connection);
            Database.createTransactionTable(connection);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("Main Menu:");
                System.out.println("1. Admin");
                System.out.println("2. Patron");
                System.out.println("3. Transaction");
                System.out.println("4. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        adminOperations(connection);
                        break;
                    case 2:
                        patronOperations(connection);
                        break;
                    case 3:
                        performTransaction(connection);
                        break;
                    case 4:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }

    private static void adminOperations(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Operations of Admin:");
            System.out.println("1. Add new books to the library");
            System.out.println("2. Update book information");
            System.out.println("3. Remove books from the library");
            System.out.println("4. Display book info");
            System.out.println("5. Go to main menu");
            System.out.print("Enter your choice: ");

            int adminChoice = scanner.nextInt();

            switch (adminChoice) {
                case 1:
                    AddBook(connection);
                    break;
                case 2:
                    UpdateBook(connection);
                    break;
                case 3:
                    RemoveBook(connection);
                    break;
                case 4:
                    DisplayBook(connection);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void DisplayBook(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter ISBN of the book to display information: ");
        String displayISBN = scanner.nextLine();
        Database.displayBookInfo(connection, displayISBN);
    }
    private static void AddBook(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter ISBN: ");
        String isbn = scanner.nextLine();
        System.out.println("Enter Title: ");
        String title = scanner.nextLine();
        System.out.println("Enter Author: ");
        String author = scanner.nextLine();
        System.out.println("Enter Genre: ");
        String genre = scanner.nextLine();
        System.out.println("Is the book available? (true/false): ");
        boolean availability = scanner.nextBoolean();

        Book newBook = new Book(isbn, title, author, genre, availability);
        Database.addBook(connection, newBook);
    }

    private static void UpdateBook(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter ISBN of the book to update: ");
        String isbn = scanner.nextLine();


        Book existingBook = Database.searchBook(connection, isbn);
        if (existingBook != null) {
            System.out.println("Enter Updated Title: ");
            String updatedTitle = scanner.nextLine();
            System.out.println("Enter Updated Author: ");
            String updatedAuthor = scanner.nextLine();
            System.out.println("Enter Updated Genre: ");
            String updatedGenre = scanner.nextLine();
            System.out.println("Is the book available? (true/false): ");
            boolean updatedAvailability = scanner.nextBoolean();

            Book updatedBook = new Book(isbn, updatedTitle, updatedAuthor, updatedGenre, updatedAvailability);
            Database.updateBook(connection, isbn, updatedBook);
        }
    }

    private static void RemoveBook(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter ISBN of the book to remove: ");
        String isbn = scanner.nextLine();

        Book existingBook = Database.searchBook(connection, isbn);
        if (existingBook != null) {
            Database.removeBook(connection, isbn);
        }
    }
    private static void performTransaction(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Book ID: ");
        int bookID = scanner.nextInt();
        System.out.println("Enter Patron ID: ");
        int patronID = scanner.nextInt();
        System.out.println("Enter Due Date (YYYY-MM-DD): ");
        String dueDateString = scanner.next();
        LocalDate dueDate = LocalDate.parse(dueDateString);


        int transactionID = generateUniqueTransactionID(connection);
        Transaction borrowedTransaction = Transaction.borrowBook(transactionID, bookID, patronID, dueDate);

        Database.addTransaction(connection, borrowedTransaction);

        System.out.println("Simulating return. Enter Return Date (YYYY-MM-DD): ");
        String returnDateString = scanner.next();
        LocalDate returnDate = LocalDate.parse(returnDateString);

        double fine = Transaction.calculateFine(dueDate, returnDate);
        borrowedTransaction.setFine(fine);

        Transaction returnedTransaction = Transaction.returnBook(transactionID, borrowedTransaction, returnDate);

        Database.updateTransaction(connection, returnedTransaction);
        System.out.println("Transaction details:");
        System.out.println("Transaction ID: " + returnedTransaction.getTransactionID());
        System.out.println("Book ID: " + returnedTransaction.getBookID());
        System.out.println("Patron ID: " + returnedTransaction.getPatronID());
        System.out.println("Due Date: " + returnedTransaction.getDueDate());
        System.out.println("Return Date: " + returnedTransaction.getReturnDate());
        System.out.println("Fine: $" + returnedTransaction.getFine());
    }

    private static int generateUniqueTransactionID(Connection connection) throws SQLException {
        int generatedID;
        do {
            generatedID = (int) (Math.random() * Integer.MAX_VALUE);
        } while (!isTransactionIDUnique(connection, generatedID));

        return generatedID;
    }
    private static boolean isTransactionIDUnique(Connection connection, int transactionID) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT 1 FROM transactions WHERE transaction_id = ?")) {
            ((PreparedStatement) statement).setInt(1, transactionID);
            try (ResultSet resultSet = statement.executeQuery()) {
                return !resultSet.next();
            }
        }
    }
    private static void patronOperations(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Operations of Patron:");
            System.out.println("1. Register new library patrons");
            System.out.println("2. Update patron information");
            System.out.println("3. Remove patrons from the system");
            System.out.println("4. Display Patron info");
            System.out.println("5. Go to main menu");
            System.out.print("Enter your choice: ");

            int patronChoice = scanner.nextInt();

            switch (patronChoice) {
                case 1:

                    System.out.println("Enter Patron ID: ");
                    int patronID = scanner.nextInt();
                    System.out.println("Enter Patron Name: ");
                    scanner.nextLine();
                    String patronName = scanner.nextLine();
                    System.out.println("Enter Patron Contact Information: ");
                    String contactInfo = scanner.nextLine();
                    Patron newPatron = new Patron(patronID, patronName, contactInfo);
                    Database.addPatron(connection, newPatron);
                    break;
                case 2:

                    System.out.println("Enter Patron ID to update: ");
                    int updatePatronID = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter Updated Patron Name: ");
                    String updatedName = scanner.nextLine();
                    System.out.println("Enter Updated Patron Contact Information: ");
                    String updatedContactInfo = scanner.nextLine();
                    Patron updatedPatron = new Patron(updatePatronID, updatedName, updatedContactInfo);
                    Database.updatePatron(connection, updatePatronID, updatedPatron);
                    break;
                case 3:
                    System.out.println("Enter Patron ID to remove: ");
                    int removePatronID = scanner.nextInt();
                    Database.removePatron(connection, removePatronID);
                    break;
                case 4:
                    System.out.println("Enter Patron ID to display information: ");
                    int displayPatronID = scanner.nextInt();
                    Database.displayPatronInfo(connection, displayPatronID);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
