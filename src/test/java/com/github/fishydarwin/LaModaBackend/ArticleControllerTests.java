package com.github.fishydarwin.LaModaBackend;

import com.github.fishydarwin.LaModaBackend.controller.ArticleController;
import com.github.fishydarwin.LaModaBackend.domain.Article;
import com.github.fishydarwin.LaModaBackend.domain.ArticleAttachment;
import com.github.fishydarwin.LaModaBackend.util.PagedResult;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
class ArticleControllerTests {

	@Autowired
	private ArticleController articleController;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void allArticlesShouldBeAtLeastFive() {
		assertThat(
				this.restTemplate.getForObject("http://localhost:8080/article/all?page=0", PagedResult.class)
		).extracting(PagedResult::size, as(InstanceOfAssertFactories.LONG)).isGreaterThanOrEqualTo(5);
	}

	@Test
	void anyArticlesOfThreeIsTrue() {
		assertThat(
				this.restTemplate.getForObject("http://localhost:8080/article/any?id=3", Boolean.class)
		).isEqualTo(true);
	}

	@Test
	void byIdArticlesOfTwoContainsVans() {
		assertThat(
				this.restTemplate.getForObject("http://localhost:8080/article/byId?id=2", Article.class)
		).extracting(Article::name).asString().contains("Vanși");
	}

	@Test
	void allByUserTwoShouldBeTwo() {
		assertThat(
				this.restTemplate.getForObject("http://localhost:8080/article/byUser?page=0&author=2",
						PagedResult.class)
		).extracting(PagedResult::size, as(InstanceOfAssertFactories.LONG)).isEqualTo(2);
	}

	@Test
	void allByCategoryThreeShouldBeOne() {
		assertThat(
				this.restTemplate.getForObject("http://localhost:8080/article/byCategory?page=0&category=3",
						PagedResult.class)
		).extracting(PagedResult::size, as(InstanceOfAssertFactories.LONG)).isEqualTo(1);
	}

	@Test
	void allMatchingTextPentruShouldBeThree() {
		assertThat(
				this.restTemplate.getForObject("http://localhost:8080/article/byMatchText?page=0&text=pentru",
						PagedResult.class)
		).extracting(PagedResult::size, as(InstanceOfAssertFactories.LONG)).isEqualTo(3);
	}

	@Test
	void addUpdateDeleteAllOperate() {

		// Create
		long lastId = this.restTemplate.postForObject("http://localhost:8080/article/add",
				new Article(
						-1, 1, 5, "Test Article",
						"Lorem ipsum dolor sit amet",
						List.of(
								new ArticleAttachment(1, 1,
										"https://www.sakurafalls.net/data/assets/logo/sfbb18.png")
						)
				),
				Long.class);
		assertThat(lastId).isEqualTo(6);

		// Update
		this.restTemplate.put("http://localhost:8080/article/update/6",
				new Article(
						6, 1, 5, "Yet I Yearn For Love",
						"Lorem ipsum dolor sit amet",
						List.of(
								new ArticleAttachment(1, 1,
										"https://www.sakurafalls.net/data/assets/logo/sfbb18.png")
						)
				));

		assertThat(
				this.restTemplate.getForObject("http://localhost:8080/article/byId?id=6", Article.class)
		).extracting(Article::name).asString().contains("Yearn For Love");

		// Delete
		this.restTemplate.delete("http://localhost:8080/article/delete/6");
		assertThat(
				this.restTemplate.getForObject("http://localhost:8080/article/any?id=6", Boolean.class)
		).isEqualTo(false);

		// Test Last ID works fine
		lastId = this.restTemplate.postForObject("http://localhost:8080/article/add",
				new Article(
						-1, 1, 4, "Another Test Article",
						"No, this is different...",
						List.of(
								new ArticleAttachment(1, 1,
										"https://www.sakurafalls.net/data/assets/logo/sfbb18.png")
						)
				),
				Long.class);
		assertThat(lastId).isEqualTo(7);

		// Clean up
		this.restTemplate.delete("http://localhost:8080/article/delete/7");
		assertThat(
				this.restTemplate.getForObject("http://localhost:8080/article/any?id=7", Boolean.class)
		).isEqualTo(false);

	}

}
