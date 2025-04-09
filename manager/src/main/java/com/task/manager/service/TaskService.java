package com.task.manager.service;

import com.task.manager.model.Task;
import com.task.manager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public List<Task> getTasksByOwnerId(Long ownerId) {
        return taskRepository.findByOwnerId(ownerId, Sort.by(Sort.Direction.ASC, "id"));
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task updateRemainingEffort(Long id, Integer remainingEffort) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            task.setRemainingEffort(remainingEffort);
            task.setCompletedHours(task.getEstimatedHours() - remainingEffort);
            return taskRepository.save(task);
        }
        return null;
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public void saveTask(Task task) {
        task.setCompletedHours(task.getEstimatedHours() - task.getRemainingEffort());
        taskRepository.save(task);
    }
}
