package com.coyoapp.tinytask.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;

@Table(name = "task")
@Entity
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Task {

  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(name = "id", nullable = false, updatable = false)
  private String id;

  private String name;

  @CreatedDate
  private Instant created;

  // task due date
  @Column(name = "due_date")
  private LocalDateTime dueDate;

  // file content in binary format
  @Lob
  @Column(name = "file_data")
  private byte[] fileData;

  // file name for reference
  @Column(name = "file_name")
  private String fileName;

  // file type (e.g., application/pdf, image/png)
  @Column(name = "file_type")
  private String fileType;
}
