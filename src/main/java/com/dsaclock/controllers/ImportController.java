package com.dsaclock.controllers;

import com.dsaclock.dto.LeetcodeProblemDTO;
import com.dsaclock.entities.Problems;
import com.dsaclock.services.LeetcodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/import")
@Tag(
        name = "Import",
        description = "API for Leetcode problems import using third party API source."
)
public class ImportController {

    public final LeetcodeService leetcodeService;

    public ImportController(LeetcodeService leetcodeService) {
        this.leetcodeService = leetcodeService;
    }

    @PostMapping //import leetcode problems with 3rd party api
    @Operation(summary = "Import Leetcode problems")
    public ResponseEntity<Void> importProblems() {

        LeetcodeProblemDTO[] dto = leetcodeService.fetchProblem(); //fetch problems from api

        List<Problems> problems = leetcodeService.convertToProblems(dto); //convert to problems obj list

        leetcodeService.importProblems(problems); //save to problems entity

        return ResponseEntity.ok().build();
    }
}
