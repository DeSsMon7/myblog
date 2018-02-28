/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ash.myblog23.restfulservice;

import com.ash.myblog23.control.UserFacade;
import com.ash.myblog23.model.User;
import com.ash.myblog23.pres.UserController;
import java.io.IOException;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 *
 * @author AsH
 */
@Path("recovery")
@Stateless
public class RecoveryService {

    @Inject
    UserFacade userFacade;

    @GET
    @Path("crypted")
    @Produces("text/html")
    public Response recoverPassword(@QueryParam("userlogin") String username, @QueryParam("hash") String oldHash, @Context HttpServletRequest request, @Context HttpServletResponse response)
            throws IOException {
        String myRecoverPage = "/faces/welcome.xhtml";
        String myWelcomePage = "/faces/welcome.xhtml";
        String contextPath = request.getContextPath();
        List<User> userList = userFacade.findByName(username);
        if (!userList.isEmpty()) {
            if (userList.get(0).getPassword().equals(oldHash)) {
                UserController.setSelectedForRecovery();
                UserController.setRecoveryOption(username);
                response.sendRedirect(contextPath + myRecoverPage);
            } else {
//              response.sendRedirect(contextPath + myRecoverPage);
                response.sendRedirect(contextPath + myWelcomePage);
            }
        }
        return Response.status(Response.Status.ACCEPTED).build();
    }

}
