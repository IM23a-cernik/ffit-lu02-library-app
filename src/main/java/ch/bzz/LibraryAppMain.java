package ch.bzz;


import java.util.List;
import java.util.Scanner;

public class LibraryAppMain {

    public static void main(String[] args) {
        String selection = "";
        String[] commands = {"help", "quit", "listBooks"};
        Scanner console = new Scanner(System.in);
        while (!selection.equals("quit")) {
            System.out.println("Gib einen Befehl ein: ");
            selection = console.nextLine();
            switch (selection) {
                case "quit" -> System.out.println("Programm beendet.");
                case "help" -> {
                    for (String command : commands) {
                        System.out.println(command);
                    }
                }
                case "listBooks" -> {
                    List<Book> books = Book.connectDB();
                    for (Book book : books) {
                        System.out.println(book);
                    }
                }
                default -> System.out.println(selection + " wurde nicht erkannt.");
            }
        }
    }
}
