package com.dsaclock.services;

import com.dsaclock.dto.RevisionActivityResponse;
import com.dsaclock.entities.RevisionActivity;
import com.dsaclock.repos.RevisionActivityRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RevisionActivityService {

    private final RevisionActivityRepo revisionActivityRepo;

    public RevisionActivityService(RevisionActivityRepo revisionActivityRepo) {
        this.revisionActivityRepo = revisionActivityRepo;
    }

    public List<RevisionActivityResponse> getActivities(Long userId) {
        List<RevisionActivity> list = revisionActivityRepo.findByUserUserId(userId);

        List<RevisionActivityResponse> responses = new ArrayList<>();

        for(RevisionActivity obj : list) {
            RevisionActivityResponse response = new RevisionActivityResponse();

            response.setDate(obj.getActivityRevisionDate());
            response.setRevisionCount(obj.getOnDateRevisionCount());

            responses.add(response);
        }

        return responses;
    }
}
