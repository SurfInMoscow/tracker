package ru.vorobyev.tracker.api;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.vorobyev.tracker.AbstractControllerTest;
import ru.vorobyev.tracker.domain.issue.Bug;
import ru.vorobyev.tracker.service.IssueService;

import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IssueControllerTest extends AbstractControllerTest {

    private static final String REST_URL = IssuesRestController.REST_URL + "/";
    public static final String BUG1 = "bug&id=100009";
    public static final String BUG2 = "bug&id=100010";
    public static final String NEW = "{\n" +
            "  \"priority\": \"MEDIUM\",\n" +
            "  \"creationDate\": \"2020-08-08T01:00:00\",\n" +
            "  \"name\": \"new task\",\n" +
            "  \"status\": \"OPEN_ISSUE\",\n" +
            "  \"executor_id\": 100002,\n" +
            "  \"reporter_id\": 100001,\n" +
            "  \"backlog_id\": 100003,\n" +
            "  \"sprint_id\": null\n" +
            "}";

    @Autowired
    private IssueService<Bug> bugService;

    @Test
    public void getUnauth() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(REST_URL + BUG1))
                .andExpect(status()
                        .isUnauthorized());
    }

    @Test
    public void get() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(REST_URL + BUG1)
                .with(httpBasic("user@ya.ru", "password")))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void save() throws Exception {
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post(REST_URL + "bug")
                .with(httpBasic("user@ya.ru", "password"))
                .content(NEW)
                .contentType(MediaType.APPLICATION_JSON));

        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        String template = "\"priority\":\"MEDIUM\",\"creationDate\":\"2020-08-08T01:00:00\",\"name\":\"new task\",\"status\":\"OPEN_ISSUE\"";
        assertTrue(contentAsString.contains(template));
    }
}