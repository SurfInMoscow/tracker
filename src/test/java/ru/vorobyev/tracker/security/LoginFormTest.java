package ru.vorobyev.tracker.security;

import org.junit.Test;
import ru.vorobyev.tracker.AbstractControllerTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

public class LoginFormTest extends AbstractControllerTest {

    @Test
    public void correctLogin() throws Exception {
        mvc
                .perform(formLogin("/tracker")
                        .user("user@ya.ru").password("password"))
                .andDo(print())
                .andExpect(redirectedUrl("/projects"));
    }

    @Test
    public void incorrectLogin() throws Exception {
        mvc
                .perform(formLogin("/tracker")
                        .user("user@ya.ru").password("badpass"))
                .andDo(print()).andExpect(redirectedUrl("/tracker?error"));
    }
}
