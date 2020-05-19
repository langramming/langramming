package com.github.langramming.database.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Language")
@Table(name = "language_v1")
public class LanguageEntity {

    @Id
    @GeneratedValue
    public Long id;

    @Column(unique = true)
    public String code;

    @Column(unique = true)
    public String name;

}
