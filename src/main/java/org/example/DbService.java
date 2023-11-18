package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DbService {
    // JDBC URL, username, and password of MySQL server
    private String JDBC_URL;
    private String USERNAME;
    private String PASSWORD;

    // JDBC variables for opening, closing, and managing connection
    private Connection connection;

    public DbService() {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            // Завантаження конфігураційних параметрів з файлу
            properties.load(input);

            // Отримання значення за ключем
            JDBC_URL = properties.getProperty("db.url");
            USERNAME = properties.getProperty("db.username");
            PASSWORD = properties.getProperty("db.password");

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            // Initialize the JDBC driver
            //Class.forName("com.mysql.cj.jdbc.Driver");
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load MySQL JDBC driver.");
        }
    }

    // Method to establish a database connection
    private void connect() {
        try {
            if (connection == null) connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the database.");
        }
    }

    // Method to close the database connection
    private void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to close the database connection.");
        }
    }
    //DONE
    public List<Transport> readTransportFromDb(){
        List<Transport> transports = new ArrayList<>();
        connect();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Transports")) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String fuelType = resultSet.getString("fuel_type");
                int maxCapacity = resultSet.getInt("max_capacity");
                String driverInfo = resultSet.getString("driver_info");

                try(ResultSet crewResultSet = connection.createStatement().executeQuery("SELECT * FROM Crew WHERE transport_id = " + id)) {
                    List<String> crewInfo = new ArrayList<>();
                    while (crewResultSet.next()) {
                        crewInfo.add(crewResultSet.getString("name"));
                    }
                    transports.add(new Transport(id, fuelType, maxCapacity, driverInfo, crewInfo));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve transports.");
        } finally {
            closeConnection();
        }

        return transports;
    }
    //DONE
    public List<Passenger> readPassengersFromDb(){
        List<Passenger> passengers = new ArrayList<>();
        connect();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Passengers")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                try(ResultSet tripResultSet = connection.createStatement().executeQuery("SELECT * FROM passenger_trip_history WHERE passenger_id = " + id)) {
                    List<PassengerTripHistory> tripHistory = new ArrayList<>();
                    while (tripResultSet.next()) {
                        int trip_id = tripResultSet.getInt("id");
                        String trip_name = tripResultSet.getString("name");
                        String trip_date = tripResultSet.getString("date");
                        tripHistory.add(new PassengerTripHistory(trip_id, trip_name, trip_date));
                    }
                    passengers.add(new Passenger(id, name, tripHistory));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve passengers.");
        } finally {
            closeConnection();
        }

        return passengers;
    }
    //DONE
    public void removePassengerById(int id){
        connect();

        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Passengers WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to remove a passenger.");
        } finally {
            closeConnection();
        }
    }
    //DONE
    public void insertTransportToDb(Transport transport) {
        connect();

        try {
            // Вставка данных в таблицу Transports
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Transports (fuel_type, max_capacity, driver_info) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, transport.fuelType);
                preparedStatement.setInt(2, transport.maxCapacity);
                preparedStatement.setString(3, transport.driverInfo);

                preparedStatement.executeUpdate();

                // Получение сгенерированного ключа (id)
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long transportId = generatedKeys.getLong(1);

                        // Вставка данных в таблицу Crew
                        try (PreparedStatement crewInsertStatement = connection.prepareStatement("INSERT INTO Crew (name, transport_id) VALUES (?, ?)")) {
                            for (String crewMember : transport.crewInfo) {
                                crewInsertStatement.setString(1, crewMember);
                                crewInsertStatement.setLong(2, transportId); // Используйте сгенерированный ключ transportId
                                crewInsertStatement.executeUpdate();
                            }
                        }
                    } else {
                        throw new SQLException("Failed to get generated key for Transport.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to insert transports.");
        } finally {
            closeConnection();
        }
    }

    public void removeTransportById(int id){
        connect();

        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Transports WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to remove a transport.");
        } finally {
            closeConnection();
        }
    }
    //DONE
    public void insertPassengerToDb(Passenger passenger) {
        connect();

        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Passengers (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, passenger.name);
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long generatedId = generatedKeys.getLong(1);
                    try(PreparedStatement tripInsertStatement = connection.prepareStatement("INSERT INTO passenger_trip_history (name, date, passenger_id) VALUES (?, ?, ?)")) {
                        for (PassengerTripHistory trip : passenger.tripHistory) {
                            tripInsertStatement.setString(1, trip.name);
                            tripInsertStatement.setString(2, trip.date);
                            tripInsertStatement.setLong(3, generatedId);
                            tripInsertStatement.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to insert a new passenger.");
        }
        finally {
            closeConnection();
        }
    }
    //DONE
    public void changePassenger(int id, Passenger passenger){
        connect();

        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `passengers` SET `name`= ? WHERE id = ?")) {
            preparedStatement.setString(1, passenger.name);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to change a passenger.");
        } finally {
            closeConnection();
        }
    }
}
