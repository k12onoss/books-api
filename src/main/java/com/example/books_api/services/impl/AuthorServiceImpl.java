package com.example.books_api.services.impl;

import com.example.books_api.domain.entities.AuthorEntity;
import com.example.books_api.repositories.AuthorRepository;
import com.example.books_api.services.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorEntity saveAuthor(AuthorEntity authorEntity) {
        return authorRepository.save(authorEntity);
    }

    @Override
    public List<AuthorEntity> findAll() {
        return StreamSupport.stream(
                authorRepository.findAll().spliterator(),
                false
        ).toList();
    }

    @Override
    public Page<AuthorEntity> findAll(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }

    @Override
    public Optional<AuthorEntity> findOne(Long id) {
        return authorRepository.findById(id);
    }

    @Override
    public boolean isPresent(Long id) {
        return authorRepository.existsById(id);
    }

    @Override
    public AuthorEntity partialUpdateAuthor(Long id, AuthorEntity authorEntity) {
        Optional<AuthorEntity> existing = authorRepository.findById(id);
        if (existing.isPresent()) {
            AuthorEntity existingAuthor = existing.get();
            Optional.ofNullable(authorEntity.getName()).ifPresent(existingAuthor::setName);
            Optional.ofNullable(authorEntity.getAge()).ifPresent(existingAuthor::setAge);
            authorRepository.save(existingAuthor);

            return existingAuthor;
        } else {
            throw new RuntimeException("Author does not exist.");
        }
    }

    @Override
    public void delete(Long id) {
        authorRepository.deleteById(id);
    }
}
