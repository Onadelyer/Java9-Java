package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    private static final String TRANSPORT_FILE_PATH = "transport.ser";
    private static final String PASSENGERS_FILE_PATH = "passengers.ser";

    public static List<Transport> readTransportFromFile() {
        List<Transport> transports = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(TRANSPORT_FILE_PATH))) {
            if (new File(TRANSPORT_FILE_PATH).length() > 0) { // Check if the file is not empty
                transports = (List<Transport>) ois.readObject();
            }
        } catch (FileNotFoundException e) {
            createFile(TRANSPORT_FILE_PATH);
        } catch (EOFException e) {
            // Handle EOFException if the file is empty
            System.out.println("The file is empty.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return transports;
    }

    public static List<Passenger> readPassengersFromFile() {
        List<Passenger> passengers = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PASSENGERS_FILE_PATH))) {
            if (new File(PASSENGERS_FILE_PATH).length() > 0) { // Check if the file is not empty
                passengers = (List<Passenger>) ois.readObject();
            }
        } catch (FileNotFoundException e) {
            createFile(PASSENGERS_FILE_PATH);
        } catch (EOFException e) {
            // Handle EOFException if the file is empty
            System.out.println("The file is empty.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return passengers;
    }

    public static void writePassengersToFile(List<Passenger> passengers) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PASSENGERS_FILE_PATH))) {
            oos.writeObject(passengers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeTransportsToFile(List<Transport> passengers) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TRANSPORT_FILE_PATH))) {
            oos.writeObject(passengers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}