package com.skndan.rdp.resource;

import java.util.Optional;
import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.skndan.rdp.config.EntityCopyUtils;
import com.skndan.rdp.entity.ProfileGroup;
import com.skndan.rdp.entity.Profile;
import com.skndan.rdp.exception.GenericException;
import com.skndan.rdp.model.GroupProfile;
import com.skndan.rdp.repo.GroupRepo;
import com.skndan.rdp.repo.ProfileRepo;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/v1/group")
@Authenticated
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Group", description = "Group Endpoints")
public class GroupResource {

  @Inject
  GroupRepo groupRepo;

  @Inject
  ProfileRepo profileRepo;

  @Inject
  EntityCopyUtils entityCopyUtils;

  @GET
  public Response list(
      @QueryParam("pageNo") @DefaultValue("0") int pageNo,
      @QueryParam("pageSize") @DefaultValue("25") int pageSize,
      @QueryParam("sortField") @DefaultValue("name") String sortField,
      @QueryParam("sortDir") @DefaultValue("ASC") String sortDir) {

    Sort sortSt = sortDir.equals("DESC") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();

    Page<ProfileGroup> user = groupRepo.findAllByActive(true, PageRequest.of(pageNo, pageSize, sortSt));
    return Response.ok(user).status(200).build();
  }

  @GET
  @Path("/get-profile/{groupId}")
  public Response listFilter(
      @PathParam("groupId") UUID groupId,
      @QueryParam("pageNo") @DefaultValue("0") int pageNo,
      @QueryParam("pageSize") @DefaultValue("25") int pageSize,
      @QueryParam("sortField") @DefaultValue("firstName") String sortField,
      @QueryParam("sortDir") @DefaultValue("ASC") String sortDir) {

    // department:HR and salary>:50000

    Sort sortSt = sortDir.equals("DESC") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();

    Page<Profile> user = profileRepo.findAllByGroupIdAndActive(groupId, true,
        PageRequest.of(pageNo, pageSize, sortSt));

    return Response.ok(user).status(200).build();
  }

  @GET
  @Path("/{id}")
  public Response getByID(@PathParam("id") UUID id) {
    ProfileGroup group = groupRepo.findById(id)
        .orElseThrow(() -> new GenericException(400, "No profile with id " + id + " exists"));

    return Response.ok(group).status(200).build();
  }

  @POST
  @Transactional
  public Response add(ProfileGroup group) {
    ProfileGroup newGroup = groupRepo.save(group);
    return Response.ok(newGroup).status(201).build();
  }

  @DELETE
  @Path("/{id}")
  @Transactional
  public Response delete(@PathParam("id") UUID id) {
    ProfileGroup entity = groupRepo.findById(id)
        .orElseThrow(() -> new WebApplicationException("Group with id of " + id + " does not exist.", 404));
    entity.setActive(false);
    groupRepo.save(entity);
    return Response.status(204).build();
  }

  @PUT
  @Path("/{id}")
  public Response update(@PathParam("id") UUID id, ProfileGroup group) {
    Optional<ProfileGroup> optional = groupRepo.findById(id);

    if (optional.isPresent()) {
      ProfileGroup exGroup = optional.get();
      entityCopyUtils.copyProperties(exGroup, group);
      ProfileGroup updatedGroup = groupRepo.save(exGroup);
      return Response.ok(updatedGroup).status(200).build();
    }

    throw new IllegalArgumentException("No group with id " + id + " exists");
  }

  @POST
  @Path("/profile/{groupId}")
  public Response add(@PathParam("groupId") UUID groupId, GroupProfile groupProfile) {

    ProfileGroup group = groupRepo.findById(groupId)
        .orElseThrow(() -> new GenericException(400, "No group with id " + groupId + " exists"));

    Profile profile = profileRepo.findById(groupProfile.getProfileId())
        .orElseThrow(() -> new GenericException(400, "No profile with id " + groupProfile.getProfileId() + " exists"));

    if (groupProfile.isOptIn()) { // Add to group
      group.setProfiles(group.getProfiles() + 1);
      profile.setGroup(group);
    } else { // Remove from group
      group.setProfiles(group.getProfiles() - 1);
      profile.setGroup(null);
    }

    groupRepo.save(group);
    profileRepo.save(profile);

    return Response.ok().status(200).build();
  }

}