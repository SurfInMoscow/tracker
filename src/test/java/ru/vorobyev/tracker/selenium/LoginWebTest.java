package ru.vorobyev.tracker.selenium;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginWebTest extends AbstractWebTest {
    @Test
    public void loginPage() {
        assertEquals("Мои проекты: Создать", projectsPage.getMyProjectsText());
    }
}
