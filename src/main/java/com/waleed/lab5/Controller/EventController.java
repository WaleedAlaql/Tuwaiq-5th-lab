package com.waleed.lab5.Controller;

import com.waleed.lab5.Entity.Event;
import com.waleed.lab5.Response.ApiResponse;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private List<Event> events = new ArrayList<>(List.of(
            new Event(1L, "Spring Boot Workshop", 50, LocalDateTime.of(2026, 9, 10, 0, 0), LocalDateTime.of(2026, 9, 12, 0, 0)),
            new Event(2L, "Java Hackathon", 100, LocalDateTime.of(2026, 10, 1, 0, 0), LocalDateTime.of(2026, 10, 3, 0, 0))
    ));

    // Display all events
    @GetMapping("/all")
    public List<Event> getAllEvents() {
        return events;
    }

    // Create a new event
    @PostMapping("/create")
    public ApiResponse createEvent(@RequestBody Event newEvent) {
        events.add(newEvent);
        return new ApiResponse("Event created successfully", 201, newEvent);
    }

    // Update an event
    @PutMapping("/update/{id}")
    public ApiResponse updateEvent(@PathVariable Long id, @RequestBody Event updatedData) {
        for (Event e : events) {
            if (e.getId().equals(id)) {
                e.setDescription(updatedData.getDescription());
                e.setCapacity(updatedData.getCapacity());
                e.setStartDate(updatedData.getStartDate());
                e.setEndDate(updatedData.getEndDate());
                return new ApiResponse("Event updated successfully", 200, e);
            }
        }
        return new ApiResponse("Event not found for update", 404, null);
    }

    // Delete an event
    @DeleteMapping("/delete/{id}")
    public ApiResponse deleteEvent(@PathVariable Long id) {
        boolean removed = events.removeIf(e -> e.getId().equals(id));

        if (removed) {
            return new ApiResponse("Event deleted successfully", 200, null);
        } else {
            return new ApiResponse("Event not found to delete", 404, null);
        }
    }

    // Change capacity
    @PutMapping("/change-capacity/{id}/{capacity}")
    public ApiResponse changeCapacity(@PathVariable Long id, @PathVariable int capacity) {
        for (Event e : events) {
            if (e.getId().equals(id)) {
                e.setCapacity(capacity);
                return new ApiResponse("Event capacity updated successfully", 200, e);
            }
        }
        return new ApiResponse("Event not found to update capacity", 404, null);
    }

    // Search for an event by given id
    @GetMapping("/search/{id}")
    public ApiResponse getEventById(@PathVariable Long id) {
        for (Event e : events) {
            if (e.getId().equals(id)) {
                return new ApiResponse("Event found successfully", 200, e);
            }
        }
        return new ApiResponse("Event not found with ID: " + id, 404, null);
    }

    /*
       ⬇️ Extra Endpoints ⬇️
     */

    // Get events with a minimum capacity
    @GetMapping("/min-capacity/{minCapacity}")
    public ApiResponse getEventsByMinCapacity(@PathVariable int minCapacity) {
        List<Event> result = new ArrayList<>();
        for (Event e : events) {
            if (e.getCapacity() >= minCapacity) {
                result.add(e);
            }
        }
        if (result.isEmpty()) {
            return new ApiResponse("No events found with minimum capacity: " + minCapacity, 404, null);
        }
        return new ApiResponse("Events retrieved successfully", 200, result);
    }

    // Search event by description keyword
    @GetMapping("/search/description/{keyword}")
    public ApiResponse searchEventByDescription(@PathVariable String keyword) {
        List<Event> result = new ArrayList<>();
        for (Event e : events) {
            if (e.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(e);
            }
        }
        if (result.isEmpty()) {
            return new ApiResponse("No events found matching description keyword: " + keyword, 404, null);
        }
        return new ApiResponse("Events found successfully", 200, result);
    }
}