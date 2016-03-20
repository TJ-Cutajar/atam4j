package me.atam.atam4jsampleapp.testsupport;

import io.dropwizard.testing.DropwizardTestSupport;
import me.atam.atam4j.PollingPredicate;
import me.atam.atam4jdomain.TestsRunResult;
import me.atam.atam4jsampleapp.ApplicationConfiguration;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.After;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

import static me.atam.atam4jsampleapp.testsupport.AcceptanceTestTimeouts.MAX_ATTEMPTS;
import static me.atam.atam4jsampleapp.testsupport.AcceptanceTestTimeouts.RETRY_POLL_INTERVAL;
import static org.junit.Assert.assertTrue;

public abstract class AcceptanceTest {

    private static final Client client = new JerseyClientBuilder().build();

    protected DropwizardTestSupport<ApplicationConfiguration> dropwizardTestSupportAppConfig;

    @After
    public void stopApplication() {
        dropwizardTestSupportAppConfig.after();
    }

    public Response getTestRunResultFromServer(String testsURI){
        return client.target(testsURI)
                     .request()
                     .get();
    }

    public Response performTestRun() {
        return client.target(getTestsURI() + "/run")
                     .request()
                     .get();
    }

    public String getTestsURI() {
        return String.format("http://localhost:%d/tests", dropwizardTestSupportAppConfig.getLocalPort());
    }

    public Response getResponseFromTestsWithCategoryOnceTestRunHasCompleted(String category) {
        waitUntilTestRunHasCompleted();
        return getTestRunResultFromServer(getTestsURI() + "/" + category);
    }

    public Response getResponseFromTestsEndpointOnceTestsRunHasCompleted() {
        waitUntilTestRunHasCompleted();
        return getTestRunResultFromServer(getTestsURI());
    }

    private void waitUntilTestRunHasCompleted() {
        PollingPredicate<Response> responsePollingPredicate = new PollingPredicate<>(
                MAX_ATTEMPTS,
                RETRY_POLL_INTERVAL,
                response -> !response.readEntity(TestsRunResult.class).getStatus().equals(TestsRunResult.Status.TOO_EARLY),
                () ->  getTestRunResultFromServer(getTestsURI()));

        assertTrue(responsePollingPredicate.pollUntilPassedOrMaxAttemptsExceeded());
    }

}
