package com.github.langramming.service;

import com.github.langramming.database.LangrammingDatabase;
import com.github.langramming.database.model.TelegramUserEntity;
import com.github.langramming.httpserver.UserContextFilter;
import com.github.langramming.model.User;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Singleton
public class UserService {

    @Inject
    private LangrammingDatabase database;

    public Optional<User> getUser(long id) {
        List<TelegramUserEntity> userEntity = database.runInTransaction(((session, transaction) -> {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<TelegramUserEntity> q = cb.createQuery(TelegramUserEntity.class);
            Root<TelegramUserEntity> root = q.from(TelegramUserEntity.class);

            return session.createQuery(
                    q.where(cb.equal(root.get("id"), cb.literal(id)))
            ).getResultList();
        }));

        return userEntity.stream().findFirst().map(this::toUser);
    }

    public Optional<User> getUserByTelegramId(long telegramId) {
        List<TelegramUserEntity> userEntity = database.runInTransaction(((session, transaction) -> {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<TelegramUserEntity> q = cb.createQuery(TelegramUserEntity.class);
            Root<TelegramUserEntity> root = q.from(TelegramUserEntity.class);

            return session.createQuery(
                    q.where(cb.equal(root.get("telegramId"), cb.literal(telegramId)))
            ).getResultList();
        }));

        return userEntity.stream().findFirst().map(this::toUser);
    }

    public User createUser(long telegramId, String name) {
        TelegramUserEntity userEntity = database.runInTransaction((session, transaction) -> {
            TelegramUserEntity newUserEntity = new TelegramUserEntity();
            newUserEntity.telegramId = telegramId;
            newUserEntity.name = name;

            session.save(newUserEntity);

            return newUserEntity;
        });

        return toUser(userEntity);
    }

    public void updateUser(User user) {
        database.runInTransaction((session, transaction) -> {
            TelegramUserEntity userEntity = session.get(TelegramUserEntity.class, user.getId());

            userEntity.name = user.getName();

            session.save(userEntity);
            return null;
        });
    }

    private User toUser(TelegramUserEntity userEntity) {
        return User.builder()
                .id(userEntity.id)
                .telegramId(userEntity.telegramId)
                .name(userEntity.name)
                .build();
    }

    @Singleton
    public static class UserProvider implements Provider<Optional<User>> {

        @Override
        public Optional<User> get() {
            return UserContextFilter.getLoggedInUser();
        }
    }

}
