package org.example;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public  class Database {
    private static final String URL = "jdbc:sqlite:library.db";


    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void createBookTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Book (" +
                "ISBN TEXT PRIMARY KEY," +
                "title TEXT," +
                "author TEXT," +
                "genre TEXT," +
                "availability BOOLEAN" +
                ");";

        try (PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL)) {
            preparedStatement.executeUpdate();
        }
    }
    public static void addBook(Connection connection, Book newBook) throws SQLException {
        String insertSQL = "INSERT INTO Book (ISBN, title, author, genre, availability) VALUES (?, ?, ?, ?, ?);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, newBook.getISBN());
            preparedStatement.setString(2, newBook.getTitle());
            preparedStatement.setString(3, newBook.getAuthor());
            preparedStatement.setString(4, newBook.getGenre());
            preparedStatement.setBoolean(5, newBook.isAvailable());

            preparedStatement.executeUpdate();
            System.out.println("Book added successfully: " + newBook.getTitle());
        }
    }
    public static void displayBookInfo(Connection connection, String ISBN) throws SQLException {
        Book book = Database.searchBook(connection, ISBN);

        if (book != null) {
            System.out.println("Book Information:");
            System.out.println("ISBN: " + book.getISBN());
            System.out.println("Title: " + book.getTitle());
            System.out.println("Author: " + book.getAuthor());
            System.out.println("Genre: " + book.getGenre());
            System.out.println("Availability: " + (book.isAvailable() ? "Available" : "Not Available"));
        }
    }
    public static void updateBook(Connection connection, String ISBN, Book updatedBook) throws SQLException {
        String updateSQL = "UPDATE Book SET title=?, author=?, genre=?, availability=? WHERE ISBN=?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, updatedBook.getTitle());
            preparedStatement.setString(2, updatedBook.getAuthor());
            preparedStatement.setString(3, updatedBook.getGenre());
            preparedStatement.setBoolean(4, updatedBook.isAvailable());
            preparedStatement.setString(5, ISBN);

            preparedStatement.executeUpdate();
            System.out.println("Book updated successfully: " + updatedBook.getTitle());
        }
    }

    public static void removeBook(Connection connection, String ISBN) throws SQLException {
        String deleteSQL = "DELETE FROM Book WHERE ISBN=?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setString(1, ISBN);

            preparedStatement.executeUpdate();
            System.out.println("Book removed successfully with ISBN: " + ISBN);
        }
    }

    public static Book searchBook(Connection connection, String ISBN) throws SQLException {
        String selectSQL = "SELECT * FROM Book WHERE ISBN=?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setString(1, ISBN);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Book(
                            resultSet.getString("ISBN"),
                            resultSet.getString("title"),
                            resultSet.getString("author"),
                            resultSet.getString("genre"),
                            resultSet.getBoolean("availability")
                    );
                }
            }
        }

        System.out.println("Book with ISBN " + ISBN + " not found.");
        return null;
    }

    public static void createPatronTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Patron (\n" +
                "  patronID INTEGER PRIMARY KEY,\n" +
                "  name TEXT NOT NULL,\n" +
                "  contactInformation TEXT\n" +
                ");";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
            System.out.println("Patron table created successfully.");
        } catch (SQLException e) {
            System.err.println("Error creating Patron table: " + e.getMessage());
            throw e;
        }
    }

    public static void addPatron(Connection connection, Patron newPatron) throws SQLException {
        String insertSQL = "INSERT INTO Patron (patronID, name, contactInformation) VALUES (?, ?, ?);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, newPatron.getPatronID());
            preparedStatement.setString(2, newPatron.getName());
            preparedStatement.setString(3, newPatron.getContactInformation());

            preparedStatement.executeUpdate();
            System.out.println("Patron added successfully: " + newPatron.getName());
        }
    }

    public static void updatePatron(Connection connection, int patronID, Patron updatedPatron) throws SQLException {
        String updateSQL = "UPDATE Patron SET name=?, contactInformation=? WHERE patronID=?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, updatedPatron.getName());
            preparedStatement.setString(2, updatedPatron.getContactInformation());
            preparedStatement.setInt(3, patronID);

            preparedStatement.executeUpdate();
            System.out.println("Patron updated successfully: " + updatedPatron.getName());
        }
    }

    public static void removePatron(Connection connection, int patronID) throws SQLException {
        String deleteSQL = "DELETE FROM Patron WHERE patronID=?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, patronID);

            preparedStatement.executeUpdate();
            System.out.println("Patron removed successfully with ID: " + patronID);
        }
    }
    public static void displayPatronInfo(Connection connection, int patronID) throws SQLException {
        Patron patron = Database.searchPatron(connection, patronID);

        if (patron != null) {
            System.out.println("Patron Information:");
            System.out.println("Patron ID: " + patron.getPatronID());
            System.out.println("Name: " + patron.getName());
            System.out.println("Contact Information: " + patron.getContactInformation());
        }
    }
    public static Patron searchPatron(Connection connection, int patronID) throws SQLException {
        String selectSQL = "SELECT * FROM Patron WHERE patronID=?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, patronID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Patron(
                            resultSet.getInt("patronID"),
                            resultSet.getString("name"),
                            resultSet.getString("contactInformation")
                    );
                }
            }
        }

        System.out.println("Patron with ID " + patronID + " not found.");
        return null;
    }
    public static void createTransactionTable(Connection connection) {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS transactions (" +
                "transaction_id INTEGER PRIMARY KEY," +
                "book_id INTEGER," +
                "patron_id INTEGER," +
                "due_date TEXT," +
                "return_date TEXT," +
                "fine REAL" +
                ");";

        try (PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL)) {
            preparedStatement.execute();
            System.out.println("Transaction table created successfully.");
        } catch (SQLException e) {
            System.err.println("Error creating transaction table: " + e.getMessage());
        }
    }
    public static Transaction getTransaction(Connection connection, int transactionID) {
        String selectSQL = "SELECT * FROM transactions WHERE transaction_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, transactionID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int bookID = resultSet.getInt("book_id");
                    int patronID = resultSet.getInt("patron_id");
                    LocalDate dueDate = resultSet.getDate("due_date").toLocalDate();
                    LocalDate returnDate = resultSet.getDate("return_date") != null
                            ? resultSet.getDate("return_date").toLocalDate()
                            : null;
                    double fine = resultSet.getDouble("fine");

                    return new Transaction(transactionID, bookID, patronID, dueDate, returnDate, fine);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting transaction: " + e.getMessage());
        }
        return null;
    }
    public static void updateTransaction(Connection connection, Transaction transaction) {
        String updateSQL = "UPDATE transactions SET book_id = ?, patron_id = ?, due_date = ?, return_date = ?, fine = ? WHERE transaction_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setInt(1, transaction.getBookID());
            preparedStatement.setInt(2, transaction.getPatronID());
            preparedStatement.setDate(3, Date.valueOf(transaction.getDueDate()));
            preparedStatement.setDate(4, transaction.getReturnDate() != null ? Date.valueOf(transaction.getReturnDate()) : null);
            preparedStatement.setDouble(5, transaction.getFine());
            preparedStatement.setInt(6, transaction.getTransactionID());

            preparedStatement.executeUpdate();
            System.out.println("Transaction updated successfully. Transaction ID: " + transaction.getTransactionID());
        } catch (SQLException e) {
            System.err.println("Error updating transaction: " + e.getMessage());
        }
    }

    public static void addTransaction(Connection connection, Transaction transaction) {
        String insertSQL = "INSERT INTO transactions (transaction_id, book_id, patron_id, due_date, return_date, fine) " +
                "VALUES (?, ?, ?, ?, ?, ?);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, transaction.getTransactionID());
            preparedStatement.setInt(2, transaction.getBookID());
            preparedStatement.setInt(3, transaction.getPatronID());
            preparedStatement.setString(4, transaction.getDueDate().toString());
            preparedStatement.setString(5, transaction.getReturnDate() != null ? transaction.getReturnDate().toString() : null);
            preparedStatement.setDouble(6, transaction.getFine());

            preparedStatement.executeUpdate();
            System.out.println("Transaction added successfully. Transaction ID: " + transaction.getTransactionID());
        } catch (SQLException e) {
            System.err.println("Error adding transaction: " + e.getMessage());
        }
    }
}
