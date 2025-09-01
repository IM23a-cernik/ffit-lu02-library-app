package ch.bzz.io;

import ch.bzz.db.BookPersistor;
import ch.bzz.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import static ch.bzz.io.BookImporter.importBooks;

public class DelimitedFileReader {
    private static final Logger log = LoggerFactory.getLogger(DelimitedFileReader.class);

    public static void readFile(String arg) {
        String delimiter = "\t";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(arg))) {
            List<Book> books = reader.lines()
                    .skip(1)
                    .map(line -> line.split(delimiter, -1))
                    .map(cols -> {
                        Book b = new Book();
                        b.setId(Integer.parseInt(cols[0]));
                        b.setIsbn(cols[1]);
                        b.setTitle(cols[2]);
                        b.setAuthor(cols[3]);
                        b.setYear(Integer.parseInt(cols[4]));
                        return b;
                    })
                    .toList();
            importBooks(books);
            System.out.println("Import abgeschlossen.");
        } catch (IOException e) {
            log.error("Datei unter dem Pfad {} konnte nicht gelesen werden.", arg);
        }
    }
}
