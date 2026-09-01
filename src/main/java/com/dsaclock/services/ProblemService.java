package com.dsaclock.services;

import com.dsaclock.entities.Problems;
import com.dsaclock.repos.ProblemRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProblemService {
    ProblemRepo problemRepo;
    public ProblemService(ProblemRepo problemRepo) {  //repository instance constructor
        this.problemRepo = problemRepo;
    }

    //return all problems in database
    public List<Problems> getProblem() {
        return problemRepo.findAll();
    }

    //return single problem with id
    public Optional<Problems> getProblem(Long problemId) {
        return problemRepo.findById(problemId);
    }

    //add new problem
    public void addProblem(Problems problem) {
        problemRepo.save(problem);
    }

    //update existing problem
    public void updateProblem(Problems problem) {
        problemRepo.save(problem);
    }

    //delete problem by id
    public void deleteProblem(Long problemId) {
        problemRepo.deleteById(problemId);
    }
}
