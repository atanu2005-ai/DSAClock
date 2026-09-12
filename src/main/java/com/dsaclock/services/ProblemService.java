package com.dsaclock.services;

import com.dsaclock.entities.Problems;
import com.dsaclock.exceptions.ProblemAlreadyExistsException;
import com.dsaclock.exceptions.ProblemNotFoundException;
import com.dsaclock.repos.ProblemRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class ProblemService {

    //problem repo reference
    private final ProblemRepo problemRepo;

    //leetcode service reference
    private final LeetcodeService leetcodeService;

    public ProblemService(ProblemRepo problemRepo, LeetcodeService leetcodeService) {  //repository instance constructor
        this.problemRepo = problemRepo;
        this.leetcodeService = leetcodeService;
    }

    //return all problems in database
    public List<Problems> getProblem() {
        return problemRepo.findAllByOrderByProblemIdAsc();
    }

    //return single problem with id
    public Problems getProblem(Long problemId) {
        return problemRepo.findByProblemId(problemId).orElseThrow(() ->
                new ProblemNotFoundException("No problem with such ID"));
    }

    //get problem_details of a problem
    public String getProblem_details(@PathVariable Long problemId) {

        Problems problem = getProblem(problemId);

        return leetcodeService.getProblemDetails(problem.getProblem_slug());
    }

    //add new problem
    public void addProblem(Problems problem) {

        if(problemRepo.findByProblemId(problem.getProblemId()).isPresent()) {
            throw new ProblemAlreadyExistsException("Problem with this ID already exists"); //throws exception if already exists
        }

        problemRepo.save(problem);
    }

    //update existing problem
    public void updateProblem(Problems problem) {

        if(problemRepo.findByProblemId(problem.getProblemId()).isEmpty()) {
            throw new ProblemNotFoundException("No such problem with this ID!"); //can't update non-existing problem
        }

        problemRepo.save(problem);
    }

    //delete problem by id
    public void deleteProblem(Long problemId) {

        if(problemRepo.findByProblemId(problemId).isEmpty()) {
            throw new ProblemNotFoundException("This problem doesn't even exists bro!"); //denies deleting non-existing problem
        }

        problemRepo.deleteById(problemId);
    }
}
