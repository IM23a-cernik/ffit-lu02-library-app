package ch.bzz.db;

import ch.bzz.Config;
import ch.bzz.model.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BookPersistor {
    private static final Logger log = LoggerFactory.getLogger(BookPersistor.class);
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("localPU", Config.getProperties());

    public static List<Book> listBooks(String limit) {
        List<Book> books = new ArrayList<>();
        if (limit == null) {
            limit = "0";
        }

        try {
            try (EntityManager em = emf.createEntityManager()) {
                var query = em.createQuery("SELECT b FROM Book b ORDER BY id", Book.class);
                if (Integer.parseInt(limit) > 0) {
                    query.setMaxResults(Integer.parseInt(limit));
                }
                return query.getResultList();
            }
        } catch (NumberFormatException e) {
            log.error("Limit muss ein integer sein.", e);
        }
        return books;
    }
}
