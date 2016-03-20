package me.atam.atam4jdomain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.Collections;

public class TestsRunResult {

    private final Collection<IndividualTestResult> tests;
    private final Status status;

    public TestsRunResult(final @JsonProperty("tests")  Collection<IndividualTestResult> tests,
                          final @JsonProperty("status") Status status) {
        this.tests = tests;
        this.status = status;
    }

    public TestsRunResult(final Collection<IndividualTestResult> tests) {
        this.tests = tests;
        this.status = buildStatus(tests);
    }

    public TestsRunResult(final Status status) {
        this.tests = Collections.emptyList();
        this.status = status;
    }

    public Collection<IndividualTestResult> getTests() {
        return tests;
    }

    public Status getStatus() {
        return status;
    }

    private Status buildStatus(final Collection<IndividualTestResult> testResults) {
        if (testResults.isEmpty()) {
            return Status.CATEGORY_NOT_FOUND;
        }
        return testResults.stream()
                          .filter(testReport -> !testReport.isPassed())
                          .findAny()
                          .map(failures -> Status.FAILURES)
                          .orElse(Status.ALL_OK);
    }

    public enum Status {
        TOO_EARLY("Too early to tell - tests not complete yet"),
        CATEGORY_NOT_FOUND("This category does not exist"),
        ALL_OK("All is A OK!"),
        FAILURES("Failures"),
        EXECUTION_EXCEPTION("Failed to run tests. Either the test run aborted or the thread was interrupted");

        private final String message;

        Status(String message) {
            this.message = message;
        }
    }
}