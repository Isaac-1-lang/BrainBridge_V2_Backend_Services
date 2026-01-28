package org.henriette.brainbridge_v2.Services;

import org.henriette.brainbridge_v2.Entities.Projects;
import org.henriette.brainbridge_v2.Enums.Status;
import org.henriette.brainbridge_v2.Enums.Visibility;
import org.henriette.brainbridge_v2.Repositories.ProjectsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectsService {

    private final ProjectsRepository repo;

    @Autowired
    public ProjectsService(ProjectsRepository repo) {
        this.repo = repo;
    }

    public Projects createProject(Projects project) {
        return repo.save(project);
    }

    public List<Projects> getAllProjects() {
        return repo.findAll();
    }

    public Optional<Projects> getProjectById(Integer id) {
        return repo.findById(id);
    }
    public Optional<Projects> getProjectByTitle(String title) {
        return repo.findProjectsByTitle(title);
    }
    public Projects updateProject(Projects project) {
        return repo.save(project);
    }

    public void deleteProject(String title) {
        repo.deleteByTitle(title);
    }

//    public List<Projects> getProjectsByStatus(Status status) {
//        return repo.findByProjectStatus(status);
//    }
//
//    public List<Projects> getProjectsByVisibility(Visibility visibility) {
//        return repo.findByProjectVisibility(visibility);
//    }
//
//    public List<Projects> getProjectsByOwner(Integer ownerId) {
//        return repo.findByOwnerId(ownerId);
//    }

    public List<Projects> getProjectsByTeam(Integer teamId) {
        return repo.findByTeamId(teamId);
    }

//    public List<Projects> searchProjectsByTitle(String keyword) {
//        return repo.findByTitleContainingIgnoreCase(keyword);
//    }
//
//    public List<Projects> getProjectsByDateRange(LocalDate start, LocalDate end) {
//        return repo.findByStartDateBetweenAndEndDateBetween(start, start, end, end);
//    }
}
