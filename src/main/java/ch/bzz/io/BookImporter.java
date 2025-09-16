package ch.bzz.io;

import ch.bzz.Config;
import ch.bzz.db.Database;
import ch.bzz.model.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class BookImporter {
    private static final Logger log = LoggerFactory.getLogger(BookImporter.class);
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("localPU", Config.jpaProperties());

    public static void importBooks(List<Book> books) {
        try (EntityManager em = emf.createEntityManager()) {
            try {
                em.getTransaction().begin();
                books.forEach(em::merge);
                em.getTransaction().commit();
            } catch (RuntimeException e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                log.error("Error during saving of books to the database:", e);
            }
        }
    }
}
