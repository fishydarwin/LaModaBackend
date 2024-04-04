package com.github.fishydarwin.LaModaBackend;

import com.github.fishydarwin.LaModaBackend.controller.UserController;
import com.github.fishydarwin.LaModaBackend.domain.User;
import com.github.fishydarwin.LaModaBackend.domain.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserControllerTests {

	@Autowired
	private UserController userController;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void allUsersShouldBeAtLeastFour() {
		assertThat(
				this.restTemplate.getForObject("http://localhost:8080/user/all", Collection.class)
		).size().isGreaterThanOrEqualTo(4);
	}

	@Test
	void anyUsersOfTenIsFalse() {
		assertThat(
				this.restTemplate.getForObject("http://localhost:8080/user/any?id=10", Boolean.class)
		).isEqualTo(false);
	}

	@Test
	void byIdUsersOfOneContainsAdministrator() {
		assertThat(
				this.restTemplate.getForObject("http://localhost:8080/user/byId?id=1", User.class)
		).extracting(User::name).isEqualTo("Administrator");
	}

	@Test
	void idByDetailsForSecondUserDoesReturnTwo() {
		assertThat(
				this.restTemplate.getForObject(
						"http://localhost:8080/user/idByDetails?" +
								"email=ioana.dan@abc.ro&" +
								"passwordObfuscated=abcdefghijk", Long.class)
		).isEqualTo(2);
	}

	@Test
	void generateAndBySessionOperateCorrectly() {
		String generated =
				this.restTemplate.getForObject("http://localhost:8080/user/generateSession?id=1", String.class);
		generated = generated.replace("\"", "");
		assertThat(generated.length()).isEqualTo(36);
		assertThat(
				this.restTemplate.getForObject("http://localhost:8080/user/bySession?sessionId=" + generated,
						User.class)
		).extracting(User::name).isEqualTo("Administrator");
	}

	@Test
	void addNewUserHasIdFive() {
		assertThat(
				this.restTemplate.postForObject("http://localhost:8080/user/add",
						new User(-1, "Lorem Ipsum", "abcdefghijk",
								"hello@world.com", UserRole.USER),
						Long.class)
		).isEqualTo(5);
	}

	@Test
	void anyByThirdUserEmailIsTrue() {
		assertThat(
				this.restTemplate.getForObject("http://localhost:8080/user/anyByEmail?" +
						"email=alii.buzbuz@abc.ro", Boolean.class)
		).isEqualTo(true);
	}

}
