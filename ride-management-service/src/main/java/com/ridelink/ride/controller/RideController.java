package com.ridelink.ride.controller;

import com.ridelink.ride.dto.AssignDriverRequest;
import com.ridelink.ride.dto.CreateRideRequest;
import com.ridelink.ride.dto.RideResponse;
import com.ridelink.ride.dto.external.AvailableDriverResponse;
import com.ridelink.ride.exception.ApiErrorResponse;
import com.ridelink.ride.service.RideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
@Tag(name = "Ride Management", description = "Operations for creating rides, assigning drivers, and managing the ride lifecycle")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    // ─── Create Ride ──────────────────────────────────────────────────────────

    @Operation(
            summary = "Create a new ride request",
            description = "Creates a new ride in REQUESTED status. A driver is not assigned at this point."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ride created successfully",
                    content = @Content(schema = @Schema(implementation = RideResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body or validation failure",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<RideResponse> createRide(@Valid @RequestBody CreateRideRequest request) {
        RideResponse response = rideService.createRide(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ─── Get Ride by ID ───────────────────────────────────────────────────────

    @Operation(
            summary = "Get a ride by ID",
            description = "Retrieves the full details of a specific ride by its unique identifier."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ride found",
                    content = @Content(schema = @Schema(implementation = RideResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ride not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<RideResponse> getRideById(
            @Parameter(description = "Unique ride ID", required = true)
            @PathVariable String id) {
        RideResponse response = rideService.getRideById(id);
        return ResponseEntity.ok(response);
    }

    // ─── Manual Driver Assignment ──────────────────────────────────────────────

    @Operation(
            summary = "Manually assign a driver to a ride",
            description = "Assigns a specific driver (by ID) to a ride. The ride must be in REQUESTED status. Transitions the ride to ASSIGNED."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Driver assigned successfully",
                    content = @Content(schema = @Schema(implementation = RideResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status transition or validation failure",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ride not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PatchMapping("/{id}/assign-driver")
    public ResponseEntity<RideResponse> assignDriver(
            @Parameter(description = "Unique ride ID", required = true)
            @PathVariable String id,
            @Valid @RequestBody AssignDriverRequest request) {
        RideResponse response = rideService.assignDriver(id, request);
        return ResponseEntity.ok(response);
    }

    // ─── Get Available Drivers ────────────────────────────────────────────────

    @Operation(
            summary = "List all available drivers",
            description = "Retrieves available drivers from the Driver & Vehicle Service. Used to support manual or automatic driver assignment."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Available drivers returned",
                    content = @Content(schema = @Schema(implementation = AvailableDriverResponse.class))),
            @ApiResponse(responseCode = "503", description = "Driver & Vehicle Service is unavailable",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/available-drivers")
    public ResponseEntity<List<AvailableDriverResponse>> getAvailableDrivers() {
        List<AvailableDriverResponse> responses = rideService.getAvailableDrivers();
        return ResponseEntity.ok(responses);
    }

    // ─── Automatic Driver Assignment ──────────────────────────────────────────

    @Operation(
            summary = "Automatically assign an available driver to a ride",
            description = "Queries the Driver & Vehicle Service for available drivers and assigns the first one returned. The ride must be in REQUESTED status. Transitions the ride to ASSIGNED."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Driver auto-assigned successfully",
                    content = @Content(schema = @Schema(implementation = RideResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status transition",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ride not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "No available driver found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "Driver & Vehicle Service is unavailable",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PatchMapping("/{id}/auto-assign-driver")
    public ResponseEntity<RideResponse> autoAssignDriver(
            @Parameter(description = "Unique ride ID", required = true)
            @PathVariable String id) {
        RideResponse response = rideService.autoAssignDriver(id);
        return ResponseEntity.ok(response);
    }

    // ─── Accept Ride ──────────────────────────────────────────────────────────

    @Operation(
            summary = "Accept a ride",
            description = "Marks a ride as accepted by the assigned driver. The ride must be in ASSIGNED status. Transitions the ride to ACCEPTED."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ride accepted successfully",
                    content = @Content(schema = @Schema(implementation = RideResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status transition or no driver assigned",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ride not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PatchMapping("/{id}/accept")
    public ResponseEntity<RideResponse> acceptRide(
            @Parameter(description = "Unique ride ID", required = true)
            @PathVariable String id) {
        RideResponse response = rideService.acceptRide(id);
        return ResponseEntity.ok(response);
    }

    // ─── Start Ride ───────────────────────────────────────────────────────────

    @Operation(
            summary = "Start a ride",
            description = "Marks a ride as in progress. The ride must be in ACCEPTED status. Transitions the ride to IN_PROGRESS."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ride started successfully",
                    content = @Content(schema = @Schema(implementation = RideResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status transition",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ride not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PatchMapping("/{id}/start")
    public ResponseEntity<RideResponse> startRide(
            @Parameter(description = "Unique ride ID", required = true)
            @PathVariable String id) {
        RideResponse response = rideService.startRide(id);
        return ResponseEntity.ok(response);
    }

    // ─── Complete Ride ────────────────────────────────────────────────────────

    @Operation(
            summary = "Complete a ride",
            description = "Marks a ride as completed. The ride must be in IN_PROGRESS status. Transitions the ride to COMPLETED (terminal state)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ride completed successfully",
                    content = @Content(schema = @Schema(implementation = RideResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status transition",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ride not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PatchMapping("/{id}/complete")
    public ResponseEntity<RideResponse> completeRide(
            @Parameter(description = "Unique ride ID", required = true)
            @PathVariable String id) {
        RideResponse response = rideService.completeRide(id);
        return ResponseEntity.ok(response);
    }

    // ─── Cancel Ride ──────────────────────────────────────────────────────────

    @Operation(
            summary = "Cancel a ride",
            description = "Cancels a ride. The ride must be in REQUESTED, ASSIGNED, or ACCEPTED status. Transitions the ride to CANCELLED (terminal state)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ride cancelled successfully",
                    content = @Content(schema = @Schema(implementation = RideResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status transition (e.g. ride is already COMPLETED or CANCELLED)",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ride not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<RideResponse> cancelRide(
            @Parameter(description = "Unique ride ID", required = true)
            @PathVariable String id) {
        RideResponse response = rideService.cancelRide(id);
        return ResponseEntity.ok(response);
    }
}
