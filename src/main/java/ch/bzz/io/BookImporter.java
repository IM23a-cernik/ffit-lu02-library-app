package ch.bzz.io;

import ch.bzz.db.Database;
import ch.bzz.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class BookImporter {
    public static void importBooks(List<Book> books) {
        String updateSql = "UPDATE books SET isbn = ?, title = ?, author = ?, publication_year = ? WHERE id = ?";
        String insertSql = "INSERT INTO books (id, isbn, title, author, publication_year) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Database.getConnection();
             PreparedStatement updatePstmt = con.prepareStatement(updateSql);
             PreparedStatement insertPstmt = con.prepareStatement(insertSql)) {

            boolean originalAutoCommit = con.getAutoCommit();
            con.setAutoCommit(false);
            try {
                for (Book book : books) {
                    updatePstmt.setString(1, book.getIsbn());
                    updatePstmt.setString(2, book.getTitle());
                    updatePstmt.setString(3, book.getAuthor());
                    updatePstmt.setInt(4, book.getYear());
                    updatePstmt.setInt(5, book.getId());
                    int updated = updatePstmt.executeUpdate();

                    if (updated == 0) {
                        insertPstmt.setInt(1, book.getId());
                        insertPstmt.setString(2, book.getIsbn());
                        insertPstmt.setString(3, book.getTitle());
                        insertPstmt.setString(4, book.getAuthor());
                        insertPstmt.setInt(5, book.getYear());
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
