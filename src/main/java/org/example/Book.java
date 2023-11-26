package org.example;
import java.util.ArrayList;
import java.util.List;

public class Book {
    private String ISBN;
    private String title;
    private String author;
    private String genre;
    private boolean availability;


    public Book(String ISBN, String title, String author, String genre, boolean availability) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.availability = availability;
    }


    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public boolean isAvailable() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public static void addBook(List<Book> bookCollection, Book newBook) {
        bookCollection.add(newBook);
        System.out.println("Book added successfully: " + newBook.getTitle());
    }


    public static void updateBook(List<Book> bookCollection, String ISBN, Book updatedBook) {
        for (int i = 0; i < bookCollection.size(); i++) {
            if (bookCollection.get(i).getISBN().equals(ISBN)) {
                bookCollection.set(i, updatedBook);
                System.out.println("Book updated successfully: " + updatedBook.getTitle());
                return;
            }
        }
        System.out.println("Book with ISBN " + ISBN + " not found.");
    }

    public static void removeBook(List<Book> bookCollection, String ISBN) {
        bookCollection.removeIf(book -> book.getISBN().equals(ISBN));
        System.out.println("Book removed successfully with ISBN: " + ISBN);
    }

    public static Book searchBook(List<Book> bookCollection, String ISBN) {
        for (Book book : bookCollection) {
            if (book.getISBN().equals(ISBN)) {
                return book;
            }
        }
        System.out.println("Book with ISBN " + ISBN + " not found.");
        return null;
    }
}
