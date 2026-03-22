package endpoint;

import business.GameBusiness;
import helper.GameInfo;
import helper.JwtUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Path("/games")
public class SearchGamesEndpoint {

    private final GameBusiness gameBusiness = new GameBusiness();

    /**
     * Search games by date, team name, or player name.
     * GET /api/games/search?type=date&value=2026-01-19
     * GET /api/games/search?type=team&value=Warriors
     * GET /api/games/search?type=player&value=Curry
     *
     * Authorization header is optional — results are returned either way,
     * but an invalid token is rejected.
     */
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchGames(
            @QueryParam("type")  String type,
            @QueryParam("value") String value,
            @HeaderParam("Authorization") String authHeader) {

        boolean authenticated = false;
        String  loggedInUser  = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7);
                loggedInUser  = JwtUtil.getUsernameFromToken(token);
                authenticated = true;
            } catch (Exception e) {
                return Response.status(Response.Status.UNAUTHORIZED)
                               .entity(errorMap("Invalid or expired token."))
                               .build();
            }
        }

        if (type == null || value == null || value.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(errorMap("Query params 'type' and 'value' are required."))
                           .build();
        }

        type  = type.trim().toLowerCase();
        value = value.trim();

        ArrayList<GameInfo> games;

        try {
            switch (type) {
                case "date":
                    games = gameBusiness.searchByDate(value);
                    break;
                case "team":
                    // value is now a team name string, not a numeric ID
                    games = gameBusiness.searchByTeam(value);
                    break;
                case "player":
                    // value is now a player name string, not a numeric ID
                    games = gameBusiness.searchByPlayer(value);
                    break;
                default:
                    return Response.status(Response.Status.BAD_REQUEST)
                                   .entity(errorMap("Invalid type. Use: date, team, or player."))
                                   .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity(errorMap("Error querying database: " + e.getMessage()))
                           .build();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("authenticated", authenticated);
        result.put("user", loggedInUser);
        result.put("count", games.size());
        result.put("games", games);

        return Response.ok(result).build();
    }

    @GET
    @Path("/health")
    @Produces(MediaType.APPLICATION_JSON)
    public Response health() {
        Map<String, String> status = new HashMap<>();
        status.put("service", "SearchGames");
        status.put("status", "UP");
        return Response.ok(status).build();
    }

    private Map<String, String> errorMap(String message) {
        Map<String, String> err = new HashMap<>();
        err.put("error", message);
        return err;
    }
}
