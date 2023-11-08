package ru.yandex.market;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Класс запуска тестов Cucumber
 */
@CucumberOptions(
        strict = false,
        monochrome = true,
        plugin = {"pretty", "io.qameta.allure.cucumber5jvm.AllureCucumber5Jvm", "json:target/cucumber-report/report.json"},
        features = "src/test/java/features",
        glue = {"stepdefs", "hooks"},
        tags = "not @excluded"
)

@RunWith(Cucumber.class)
public class RunnerTest {
}
