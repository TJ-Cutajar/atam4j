package me.atam.atam4j.resources;

import me.atam.atam4j.AcceptanceTestsRunnerTaskScheduler;
import me.atam.atam4j.TestRunListener;
import me.atam.atam4jdomain.TestsRunResult;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.concurrent.ExecutionException;

@Path("/tests")
@Produces("application/json")
public class TestStatusResource {

    private final AcceptanceTestsRunnerTaskScheduler acceptanceTestsRunnerTaskScheduler;
    private final TestRunListener testRunListener;

    public TestStatusResource(AcceptanceTestsRunnerTaskScheduler acceptanceTestsRunnerTaskScheduler,
                              TestRunListener testRunListener) {
        this.acceptanceTestsRunnerTaskScheduler = acceptanceTestsRunnerTaskScheduler;
        this.testRunListener = testRunListener;
    }

    @GET
    public Response getTestStatus() {
        return buildResponse(testRunListener.getTestsRunResult());
    }

    @GET
    @Path("{category}")
    public Response getTestStatusForACategory(@PathParam("category") String category) {
        return buildResponse(testRunListener.getTestsRunResult(category));
    }

    @GET
    @Path("run")
    public Response runTests() {
        try {
            acceptanceTestsRunnerTaskScheduler.runAcceptanceTests().get();
        } catch (InterruptedException | ExecutionException e) {
            return buildResponse(new TestsRunResult(TestsRunResult.Status.EXECUTION_EXCEPTION));
        }
        return buildResponse(testRunListener.getTestsRunResult());
    }

    private Response buildResponse(TestsRunResult testRunResult) {
        if (testRunResult.getStatus().equals(TestsRunResult.Status.FAILURES) ||
            testRunResult.getStatus().equals(TestsRunResult.Status.EXECUTION_EXCEPTION)) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity(testRunResult)
                           .build();
        } else if (testRunResult.getStatus().equals(TestsRunResult.Status.CATEGORY_NOT_FOUND)) {
            return Response.status(Response.Status.NOT_FOUND)
                           .build();
        } else {
            return Response.status(Response.Status.OK)
                           .entity(testRunResult)
                           .build();
        }
    }
}