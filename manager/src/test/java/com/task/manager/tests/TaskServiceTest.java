package com.task.manager.tests;

import com.task.manager.model.Task;
import com.task.manager.model.Owner;
import com.task.manager.repository.TaskRepository;
import com.task.manager.service.TaskService;
import com.task.manager.service.OwnerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private OwnerService ownerService;

    @InjectMocks
    private TaskService taskService;

    @Test
    public void testCreateTaskWithValidData() {
        Task task = new Task();
        task.setDescription("New Task");
        task.setEstimatedHours(10);
        task.setRemainingEffort(5);

        Owner owner = mock(Owner.class);
        when(owner.getId()).thenReturn(1L);
        task.setOwner(owner);

        when(ownerService.getOwnerById(1L)).thenReturn(Optional.of(owner));
        doNothing().when(taskRepository).save(task);

        taskService.saveTask(task);

        verify(taskRepository, times(1)).save(task);
        assertEquals("New Task", task.getDescription());
        assertEquals(10, task.getEstimatedHours().intValue());
        assertEquals(5, task.getRemainingEffort().intValue());
        assertEquals(5, task.getCompletedHours().intValue());
        assertEquals(owner, task.getOwner());
    }

    @Test
    public void testUpdateRemainingEffort() {
        Task task = new Task();
        task.setId(1L);
        task.setEstimatedHours(10);
        task.setRemainingEffort(5);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).save(task);

        taskService.updateRemainingEffort(1L, 3);

        verify(taskRepository, times(1)).save(task);
        assertEquals(3, task.getRemainingEffort().intValue());
        assertEquals(7, task.getCompletedHours().intValue());
    }

    @Test
    public void testDeleteTask() {
        Task task = new Task();
        task.setId(1L);

        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetTasksByOwnerId() {
        Owner owner = mock(Owner.class);
        when(owner.getId()).thenReturn(1L);

        Task task1 = new Task();
        task1.setOwner(owner);
        task1.setId(1L);

        Task task2 = new Task();
        task2.setOwner(owner);
        task2.setId(2L);

        List<Task> tasks = Arrays.asList(task1, task2);

        when(taskRepository.findByOwnerId(1L, Sort.by(Sort.Direction.DESC, "id"))).thenReturn(tasks);

        List<Task> result = taskService.getTasksByOwnerId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(2L, result.get(0).getId().longValue());
        assertEquals(1L, result.get(1).getId().longValue());
    }

    @Test
    public void testGetAllTasksSortedByIdDesc() {
        Task task1 = new Task();
        task1.setId(1L);

        Task task2 = new Task();
        task2.setId(2L);

        List<Task> tasks = Arrays.asList(task2, task1); // Sorted in descending order

        when(taskRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))).thenReturn(tasks);

        List<Task> result = taskService.getAllTasks();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(2L, result.get(0).getId().longValue());
        assertEquals(1L, result.get(1).getId().longValue());
    }
}
