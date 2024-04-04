package com.github.fishydarwin.LaModaBackend;

import com.github.fishydarwin.LaModaBackend.controller.CategoryController;
import com.github.fishydarwin.LaModaBackend.domain.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
class CategoryControllerTests {

	@Autowired
	private CategoryController categoryController;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void allCategoriesShouldBeFive() {
		assertThat(
				this.restTemplate.getForObject("http://localhost:8080/category/all", Collection.class)
		).size().isEqualTo(5);
	}

	@Test
	void anyCategoriesOfOneIsTrue() {
		assertThat(
				this.restTemplate.getForObject("http://localhost:8080/category/any?id=1", Boolean.class)
		).isEqualTo(true);
	}

	@Test
	void byIdCategoriesOfOneContainsImbracaminte() {
		assertThat(
				this.restTemplate.getForObject("http://localhost:8080/category/byId?id=1", Category.class)
		).extracting(Category::name).isEqualTo("Îmbrăcăminte");
	}

}
