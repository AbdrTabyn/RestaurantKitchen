package Facade;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AdminFacade {
    public void showAllData() {
        System.out.println("\n=== RESTAURANT DATA ===");
        System.out.println("\n--- ORDERS ---");
        showFile("orders.txt");

        System.out.println("\n--- KITCHEN ---");
        showFile("kitchen.txt");

        System.out.println("\n--- DELIVERIES ---");
        showFile("delivery.txt");

        System.out.println("\n--- CHAIN STATE ---");
        showFile("chain.txt");
    }

    private void showFile(String fileName) {
        Path path = Path.of(fileName);
        if (!Files.exists(path)) {
            System.out.println("No data.");
            return;
        }
        try {
            Files.lines(path).forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Could not read " + fileName);
        }
    }
}