package ch.bzz;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Book {
    int id;
    String isbn;
    String title;
    String author;
    int year;

    @Override
    public String toString() {
        return id + " " + isbn + " " + title + " " + author + " " + year;
    }

    public static List<Book> connectDB() {
        List<Book> books = new ArrayList<>();
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("config.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (Connection con = DriverManager.getConnection(
                props.getProperty("DB_URL"), props.getProperty("DB_USER"), props.getProperty("DB_PASSWORD"));
             Statement stmt = con.createStatement();
             ResultSet resultSet = stmt.executeQuery(
                     "SELECT id, isbn, title, author, publication_year FROM books")) {

            while (resultSet.next()) {
                Book book = new Book();
                book.id = resultSet.getInt("id");
                book.isbn = resultSet.getString("isbn");
                book.title = resultSet.getString("title");
                book.author = resultSet.getString("author");
                book.year = resultSet.getInt("publication_year");
                books.add(book);
            }
        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }
        return books;
    }
}
