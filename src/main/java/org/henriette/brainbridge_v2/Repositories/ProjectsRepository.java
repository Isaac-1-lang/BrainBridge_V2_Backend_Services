package org.henriette.brainbridge_v2.Repositories;

import org.henriette.brainbridge_v2.Entities.Projects;
import org.henriette.brainbridge_v2.Enums.Status;
import org.henriette.brainbridge_v2.Enums.Visibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectsRepository extends JpaRepository<Projects, Integer> {

    List<Projects> findByProjectStatus(Status status);
    Optional<Projects> findProjectsByTitle(String title);

    List<Projects> findByProjectVisibility(Visibility visibility);

    List<Projects> findByOwnerId(Integer ownerId);

    List<Projects> findByTeamId(Integer teamId);

//    List<Projects> findBySourceIdeaId(Integer sourceIdeaId);
//
//    List<Projects> findByProjectStatusAndOwnerId(Status status, Integer ownerId);
//
//    List<Projects> findByProjectStatusAndTeamId(Status status, Integer teamId);
//
//    List<Projects> findByProjectVisibilityAndTeamId(Visibility visibility, Integer teamId);

    List<Projects> findByTitleContainingIgnoreCase(String title);

//    List<Projects> findByStartDateAfter(java.time.LocalDate date);
//
//    List<Projects> findByEndDateBefore(java.time.LocalDate date);

    List<Projects> findByStartDateBetweenAndEndDateBetween(
            java.time.LocalDate startStart, java.time.LocalDate startEnd,
            java.time.LocalDate endStart, java.time.LocalDate endEnd
    );

    void deleteByTitle(String title);
}
