package uz.khakimjanovich.homework;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HomeworkApiResponseTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void wrapsSuccessfulResponsesInApiEnvelope() throws Exception {
        mvc.perform(get("/api/users")
                        .header("Accept-Language", "en"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(0)))
                .andExpect(jsonPath("$.message", is("Success")))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void translatesValidationErrorsByAcceptLanguage() throws Exception {
        mvc.perform(post("/api/users")
                        .header("Accept-Language", "uz")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "",
                                  "displayName": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(1001)))
                .andExpect(jsonPath("$.message", is("Validatsiya xatosi")))
                .andExpect(jsonPath("$.data.detail").exists());
    }
}
