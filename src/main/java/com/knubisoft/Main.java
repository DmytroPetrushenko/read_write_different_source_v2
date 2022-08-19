package com.knubisoft;

import com.knubisoft.dto.impl.FileReadWriteSource;
import com.knubisoft.dto.impl.SqlReadWriteSource;
import com.knubisoft.model.Person;
import com.knubisoft.orm.MyOrm;
import com.knubisoft.orm.impl.MyOrmImpl;
import com.knubisoft.utils.ConnectionUtil;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.SneakyThrows;

public class Main {
    private static final MyOrm orm = new MyOrmImpl();
    private static final List<String> filesNames = new ArrayList<>();
    private static ResultSet resultSet = null;

    @SneakyThrows
    public static void main(String[] args) {
        filesNames.add("file.csv");
        filesNames.add("file.json");
        filesNames.add("file.xml");
        List<List<Person>> collect = filesNames.stream()
                .map(Main::readFromFiles)
                .collect(Collectors.toList());
        workWithDatabase();
        collect.forEach(Main::writeToFile);

    }

    private static List<Person> readFromFiles(String name) {
        String path = Objects.requireNonNull(Main.class.getClassLoader()
                .getResource(name)).getPath();
        File file = new File(path);
        List<Person> personList = orm.readAll(new FileReadWriteSource(file), Person.class);
        personList.forEach(System.out::println);
        return personList;
    }

    private static void writeToFile(List<Person> personList) {
        File fileOutCsv = new File("src/main/resources/output.csv");
        orm.writeAll(new FileReadWriteSource(fileOutCsv), personList);

        File fileOutJson = new File("src/main/resources/output.json");
        orm.writeAll(new FileReadWriteSource(fileOutJson), personList);

        File fileOutXml = new File("src/main/resources/output.xml");
        orm.writeAll(new FileReadWriteSource(fileOutXml), personList);

        orm.writeAll(new SqlReadWriteSource(resultSet), personList);
    }

    @SneakyThrows
    private static void workWithDatabase() {
        ConnectionUtil util = new ConnectionUtil();
        String query = "SELECT * FROM orm_db.persons";
        try (Connection connection = util.getConnection();
                 Statement statement = connection.createStatement()) {
            resultSet = statement.executeQuery(query);
            List<Person> people = orm.readAll(new SqlReadWriteSource(resultSet), Person.class);
            people.forEach(System.out::println);
        }
    }
}
