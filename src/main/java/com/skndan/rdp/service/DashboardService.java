package com.skndan.rdp.service;

import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.skndan.rdp.entity.Profile;
import com.skndan.rdp.model.DashboardResponse;
import com.skndan.rdp.repo.GroupRepo;
import com.skndan.rdp.repo.InstanceRepo;
import com.skndan.rdp.repo.ProfileRepo;
import com.skndan.rdp.service.keycloak.KeycloakService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DashboardService {

  @Inject
  ProfileRepo profileRepo;

  @Inject
  InstanceRepo instanceRepo;

  @Inject
  KeycloakService keycloakService;

  @Inject
  GroupRepo groupRepo;

  @Transactional
  public DashboardResponse getStats() {
    DashboardResponse response = new DashboardResponse();

    long instances = instanceRepo.count();
    long learners = getProfileCount("student");
    long trainers = getProfileCount("instructor");
    long groups = groupRepo.count();

    response.setGroups(groups);
    response.setInstances(instances);
    response.setLearners(learners);
    response.setTrainers(trainers);

    return response;
  }

  private long getProfileCount(String roleName) {

    Sort sortSt = Sort.by("createdAt").descending();

    RoleRepresentation roleRepresentation = keycloakService.findRoleByName(roleName);

    Page<Profile> user = profileRepo.findAllByRoleIdAndActive(roleRepresentation.getId(), true,
        PageRequest.of(0, 100, sortSt));

    return user.getTotalElements();
  }

}
