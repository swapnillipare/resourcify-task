package com.coyoapp.tinytask.service.mapper;

import org.springframework.stereotype.Component;

import com.coyoapp.tinytask.domain.Task;
import com.coyoapp.tinytask.dto.TaskRequest;
import com.coyoapp.tinytask.dto.TaskResponse;

import java.util.Base64;

@Component
public class TaskMapper {

  public Task toTask(TaskRequest request) {
    Task task = new Task();
    task.setName(request.getName());
    task.setDueDate(request.getDueDate());
    // Decode and set file data if present
    if (request.getFileData() != null) {
      task.setFileData(Base64.getDecoder().decode(request.getFileData()));
      task.setFileName(request.getFileName());
      task.setFileType(request.getFileType());
    }
    return task;
  }

  public TaskResponse toResponse(Task task) {
    return TaskResponse.builder()
      .id(task.getId())
      .name(task.getName())
      .dueDate(task.getDueDate())
      .fileData(task.getFileData() != null ? Base64.getEncoder().encodeToString(task.getFileData()) : null)
      .fileName(task.getFileName())
      .fileType(task.getFileType())
      .build();
  }

}
