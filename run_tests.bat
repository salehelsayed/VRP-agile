@echo off
echo Running clean build and tests with coverage...

call gradlew.bat clean build test jacocoTestReport

echo.
echo Test results and coverage reports can be found in:
echo build/reports/tests/test/index.html
echo build/reports/jacoco/test/html/index.html

pause
