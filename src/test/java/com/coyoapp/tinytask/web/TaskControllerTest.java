package com.coyoapp.tinytask.web;

import com.coyoapp.tinytask.domain.Task;
import com.coyoapp.tinytask.dto.TaskRequest;
import com.coyoapp.tinytask.dto.TaskResponse;
import com.coyoapp.tinytask.exception.TaskNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class TaskControllerTest extends BaseControllerTest {

  private static final String PATH = "/tasks";

  @Test
  void shouldCreateTask() throws Exception {
    // given
    String id = "task-id";
    String name = "task-name";
    TaskRequest taskRequest = TaskRequest.builder().name(name).build();
    TaskResponse taskResponse = TaskResponse.builder().id(id).name(name).build();
    when(taskService.createTask(taskRequest)).thenReturn(taskResponse);

    // when
    ResultActions actualResult = this.mockMvc.perform(post(PATH).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(taskRequest)));

    // then
    actualResult.andDo(print()).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.id", is(notNullValue()))).andExpect(jsonPath("$.name", is(name)));
  }

  @Test
  void shouldGetTasks() throws Exception {
    // given
    String id = "task-id";
    String name = "task-name";
    TaskResponse taskResponse = TaskResponse.builder().id(id).name(name).build();
    when(taskService.findAllByOrderByDueDateAsc()).thenReturn(Collections.singletonList(taskResponse));

    // when
    ResultActions actualResult = this.mockMvc.perform(get(PATH));

    // then
    actualResult.andDo(print()).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].id", is(notNullValue()))).andExpect(jsonPath("$[0].name", is(name)));
  }

  @Test
  void shouldDeleteTask() throws Exception {
    // given
    String id = "task-id";

    // when
    ResultActions actualResult = this.mockMvc.perform(delete(PATH + "/" + id));

    // then
    actualResult.andDo(print()).andExpect(status().isOk());

    verify(taskService).deleteTask(id);
  }

  @Test
  void shouldNotDeleteTask() throws Exception {
    // given
    String id = "unknown-task-id";
    doThrow(new TaskNotFoundException()).when(taskService).deleteTask(id);

    // when
    ResultActions actualResult = this.mockMvc.perform(delete(PATH + "/" + id));

    // then
    actualResult.andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  void shouldDownloadFileWhenTaskExistsWithFileData() throws Exception {
    // given
    String taskId = "task-id";
    String fileName = "example.txt";
    String fileType = "text/plain";
    byte[] fileData = "Sample file content".getBytes();

    // Create a task object directly using the constructor
    Task task = new Task();
    task.setId(taskId);
    task.setFileName(fileName);
    task.setFileType(fileType);
    task.setFileData(fileData);

    when(taskService.findById(taskId)).thenReturn(Optional.of(task));

    // when
    ResultActions actualResult = this.mockMvc.perform(get(PATH + "/files/stream/" + taskId));

    // then
    actualResult.andDo(print())
      .andExpect(status().isOk())
      .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\""))
      .andExpect(header().string(HttpHeaders.CONTENT_TYPE, fileType))
      .andExpect(content().bytes(fileData)); // Verify that the body matches the file data
  }

  @Test
  void shouldReturnNotFoundWhenTaskDoesNotExist() throws Exception {
    // given
    String taskId = "non-existent-task-id";
    when(taskService.findById(taskId)).thenReturn(Optional.empty());

    // when
    ResultActions actualResult = this.mockMvc.perform(get(PATH + "/files/stream/" + taskId));

    // then
    actualResult.andDo(print())
      .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnNotFoundWhenTaskHasNoFileData() throws Exception {
    // given
    String taskId = "task-id";
    Task task = new Task();
    task.setId(taskId);

    when(taskService.findById(taskId)).thenReturn(Optional.of(task));

    // when
    ResultActions actualResult = this.mockMvc.perform(get(PATH + "/files/stream/" + taskId));

    // then
    actualResult.andDo(print())
      .andExpect(status().isNotFound());
  }
}
