package ru.vorobyev.tracker.service.inmemory.user;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
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
        userService = new UserServiceImpl(new UserRepositoryImpl());
        userService.save(UserTestData.USER1);
        userService.save(UserTestData.USER2);
    }

    @Test
    public void save() {
        User tmpUser = userService.save(new User());

        assertNotNull(tmpUser);

        tmpUser.setEmail("newEmail@gmail.com");

        userService.save(tmpUser);

        assertEquals("newEmail@gmail.com", userService.get(tmpUser.getId()).getEmail());
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