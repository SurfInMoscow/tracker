package ru.vorobyev.tracker.service.jdbc.user;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.vorobyev.tracker.domain.user.Role;
import ru.vorobyev.tracker.domain.user.User;
import ru.vorobyev.tracker.exception.NotExistException;
import ru.vorobyev.tracker.repository.jdbc.user.UserJdbcRepositoryImpl;
import ru.vorobyev.tracker.service.UserService;
import ru.vorobyev.tracker.service.user.UserServiceImpl;

import static org.junit.Assert.*;
import static ru.vorobyev.tracker.service.user.UserTestData.*;

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
        User user = userService.save(USER2);

        assertNotNull(user);

        user.setName("New Name");
        user.getRoles().remove(Role.ROLE_ADMIN);

        user = userService.save(user);

        assertEquals("New Name", userService.get(user.getId()).getName());
    }

    @Test
    public void get() {
        User user = userService.save(USER1);

        user = userService.get(user.getId());

        assertNotNull(user);

        assertTrue(user.getRoles().contains(Role.ROLE_USER));
    }

    @Test
    public void delete() {
        User user = userService.save(USER3);

        boolean delete = userService.delete(user.getId());

        assertTrue(delete);

        assertThrows(NotExistException.class, () -> userService.get(user.getId()));
    }
}
