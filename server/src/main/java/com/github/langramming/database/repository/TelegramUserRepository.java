package com.github.langramming.database.repository;

import com.github.langramming.database.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TelegramUserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByTelegramId(long telegramId);
}
