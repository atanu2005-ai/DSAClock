package com.dsaclock.services;

import com.dsaclock.dto.LeetcodeProblemDTO;
import com.dsaclock.entities.Problems;
import com.dsaclock.repos.ProblemRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LeetcodeService {

    private final RestClient restClient;
    private final ProblemRepo problemRepo;

    //web client reference
    private final WebClient webClient;

    //jsonNode reference


    public LeetcodeService(RestClient restClient,
                           ProblemRepo problemRepo,
                           WebClient.Builder builder) {
        this.restClient = restClient;
        this.problemRepo = problemRepo;
        this.webClient = builder.baseUrl("https://leetcode.com").build();
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
        problem.setProblem_slug(dto.getTitle_slug());
        problem.setProblem_likes(dto.getLikes());
        problem.setProblem_dislikes(dto.getDislikes());

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

    //method to get problem details with leetcode graphql query
    public String getProblemDetails(String slug) {

        String query = """
            query questionContent($titleSlug: String!) {
                question(titleSlug: $titleSlug) {
                    content
                }
            }
            """;

        JsonNode response = webClient.post()//json node object creation
                .uri("/graphql")
                .bodyValue(Map.of(
                        "query", query,
                        "variables", Map.of("titleSlug", slug)
                ))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        String content = response //extracting the content field from the JSON body
                .path("data")
                .path("question")
                .path("content")
                .asString();

        int descIndex = content.indexOf("<p><strong class=\"example\">");  //finding the index of the string where desc ends

        //final description paragraph
        if(descIndex != -1) {
            content = content.substring(0, descIndex);
        }

        return content;

    }
}
