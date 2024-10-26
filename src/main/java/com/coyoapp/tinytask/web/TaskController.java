package com.coyoapp.tinytask.web;

import com.coyoapp.tinytask.domain.Task;
import com.coyoapp.tinytask.dto.TaskRequest;
import com.coyoapp.tinytask.dto.TaskResponse;
import com.coyoapp.tinytask.service.TaskService;
import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@Slf4j
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
class TaskController {

  private final TaskService taskService;

  @PostMapping
  public TaskResponse createTask(@RequestBody @Valid TaskRequest taskRequest) {
    log.debug("createTask(createTask={})", taskRequest);
    return taskService.createTask(taskRequest);
  }

  @GetMapping
  public List<TaskResponse> getTasks() {
    log.debug("getTasks()");
    return taskService.findAllByOrderByDueDateAsc();
  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping(path = "/{taskId}")
  public void deleteTask(@PathVariable String taskId) {
    log.debug("deleteTask(taskId={})", taskId);
    taskService.deleteTask(taskId);
  }

  @GetMapping("/files/download/{taskId}")
  public ResponseEntity<String> generateDownloadUrl(@PathVariable String taskId) {
    Task task = taskService.findById(taskId).orElse(null);

    if (task == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
    }

    // Build the URL for downloading the file
    String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
      .path("/tasks/files/stream/" + task.getId())
      .toUriString();

    return ResponseEntity.ok(downloadUrl);
  }

  // Endpoint for streaming the file
  @GetMapping("/files/stream/{taskId}")
  public ResponseEntity<byte[]> downloadFile(@PathVariable String taskId) {
    Task task = taskService.findById(taskId).orElse(null);

    if (task == null || task.getFileData() == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    return ResponseEntity.ok()
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + task.getFileName() + "\"")
      .header(HttpHeaders.CONTENT_TYPE, task.getFileType())
      .body(task.getFileData());
  }
}
