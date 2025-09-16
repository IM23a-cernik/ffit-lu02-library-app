package ch.bzz.io;

import ch.bzz.Config;
import ch.bzz.model.Book;
import ch.bzz.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserCreator {
    private static final Logger log = LoggerFactory.getLogger(UserCreator.class);
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("localPU", Config.getProperties());

    public static void saveAll(User user) {
        try (EntityManager em = emf.createEntityManager()) {
            try {
                em.getTransaction().begin();
                em.merge(user);
                em.getTransaction().commit();
            } catch (RuntimeException e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                log.error("Error during saving of user to the database:", e);
            }
        }
    }
}
