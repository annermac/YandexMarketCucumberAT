package helpers;

import io.qameta.allure.Step;

/**
 * Класс Assertions переопределяет стандартный класс Assertions для удобного отчета
 *
 * @author Ермаченкова Анна
 * @version 1.0
 */
public class Assertions {
    @Step("Проверяем что нет ошибки: {message}")
    public static void assertTrue(boolean condition, String message) {
        org.junit.jupiter.api.Assertions.assertTrue(condition, message);
    }

    @Step("Проверяем что нет ошибки: {message}")
    public static void assertFalse(boolean condition, String message) {
        org.junit.jupiter.api.Assertions.assertFalse(condition, message);
    }

    @Step("Проверяем что нет ошибки: {message}")
    public static void assertEquals(Object actual, Object expected, String message) {
        org.junit.jupiter.api.Assertions.assertEquals(actual, expected, message);
    }

    @Step("Ошибка")
    public static void fail(String message) {
        org.junit.jupiter.api.Assertions.fail(message);
    }
}
