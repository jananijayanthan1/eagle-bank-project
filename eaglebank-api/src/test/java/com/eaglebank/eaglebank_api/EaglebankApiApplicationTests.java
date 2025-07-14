package com.eaglebank.eaglebank_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EaglebankApiApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void rootReturns404() throws Exception {
		mockMvc.perform(get("/"))
			.andExpect(status().isNotFound());
	}

	@Test
	void createUserReturns400ForEmptyBody() throws Exception {
		mockMvc.perform(post("/v1/users")
			.contentType("application/json"))
			.andExpect(status().isBadRequest());
	}

	// @Test
	// void authReturns400ForEmptyBody() throws Exception {
	// 	mockMvc.perform(post("/v1/auth")
	// 		.contentType("application/json"))
	// 		.andExpect(status().isBadRequest());
	// }
}
