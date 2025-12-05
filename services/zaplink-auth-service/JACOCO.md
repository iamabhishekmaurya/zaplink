# JaCoCo Code Coverage

This project uses JaCoCo for code coverage reporting and analysis.

## Available Tasks

### Generate Coverage Report
```bash
./gradlew coverageReport
```
Generates HTML and XML coverage reports in `build/reports/jacoco/`.

### Verify Coverage Thresholds
```bash
./gradlew checkCoverage
```
Verifies that code coverage meets the minimum threshold (50%).

### Run Tests with Coverage
```bash
./gradlew test
```
Automatically generates coverage reports after running tests.

### Full Check with Coverage
```bash
./gradlew check
```
Runs all checks including coverage verification.

## Coverage Reports

- **HTML Report**: `build/reports/jacoco/index.html`
- **XML Report**: `build/reports/jacoco/test/jacocoTestReport.xml`
- **Current Threshold**: 50% minimum coverage

## Configuration

- Excludes DTOs, entities, and configuration classes from coverage calculations
- Generates both HTML (for viewing) and XML (for CI/CD) reports
- Coverage verification is integrated into the standard `check` task

## Viewing Coverage

Open the HTML report in your browser:
```bash
open build/reports/jacoco/index.html
```
