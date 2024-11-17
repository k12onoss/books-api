package com.example.books_api.controllers;

import com.example.books_api.TestDataUtil;
import com.example.books_api.domain.entities.AuthorEntity;
import com.example.books_api.services.AuthorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthorControllerIntegrationTest {
    private final MockMvc mockMvc;
    private final AuthorService authorService;
    private final ObjectMapper objectMapper;

    @Autowired
    public AuthorControllerIntegrationTest(MockMvc mockMvc, AuthorService authorService) {
        this.mockMvc = mockMvc;
        this.authorService = authorService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsHttp201Created() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        author.setId(null);
        String authorJson = objectMapper.writeValueAsString(author);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsSavedAuthor() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        author.setId(null);
        String authorJson = objectMapper.writeValueAsString(author);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Jane Foster")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value("49")
        );
    }

    @Test
    public void testThatListAuthorSuccessfullyReturnsHttp200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListAuthorSuccessfullyReturnsSavedAuthors() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        authorService.saveAuthor(author);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Jane Foster")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].age").value("49")
        );
    }

    @Test
    public void testThatGetAuthorSuccessfullyReturnsHttp200OkWhenAuthorExits() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        authorService.saveAuthor(author);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/" + author.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetAuthorSuccessfullyReturnsHttp404NotFoundWhenAuthorDoesntExits() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/99")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetAuthorSuccessfullyReturnsSavedAuthor() throws Exception {
        AuthorEntity author = TestDataUtil.createTestAuthor();
        authorService.saveAuthor(author);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/" + author.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(author.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Jane Foster")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value("49")
        );
    }

    @Test
    public void testThatFullUpdateAuthorSuccessfullyReturnsHttp200OkWhenAuthorExits() throws Exception {
        List<AuthorEntity> authorEntities = TestDataUtil.createTestAuthors();
        authorService.saveAuthor(authorEntities.getFirst());

        String authorJson = objectMapper.writeValueAsString(authorEntities.get(1));

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/" + authorEntities.getFirst().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateAuthorSuccessfullyReturnsHttp404NotFoundWhenAuthorDoesntExits() throws Exception {
        List<AuthorEntity> authorEntities = TestDataUtil.createTestAuthors();
        String authorJson = objectMapper.writeValueAsString(authorEntities.get(1));

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFullUpdateAuthorSuccessfullyReturnsUpdatedAuthor() throws Exception {
        List<AuthorEntity> authorEntities = TestDataUtil.createTestAuthors();
        authorService.saveAuthor(authorEntities.getFirst());

        String authorJson = objectMapper.writeValueAsString(authorEntities.get(1));

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/" + authorEntities.getFirst().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(authorEntities.getFirst().getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(authorEntities.get(1).getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorEntities.get(1).getAge())
        );
    }

    @Test
    public void testThatPartialUpdateAuthorSuccessfullyReturnsHttp200OkWhenAuthorExits() throws Exception {
        List<AuthorEntity> authorEntities = TestDataUtil.createTestAuthors();
        authorService.saveAuthor(authorEntities.getFirst());

        String authorJson = objectMapper.writeValueAsString(authorEntities.get(1));

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/" + authorEntities.getFirst().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateAuthorSuccessfullyReturnsHttp404NotFoundWhenAuthorDoesntExits() throws Exception {
        List<AuthorEntity> authorEntities = TestDataUtil.createTestAuthors();
        String authorJson = objectMapper.writeValueAsString(authorEntities.get(1));

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatPartialUpdateAuthorSuccessfullyReturnsUpdatedAuthor() throws Exception {
        List<AuthorEntity> authorEntities = TestDataUtil.createTestAuthors();
        authorService.saveAuthor(authorEntities.getFirst());

        String authorJson = objectMapper.writeValueAsString(authorEntities.get(1));

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/" + authorEntities.getFirst().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(authorEntities.getFirst().getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(authorEntities.get(1).getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorEntities.get(1).getAge())
        );
    }

    @Test
    public void testThatDeleteAuthorSuccessfullyReturnsHttp200OkForExistingAuthor() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        authorService.saveAuthor(authorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/" + authorEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testThatDeleteAuthorSuccessfullyReturnsHttp404NotFoundForNonExistingAuthor() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/99")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }
}
