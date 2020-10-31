package com.oliiarchyk.campaigns.repositories;

import com.oliiarchyk.campaigns.models.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    @Query("select c from Campaign c where lower(c.campaign) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.ranking) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.updateAt) like lower(concat('%', :searchTerm, '%')) ")
    List<Campaign> search(@Param("searchTerm") String searchTerm);
}