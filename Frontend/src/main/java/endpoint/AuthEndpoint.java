package endpoint;

import business.UserBusiness;
import helper.JwtUtil;
import helper.UserInfo;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/auth")
public class AuthEndpoint {

    private final UserBusiness userBusiness = new UserBusiness();

    /**
     * POST /api/auth/login
     * Body (form): username, password
     * Returns: { "token": "...", "username": "...", "role": "..." }
     */
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(
            @FormParam("username") String username,
            @FormParam("password") String password) {

        if (username == null || password == null ||
            username.trim().isEmpty() || password.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(errorMap("Username and password are required."))
                           .build();
        }

        UserInfo user = userBusiness.login(username.trim(), password);

        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity(errorMap("Invalid username or password."))
                           .build();
        }

        String token = JwtUtil.generateToken(user.getUsername(), user.getRole());

        Map<String, Object> result = new HashMap<>();
        result.put("token",    token);
        result.put("username", user.getUsername());
        result.put("role",     user.getRole());
        result.put("userId",   user.getUserId());

        return Response.ok(result).build();
    }

    /**
     * POST /api/auth/register
     * Body (form): username, password, email
     * Returns: { "message": "Registration successful." }
     */
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(
            @FormParam("username") String username,
            @FormParam("password") String password,
            @FormParam("email")    String email) {

        if (username == null || password == null || email == null ||
            username.trim().isEmpty() || password.trim().isEmpty() || email.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(errorMap("Username, password, and email are required."))
                           .build();
        }

        boolean success = userBusiness.register(username.trim(), password, email.trim());

        if (!success) {
            return Response.status(Response.Status.CONFLICT)
                           .entity(errorMap("Registration failed. Username may already exist."))
                           .build();
        }

        Map<String, String> result = new HashMap<>();
        result.put("message", "Registration successful. You may now log in.");
        return Response.status(Response.Status.CREATED).entity(result).build();
    }

    /**
     * POST /api/auth/validate
     * Header: Authorization: Bearer <token>
     * Returns: { "valid": true, "username": "...", "role": "..." }
     */
    @POST
    @Path("/validate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response validate(@HeaderParam("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity(errorMap("No token provided."))
                           .build();
        }
        try {
            String token    = authHeader.substring(7);
            String username = JwtUtil.getUsernameFromToken(token);
            String role     = JwtUtil.getRoleFromToken(token);

            Map<String, Object> result = new HashMap<>();
            result.put("valid",    true);
            result.put("username", username);
            result.put("role",     role);
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity(errorMap("Token is invalid or expired."))
                           .build();
        }
    }

    private Map<String, String> errorMap(String message) {
        Map<String, String> err = new HashMap<>();
        err.put("error", message);
        return err;
    }
}
