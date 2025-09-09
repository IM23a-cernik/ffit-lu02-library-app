package ch.bzz;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static ch.bzz.Book.importBooks;

public class LibraryAppMain {

    public static void main(String[] args) {
        String selection = "";
        String[] commands = {"help", "quit", "listBooks", "importBooks"};
        Scanner console = new Scanner(System.in);
        String delimiter = "\t";
        while (!selection.equals("quit")) {
            System.out.println("Gib einen Befehl ein: ");
            selection = console.nextLine();
            String trimmed = selection.trim();
            String command = trimmed.split("\\s+", 2)[0];
            String arg = trimmed.contains(" ") ? trimmed.substring(trimmed.indexOf(' ') + 1).trim() : null;
            switch (command) {
                case "quit" -> System.out.println("Programm beendet.");
                case "help" -> {
                    for (String cmd : commands) {
                        System.out.println(cmd);
                    }
                }
                case "listBooks" -> {
                    List<Book> books = Book.connectDB();
                    for (Book book : books) {
                        System.out.println(book);
                    }
                }
                case "importBooks" -> {
                    try (BufferedReader reader = Files.newBufferedReader(Paths.get(arg))) {
                        List<Book> books = reader.lines()
                                .skip(1)
                                .map(line -> line.split(delimiter, -1))
                                .map(cols -> {
                                    Book b = new Book();
                                    b.id = Integer.parseInt(cols[0]);
                                    b.isbn = cols[1];
                                    b.title = cols[2];
                                    b.author = cols[3];
                                    b.year = Integer.parseInt(cols[4]);
                                    return b;
                                })
                                .toList();
                        importBooks(books);
                        System.out.println("Import abgeschlossen.");
                    } catch (IOException e) {
                        System.out.println("Datei unter dem Pfad " + arg + " konnte nicht gelesen werden.");
                    }
                }
                default -> System.out.println(selection + " wurde nicht erkannt.");
            }
        }
    }
}
