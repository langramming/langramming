package com.github.langramming.database.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Language")
@Table(name = "language_v1")
public class LanguageEntity {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    public Long id;

    @Column(name = "code", unique = true, nullable = false)
    public String code;

    @Column(name = "name", unique = true, nullable = false)
    public String name;
}
