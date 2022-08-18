package com.knubisoft.model;

import com.knubisoft.annotation.Table;
import com.knubisoft.annotation.Xml;
import java.time.LocalDate;
import lombok.Data;

@Data
@Xml(name = "person")
@Table(name = "persons")
public class Person {
    private Long id;
    private String name;
    private LocalDate birthDay;
}
