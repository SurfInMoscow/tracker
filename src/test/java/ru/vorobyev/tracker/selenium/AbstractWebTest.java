package ru.vorobyev.tracker.selenium;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AbstractWebTest {
    protected static ChromeOptions chromeOptions = new ChromeOptions();
    protected static WebDriver chromeDriver;
    protected WebDriverWait waitChrome = new WebDriverWait(chromeDriver, 10L);
    protected static LoginPage loginPage;
    protected static ProjectsPage projectsPage;

    static {
        chromeOptions.addArguments("--headless");
        System.setProperty("webdriver.chrome.driver", "/Users/vorobyev/Tools/selenium/chromedriver/86.0.4240.22/chromedriver");
    }

    @BeforeClass
    public static void beforeWeb() {
        chromeDriver = new ChromeDriver(chromeOptions);
        login();
    }

    @AfterClass
    public static void afterWeb() {
        chromeDriver.quit();
    }

    private static void login() {
        chromeDriver.get("http://localhost:8080/tracker");
        loginPage = new LoginPage(chromeDriver);
        projectsPage = loginPage.loginValidUser("user@ya.ru", "password");
    }

    static class LoginPage {
        private WebDriver driver;
        private By usernameBy = By.name("username");
        private By passwordBy = By.name("password");
        private By signInBy = By.className("btn");

        public LoginPage(WebDriver webDriver) {
            this.driver = webDriver;
        }

        public ProjectsPage loginValidUser(String username, String password) {
            driver.findElement(usernameBy).sendKeys(username);
            driver.findElement(passwordBy).sendKeys(password);
            driver.findElement(signInBy).click();

            return new ProjectsPage(driver);
        }
    }

    static class ProjectsPage {
        private WebDriver driver;
        private By textBy = By.className("text-left");
        private By createProjectBy = By.className("btn-success");

        public ProjectsPage(WebDriver driver) {
            this.driver = driver;
        }

        public String getMyProjectsText() {
            return driver.findElement(textBy).getText();
        }

        public CreateProjectWindow getCreateProjectWindow() {
            driver.findElement(createProjectBy).click();

            return new CreateProjectWindow(driver);
        }

        static class CreateProjectWindow {
            private WebDriver driver;
            private By modalTitleBy = By.className("modal-title");

            public CreateProjectWindow(WebDriver driver) {
                this.driver = driver;
            }

            public String getMyModalTitleText() {
                return driver.findElement(modalTitleBy).getText();
            }
        }
    }
}
