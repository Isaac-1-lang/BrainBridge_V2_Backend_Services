package org.henriette.brainbridge_v2.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.henriette.brainbridge_v2.DTOs.ProjectDTO;
import org.henriette.brainbridge_v2.Entities.Projects;
import org.henriette.brainbridge_v2.Services.ProjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Projects API",description = "Projects operations")
@RequestMapping("/projects/api")
public class ProjectsController {
    private ProjectsService  service;
    @Autowired
    public ProjectsController(ProjectsService service) {
        this.service = service;
    }
@PostMapping("/add")
    public ResponseEntity<?> createProject(@Valid @RequestBody ProjectDTO projectDTO) {
    Projects project = new Projects();
    project.setTitle(projectDTO.getTitle());
    project.setTeamId(projectDTO.getTeam_id());
    project.setStartDate(projectDTO.getStart_date());
    project.setEndDate(projectDTO.getEnd_date());
    project.setRepoUrl(projectDTO.getRepo_url());
    project.setProjectStatus(projectDTO.getProject_status());
    project.setProjectVisibility(projectDTO.getProject_visibility());
    service.createProject(project);
    return ResponseEntity.status(HttpStatus.CREATED).body(project);
}
@GetMapping("/all")
    public ResponseEntity<?> getAllProjects() {
        List<Projects> projects = service.getAllProjects();
        if(projects.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No projects found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(projects);
}
@PutMapping("/update")
    public ResponseEntity<?> updateProject(@Valid @RequestBody ProjectDTO projectDTO) {
        Projects project = new Projects();
        project.setProjectStatus(projectDTO.getProject_status());
        project.setProjectVisibility(projectDTO.getProject_visibility());
        service.updateProject(project);
        return ResponseEntity.status(HttpStatus.OK).body(project);
}
@GetMapping("/team/{teamName}")
    public ResponseEntity<?> getProject(@PathVariable("teamName") Integer teamName) {
        List<Projects> project = service.getProjectsByTeam(teamName);
        return ResponseEntity.status(HttpStatus.OK).body(project);
}
@GetMapping("/fetch/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable("id") Integer id) {
        Optional<Projects> project = service.getProjectById(id);
        if(project == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No projects found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(project);
}
@DeleteMapping("/remove/{title}")
    public ResponseEntity<?> deleteProject(@PathVariable("id") String title) {
        Optional<Projects> project = service.getProjectByTitle(title);
        if(project == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");
        }
        service.deleteProject(title);
        return ResponseEntity.status(HttpStatus.OK).body("Project "+ project.get().getTitle()+" deleted");

}

}
