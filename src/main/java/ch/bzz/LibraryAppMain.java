package ch.bzz;


import java.util.Scanner;

public class LibraryAppMain {

    public static void main(String[] args) {
        String selection = "";
        String[] commands = {"help", "quit"};
        Scanner console = new Scanner(System.in);
        while (!selection.equals("quit")) {
            System.out.println("Gib einen Befehl ein: ");
            selection = console.nextLine();
            if (selection.equals("quit")) {
                System.out.println("Programm beendet.");
            } else if (selection.equals("help")) {
                for (String command : commands) {
                    System.out.println(command);
                }
            } else {
                System.out.println(selection+" wurde nicht erkannt.");
            }
        }
    }
}
