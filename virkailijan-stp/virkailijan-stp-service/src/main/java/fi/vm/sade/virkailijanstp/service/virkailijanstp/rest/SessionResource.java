package fi.vm.sade.virkailijanstp.service.virkailijanstp.rest;


import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;


@Component
@Path("session")
public class SessionResource {

    @GET
    @Path("/maxinactiveinterval")
    @PreAuthorize("isAuthenticated()")
    @Produces(MediaType.TEXT_PLAIN)
    public String maxInactiveInterval(@Context HttpServletRequest req) {
        return Integer.toString(req.getSession().getMaxInactiveInterval());
    }
}
