package org.example;

import java.io.Serializable;
import java.util.*;

class Passenger implements Serializable {
    public int id;
    public String name;
    public List<PassengerTripHistory> tripHistory;

    // Конструктор
    public Passenger(String name, List<PassengerTripHistory> tripHistory) {
        this.name = name;
        this.tripHistory = tripHistory;
    }

    public Passenger(int id, String name, List<PassengerTripHistory> tripHistory) {
        this.id = id;
        this.name = name;
        this.tripHistory = tripHistory;
    }

    // Метод для знаходження найчастіших поїздок
    public PassengerTripHistory mostFrequentTrip() {
        Map<PassengerTripHistory, Integer> tripCount = new HashMap<>();
        for (PassengerTripHistory trip : tripHistory) {
            tripCount.put(trip, tripCount.getOrDefault(trip, 0) + 1);
        }
        return Collections.max(tripCount.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    public static Passenger generatePassenger() {
        String name = "Іван Іванов";
        List<PassengerTripHistory> tripHistory = new ArrayList<>();
        tripHistory.add(PassengerTripHistory.generatePassengerTripHistory());
        return new Passenger(name, tripHistory);
    }

    @Override public String toString() {
        String tripHistoryString = "";

        for (PassengerTripHistory trip : tripHistory) {
            tripHistoryString += trip.toString();
        }

        return "Ім'я: " + name + "\n" +
                "Історія поїздок:" + tripHistoryString;
    }
}
