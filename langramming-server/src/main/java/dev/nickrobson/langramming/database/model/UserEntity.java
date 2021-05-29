package dev.nickrobson.langramming.database.model;

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
@Entity(name = "User")
@Table(name = "user_v1")
public class UserEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    public Long id;

    @Column(name = "telegram_id", unique = true, nullable = false)
    public Long telegramId;

    @Column(name = "name", nullable = false)
    public String name;
}
