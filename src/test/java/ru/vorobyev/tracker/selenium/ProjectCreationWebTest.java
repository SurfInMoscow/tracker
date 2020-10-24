package ru.vorobyev.tracker.selenium;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectCreationWebTest extends AbstractWebTest {
    @Test
    public void createProjectWindow() {
        ProjectsPage.CreateProjectWindow projectWindow = projectsPage.getCreateProjectWindow();
        String myModalTitleText = projectWindow.getMyModalTitleText();
        assertEquals("Создание проекта", myModalTitleText);
    }
}
