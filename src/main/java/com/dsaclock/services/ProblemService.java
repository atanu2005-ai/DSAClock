package com.dsaclock.services;

import com.dsaclock.entities.Problems;
import com.dsaclock.exceptions.ProblemAlreadyExistsException;
import com.dsaclock.exceptions.ProblemNotFoundException;
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
    public Problems getProblem(Long problemId) {
        return problemRepo.findByProblemId(problemId).orElseThrow(() ->
                new ProblemNotFoundException("No problem with such ID"));
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
