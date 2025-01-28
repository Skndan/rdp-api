package com.skndan.rdp.resource;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.skndan.rdp.client.GuacamoleService;
import com.skndan.rdp.entity.Instance;
import com.skndan.rdp.model.guacamole.Connection;
import com.skndan.rdp.service.integration.AwsService;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/v1/test")
@PermitAll
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Test", description = "Test Endpoints")
public class TestResource {

  @Inject
  GuacamoleService awsService;

  @Inject
  AwsService awsService2;

  @POST
  @Path("/instance")
  public Response getByID(Connection instance) { 
    // Connection connection = awsService.createConnection(instance);
    return Response.ok().status(200).build();
  }

  @GET
  @Path("/guacamole")
  public Response getByID() { 
    // Connection connection = awsService.createConnection(instance);
    String ss = awsService.authenticate();
    return Response.ok(ss).status(200).build();
  }

  
  @GET
  @Path("/base64")
  public Response encoder() { 
    // Connection connection = awsService.createConnection(instance);

    // String ss = awsService2.convertBase64("14\0c\0postgresql");
    return Response.ok("ss").status(200).build();
  }
}