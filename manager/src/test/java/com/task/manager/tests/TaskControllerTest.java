package com.task.manager.tests;

import com.task.manager.model.Task;
import com.task.manager.model.Owner;
import com.task.manager.model.Comment;
import com.task.manager.service.TaskService;
import com.task.manager.service.OwnerService;
import com.task.manager.service.CommentService;
import com.task.manager.controller.TaskController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @Mock
    private OwnerService ownerService;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private TaskController taskController;

    @Test
    public void testGetAllTasks() {
        Model model = mock(Model.class);
        Long ownerId = 1L;

        Owner owner = mock(Owner.class);
        when(owner.getId()).thenReturn(ownerId);

        Task task1 = new Task();
        task1.setOwner(owner);
        task1.setId(1L);

        Task task2 = new Task();
        task2.setOwner(owner);
        task2.setId(2L);

        when(ownerService.getAllOwners()).thenReturn(List.of(owner));
        when(taskService.getTasksByOwnerId(ownerId)).thenReturn(Arrays.asList(task1, task2));

        String viewName = taskController.getAllTasks(ownerId, model);

        assertEquals("taskList", viewName);
        verify(ownerService, times(1)).getAllOwners();
        verify(taskService, times(1)).getTasksByOwnerId(ownerId);
    }

    @Test
    public void testCreateTask() {
        Task task = new Task();
        task.setDescription("New Task");
        task.setEstimatedHours(10);
        task.setRemainingEffort(5);

        Long ownerId = 1L;
        Owner owner = mock(Owner.class);
        when(owner.getId()).thenReturn(ownerId);

        when(ownerService.getOwnerById(ownerId)).thenReturn(Optional.of(owner));
        doNothing().when(taskService).saveTask(task);

        String viewName = taskController.createTask(task, ownerId);

        assertEquals("redirect:/tasks", viewName);
        verify(taskService, times(1)).saveTask(task);
    }

    @Test
    public void testUpdateRemainingEffort() {
        Model model = mock(Model.class);
        Long taskId = 1L;
        Integer remainingEffort = 3;

        Task task = new Task();
        task.setId(taskId);
        task.setEstimatedHours(10);
        task.setRemainingEffort(5);

        when(taskService.updateRemainingEffort(taskId, remainingEffort)).thenReturn(task);

        String viewName = taskController.updateRemainingEffort(taskId, remainingEffort, model);

        assertEquals("taskDetail", viewName);
        verify(taskService, times(1)).updateRemainingEffort(taskId, remainingEffort);
    }

    @Test
    public void testDeleteTask() {
        Model model = mock(Model.class);
        Long taskId = 1L;

        doNothing().when(taskService).deleteTask(taskId);

        String viewName = taskController.deleteTask(taskId, model);

        assertEquals("redirect:/tasks", viewName);
        verify(taskService, times(1)).deleteTask(taskId);
    }

    @Test
    public void testAddComment() {
        Model model = mock(Model.class);
        Long taskId = 1L;
        String content = "New Comment";

        Task task = new Task();
        task.setId(taskId);

        when(taskService.getTaskById(taskId)).thenReturn(Optional.of(task));
        doNothing().when(commentService).addComment(any(Comment.class));

        String viewName = taskController.addComment(taskId, content, model);

        assertEquals("redirect:/tasks/" + taskId, viewName);
        verify(taskService, times(1)).getTaskById(taskId);
        verify(commentService, times(1)).addComment(any(Comment.class));
    }
}
