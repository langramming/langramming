package com.github.langramming.database.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name = "Language")
@Table(name = "language_v1", uniqueConstraints = {
        @UniqueConstraint(name = "id", columnNames = "id"),
        @UniqueConstraint(name = "code", columnNames = "code"),
        @UniqueConstraint(name = "name", columnNames = "name"),
})
public class LanguageEntity {

    @Id
    @GeneratedValue
    public Long id;

    public String code;

    public String name;

}
