package ru.vorobyev.tracker.service.inmemory.user;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.vorobyev.tracker.domain.user.User;
import ru.vorobyev.tracker.repository.inmemory.user.UserRepositoryImpl;
import ru.vorobyev.tracker.service.UserService;
import ru.vorobyev.tracker.service.user.UserServiceImpl;

import java.util.List;

import static org.junit.Assert.*;

public class UserServiceImplTest {
    private static UserService userService;

    /*Sequence для ID стартует со 100*/

    @BeforeClass
    public static void setUp() {
        userService = new UserServiceImpl(new UserRepositoryImpl(), new BCryptPasswordEncoder());
        userService.save(UserTestData.USER1);
        userService.save(UserTestData.USER2);
    }

    @Test
    public void save() {
        User user = new User();
        user.setPassword("super12345pass");
        user.setEmail("newEmail@gmail.com");

        user = userService.save(user);

        assertNotNull(user);

        assertEquals("newemail@gmail.com", userService.get(user.getId()).getEmail());
    }

    @Test
    public void delete() {
        User tmpUser = userService.save(UserTestData.USER3);
        assertTrue(userService.delete(tmpUser.getId()));
    }

    @Test
    public void get() {
        User tmpUser = userService.save(UserTestData.USER1);

        assertNotNull(tmpUser.getId());
    }

    @Test
    public void getByEmail() {
        assertEquals("petka@yahoo.com", userService.getByEmail("petka@yahoo.com").getEmail());
    }

    @Test
    public void getAll() {
        List<User> users = userService.getAll();

        assertNotSame(0, users.size());

        users.forEach(Assert::assertNotNull);
    }

}