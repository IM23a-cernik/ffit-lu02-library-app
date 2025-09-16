package ch.bzz;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    public static String get(String item) {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("config.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return props.getProperty(item);
    }

    private static final Properties CACHED = new Properties();
    static {
        try (FileInputStream in = new FileInputStream("config.properties")) {
            CACHED.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }


    public static Properties getProperties() {
        Properties p = new Properties();
        String url = CACHED.getProperty("jakarta.persistence.jdbc.url",
                CACHED.getProperty("DB_URL"));
        String user = CACHED.getProperty("jakarta.persistence.jdbc.user",
                CACHED.getProperty("DB_USER"));
        String pass = CACHED.getProperty("jakarta.persistence.jdbc.password",
                CACHED.getProperty("DB_PASSWORD"));

        if (url != null) p.setProperty("jakarta.persistence.jdbc.url", url);
        if (user != null) p.setProperty("jakarta.persistence.jdbc.user", user);
        if (pass != null) p.setProperty("jakarta.persistence.jdbc.password", pass);
        return p;
    }

}
