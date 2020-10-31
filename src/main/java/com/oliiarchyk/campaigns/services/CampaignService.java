package com.oliiarchyk.campaigns.services;

import com.oliiarchyk.campaigns.models.Campaign;
import com.oliiarchyk.campaigns.repositories.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampaignService {
    private CampaignRepository repository;

    @Autowired
    public CampaignService(CampaignRepository repository) {
        this.repository = repository;
    }

    public void delete(Campaign c) {
        repository.delete(c);
    }

    public void save(Campaign c) {
        repository.save(c);
    }

    public List<Campaign> getAll(String filter) {
        if (filter == null || filter.isEmpty()) {
            return repository.findAll();
        } else {
            return repository.search(filter);
        }
    }
}