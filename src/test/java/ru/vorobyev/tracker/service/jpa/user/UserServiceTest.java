package ru.vorobyev.tracker.service.jpa.user;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vorobyev.tracker.AbstractJpaServiceTest;
import ru.vorobyev.tracker.domain.project.Project;
import ru.vorobyev.tracker.domain.user.Role;
import ru.vorobyev.tracker.domain.user.User;
import ru.vorobyev.tracker.service.UserService;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static ru.vorobyev.tracker.service.jpa.user.UserJpaTestData.USER1;
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
        User user = userService.save(USER1);

        boolean delete = userService.delete(user.getId());

        assertTrue(delete);

        assertNull(userService.get(user.getId()));
    }

    @Test
    public void get() {
        User user = userService.get(100_000);

        assertNotNull(user);

        assertEquals(Role.ROLE_USER, user.getRoles().iterator().next());

        Set<Project> projects = user.getProjects();

        assertEquals(2, projects.size());

    }

    @Test
    public void getByEmail() {
        User user = userService.getByEmail("user@ya.ru");

        assertNotNull(user);

        assertEquals(Role.ROLE_USER, user.getRoles().iterator().next());

        Set<Project> projects = user.getProjects();

        assertEquals(2, projects.size());

    }

    @Test
    public void getAll() {
        List<User> users = userService.getAll();

        assertNotSame(0, users.size());

        users.forEach(user -> assertNotNull(user.getId()));
    }
}