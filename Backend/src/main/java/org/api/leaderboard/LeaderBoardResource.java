package org.api.leaderboard;

import org.managers.LeaderBoardManager;
import org.utils.ResponseMessage;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("lidlboard")
@Produces(MediaType.APPLICATION_JSON)
public class LeaderBoardResource {

    @Inject
    LeaderBoardManager leaderboardManager;

    @GET
    @Path("top-50")
    public Response getLeaderboardTop50() {
        try {
            return Response.status(200).entity(leaderboardManager.getLeaderboardTop50()).build();
        } catch (Exception e) {
            return Response.status(400).entity(new ResponseMessage(e.toString())).build();
        }
    }

    @GET
    @Path("pleb-50")
    public Response getLeaderboardPleb50() {
        try {
            return Response.status(200).entity(leaderboardManager.getLeaderboardPleb50()).build();
        } catch (Exception e) {
            return Response.status(400).entity(new ResponseMessage(e.toString())).build();
        }
    }

}
