package com.skndan.rdp.resource;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.skndan.rdp.model.DashboardResponse;
import com.skndan.rdp.service.DashboardService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/v1/dashboard")
@Authenticated
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Dashboard", description = "Dashboard Endpoints")
public class DashboardResource {

  @Inject
  DashboardService dashboardService;

  @GET
  @Transactional
  public Response getDashboard() {
    DashboardResponse dashboardResponse = dashboardService.getStats();
    return Response.ok(dashboardResponse).status(200).build();
  }

}