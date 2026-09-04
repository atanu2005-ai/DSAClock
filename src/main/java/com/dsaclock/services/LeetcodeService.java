package com.dsaclock.services;

import com.dsaclock.dto.LeetcodeProblemDTO;
import com.dsaclock.entities.Problems;
import com.dsaclock.repos.ProblemRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class LeetcodeService {

    private final RestClient restClient;
    private final ProblemRepo problemRepo;

    public LeetcodeService(RestClient restClient, ProblemRepo problemRepo) {
        this.restClient = restClient;
        this.problemRepo = problemRepo;
    }

    public LeetcodeProblemDTO[] fetchProblem() { //fetch leetcode problems from the 3rd party api and convert them into dto
        return restClient.get()
                .uri("https://leetcode-api-pied.vercel.app/problems")
                .retrieve().body(LeetcodeProblemDTO[].class);
    }

    public Problems convertToProblem(LeetcodeProblemDTO dto) { //to convert to problem obj

        Problems problem = new Problems();

        problem.setProblemId(dto.getFrontend_id());
        problem.setProblem_title(dto.getTitle());
        problem.setProblem_diff(dto.getDifficulty());
        problem.setProblem_url(dto.getUrl());

        return problem;
    }

    public List<Problems> convertToProblems(LeetcodeProblemDTO[] dtoArray) {

        List<Problems> problems = new ArrayList<>();

        for(LeetcodeProblemDTO dto : dtoArray) {
            problems.add(convertToProblem(dto)); //converting dto obj to problem obj and saving to problems list
        }

        return problems;
    }

    public void importProblems(List<Problems> problems) { //finally import to the entity
        problemRepo.saveAll(problems);
    }
}
