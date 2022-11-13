package net.test.perfect.number.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import net.test.perfect.number.exception.InvalidRequestException;
import net.test.perfect.number.service.PerfectNumberService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/perfect-number")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PerfectNumberController {

    private PerfectNumberService perfectService = new PerfectNumberService();

    @GET
    @Path("/validate/{number}")
    @Operation(summary = "This api checks if the requested number is a perfect number or not.",
            tags = {"perfectNumber"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Request processed"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad input")})
    public Response isPerfectNumber(@PathParam("number") Integer number) {
        if (number == null || Integer.signum(number) < 0) {
            throw new InvalidRequestException("Invalid requested parameters");
        }
        boolean result = perfectService.checkForPerfectNumber(number);
        return Response.status(Response.Status.OK)
                .entity(result)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }


    @GET
    @Path("/listAll")
    @Operation(summary = "This api lists all the perfect number in the given range",
            tags = {"perfectNumber"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Request processed"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad input")})
    public Response listAllPerfectNumbers(@QueryParam("from") Integer from,
                                          @QueryParam("to") Integer to) {

        if (from == null || to == null || Integer.signum(from) < 0 || Integer.signum(to) < 0) {
            throw new InvalidRequestException("Requested parameters are not valid ");
        }

        List<Integer> perfectNumberList = perfectService.listAllPerfectNumber(from, to);
        return Response.status(Response.Status.OK)
                .entity(perfectNumberList)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }


}
