package org.example;

import java.io.Serializable;

class PassengerTripHistory implements Serializable {
    int id;
    public String name;
    public String date;

    // Конструктор
    public PassengerTripHistory(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public PassengerTripHistory(int id, String name, String date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public static PassengerTripHistory generatePassengerTripHistory() {
        String name = "Generated";
        String date = "2023-11-01";
        return new PassengerTripHistory(name, date);
    }

    @Override public String toString() {
        return "\n\tІдентифікатор: " + id +
                "\n\tДата: " + date + "" +
                "\n\tНазва: " + name + "";
    }
}
