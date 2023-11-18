package org.example;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

class Transport implements Serializable {
    public int id;
    public String fuelType;
    public int maxCapacity;
    public String driverInfo;
    public List<String> crewInfo;

    public Transport(String fuelType, int maxCapacity, String driverInfo, List<String> crewInfo) {
        this.fuelType = fuelType;
        this.maxCapacity = maxCapacity;
        this.driverInfo = driverInfo;
        this.crewInfo = crewInfo;
    }

    public Transport(int id, String fuelType, int maxCapacity, String driverInfo, List<String> crewInfo) {
        this.id = id;
        this.fuelType = fuelType;
        this.maxCapacity = maxCapacity;
        this.driverInfo = driverInfo;
        this.crewInfo = crewInfo;
    }

    public static Transport generateTransport() {
        String fuelType = "дизель";
        int maxCapacity = 50;
        String driverInfo = "Іван Петров";
        List<String> crewInfo = Arrays.asList("Марія Іванова", "Петро Сидоров");
        return new Transport(fuelType, maxCapacity, driverInfo, crewInfo);
    }

    @Override public String toString(){
        String crewInfoString = "";

        for (String crewMember : crewInfo) {
            crewInfoString += "\t" + crewMember + "\n";
        }

        return "Ідентифікатор: " + id + "\n" +
                "Тип палива: " + fuelType + "\n" +
                "Максимальна вмістимість: " + maxCapacity + "\n" +
                "Інформація про водія: " + driverInfo + "\n" +
                "Інформація про екіпаж: \n" + crewInfoString;
    }
}
