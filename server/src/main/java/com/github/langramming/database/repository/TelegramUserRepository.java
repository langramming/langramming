package com.github.langramming.database.repository;

import com.github.langramming.database.model.TelegramUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUserEntity, Long> {
}
