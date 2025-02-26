package com.task.manager.controller;

import com.task.manager.model.Task;
import com.task.manager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping
    public String getAllTasks(Model model) {
        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);
        return "taskList"; // Ensure this view name does not match the request URL
    }

    @GetMapping("/owner/{ownerId}")
    public String getTasksByOwnerId(@PathVariable Long ownerId, Model model) {
        List<Task> tasks = taskService.getTasksByOwnerId(ownerId);
        model.addAttribute("tasks", tasks);
        return "taskList"; // Ensure this view name does not match the request URL
    }

    @GetMapping("/{id}")
    public String getTaskById(@PathVariable Long id, Model model) {
        Optional<Task> task = taskService.getTaskById(id);
        model.addAttribute("task", task.orElse(null));
        return "taskDetail"; // Ensure this view name does not match the request URL
    }

    @PostMapping("/{id}/remainingEffort")
    public String updateRemainingEffort(@PathVariable Long id, @RequestParam Integer remainingEffort, Model model) {
        Task task = taskService.updateRemainingEffort(id, remainingEffort);
        model.addAttribute("task", task);
        return "taskDetail"; // Ensure this view name does not match the request URL
    }

    @PostMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id, Model model) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }
}