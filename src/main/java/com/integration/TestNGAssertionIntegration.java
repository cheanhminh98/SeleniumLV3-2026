package com.integration;

import com.assertion.Assertion;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;

public interface TestNGAssertionIntegration extends IHookable {

    /**
     * Runs the test method and finalizes collected soft assertions.
     *
     * @param callback   callback used to execute the test method
     * @param testResult current test result
     */
    @Override
    default void run(IHookCallBack callback, ITestResult testResult) {
        try {
            callback.runTestMethod(testResult);
        } finally {
            try {
                Assertion.finishTest();
            } catch (AssertionError assertionError) {
                Throwable testFailure = testResult.getThrowable();
                if (testFailure == null) {
                    testResult.setThrowable(assertionError);
                    testResult.setStatus(ITestResult.FAILURE);
                } else {
                    testFailure.addSuppressed(assertionError);
                }
            }
        }
    }
}
