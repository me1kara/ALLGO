package test.testspring.controller;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(HomeControllerTest.class)
@ComponentScan(basePackages={"test.testspring"})
public class HomeControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Test
    @DisplayName("home test:")
    void getHome() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andDo(print());
    }


}
