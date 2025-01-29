package com.skndan.rdp.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DashboardResponse {

  private long learners;
  
  private long groups;
  
  private long trainers;

  private long instances;

}
