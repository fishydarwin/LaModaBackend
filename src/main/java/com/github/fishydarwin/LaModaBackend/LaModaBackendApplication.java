package com.github.fishydarwin.LaModaBackend;

import com.github.fishydarwin.LaModaBackend.controller.ArticleController;
import com.github.fishydarwin.LaModaBackend.domain.Article;
import com.github.fishydarwin.LaModaBackend.domain.ArticleAttachment;
import com.github.fishydarwin.LaModaBackend.repository.faker.ArticleFaker;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@SpringBootApplication
public class LaModaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(LaModaBackendApplication.class, args);
	}

}

