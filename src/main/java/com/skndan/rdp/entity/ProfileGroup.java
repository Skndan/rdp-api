package com.skndan.rdp.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ProfileGroup extends BaseEntity {
  
  @NotBlank(message = "Group name should be present")
  private String name;
    
  @NotBlank(message = "Group description should be present")
  private String description;

  private int profiles = 0;
}
