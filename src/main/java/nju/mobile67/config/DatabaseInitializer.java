package nju.mobile67.config;

import nju.mobile67.model.entity.Type;
import nju.mobile67.model.entity.User;
import nju.mobile67.repository.TypeRepository;
import nju.mobile67.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final TypeRepository typeRepository;

    private final UserRepository userRepository;

    public DatabaseInitializer(TypeRepository typeRepository, UserRepository userRepository) {
        this.typeRepository = typeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        initType("default");
        initType("outgoing");
        initType("constellation");
        initType("weather");
        initUser("admin", "admin");
    }

    private void initType(String typeName) {
        Optional<Type> type = typeRepository.findByName(typeName);
        if (type.isEmpty()) {
            Type defaultType = new Type();
            defaultType.setName(typeName);
            typeRepository.save(defaultType);
        }
    }

    private void initUser(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            User admin = new User();
            admin.setUsername(username);
            admin.setPassword(password);
            userRepository.save(admin);
        }
    }
}