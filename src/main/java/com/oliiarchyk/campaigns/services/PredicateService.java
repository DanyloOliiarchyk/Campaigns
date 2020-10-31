package com.oliiarchyk.campaigns.services;

import com.oliiarchyk.campaigns.models.Predicate;
import com.oliiarchyk.campaigns.repositories.PredicateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PredicateService {
    private PredicateRepository repository;

    @Autowired
    public PredicateService(PredicateRepository repository) {
        this.repository = repository;
    }

    public void delete(Predicate p) {
        repository.delete(p);
    }

    public List<Predicate> getAllByCampId(Long id) {
        return repository.findAllByCampId(id);
    }

    public void save(Predicate p) {
        repository.save(p);
    }
}
