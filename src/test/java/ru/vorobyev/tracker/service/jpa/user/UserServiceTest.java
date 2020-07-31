package ru.vorobyev.tracker.service.jpa.user;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vorobyev.tracker.AbstractJpaServiceTest;
import ru.vorobyev.tracker.domain.user.Role;
import ru.vorobyev.tracker.domain.user.User;
import ru.vorobyev.tracker.service.UserService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static ru.vorobyev.tracker.service.jpa.user.UserJpaTestData.USER2;


public class UserServiceTest extends AbstractJpaServiceTest {

    @Autowired
    private UserService userService;

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
    public void delete() {
    }

    @Test
    public void get() {
    }

    @Test
    public void getByEmail() {
    }

    @Test
    public void getAll() {
    }
}