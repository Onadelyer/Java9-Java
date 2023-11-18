package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        while (true) {
            System.out.println("Виберіть опцію: " +
                    "\n1. Подивитись транспорт" +
                    "\n2. Подивитись пасажирів" +
                    "\n3. Додати пассажира" +
                    "\n4. Видалити пассажира" +
                    "\n5. Редагувати дані пасажира" +
                    "\n6. Додати транспорт" +
                    "\n7. Видалити транспорт" +
                    "\n8. Редагувати дані транспорту");

            Scanner scanner = new Scanner(System.in);
            int option = scanner.nextInt();
            DbService db = new DbService();

            switch (option) {
                case 1:
                    List<Transport> transportList = db.readTransportFromDb();
                    for (Transport transport : transportList) {
                        System.out.println(transport.toString());
                    }
                    break;
                case 2:
                    List<Passenger> passengerList = db.readPassengersFromDb();
                    for (Passenger passenger : passengerList) {
                        System.out.println("Пасажир №" + passenger.id);
                        System.out.println(passenger.toString());
                    }
                    break;
                case 3:
                    Passenger newPassenger = InputPassenger();
                    db.insertPassengerToDb(newPassenger);
                    System.out.println("Пасажир доданий");
                    break;
                case 4:
                    int removePassengerIndex = InputIndex();
                    db.removePassengerById(removePassengerIndex);
                    System.out.println("Пасажир видалений");
                    break;
                case 5:
                    int changePassengerid = InputIndex() - 1;
                    Passenger changedPassenger = InputPassenger();
                    db.changePassenger(changePassengerid, changedPassenger);
                    System.out.println("Пасажир змінений");
                    break;
                case 6:
                    Transport newTransport = InputTransport();
                    db.insertTransportToDb(newTransport);
                    System.out.println("Транспорт доданий");
                    break;
                case 7:
                    int removedTransportIndex = InputIndex();
                    db.removeTransportById(removedTransportIndex);
                    System.out.println("Транспорт видалений");
                    break;
                case 8:
                    List<Transport> transports2 = FileHandler.readTransportFromFile();
                    int index2 = InputIndex() - 1;
                    ChangeTransport(transports2, index2);
                    FileHandler.writeTransportsToFile(transports2);
                    System.out.println("Транспорт змінений");
                    break;
                default:
                    System.out.println("Невірна опція");
            }
        }
    }

    public static void ChangePassenger(List<Passenger> passengers, int index){
        System.out.println("Пасажир до: \n"+
                passengers.get(index).toString()+
                "\nПасажир після: \n");
        passengers.add(index, InputPassenger());
    }

    public static void ChangeTransport(List<Transport> passengers, int index){
        System.out.println("Транспорт до: \n"+
                passengers.get(index).toString()+
                "\nТранспорт після: \n");
        passengers.add(index, InputTransport());
    }

    public static int InputIndex(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введіть індекс: ");
        int index = scanner.nextInt();
        return index;
    }

    public static Transport InputTransport() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введіть тип палива: ");
        String fuelType = scanner.nextLine();

        System.out.print("Введіть максимальну вмістимість: ");
        int maxCapacity = scanner.nextInt();

        scanner.nextLine();

        System.out.print("Введіть інформацію про водія: ");
        String driverInfo = scanner.nextLine();

        System.out.print("Введіть кількість членів екіпажу: ");
        int numCrew = scanner.nextInt();

        List<String> crewInfo = new ArrayList<>();
        for (int i = 0; i < numCrew; i++) {
            System.out.print("Введіть інформацію про члена екіпажу " + (i + 1) + ": ");
            String crewMember = scanner.nextLine();
            if(i == 0)
                crewMember = scanner.nextLine();
            crewInfo.add(crewMember);
        }

        return new Transport(fuelType, maxCapacity, driverInfo, crewInfo);
    }

    public static Passenger InputPassenger(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введіть ім'я пасажира: ");
        String name = scanner.nextLine();

        System.out.println("Введіть кількість поїздок: ");
        int numTrips = scanner.nextInt();

        scanner.nextLine();

        List<PassengerTripHistory> tripHistory = new ArrayList<>();
        for (int i = 0; i < numTrips; i++) {
            System.out.println("Введіть інформацію про поїздку " + (i + 1) + ": ");
            tripHistory.add(InputPassengerTripHistory());
        }

        return new Passenger(name, tripHistory);
    }

    public static PassengerTripHistory InputPassengerTripHistory(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("\tВведіть назву маршруту: ");
        String routeName = scanner.nextLine();

        System.out.println("\tВведіть дату поїздки: ");
        String date = scanner.nextLine();

        return new PassengerTripHistory(routeName, date);
    }
}