package com.coyoapp.tinytask.repository;

import com.coyoapp.tinytask.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, String> {
  @Query("SELECT t FROM Task t ORDER BY CASE WHEN t.dueDate IS NULL THEN 1 ELSE 0 END, t.dueDate ASC")
  List<Task> findAllByOrderByDueDateAscDueDateIsNull(); // Sort by due date
}
