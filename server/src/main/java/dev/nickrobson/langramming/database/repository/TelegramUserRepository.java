package dev.nickrobson.langramming.database.repository;

import dev.nickrobson.langramming.database.model.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramUserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByTelegramId(long telegramId);
}
