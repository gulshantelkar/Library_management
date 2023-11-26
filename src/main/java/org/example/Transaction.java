package org.example;

import java.time.LocalDate;

public class Transaction {
    private int transactionID;
    private int bookID;
    private int patronID;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double fine;

    public Transaction(int transactionID, int bookID, int patronID, LocalDate dueDate, LocalDate returnDate, double fine) {
        this.transactionID = transactionID;
        this.bookID = bookID;
        this.patronID = patronID;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.fine = fine;
    }
    public int getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public int getPatronID() {
        return patronID;
    }

    public void setPatronID(int patronID) {
        this.patronID = patronID;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public double getFine() {
        return fine;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }

    public static Transaction borrowBook(int transactionID, int bookID, int patronID, LocalDate dueDate) {

        System.out.println("Book with ID " + bookID + " borrowed by Patron with ID " + patronID + ". Transaction ID: " + transactionID);

        return new Transaction(transactionID, bookID, patronID, dueDate, null, 0.0);
    }


    public static Transaction returnBook(int returnTransactionID, Transaction borrowedTransaction, LocalDate returnDate) {
        borrowedTransaction.setReturnDate(returnDate);
        borrowedTransaction.setFine(calculateFine(borrowedTransaction.getDueDate(), returnDate));

        System.out.println("Book returned. Transaction ID: " + borrowedTransaction.getTransactionID() + ". Return Date: " + returnDate);

        return new Transaction(returnTransactionID, borrowedTransaction.getBookID(), borrowedTransaction.getPatronID(), borrowedTransaction.getDueDate(), returnDate, borrowedTransaction.getFine());
    }

    public static double calculateFine(LocalDate dueDate, LocalDate returnDate) {

        long daysOverdue = Math.max(0, dueDate.until(returnDate).getDays());
        return daysOverdue * 1.0;
    }
}
