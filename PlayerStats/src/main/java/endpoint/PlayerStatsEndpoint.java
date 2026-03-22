package endpoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import business.StatsBusiness;
import helper.JwtUtil;
import helper.PlayerStatsInfo;

@Path("/stats")
public class PlayerStatsEndpoint {

    private final StatsBusiness statsBusiness = new StatsBusiness();

    /**
     * GET /api/stats?homeTeam=Warriors              — all players in game(s) with this home team
     * GET /api/stats?awayTeam=Heat                  — all players in game(s) with this away team
     * GET /api/stats?homeTeam=Nets&awayTeam=Celtics — players in that specific matchup
     * GET /api/stats?player=Curry                   — all games for a player (by name)
     *
     * Requires: Authorization: Bearer <token>
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStats(
            @QueryParam("homeTeam") String homeTeam,
            @QueryParam("awayTeam") String awayTeam,
            @QueryParam("player")   String playerName,
            @HeaderParam("Authorization") String authHeader) {

        // JWT is REQUIRED
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity(errorMap("Authentication required. Please log in."))
                           .build();
        }

        String loggedInUser;
        try {
            String token = authHeader.substring(7);
            loggedInUser = JwtUtil.getUsernameFromToken(token);
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity(errorMap("Invalid or expired token."))
                           .build();
        }

        boolean hasHome   = homeTeam   != null && !homeTeam.trim().isEmpty();
        boolean hasAway   = awayTeam   != null && !awayTeam.trim().isEmpty();
        boolean hasPlayer = playerName != null && !playerName.trim().isEmpty();

        if (!hasHome && !hasAway && !hasPlayer) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(errorMap("Provide at least one of: homeTeam, awayTeam, or player."))
                           .build();
        }

        String title;
        Map<String, Object> result = new HashMap<>();
        result.put("user", loggedInUser);

        try {
            if (hasHome || hasAway) {
                String ht = hasHome ? homeTeam.trim() : "";
                String at = hasAway ? awayTeam.trim() : "";
                if (hasHome && hasAway) {
                    title = "Stats: " + ht + " (home) vs " + at + " (away)";
                } else if (hasHome) {
                    title = "Stats for home team: " + ht;
                } else {
                    title = "Stats for away team: " + at;
                }
                ArrayList<PlayerStatsInfo> stats = statsBusiness.getStatsForTeams(ht, at);
                result.put("title", title);
                result.put("count", stats.size());
                result.put("stats", stats);

            } else {
                title = "All stats for player: " + playerName;
                ArrayList<PlayerStatsInfo> stats = statsBusiness.getStatsForPlayerName(playerName.trim());
                result.put("title", title);
                result.put("count", stats.size());
                result.put("stats", stats);
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity(errorMap("Database error: " + e.getMessage()))
                           .build();
        }

        return Response.ok(result).build();
    }

    @GET
    @Path("/health")
    @Produces(MediaType.APPLICATION_JSON)
    public Response health() {
        Map<String, String> status = new HashMap<>();
        status.put("service", "PlayerStats");
        status.put("status", "UP");
        return Response.ok(status).build();
    }

    /**
     * Returns all games for the frontend dropdown.
     * GET /api/stats/games
     * Requires: Authorization: Bearer <token>
     */
    @GET
    @Path("/games")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGames(@HeaderParam("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(errorMap("Authentication required."))
                        .build();
        }
        try {
            JwtUtil.getUsernameFromToken(authHeader.substring(7));
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(errorMap("Invalid or expired token."))
                        .build();
        }
        java.util.ArrayList<helper.GameInfo> games = statsBusiness.getAllGames();
        Map<String, Object> result = new HashMap<>();
        result.put("games", games);
        return Response.ok(result).build();
    }

    private Map<String, String> errorMap(String message) {
        Map<String, String> err = new HashMap<>();
        err.put("error", message);
        return err;
    }
}
