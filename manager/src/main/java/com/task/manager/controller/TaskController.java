package com.task.manager.controller;

import com.task.manager.model.Comment;
import com.task.manager.model.Owner;
import com.task.manager.model.Task;
import com.task.manager.service.OwnerService;
import com.task.manager.service.TaskService;
import com.task.manager.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private OwnerService ownerService;

    private Long getCurrentOwnerId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            Optional<Owner> ownerOpt = ownerService.findByEmail(email);
            if (ownerOpt.isPresent()) {
                return ownerOpt.get().getId();
            }
        }
        return null;
    }


    @GetMapping
    public String getAllTasks(@RequestParam(required = false) Long ownerId, Model model) {

        Long currentOwnerId = getCurrentOwnerId();
        List<Task> tasks;

        if (ownerId == null) {
            ownerId = currentOwnerId;
            tasks = taskService.getTasksByOwnerId(ownerId);
        } else if (ownerId == 0) {
            tasks = taskService.getAllTasks();
        } else {
            tasks = taskService.getTasksByOwnerId(ownerId);
        }

        List<Owner> owners = ownerService.getAllOwners();
        model.addAttribute("owners", owners);
        model.addAttribute("tasks", tasks);
        model.addAttribute("selectedOwnerId", ownerId);
        return "taskList";
    }

    @GetMapping("/owner/{ownerId}")
    public String getTasksByOwnerId(@PathVariable Long ownerId, Model model) {
        List<Task> tasks = taskService.getTasksByOwnerId(ownerId);
        model.addAttribute("tasks", tasks);
        return "taskList";
    }

    @GetMapping("/new")
    public String showCreateTaskForm(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("ownerId", getCurrentOwnerId());
        return "createTask";
    }

    @PostMapping("/new")
    public String createTask(@ModelAttribute Task task, @RequestParam Long ownerId) {
        Optional<Owner> ownerOpt = ownerService.getOwnerById(ownerId);
        if (ownerOpt.isPresent()) {
            task.setOwner(ownerOpt.get());
            taskService.saveTask(task);
        }
        return "redirect:/tasks";
    }

    @GetMapping("/{id}")
    public String getTaskById(@PathVariable Long id, Model model) {
        Optional<Task> taskOpt = taskService.getTaskById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            if (task.getComments() == null) {
                task.setComments(new ArrayList<>());
            }
            model.addAttribute("task", task);
        } else {
            model.addAttribute("task", null);
        }
        return "taskDetail";
    }

    @PostMapping("/{id}/remainingEffort")
    public String updateRemainingEffort(@PathVariable Long id, @RequestParam Integer remainingEffort, Model model) {
        Task task = taskService.updateRemainingEffort(id, remainingEffort);
        model.addAttribute("task", task);
        return "taskDetail";
    }

    @PostMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id, Model model) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }

    @PostMapping("/{taskId}/comments")
    public String addComment(@PathVariable Long taskId, @RequestParam String content, Model model) {
        Task task = taskService.getTaskById(taskId).orElse(null);
        if (task != null) {
            Comment comment = new Comment();
            comment.setTask(task);
            comment.setContent(content);
            comment.setCreatedAt(LocalDateTime.now());
            commentService.addComment(comment);
        }
        return "redirect:/tasks/" + taskId;
    }

    @PostMapping("/comments/{commentId}/edit")
    public String editComment(@PathVariable Long commentId, @RequestParam String content, Model model) {
        commentService.updateComment(commentId, content);
        return "redirect:/tasks";
    }

    @PostMapping("/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long commentId, Model model) {
        commentService.deleteComment(commentId);
        return "redirect:/tasks";
    }
}