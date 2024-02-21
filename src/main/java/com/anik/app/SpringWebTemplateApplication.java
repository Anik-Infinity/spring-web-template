package com.anik.app;

import com.anik.app.dto.author.CreateAuthorDto;
import com.anik.app.entity.author.Author;
import com.anik.app.mapper.AuthorMapper;
import com.anik.app.repository.AuthorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
@RequiredArgsConstructor
//@EnableJpaAuditing(auditorAwareRef = "databaseAuditing")
@Slf4j
public class SpringWebTemplateApplication implements CommandLineRunner {
	private final AuthorRepository authorRepository;
	private final AuthorMapper authorMapper;
	public static void main(String[] args) {
		SpringApplication.run(SpringWebTemplateApplication.class, args);
	}

	@Override
	public void run(String... args) {
		if(this.authorRepository.findAll().isEmpty()) {
			try (InputStream inputStream = new ClassPathResource("/jsonData/seed.json").getInputStream()) {
				ObjectMapper objectMapper = new ObjectMapper();
				CreateAuthorDto[] authorDtos = objectMapper.readValue(inputStream, CreateAuthorDto[].class);
				for(var authorDto : authorDtos) {
					Author author = this.authorMapper.toEntity(authorDto);
					author.getBooks().forEach(book -> {
						book.setAuthor(author);
					});
					this.authorRepository.save(author);
				}
			} catch (IOException e) {
				log.info(e.toString());
			}
		}
	}
}
