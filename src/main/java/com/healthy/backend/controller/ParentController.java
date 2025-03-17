package com.healthy.backend.controller;


import com.healthy.backend.dto.user.UsersResponse;
import com.healthy.backend.exception.OperationFailedException;
import com.healthy.backend.security.TokenService;
import com.healthy.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/parents")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "User Controller", description = "Users related management APIs")
public class ParentController {

    private final UserService userService;
    private final TokenService tokenService;

    @Operation(
            summary = "Get user by ID",
            description = "Returns the user details with the specified ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/details")
    public ResponseEntity<UsersResponse> getUserDetailsById(
            @RequestParam(required = false) String userId,
            HttpServletRequest request) {
        userId = tokenService.validateRequestUserID(request, userId);

        if (tokenService.validateUID(request, userId)
                && !tokenService.isManager(request) && !tokenService.isParent(request)) {
            throw new OperationFailedException("You can not get other users details");
        }
        UsersResponse user = userService.getUserDetailsById(userId);
        return user == null
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(user);
    }
}
