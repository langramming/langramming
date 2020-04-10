package com.github.langramming.database.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "Language")
@Table(name = "language_v1")
public class LanguageEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String code;

    private String name;

}
