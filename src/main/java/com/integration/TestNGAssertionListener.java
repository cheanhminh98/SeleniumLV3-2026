package com.integration;

import com.assertion.Assertion;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

public class TestNGAssertionListener implements IInvokedMethodListener {

    /**
     * Executes the test method and automatically finishes
     * all collected soft assertions.
     * Soft assertion failures are collected while the test
     * method is running. After the test method finishes,
     *
     * @param testResult current TestNG test result
     */
    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (!method.isTestMethod()) {
            return;
        }
        try {
            Assertion.finishTest();
        } catch (AssertionError error) {
            testResult.setThrowable(error);
            testResult.setStatus(ITestResult.FAILURE);
        } finally {
            Assertion.clear();
        }
    }
}
