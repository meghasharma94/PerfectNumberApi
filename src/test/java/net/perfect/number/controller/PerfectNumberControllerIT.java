package net.perfect.number.controller;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.ServletDeploymentContext;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerFactory;

import org.junit.jupiter.api.TestInstance;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PerfectNumberControllerIT extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(PerfectNumberController.class);
    }

    @Override
    protected TestContainerFactory getTestContainerFactory() {
        return new GrizzlyWebTestContainerFactory();
    }

    @Override
    public DeploymentContext configureDeployment() {
        return ServletDeploymentContext
                .forServlet(new ServletContainer((ResourceConfig) configure()))
                .build();
    }

    //@Test
    void givenCheckNumber_whenCorrectRequest_thenResponseIsOkWithStatus() {
        WebTarget target = target("perfect-numbers/");

        Response response = target
                .path("validate/10")
                .request()
                .get();
        assertEquals(Response.Status.ACCEPTED, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
        assertEquals(Response.Status.ACCEPTED, response.getStatus());

        boolean result = response.readEntity(Boolean.class);
        assertEquals(false, result);
    }

    //@Test
    void givenCheckNumber_whenCorrectRequest_thenResponseIsTrueWithStatus() {
        WebTarget target = target("perfect-numbers/");

        Response response = target
                .path("validate/6")
                .request()
                .get();
        assertEquals(Response.Status.ACCEPTED, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
        assertEquals(Response.Status.ACCEPTED, response.getStatus());

        boolean result = response.readEntity(Boolean.class);
        assertEquals(true, result);
    }

    //@Test
    void listAllPerfectNumbers_whenCorrectRequest_thenResponseIsListWithStatus() {
        WebTarget target = target("perfect-numbers/");

        Response response = target
                .path("listAllPerfectNumbers/")
                .queryParam("from",1)
                .queryParam("to",100)
                .request()
                .get();
        assertEquals(Response.Status.ACCEPTED, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
        assertEquals(Response.Status.ACCEPTED, response.getStatus());

        List<Integer> result = response.readEntity(List.class);
        assertEquals(Arrays.asList(1,6,28), result);
    }

   // @Test
    void listAllPerfectNumbers_whenCorrectRequest_thenResponseIsEmptyListWithStatus() {
        WebTarget target = target("perfect-numbers/");

        Response response = target
                .path("listAllPerfectNumbers/")
                .queryParam("from",10)
                .queryParam("to",20)
                .request()
                .get();
        assertEquals(Response.Status.ACCEPTED, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
        assertEquals(Response.Status.ACCEPTED, response.getStatus());

        List<Integer> result = response.readEntity(List.class);
        assertEquals(Arrays.asList(), result);
    }
}
