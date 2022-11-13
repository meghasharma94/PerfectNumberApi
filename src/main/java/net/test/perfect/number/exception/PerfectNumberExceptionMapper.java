package net.test.perfect.number.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class PerfectNumberExceptionMapper implements ExceptionMapper<PerfectNumberException> {
    @Override
    public Response toResponse(PerfectNumberException e) {
        if(e instanceof InvalidRequestException){
            return buildErrorResponse(e.getMessage(), Response.Status.BAD_GATEWAY);
        }
        if(e instanceof UnauthorizedException){
            return buildErrorResponse(e.getMessage(), Response.Status.UNAUTHORIZED);
        }
        return buildErrorResponse(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
    }

    private Response buildErrorResponse(String message, Response.Status status) {
        return Response.status(status)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .entity(new ErrorResponseEntity(message)).build();
    }

}
