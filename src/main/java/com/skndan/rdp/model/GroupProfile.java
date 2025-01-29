package com.skndan.rdp.model;

import java.util.UUID;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class GroupProfile {
  private UUID profileId;
  private boolean optIn;
}