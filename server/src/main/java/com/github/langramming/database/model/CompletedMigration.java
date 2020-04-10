package com.github.langramming.database.model;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "CompletedMigration")
@Table(name = CompletedMigration.TABLE_NAME)
@Getter
public class CompletedMigration {

    public static final String TABLE_NAME = "completed_migrations";

    public CompletedMigration(String migrationClassName) {
        this.migrationClassName = migrationClassName;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String migrationClassName;

}
