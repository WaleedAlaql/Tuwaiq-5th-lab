package com.waleed.lab5.Controller;

import com.waleed.lab5.Entity.Student;
import com.waleed.lab5.Response.ApiResponse;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private List<Student> students = new ArrayList<>(List.of(
            new Student(1L, "Waleed", 23, "Software Engineering", 4.5),
            new Student(2L, "Abdullah", 22, "Computer Science", 3.8),
            new Student(3L, "Omar", 21, "Information Systems", 4.8)
    ));

    // Display all students
    @GetMapping("/all")
    public List<Student> getAllStudents() {
        return students;
    }

    // Create a new student
    @PostMapping("/create")
    public ApiResponse createStudent(@RequestBody Student newStudent) {
        students.add(newStudent);
        return new ApiResponse("Student created successfully", 201, newStudent);
    }

    // 3. Update a student
    @PutMapping("/update/{id}")
    public ApiResponse updateStudent(@PathVariable Long id, @RequestBody Student updatedData) {
        for (Student s : students) {
            if (s.getId().equals(id)) {
                s.setName(updatedData.getName());
                s.setAge(updatedData.getAge());
                s.setDegree(updatedData.getDegree());
                s.setGpa(updatedData.getGpa());
                return new ApiResponse("Student updated successfully", 200, s);
            }
        }
        return new ApiResponse("Student not found for update", 404, null);
    }

    // Delete a student
    @DeleteMapping("/delete/{id}")
    public ApiResponse deleteStudent(@PathVariable Long id) {
        boolean removed = students.removeIf(s -> s.getId().equals(id));

        if (removed) {
            return new ApiResponse("Student deleted successfully", 200, null);
        } else {
            return new ApiResponse("Student not found to delete", 404, null);
        }
    }

    // Based on GPA, classify students into honors categories
    @GetMapping("/honors/{minGpa}")
    public ApiResponse getStudentsByHonors(@PathVariable double minGpa) {
        List<Student> honorStudents = new ArrayList<>();
        for (Student s : students) {
            if (s.getGpa() >= minGpa) {
                honorStudents.add(s);
            }
        }
        return new ApiResponse("Honor students retrieved successfully", 200, honorStudents);
    }

    // Display a group of students whose GPA is greater than the average GPA
    @GetMapping("/above-average")
    public ApiResponse getStudentsAboveAverageGPA() {
        if (students.isEmpty()) {
            return new ApiResponse("No students available to calculate average", 404, null);
        }

        double totalGpa = 0;
        for (Student s : students) {
            totalGpa += s.getGpa();
        }
        double averageGpa = totalGpa / students.size();

        List<Student> aboveAverageList = new ArrayList<>();
        for (Student s : students) {
            if (s.getGpa() > averageGpa) {
                aboveAverageList.add(s);
            }
        }
        return new ApiResponse("Students with GPA greater than average (" + averageGpa + ") retrieved successfully", 200, aboveAverageList);
    }


    /*
       ⬇️ Extra Endpoints ⬇️
     */

    // Get students by degree
    @GetMapping("/degree/{degree}")
    public ApiResponse getStudentsByDegree(@PathVariable String degree) {
        List<Student> result = new ArrayList<>();
        for (Student s : students) {
            if (s.getDegree().equalsIgnoreCase(degree)) {
                result.add(s);
            }
        }
        if (result.isEmpty()) {
            return new ApiResponse("No students found with degree: " + degree, 404, null);
        }
        return new ApiResponse("Students retrieved successfully", 200, result);
    }

    // Get students older than a specific age
    @GetMapping("/older-than/{age}")
    public ApiResponse getStudentsOlderThan(@PathVariable int age) {
        List<Student> result = new ArrayList<>();
        for (Student s : students) {
            if (s.getAge() > age) {
                result.add(s);
            }
        }
        if (result.isEmpty()) {
            return new ApiResponse("No students found older than: " + age, 404, null);
        }
        return new ApiResponse("Students retrieved successfully", 200, result);
    }
}