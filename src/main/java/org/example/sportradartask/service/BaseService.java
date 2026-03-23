package org.example.sportradartask.service;


import org.example.sportradartask.exeptions.NotFoundException;
import org.example.sportradartask.mapper.Mappable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public abstract class BaseService<E, D, R extends JpaRepository<E, Long>> {

    protected final R repository;
    protected final Mappable<E, D> mapper;

    protected BaseService(R repository, Mappable<E, D> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<D> findAll() {
        return mapper.toDto(repository.findAll());
    }

    public D findById(Long id) {
        return mapper.toDto(getOrThrow(id));
    }

    @Transactional
    public D create(D dto) {
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    @Transactional
    public D update(Long id, D dto) {
        E entity = getOrThrow(id);
        mapper.updateEntityFromDto(dto, entity);
        return mapper.toDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(getOrThrow(id));
    }

    protected E getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Entity not found with id: " + id));
    }
}
