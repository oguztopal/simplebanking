package com.assignment.simplebanking.base;

import com.assignment.simplebanking.type.Status;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity  implements Serializable{

  @Column(name = "status", columnDefinition="VARCHAR")
  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name = "created_at", columnDefinition = "TIMESTAMP", updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
  private LocalDateTime updatedAt;


  @PrePersist
  public void onPrePersist() {
    setStatus(Status.ACTIVE);
    setCreatedAt(LocalDateTime.now());
  }

  @PreUpdate
  public void onPreUpdate() {
    setUpdatedAt(LocalDateTime.now());
  }
}
