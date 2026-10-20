# SeleniumLV3-2026

A UI test automation framework built with **Java 11, Selenium WebDriver, TestNG, and Maven**. It provides reusable browser management, a custom element layer, configurable waits and retries, custom assertions, and pluggable reporting. Browser and report implementations are discovered through Java `ServiceLoader`, helping keep provider selection separate from the framework's core logic.

## Features

- **Extensible browser management** — discovers browser driver providers through Java `ServiceLoader`.
- **Local and remote execution** — supports local WebDriver sessions and Selenium Grid.
- **JSON-based configuration** — loads browser-specific settings and supports selected command-line overrides.
- **Smart Element Layer** — centralizes common Selenium interactions behind a reusable `Element` API.
- **Waits and conditions** — provides explicit waits and reusable, composable element conditions.
- **Retry handling** — retries supported element operations for selected transient Selenium exceptions.
- **Custom assertions** — supports hard assertions, soft assertions, and condition-based assertions with retry.
- **Page Object Model (POM)** — separates page interactions from test cases.
- **Pluggable reporting** — supports Allure and Extent Reports through a provider-based design.
- **TestNG integration** — uses listeners to manage report events and soft-assertion results.
- **GitHub Actions workflow** — supports automated Maven test execution in CI.

## Technology Stack

| Technology | Version / Purpose |
|---|---|
| Java | 11 |
| Selenium WebDriver | 4.43.0 |
| TestNG | 7.12.0 |
| JUnit Jupiter | 5.13.4 — dependency included in the project |
| Maven | Build and dependency management |
| Lombok | 1.18.46 |
| Gson | 2.14.0 |
| Logback | 1.6.3 |
| Allure | 2.35.4 |
| Extent Reports | 5.0.9 |

> JUnit Jupiter is listed as a project dependency; this alone does not imply that all framework integrations support running JUnit tests. The examples in this README use TestNG.

## Architecture

```text
Test Cases
    |
    v
Page Objects
    |
    v
Element
    |-- ElementWait
    |-- ElementRetry
    `-- ElementConditions / ElementCondition
    |
    v
DriverManager
    |
    v
DriverContainer
    |
    v
BaseDriverFactory -- ServiceLoader --> Browser Driver Provider
    |
    v
Local WebDriver or RemoteWebDriver (Selenium Grid)
```

Reporting follows a similar provider-based design:

```text
TestNG Listeners
       |
       v
 ReportManager
       |
       v
 ServiceLoader<Report>
       |
       +-- AllureReport
       `-- ExtentReport
```

## Project Structure

```text
SeleniumLV3-2026/
├── .github/
│   └── workflows/             # GitHub Actions workflows
├── src/
│   ├── main/java/com/
│   │   ├── adapter/            # Gson type adapters
│   │   ├── assertion/          # Hard/soft assertions and assertion waits
│   │   ├── constant/           # Framework constants
│   │   ├── data/               # Browser-related data types
│   │   ├── driver/             # Driver providers, configuration and lifecycle
│   │   ├── element/            # Element wrapper, waits, retries and conditions
│   │   ├── integration/        # TestNG listeners
│   │   ├── page/               # Page Object classes
│   │   ├── report/             # Report API, manager and providers
│   │   └── utilities/          # JSON and driver configuration helpers
│   └── test/
│       ├── java/test/          # Test base and test cases
│       └── resources/
│           ├── config/         # Browser JSON configuration
│           └── suite/          # TestNG suite XML files
└── pom.xml
```

## Prerequisites

Install the following before running the project locally:

- JDK 11
- Apache Maven
- Git
- A supported browser for local execution
- Allure CLI if you want to generate and open an Allure HTML report locally
- Selenium Grid if you want to run tests remotely

## Installation

Clone the repository and enter the project directory:

```bash
git clone https://github.com/cheanhminh98/SeleniumLV3-2026.git
cd SeleniumLV3-2026
```

Build the project and resolve dependencies:

```bash
mvn clean install
```

## Browser Configuration

Driver configuration files are loaded from:

```text
src/test/resources/config/
```

The configuration loader selects a JSON file based on the configured browser type. For example, `chrome.json` can contain:

```json
{
  "browser": "chrome",
  "headless": false,
  "remoteURL": "",
  "baseUrl": "https://google.com",
  "startMaximized": true,
  "timeout": 5000,
  "pageLoadTimeout": 30000,
  "pollingInterval": 200
}
```

**Duration values in the JSON file are milliseconds.** They are converted to Java `Duration` values by the framework's Gson adapter.

The driver configuration supports:

| Property | Purpose |
|---|---|
| `browser` | Browser name used to select a driver provider |
| `headless` | Enables headless mode when supported by the provider |
| `remoteURL` | Empty for local execution; set a Grid URL for remote execution |
| `baseUrl` | Default application URL for the test setup |
| `startMaximized` | Browser-window preference stored in the configuration; confirm that the selected provider applies it |
| `timeout` | Default element/assertion timeout in milliseconds |
| `pageLoadTimeout` | Page-load timeout in milliseconds |
| `pollingInterval` | Wait polling interval in milliseconds |

The current `DriverConfigLoader` supports system-property overrides for `headless`, `remoteURL`, and `baseUrl`. These override the corresponding JSON values. Although `startMaximized` is part of `DriverConfig`, the provider implementations should be checked before relying on it because the current browser option methods do not apply that setting.

For example, run Chrome headlessly:

```bash
mvn clean test -Dheadless=true
```

Override the application URL:

```bash
mvn clean test -DbaseUrl=https://example.com
```

## Browser Driver Providers

The framework uses `BaseDriver<T>` and `BaseDriverFactory` to locate a browser provider by its name through Java `ServiceLoader`. Built-in provider classes in the submitted code include Chrome, Edge, and Firefox managers.

The provider name must match the browser name in `DriverConfig`. Provider implementations must also be registered in the appropriate `META-INF/services` descriptor so `ServiceLoader` can discover them.

Local execution creates a browser-specific WebDriver. When `remoteURL` is configured, the base driver creates a `RemoteWebDriver` using the same browser options and configures a `LocalFileDetector`.

## Smart Element Layer

The `Element` wrapper provides a locator-based API and centralizes common Selenium interactions.

```java
import com.element.Element;
import org.openqa.selenium.By;

Element username = new Element(By.id("username"));
username.setValue("admin");

Element submit = new Element(By.id("submit"));
submit.click();
```

Examples of available operations include:

```java
element.click();
element.setValue("admin");
element.enter("additional text");
element.hover();
element.scrollToView();

String text = element.getText();
String value = element.getValue();

boolean displayed = element.isDisplayed();
boolean exists = element.isExist();
boolean enabled = element.isEnabled();
boolean checked = element.isChecked();
```

The wrapper uses the configured driver timeout for operations that use retry-aware reads or interactions. `isExist()` checks whether a matching element is present without waiting for it to become visible.

## Waits and Conditions

`ElementWait` is based on Selenium's `WebDriverWait` and uses the configured timeout and polling interval. `ElementCondition` represents a reusable condition, while `ElementConditions` provides common conditions.

```java
import com.element.Element;
import com.element.ElementConditions;
import com.element.ElementWait;
import org.openqa.selenium.By;

Element submit = new Element(By.id("submit"));
new ElementWait(submit).waitUntil(ElementConditions.isVisible());
```

Available condition helpers include:

```java
ElementConditions.isExist();
ElementConditions.isVisible();
ElementConditions.isEnabled();
ElementConditions.isClickable();
ElementConditions.isInvisible();
ElementConditions.isDisabled();
```

Conditions can be combined:

```java
ElementConditions.isVisible()
    .and(ElementConditions.isEnabled());

ElementConditions.isVisible()
    .or(ElementConditions.isClickable());
```

## Retry Behavior

`ElementRetry` wraps element actions and values with retry-aware waiting. The implementation ignores stale-element failures in the shared wait layer and can accept additional exception types for particular interactions. Click and input operations configure additional retry exceptions appropriate to those actions.

Retries are intended to handle transient UI conditions; they do not replace stable locators or correct synchronization in the test.

## Custom Assertions

The assertion package contains hard and soft assertion APIs, as well as condition-based assertions that can wait until a condition or value reaches the expected state.

### Hard assertions

A failed hard assertion throws an `AssertionError` immediately.

### Soft assertions

Soft assertion failures are collected for the current thread and reported together after the test method. `TestNGAssertionListener` calls `Assertion.finishTest()` and clears the collected state after each TestNG test method.

Example usage follows the framework's assertion API:

```java
HardAssertion hard = new HardAssertion();
hard.assertEquals(actual, expected, "Values should match");

SoftAssertion soft = new SoftAssertion();
soft.assertTrue(condition, "Condition should be true");
```

For dynamic UI values, the assertion API also supports `Supplier<T>` overloads and condition-based checks that retry until success or timeout. The default timeout is taken from the current driver configuration unless an explicit timeout is supplied.

## Reporting

The framework supports two report providers through the `Report` interface and Java `ServiceLoader`:

- **Allure** — default provider; writes results to `allure-results/`.
- **Extent Reports** — creates an HTML report at `test-output/ExtentReport/index.html` by default.

`ReportManager` selects the provider using the `report` Java system property. If the property is absent or blank, the default is `allure`. Only the selected provider is initialized through `ReportManager`; an unsupported provider name causes an `IllegalArgumentException`.

### Run with Allure

```bash
mvn clean test -Dreport=allure
```

### Run with Extent Reports

```bash
mvn clean test -Dreport=extent
```

You can combine report selection with a TestNG suite and other configuration overrides:

```bash
mvn --batch-mode --no-transfer-progress clean test \
  -DsuiteXmlFile=src/test/resources/suite/testng.xml \
  -Dreport=allure \
  -Dheadless=true
```

Replace the example suite path with a suite XML file that exists in the repository. The `suiteXmlFile` property must be wired through the Maven Surefire configuration for the command to select a suite.

### Report output

| Report | Default output |
|---|---|
| Allure results | `allure-results/` |
| Extent HTML | `test-output/ExtentReport/index.html` |
| Surefire/TestNG results | `target/surefire-reports/` |

To change the Extent HTML output, set `extent.reporter.spark.out`:

```bash
mvn clean test -Dreport=extent -Dextent.reporter.spark.out=target/reports/extent.html
```

To open an Allure report locally, install the Allure CLI and run:

```bash
allure serve allure-results
```

The TestNG report listener starts the selected report at execution start, records passed/failed/skipped outcomes, and flushes the report when execution finishes. Failure screenshots are handled through the report integration; browser screenshots require an initialized WebDriver and may not be available for failures that occur before driver startup. The Allure and Extent implementations handle screenshots through their respective report integrations.

## Running Tests

Run tests using the Maven configuration:

```bash
mvn clean test
```

Run a specific TestNG suite:

```bash
mvn clean test -DsuiteXmlFile=src/test/resources/suite/regression.xml
```

Run headlessly with Allure:

```bash
mvn clean test \
  -DsuiteXmlFile=src/test/resources/suite/testng.xml \
  -Dheadless=true \
  -Dreport=allure
```

Make sure the suite path matches an XML file under `src/test/resources/suite/` and that the Maven Surefire plugin is configured to use the `suiteXmlFile` property.

## Selenium Grid

To use remote execution, start a Selenium Grid and set `remoteURL` in the selected browser JSON configuration or override it from the command line:

```bash
mvn clean test \
  -DremoteURL=http://localhost:4444 \
  -Dheadless=true
```

The Grid endpoint must be reachable from the machine running Maven. The browser name and options are still selected through the configured browser provider.

## GitHub Actions CI/CD

Workflow definitions belong in:

```text
.github/workflows/
```

The CI workflow should set up JDK 11, use Maven to execute the selected TestNG suite, and enable headless browser execution in the CI environment. The project has been developed around commands such as:

```bash
mvn --batch-mode --no-transfer-progress clean test \
  -DsuiteXmlFile=src/test/resources/suite/testng.xml \
  -Dheadless=true \
  -Dreport=allure
```

Update the suite path and report option to match the actual workflow configuration. For useful failure diagnostics, configure the workflow to upload available outputs such as `target/surefire-reports/`, `allure-results/`, and/or `test-output/ExtentReport/` even when the test step fails.

A failed workflow can indicate either an infrastructure/configuration problem or a real test failure. Inspect the Surefire output and the selected report before changing the CI configuration. Browser/CDP compatibility warnings should be evaluated separately from the actual test result.

## Logging

The framework uses Logback for runtime logging. Logs help track test lifecycle events, report initialization, and failures during test execution.

## Design Patterns

- **Factory Pattern** — centralizes browser-provider lookup and driver creation.
- **Template Method Pattern** — `BaseDriver` defines the shared local/remote creation flow while concrete providers supply browser options and local driver creation.
- **Page Object Model** — separates page-specific interactions from test cases.
- **Strategy/provider design** — the `Report` interface and `ServiceLoader` allow report implementations to be discovered without hard-coding provider classes in `ReportManager`.
- **Functional conditions** — `ElementCondition` supports reusable conditions and composition with `and()` / `or()`.

## Author

**Minh Che**  
GitHub: [cheanhminh98](https://github.com/cheanhminh98)  
Repository: [SeleniumLV3-2026](https://github.com/cheanhminh98/SeleniumLV3-2026)

