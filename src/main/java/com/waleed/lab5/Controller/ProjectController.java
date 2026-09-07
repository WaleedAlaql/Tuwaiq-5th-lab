package com.waleed.lab5.Controller;

import com.waleed.lab5.Entity.Project;
import com.waleed.lab5.Response.ApiResponse;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private List<Project> projects = new ArrayList<>(List.of(
            new Project(1L, "E-Commerce API", "Backend for online store", "Not Done", "TechCorp"),
            new Project(2L, "Mobile App", "Flutter application", "Done", "InnovateX")
    ));

    // Display all projects
    @GetMapping("/all")
    public List<Project> getAllProjects() {
        return projects;
    }

    // Create a new project
    @PostMapping("/create")
    public ApiResponse createProject(@RequestBody Project newProject) {
        projects.add(newProject);
        return new ApiResponse("Project created successfully", 201, newProject);
    }

    // Update a project
    @PutMapping("/update/{id}")
    public ApiResponse updateProject(@PathVariable Long id, @RequestBody Project updatedData) {
        for (Project p : projects) {
            if (p.getId().equals(id)) {
                p.setTitle(updatedData.getTitle());
                p.setDescription(updatedData.getDescription());
                p.setStatus(updatedData.getStatus());
                p.setCompanyName(updatedData.getCompanyName());
                return new ApiResponse("Project updated successfully", 200, p);
            }
        }
        return new ApiResponse("Project not found for update", 404, null);
    }

    // Delete a project
    @DeleteMapping("/delete/{id}")
    public ApiResponse deleteProject(@PathVariable Long id) {
        boolean removed = projects.removeIf(p -> p.getId().equals(id));

        if (removed) {
            return new ApiResponse("Project deleted successfully", 200, null);
        } else {
            return new ApiResponse("Project not found to delete", 404, null);
        }
    }

    // Change the project status as done or not done
    @PutMapping("/change-status/{id}")
    public ApiResponse changeProjectStatus(@PathVariable Long id, @RequestBody Project updatedData) {
        for (Project p : projects) {
            if (p.getId().equals(id)) {
                p.setStatus(updatedData.getStatus());
                return new ApiResponse("Project status updated successfully", 200, p);
            }
        }
        return new ApiResponse("Project not found to change status", 404, null);
    }

    // Search for a project by given title
    @GetMapping("/search/title/{title}")
    public ApiResponse searchProjectByTitle(@PathVariable String title) {
        List<Project> results = new ArrayList<>();
        for (Project p : projects) {
            if (p.getTitle().toLowerCase().contains(title.toLowerCase())) {
                results.add(p);
            }
        }
        if (results.isEmpty()) {
            return new ApiResponse("No projects found matching the title: " + title, 404, null);
        }
        return new ApiResponse("Project found successfully", 200, results);
    }

    // Display All projects for one company by companyName
    @GetMapping("/company/{companyName}")
    public ApiResponse getProjectsByCompany(@PathVariable String companyName) {
        List<Project> companyProjects = new ArrayList<>();
        for (Project p : projects) {
            if (p.getCompanyName().equalsIgnoreCase(companyName)) {
                companyProjects.add(p);
            }
        }

        if (companyProjects.isEmpty()) {
            return new ApiResponse("No projects found for company: " + companyName, 404, null);
        }
        return new ApiResponse("Company projects retrieved successfully", 200, companyProjects);
    }
}