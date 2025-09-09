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

    public static void importBooks(List<Book> books) {
        String updateSql = "UPDATE books SET isbn = ?, title = ?, author = ?, publication_year = ? WHERE id = ?";
        String insertSql = "INSERT INTO books (id, isbn, title, author, publication_year) VALUES (?, ?, ?, ?, ?)";
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("config.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (Connection con = DriverManager.getConnection(props.getProperty("DB_URL"), props.getProperty("DB_USER"), props.getProperty("DB_PASSWORD"));
             PreparedStatement updatePstmt = con.prepareStatement(updateSql);
             PreparedStatement insertPstmt = con.prepareStatement(insertSql)) {

            boolean originalAutoCommit = con.getAutoCommit();
            con.setAutoCommit(false);
            try {
                for (Book book : books) {
                    updatePstmt.setString(1, book.isbn);
                    updatePstmt.setString(2, book.title);
                    updatePstmt.setString(3, book.author);
                    updatePstmt.setInt(4, book.year);
                    updatePstmt.setInt(5, book.id);
                    int updated = updatePstmt.executeUpdate();

                    if (updated == 0) {
                        insertPstmt.setInt(1, book.id);
                        insertPstmt.setString(2, book.isbn);
                        insertPstmt.setString(3, book.title);
                        insertPstmt.setString(4, book.author);
                        insertPstmt.setInt(5, book.year);
                        insertPstmt.executeUpdate();
                    }
                }
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(originalAutoCommit);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
