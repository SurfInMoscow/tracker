package ru.vorobyev.tracker.service.jdbc.user;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vorobyev.tracker.domain.user.Role;
import ru.vorobyev.tracker.domain.user.User;
import ru.vorobyev.tracker.exception.NotExistException;
import ru.vorobyev.tracker.repository.jdbc.user.UserJdbcRepositoryImpl;
import ru.vorobyev.tracker.service.UserService;
import ru.vorobyev.tracker.AbstractJdbcServiceTest;

import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;
import static ru.vorobyev.tracker.domain.user.Role.ROLE_ADMIN;
import static ru.vorobyev.tracker.domain.user.Role.ROLE_USER;
import static ru.vorobyev.tracker.service.jdbc.user.UserJdbcTestData.*;


public class UserServiceTest extends AbstractJdbcServiceTest {

    @Autowired
    private UserService userService;

    @BeforeClass
    public static void setUp() {
        UserJdbcRepositoryImpl userRepository = new UserJdbcRepositoryImpl();
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

    @Test
    public void getByEmail() {
        USER2.setEmail("privet@sberbank.ru");
        User user = userService.save(USER2);

        assertNotNull(user);

        assertEquals("privet@sberbank.ru", userService.getByEmail(user.getEmail()).getEmail());
    }

    @Test
    public void getAll() {
        User user1 = userService.save(new User("Pet", "pet@yahoo.com", "password", new HashSet<>(), ROLE_USER));
        User user2 = userService.save(new User("Kat", "kat@sberbank.ru", "katkapass", new HashSet<>(), ROLE_USER, ROLE_ADMIN));
        User user3 = userService.save(new User("Vas", "vas@ya.ru", "etovaska", new HashSet<>(), ROLE_USER));

        List<User> users = userService.getAll();

        assertNotEquals(0, users.size());

        users.forEach(user -> {
            assertNotNull(user.getId());
            assertTrue(user.getRoles().size() >= 1);
        });
    }
}
