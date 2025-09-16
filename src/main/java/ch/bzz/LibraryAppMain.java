package ch.bzz;


import ch.bzz.db.BookPersistor;
import ch.bzz.io.DelimitedFileReader;
import ch.bzz.io.UserCreator;
import ch.bzz.model.Book;
import ch.bzz.model.User;

import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class LibraryAppMain {

    public static void main(String[] args) {
        String selection = "";
        String[] commands = {"help", "quit", "listBooks", "importBooks", "createUser"};
        Scanner console = new Scanner(System.in);

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
                    int limit = 0;
                    if (arg != null) {
                        try {
                            limit = Integer.parseInt(arg);
                        } catch (NumberFormatException e) {
                            System.out.println("Argument muss eine Zahl sein.");
                            return;
                        }
                    }
                    List<Book> books = BookPersistor.getAll(limit);
                    for (Book book : books) {
                        System.out.println(book);
                    }
                }
                case "importBooks" -> {
                    DelimitedFileReader.readFile(arg);
                }

                case "createUser" -> {
                    String[] parts = trimmed.split("\\s+");
                    Date date = Date.valueOf(parts[3]);
                    try {
                        User user = new User(parts[1], parts[2], date, parts[4], parts[5]);
                        UserCreator.saveAll(user);
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    }
                }
                default -> System.out.println(selection + " wurde nicht erkannt.");
            }
        }
    }
}
