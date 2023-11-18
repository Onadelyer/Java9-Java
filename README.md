
# Проект Laba9

Цей проект є прикладом інтеграції різних інструментів для розробки Java-додатків. У цьому README описано кроки для налаштування та використання автоматизованого збірника, логування, процесу встановлення Project Lombok, інтеграції бібліотеки Jackson для роботи з JSON, а також документації з внесених змін.

## Вимоги:
    1. Java (версія 8 або новіша)
    2. Apache Ant, Apache Maven, або Gradle (виберіть одну систему збірки)
    3. Project Lombok
    4. Apache Log4j
    5. Jackson

#### Приклад конфігурації Maven в проекті
```xml
<dependencies>
        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.30</version>
            <scope>provided</scope>
        </dependency>

        <!-- Log4j -->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-api</artifactId>
            <version>2.14.1</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>2.14.1</version>
        </dependency>

        <!-- Jackson -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.12.3</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.33</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

#### Інтеграція Project Lombok

Project Lombok допомагає спростити створення класів. Приклад використання в проекті: 

```java
@Getter
@Setter
@ToString
class PassengerTripHistory implements Serializable {
    private int id;
    private String name;
    private String date;
    private List<String> attractions;
}
```

#### Логування за допомогою **Log4j**

```java
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

        logger.log(java.util.logging.Level.INFO, "Successfully connected to the database.");
    }
```

#### Інтеграція Jackson для JSON

Бібліотека для сереалізації об'єктів використовуючи **JSON**

```java
while (tripResultSet.next()) {
    int trip_id = tripResultSet.getInt("id");
    String trip_name = tripResultSet.getString("name");
    String trip_date = tripResultSet.getString("date");
    String jsonAttractions = tripResultSet.getString("attractions");

    List<String> attractions = new ArrayList<>();

    if(!Objects.equals(jsonAttractions, "")){
        ObjectMapper objectMapper = new ObjectMapper();
        attractions = objectMapper.readValue(jsonAttractions, List.class);
                        }

    PassengerTripHistory newTrip = new PassengerTripHistory(trip_id, trip_name, trip_date);

    newTrip.setAttractions(attractions);

    tripHistory.add(newTrip);
}
```
