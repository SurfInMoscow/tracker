package ru.vorobyev.tracker.service.jdbc.user;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.vorobyev.tracker.domain.user.Role;
import ru.vorobyev.tracker.domain.user.User;
import ru.vorobyev.tracker.repository.jdbc.user.UserJdbcRepositoryImpl;
import ru.vorobyev.tracker.service.UserService;
import ru.vorobyev.tracker.service.user.UserServiceImpl;

import static org.junit.Assert.assertNotNull;
import static ru.vorobyev.tracker.service.user.UserTestData.USER2;

public class UserJdbcServiceTest {

    private static UserService userService;

    @BeforeClass
    public static void setUp() {
        UserJdbcRepositoryImpl userRepository = new UserJdbcRepositoryImpl();
        userService = new UserServiceImpl(userRepository);
        userRepository.clear();
    }

    @Test
    public void save() {
        User tmpUser = userService.save(USER2);


        tmpUser.setName("New Name");
        tmpUser.getRoles().remove(Role.ROLE_ADMIN);

        tmpUser = userService.save(tmpUser);

        assertNotNull(tmpUser);
    }

}
