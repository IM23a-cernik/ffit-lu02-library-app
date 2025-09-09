package ch.bzz.db;

import ch.bzz.model.Book;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookPersistor {
    public static void listBooks() {
        List<Book> books = new ArrayList<>();
        try (Connection con = Database.getConnection();
             Statement stmt = con.createStatement();
             ResultSet resultSet = stmt.executeQuery(
                     "SELECT id, isbn, title, author, publication_year FROM books")) {

            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setIsbn(resultSet.getString("isbn"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setYear(resultSet.getInt("publication_year"));
                books.add(book);
            }
        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }
        for (Book book : books) {
            System.out.println(book);
        }
    }
}
