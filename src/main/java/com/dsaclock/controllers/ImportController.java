package com.dsaclock.controllers;

import com.dsaclock.dto.LeetcodeProblemDTO;
import com.dsaclock.entities.Problems;
import com.dsaclock.services.LeetcodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/import")
public class ImportController {

    public final LeetcodeService leetcodeService;

    public ImportController(LeetcodeService leetcodeService) {
        this.leetcodeService = leetcodeService;
    }

    @PostMapping //import leetcode problems with 3rd party api
    public ResponseEntity<Void> importProblems() {

        LeetcodeProblemDTO[] dto = leetcodeService.fetchProblem(); //fetch problems from api

        List<Problems> problems = leetcodeService.convertToProblems(dto); //convert to problems obj list

        leetcodeService.importProblems(problems); //save to problems entity

        return ResponseEntity.ok().build();
    }
}
