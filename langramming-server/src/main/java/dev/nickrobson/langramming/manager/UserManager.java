package dev.nickrobson.langramming.manager;

import dev.nickrobson.langramming.database.model.UserEntity;
import dev.nickrobson.langramming.database.repository.TelegramUserRepository;
import dev.nickrobson.langramming.httpserver.UserContextFilter;
import dev.nickrobson.langramming.model.User;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class UserManager {

    private final TelegramUserRepository userRepository;

    @Inject
    public UserManager(TelegramUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUser(long id) {
        return userRepository.findById(id).map(this::toUser);
    }

    public Optional<User> getUserByTelegramId(long telegramId) {
        return userRepository.findByTelegramId(telegramId).map(this::toUser);
    }

    public User createUser(long telegramId, String name) {
        UserEntity newUserEntity = UserEntity.builder().telegramId(telegramId).name(name).build();

        return toUser(userRepository.save(newUserEntity));
    }

    public void updateUser(User user) {
        Optional<UserEntity> userEntityOpt = userRepository.findById(user.getId());
        if (userEntityOpt.isPresent()) {
            UserEntity userEntity = userEntityOpt.get();
            userEntity.name = user.getName();
            userRepository.save(userEntity);
        }
    }

    private User toUser(UserEntity userEntity) {
        return User
            .builder()
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
