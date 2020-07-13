package ru.vorobyev.tracker.service.user;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.vorobyev.tracker.domain.user.User;
import ru.vorobyev.tracker.repository.inmemory.user.UserRepositoryImpl;
import ru.vorobyev.tracker.service.UserService;

import java.util.List;

import static org.junit.Assert.*;
import static ru.vorobyev.tracker.service.user.UserTestData.*;

public class UserServiceImplTest {
    private static UserService userService;

    /*Sequence для ID стартует со 100*/

    @BeforeClass
    public static void setUp() {
        userService = new UserServiceImpl(new UserRepositoryImpl());
        userService.save(USER1);
        userService.save(USER2);
    }

    @Test
    public void save() {
        User tmpUser = userService.save(USER3);

        assertNotNull(tmpUser);

        tmpUser.setEmail("newEmail@gmail.com");

        userService.save(tmpUser);

        assertEquals("newEmail@gmail.com", userService.get(tmpUser.getId()).getEmail());
    }

    @Test
    public void delete() {
        assertTrue(userService.delete(101));
    }

    @Test
    public void get() {
        User tmpUser = userService.save(USER1);

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