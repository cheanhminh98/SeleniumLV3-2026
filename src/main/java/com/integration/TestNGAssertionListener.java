package com.integration;

import com.assertion.Assertion;
import com.report.ReportManager;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

public class TestNGAssertionListener implements IInvokedMethodListener {

    /**
     * Finishes collected soft assertions after the test method completes.
     * Attaches a screenshot if the test has failed and clears assertion state.
     *
     * @param method     current invoked method
     * @param testResult current TestNG test result
     */
    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (!method.isTestMethod()) {
            return;
        }
        try {
            Assertion.finishTest();
        } catch (AssertionError softFailure) {
            Throwable original = testResult.getThrowable();
            if (testResult.getStatus() == ITestResult.FAILURE
                    && original != null) {
                if (original != softFailure) {
                    original.addSuppressed(softFailure);
                }
            } else {
                if (original != null && original != softFailure) {
                    softFailure.addSuppressed(original);
                }
                testResult.setThrowable(softFailure);
            }
            testResult.setStatus(ITestResult.FAILURE);
        } finally {
            Assertion.clear();
        }
    }
}
