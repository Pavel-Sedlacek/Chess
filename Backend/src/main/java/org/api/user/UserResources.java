package org.api.user;


import org.data.dao.ChallengeDAO;
import org.data.entities.Challenge;
import org.data.entities.User;
import org.managers.GameManager;
import org.managers.UserApiManager;
import org.utils.ResponseMessage;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResources {

    @Inject
    UserApiManager apiManager;

    @Inject
    ChallengeDAO challengeDAO;
    @Inject
    GameManager gameManager;
    @Inject
    LoggedUser loggedUser;

    @GET
    @Path("{id}")
    public Response getUser(@PathParam("id") int id) {
        try {
            User u = apiManager.getUserById(id);
            if (u == null) {
                return Response.status(404).entity(new ResponseMessage("User doesn't exist")).build();
            }
            return Response.status(200).entity(u).build();
        } catch (Exception e) {
            return Response.status(400).entity(new ResponseMessage(e.toString())).build();
        }
    }

    //region user/authentication
    @GET
    @Path("authentication")
    public Response getLoggedUser() {
        try {
            if (loggedUser.getLoggedUser() != null)
                return Response.status(200).entity(loggedUser.getLoggedUser()).build();
            else
                return Response.status(404).entity(new ResponseMessage("User is not logged in")).build();
        } catch (NullPointerException e) {
            return Response.status(400).entity(new ResponseMessage(e.toString())).build();
        }
    }

    @POST
    @Path("authentication/new")
    public Response createUser(User u) {
        try {
            if (apiManager.isUserWithName(u.getUsername())) {
                return Response.status(409).entity(new ResponseMessage("Name already in use")).build();
            }
            return Response.status(200).entity(apiManager.createUser(u)).build();
        } catch (Exception e) {
            return Response.status(400).entity(new ResponseMessage(e.toString())).build();
        }
    }

    @POST
    @Path("authentication")
    public Response logUser(User user) {
        try {
            if (apiManager.isValidUser(user)) {
                loggedUser.setLoggedUser(apiManager.getNormalizedUser(user).getId());
                return Response.status(200).entity(new ResponseMessage("Logged in")).build();
            }
        } catch (Exception e) {
            return Response.status(400).entity(new ResponseMessage(e.toString())).build();
        }
        return Response.status(420).entity(new ResponseMessage("You WATAFAK")).build();
    }

    @DELETE
    @Path("authentication")
    public Response signOut() {
        try {
            if (this.loggedUser.isLogged()) {
                loggedUser.setLoggedUser(null);
                return Response.status(200).entity(new ResponseMessage("user signed out")).build();
            }
            return Response.status(404).entity(new ResponseMessage("No user is logged in")).build();
        } catch (Exception e) {
            return Response.status(400).entity(new ResponseMessage(e.toString())).build();
        }
    }

    //endregion

    //region user/settings
    @DELETE
    @Path("delete")
    public Response deleteUser(User u) {
        if (!this.loggedUser.isLogged()) {
            return Response.status(400).entity(new ResponseMessage("You must be logged in order to delete user")).build();
        }
        try {
            if (apiManager.isValidUser(u)) {
                return Response.status(200).entity(apiManager.deleteUser(u)).build();
            }
            return Response.status(404).entity(new ResponseMessage("User doesn't exist")).build();
        } catch (Exception e) {
            return Response.status(400).entity("Something went wrong").build();
        }
    }

    @PUT
    @Path("password")
    public Response changeUserPassword(User u) {
        if (!this.loggedUser.isLogged()) {
            return Response.status(400).entity(new ResponseMessage("You must be logged in order to change password")).build();
        }
        try {
            if (apiManager.idMatchesUsername(loggedUser.getLoggedUser().getId(), u)) {
                return Response.status(200).entity(apiManager.changeUserPassword(loggedUser.getLoggedUser(), u)).build();
            }
            return Response.status(404).entity(new ResponseMessage("User doesn't exist")).build();
        } catch (Exception e) {
            return Response.status(400).entity("Something went wrong").build();
        }
    }

    @PUT
    @Path("username")
    public Response changeUserName(User u) {
        if (!this.loggedUser.isLogged()) {
            return Response.status(400).entity(new ResponseMessage("You must be logged in order to change username")).build();
        }
        try {
            if (apiManager.idMatchesPassword(this.loggedUser.getLoggedUser().getId(), u)) {
                return Response.status(200).entity(this.apiManager.changeUserName(this.loggedUser.getLoggedUser(), u)).build();
            }
            return Response.status(404).entity(new ResponseMessage("User doesn't exist")).build();
        } catch (Exception e) {
            return Response.status(400).entity("Something went wrong").build();
        }
    }
    //endregion

    @DELETE
    @Path("challenge/{id}/del")
    public Response revokeChallenge() {
        //TODO revoke/decline challenge
        return Response.status(400).entity(new ResponseMessage("NOT IMPLEMENTED YET!")).build();
    }

    //region challenge
    @GET
    @Path("challenge")
    public Response getChallenges() {
        if (!this.loggedUser.isLogged())
            return Response.status(404).entity(new ResponseMessage("You must be logged in order to view challenges")).build();
        try {
            return Response.status(200).entity(challengeDAO.getChallengesForUser(this.loggedUser.getLoggedUser())).build();
        } catch (Exception e) {
            return Response.status(400).entity(new ResponseMessage(e.toString())).build();
        }
    }

    @PUT
    @Path("challenge/{id}")
    public Response acceptChallenge(@PathParam("id") long id) {
        if (!this.loggedUser.isLogged())
            return Response.status(404).entity(new ResponseMessage("You must be logged in order to accept challenges")).build();
        if (challengeDAO.get(id) == null)
            return Response.status(404).entity(new ResponseMessage("No such challenge")).build();
        if (this.challengeDAO.get(id).isAccepted()) {
            return Response.status(200).entity(new ResponseMessage("Challenge already accepted")).build();
        }
        try {
            challengeDAO.acceptChallenge(id);
            gameManager.matchChallenges();
            return Response.status(200).entity(new ResponseMessage("Challenge accepted successfully")).build();
        } catch (Exception e) {
            return Response.status(400).entity(new ResponseMessage(e.toString())).build();
        }
    }

    @POST
    @Path("{id}/challenge")
    public Response createChallenge(@PathParam("id") int idChallenged) {
        if (!this.loggedUser.isLogged())
            return Response.status(404).entity(new ResponseMessage("You must be logged in order to challenge someone")).build();
        if (this.apiManager.getUserById(idChallenged) == null)
            return Response.status(404).entity(new ResponseMessage("No such player")).build();
        if (idChallenged == this.loggedUser.getLoggedUser().getId()) {
            return Response.status(400).entity(new ResponseMessage("Cannot challenge yourself")).build();
        }
        try {
            challengeDAO.save(new Challenge(this.loggedUser.getLoggedUser(), apiManager.getUserById(idChallenged)));
            return Response.status(200).entity(new ResponseMessage("Player challenged successfully")).build();
        } catch (Exception e) {
            return Response.status(400).entity(new ResponseMessage(e.toString())).build();
        }
    }
    //endregion
}
