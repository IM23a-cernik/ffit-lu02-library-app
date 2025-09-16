package ch.bzz.db;

import ch.bzz.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookPersistor {
    private static final Logger log = LoggerFactory.getLogger(BookPersistor.class);

    public static void listBooks(String limit) {
        List<Book> books = new ArrayList<>();
        try {
            if (limit != null) {
                Integer.parseInt(limit);
            }
            try (Connection con = Database.getConnection();
                 Statement stmt = con.createStatement();
                 ResultSet resultSet = stmt.executeQuery(
                         "SELECT id, isbn, title, author, publication_year FROM books ORDER BY id ASC LIMIT " + limit)) {

                while (resultSet.next()) {
                    Book book = new Book();
                    book.setId(resultSet.getInt("id"));
                    book.setIsbn(resultSet.getString("isbn"));
                    book.setTitle(resultSet.getString("title"));
                    book.setAuthor(resultSet.getString("author"));
                    book.setYear(resultSet.getInt("publication_year"));
                    books.add(book);
                }

                for (Book book : books) {
                    System.out.println(book);
                }
            } catch (SQLException e) {
                log.error("Error while retrieving books.", e);
            }
        } catch (NumberFormatException e) {
            log.error("Limit muss ein integer sein.", e);
        }
    }
}
