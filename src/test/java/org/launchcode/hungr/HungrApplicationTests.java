package org.launchcode.hungr;

import org.junit.jupiter.api.Test;
import org.launchcode.hungr.data.UserRepository;
import org.launchcode.hungr.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Optional;


@AutoConfigureMockMvc
@SpringBootTest
class HungrApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserRepository userRepository;

	@Test
	void contextLoads() {
	}

	@Test
	public void shouldReturnLoginPage() throws Exception {
		this.mockMvc.perform(get("/login"))
				.andExpect(status().isOk())
				.andExpect(view().name("login"));
	}

	@Test
	public void unauthenticatedRequestRedirectsToLogin() throws Exception {
		this.mockMvc.perform(get("/recipes"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login"));
	}

	@Test
	public void givenUser_whenLoginAttempted_LoginSucceeds()
			throws Exception {

		User alex = new User("alex", "password123");

		given(userRepository.findByUsername("alex")).willReturn(alex);

		MvcResult result = mockMvc.perform(post("/login")
						.param("username","alex")
						.param("password", "password123"))
				.andDo(print()).andExpect(status().is3xxRedirection())
				.andReturn();
		Assert.notNull(result.getRequest().getSession(false), "Session is created");
	}

	@Test
	public void givenValidUser_whenLoginIncorrectlyAttempted_thenPasswordInvalidFieldError()
			throws Exception {

		User alex = new User("alex", "password123");

		given(userRepository.findByUsername("alex")).willReturn(alex);

		mockMvc.perform(post("/login")
						.param("username","alex")
						.param("password", "password1234"))
				.andExpect(model().hasErrors())
				.andExpect(model().attributeHasFieldErrorCode("loginFormDTO", "password", "password.invalid"));
	}

	@Test
	public void whenLoginWithUnknownUsername_usernameInvalidFieldError() throws Exception {
		given(userRepository.findByUsername("alex")).willReturn(null);
		mockMvc.perform(post("/login").param("username","alex").param("password", "password1234"))
				.andExpect(model().hasErrors()).andExpect(model().attributeHasFieldErrorCode("loginFormDTO", "username", "user.invalid"));
	}


}
