package com.dsaclock.repos;

import com.dsaclock.entities.RevisionActivity;
import com.dsaclock.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RevisionActivityRepo extends JpaRepository<RevisionActivity, Long> {

    List<RevisionActivity> findByUserUserId(Long userId);

    Optional<RevisionActivity> findByUserUserIdAndActivityRevisionDate(Long userId, LocalDate date);
}
