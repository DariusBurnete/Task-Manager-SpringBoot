package com.task.manager.service;

import com.task.manager.model.Task;
import com.task.manager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getTasksByOwnerId(Long ownerId) {
        return taskRepository.findByOwnerId(ownerId);
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task updateRemainingEffort(Long id, Integer remainingEffort) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            task.setRemainingEffort(remainingEffort);
            return taskRepository.save(task);
        }
        return null;
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public String computeEstimationAccuracy(Long id) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            Integer estimatedHours = task.getEstimatedHours();
            Integer completedHours = task.getCompletedHours();

            if (estimatedHours == null || estimatedHours == 0) {
                return "NOT_ESTIMATED";
            } else if (0.9 * estimatedHours <= completedHours && completedHours <= 1.1 * estimatedHours) {
                return "ACCURATELY_ESTIMATED";
            } else if (completedHours < 0.9 * estimatedHours) {
                return "OVER_ESTIMATED";
            } else {
                return "UNDER_ESTIMATED";
            }
        }
        return "TASK_NOT_FOUND";
    }
}
