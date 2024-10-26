package com.coyoapp.tinytask.service;

import com.coyoapp.tinytask.domain.Task;
import com.coyoapp.tinytask.dto.TaskRequest;
import com.coyoapp.tinytask.dto.TaskResponse;
import com.coyoapp.tinytask.exception.TaskNotFoundException;
import com.coyoapp.tinytask.repository.TaskRepository;
import com.coyoapp.tinytask.service.mapper.TaskMapper;

import java.util.Base64;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultTaskService implements TaskService {

  private final TaskRepository taskRepository;
  private final TaskMapper taskMapper;

  @Override
  @Transactional
  public TaskResponse createTask(TaskRequest taskRequest) {
    log.debug("createTask(createTask={})", taskRequest);
    Task task = taskMapper.toTask(taskRequest);
    // Decode Base64 file data
    if (taskRequest.getFileData() != null) {
      task.setFileData(Base64.getDecoder().decode(taskRequest.getFileData()));
      task.setFileName(taskRequest.getFileName());
      task.setFileType(taskRequest.getFileType());
    }
    return transformToResponse(taskRepository.save(task));
  }

  @Override
  @Transactional(readOnly = true)
  public List<TaskResponse> getTasks() {
    log.debug("getTasks()");
    return taskRepository.findAll().stream().map(this::transformToResponse).collect(toList());
  }

  private TaskResponse transformToResponse(Task task) {
    return taskMapper.toResponse(task);
  }

  @Override
  @Transactional
  public void deleteTask(String taskId) {
    log.debug("deleteTask(taskId={})", taskId);
    taskRepository.delete(getTaskOrThrowException(taskId));
  }

  @Override
  public List<TaskResponse> findAllByOrderByDueDateAsc() {
    log.debug("findAllByOrderByDueDateAsc()");
    return taskRepository.findAllByOrderByDueDateAscDueDateIsNull().stream().map(this::transformToResponse).collect(toList());
  }

  private Task getTaskOrThrowException(String taskId) {
    return taskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
  }

}
