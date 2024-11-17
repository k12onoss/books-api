package com.example.books_api.repositories;

import com.example.books_api.TestDataUtil;
import com.example.books_api.domain.entities.AuthorEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthorEntityRepositoryIntegrationTests {
    private final AuthorRepository underTest;

    @Autowired
    public AuthorEntityRepositoryIntegrationTests(AuthorRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatAuthorCanBeCreatedAndRecalled() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();

        underTest.save(authorEntity);
        Optional<AuthorEntity> result = underTest.findById(authorEntity.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(authorEntity);
    }

    @Test
    public void testThatMultipleAuthorsCanBeCreatedAndRecalled() {
        List<AuthorEntity> authorEntities = TestDataUtil.createTestAuthors();
        for (AuthorEntity authorEntity : authorEntities) underTest.save(authorEntity);

        Iterable<AuthorEntity> result = underTest.findAll();

        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyInAnyOrderElementsOf(authorEntities);
    }

    @Test
    public void testThatAuthorCanBeUpdated() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        underTest.save(authorEntity);

        authorEntity.setName("UPDATED");
        underTest.save(authorEntity);

        Optional<AuthorEntity> result = underTest.findById(authorEntity.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(authorEntity);
    }

    @Test
    public void testThatAuthorCanBeDeleted() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthor();
        underTest.save(authorEntity);

        underTest.deleteById(authorEntity.getId());

        Optional<AuthorEntity> result = underTest.findById(authorEntity.getId());

        assertThat(result).isEmpty();
    }

    @Test
    public void testThatAuthorLessThanAgeAreRecalled() {
        List<AuthorEntity> authorEntities = TestDataUtil.createTestAuthors();
        for (AuthorEntity authorEntity : authorEntities) underTest.save(authorEntity);

        Iterable<AuthorEntity> result = underTest.ageLessThan(50);

        assertThat(result).containsExactly(authorEntities.get(0), authorEntities.get(1));
    }
}
