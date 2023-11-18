package org.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Route implements Serializable {
    public String name;
    public List<String> stops;
    public String travelTime;
    public String schedule;
    public String transportType;

    // Конструктор
    public Route(String name, List<String> stops, String travelTime, String schedule, String transportType) {
        this.name = name;
        this.stops = stops;
        this.travelTime = travelTime;
        this.schedule = schedule;
        this.transportType = transportType;
    }

    public static Route generateRoute() {
        List<String> stops = new ArrayList<>(Arrays.asList("Зупинка 1", "Зупинка 2", "Зупинка 3"));
        String name = "Маршрут 1";
        String travelTime = "2 години";
        String schedule = "Графік 1";
        String transportType = "Автобус";
        return new Route(name, stops, travelTime, schedule, transportType);
    }

    @Override public String toString() {
        String stopsString = "";
        for (String stop : stops) {
            stopsString += stop + "\n";
        }
        return "Назва: " + name + "\n" +
                "Зупинки: \n" + stopsString +
                "Час подорожі: " + travelTime + "\n" +
                "Графік: " + schedule + "\n" +
                "Тип транспорту: " + transportType + "\n";
    }
}
