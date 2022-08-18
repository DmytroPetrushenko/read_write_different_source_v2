package com.knubisoft;

import com.knubisoft.dto.impl.FileReadWriteSource;
import com.knubisoft.dto.impl.SqlReadWriteSource;
import com.knubisoft.model.Person;
import com.knubisoft.orm.MyOrm;
import com.knubisoft.orm.impl.MyOrmImpl;
import com.knubisoft.utils.ConnectionUtil;
import java.io.File;
import java.sql.ResultSet;
import java.util.List;
import java.util.Objects;
import lombok.SneakyThrows;

public class Main {

    @SneakyThrows
    public static void main(String[] args) {
        MyOrm orm = new MyOrmImpl();
        String path = Objects.requireNonNull(Main.class.getClassLoader()
                .getResource("file.xml")).getPath();
        File file = new File(path);
        List<Person> personList = orm.readAll(new FileReadWriteSource(file), Person.class);
        personList.forEach(System.out::println);

        ConnectionUtil util = new ConnectionUtil();
        ResultSet resultSet = null;
        /*
                String query = "SELECT * FROM orm_db.persons";
        try (Connection connection = util.getConnection();
                Statement statement = connection.createStatement()) {
            resultSet = statement.executeQuery(query);
            List<Person> people = orm.readAll(new SqlReadWriteSource(resultSet), Person.class);
            people.forEach(System.out::println);
        }
         */
        File fileOutCsv = new File("src/main/resources/output.csv");
        orm.writeAll(new FileReadWriteSource(fileOutCsv), personList);

        File fileOutJson = new File("src/main/resources/output.json");
        orm.writeAll(new FileReadWriteSource(fileOutJson), personList);

        File fileOutXml = new File("src/main/resources/output.xml");
        orm.writeAll(new FileReadWriteSource(fileOutXml), personList);

        orm.writeAll(new SqlReadWriteSource(resultSet), personList);
    }
}
