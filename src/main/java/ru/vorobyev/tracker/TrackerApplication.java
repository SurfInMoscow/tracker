package ru.vorobyev.tracker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;
import ru.vorobyev.tracker.config.TrackerConfig;
import ru.vorobyev.tracker.domain.project.Project;
import ru.vorobyev.tracker.domain.user.Role;
import ru.vorobyev.tracker.domain.user.User;
import ru.vorobyev.tracker.exception.NotExistException;
import ru.vorobyev.tracker.service.ProjectService;
import ru.vorobyev.tracker.service.UserService;
import ru.vorobyev.tracker.web.CheckHealthResponseErrorHandler;
import ru.vorobyev.tracker.web.RestClient;

import java.net.URI;
import java.util.Set;

@Slf4j
@SpringBootApplication
@EnableScheduling
@Import(TrackerConfig.class)
public class TrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrackerApplication.class, args);
    }

    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private RestClient restClient;
    private RestTemplate restTemplate;
    private User systemUser;

    @Bean
    CommandLineRunner systemUserInit() {
        return args -> {
            try {
                systemUser = userService.getByEmail("system@system.net");
            } catch (NotExistException e) {
                log.info(e.getMessage());
            }

            if (systemUser == null) {
                Project project = projectService.save(new Project("system", "system", "system", "system@system.net", "system@system.net"));
                systemUser = new User("system", "system@system.net", "system", Set.of(project), Role.ROLE_SYSTEM);
                userService.save(systemUser);
            }
        };
    }

    @Scheduled(fixedDelay = 3000, initialDelay = 3000)
    private void checkHealth() {
        if (restTemplate == null) {
            restTemplate = new RestTemplateBuilder().basicAuthentication(systemUser.getEmail(), "system").build();
            restTemplate.setErrorHandler(new CheckHealthResponseErrorHandler(restTemplate, restClient));
        }

        restTemplate.exchange(URI.create(restClient.getUrl().concat(restClient.getBasePath()).concat("/health")), HttpMethod.GET, null, String.class);
    }
}
