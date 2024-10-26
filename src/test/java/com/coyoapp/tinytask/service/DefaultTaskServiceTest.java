package com.coyoapp.tinytask.service;

import com.coyoapp.tinytask.domain.Task;
import com.coyoapp.tinytask.dto.TaskRequest;
import com.coyoapp.tinytask.dto.TaskResponse;
import com.coyoapp.tinytask.exception.TaskNotFoundException;
import com.coyoapp.tinytask.repository.TaskRepository;
import com.coyoapp.tinytask.service.mapper.TaskMapper;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultTaskServiceTest {


  @Mock
  private TaskRepository taskRepository;

  @Mock
  private TaskMapper taskMapper;

  @InjectMocks
  private DefaultTaskService objectUnderTest;

  @Test
  void shouldCreateTask() {
    // given
    TaskRequest taskRequest = mock(TaskRequest.class);
    Task task = mock(Task.class);
    Task savedTask = mock(Task.class);
    TaskResponse taskResponse = mock(TaskResponse.class);
    doReturn(task).when(taskMapper).toTask(taskRequest);
    when(taskRepository.save(task)).thenReturn(savedTask);
    doReturn(taskResponse).when(taskMapper).toResponse(savedTask);

    // when
    TaskResponse actualResponse = objectUnderTest.createTask(taskRequest);

    // then
    assertThat(actualResponse).isEqualTo(taskResponse);
  }

  @Test
  void shouldGetTasks() {
    // given
    Task task = mock(Task.class);
    TaskResponse taskResponse = mock(TaskResponse.class);
    when(taskRepository.findAll()).thenReturn(List.of(task));
    when(taskMapper.toResponse(task)).thenReturn(taskResponse);

    // when
    List<TaskResponse> actualTasks = objectUnderTest.getTasks();

    // then
    assertThat(actualTasks).contains(taskResponse);
  }

  @Test
  void shouldDeleteTask() {
    // given
    String id = "task-id";
    Task task = mock(Task.class);
    when(taskRepository.findById(id)).thenReturn(Optional.of(task));

    // when
    objectUnderTest.deleteTask(id);

    // then
    verify(taskRepository).delete(task);
  }

  @Test
  void shouldNotDeleteTask() {
    // given
    String id = "task-id";
    when(taskRepository.findById(id)).thenReturn(Optional.empty());

    // when
    Throwable thrown = catchThrowable(() -> objectUnderTest.deleteTask(id));

    // then
    assertThat(thrown).isInstanceOf(TaskNotFoundException.class);
  }

  @Test
  void shouldFindAllTasksOrderedByDueDate() {
    // given
    Task task1 = mock(Task.class);
    Task task2 = mock(Task.class);
    TaskResponse response1 = mock(TaskResponse.class);
    TaskResponse response2 = mock(TaskResponse.class);

    // Set up the repository to return the list of tasks ordered by due date
    when(taskRepository.findAllByOrderByDueDateAscDueDateIsNull()).thenReturn(List.of(task1, task2));
    when(taskMapper.toResponse(task1)).thenReturn(response1);
    when(taskMapper.toResponse(task2)).thenReturn(response2);

    // when
    List<TaskResponse> actualTasks = objectUnderTest.findAllByOrderByDueDateAsc();

    // then
    assertThat(actualTasks).containsExactly(response1, response2);
    verify(taskRepository, times(1)).findAllByOrderByDueDateAscDueDateIsNull(); // Verifies method call
    verify(taskMapper, times(1)).toResponse(task1); // Verifies mapping of each task
    verify(taskMapper, times(1)).toResponse(task2);
  }

  @Test
  void shouldFindTaskById() {
    // given
    String taskId = "task-id";
    Task task = mock(Task.class);

    // Mock the repository to return an Optional containing the task
    when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

    // when
    Optional<Task> actualTask = objectUnderTest.findById(taskId);

    // then
    assertThat(actualTask).isPresent().contains(task); // Verifies that the returned task is present and matches
    verify(taskRepository, times(1)).findById(taskId); // Verifies that the repository method was called once with the correct taskId
  }

  @Test
  void shouldReturnEmptyOptionalWhenTaskNotFound() {
    // given
    String taskId = "non-existent-task-id";

    // Mock the repository to return an empty Optional
    when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

    // when
    Optional<Task> actualTask = objectUnderTest.findById(taskId);

    // then
    assertThat(actualTask).isNotPresent(); // Verifies that an empty Optional is returned
    verify(taskRepository, times(1)).findById(taskId); // Verifies that the repository method was called once with the correct taskId
  }
}
