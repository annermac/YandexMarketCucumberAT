package pages;

import helpers.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Класс YandexMarketCategory в категории товаров проверяет правильно ли работают фильтры
 *
 * @author Ермаченкова Анна
 * @version 1.0
 */
public class YandexMarketCategory {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final String selectorTitleProduct = ".//h3[@data-zone-name='title']/a/span";
    private final String selectorBlockFilter = "//div[@data-zone-name='Filter']";

    public YandexMarketCategory(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    /**
     * Метод проверяет, корректная ли страница в каталоге
     *
     * @param subCategoryName название подкаталога
     */
    public void checkCorrectPage(String subCategoryName) {
        boolean categoryFind = driver.findElements(By.xpath("//div[@data-zone-name='searchTitle']/h1[text()='" + subCategoryName + "']")).isEmpty();
        Assertions.assertFalse(categoryFind, "Заголовок " + subCategoryName + " не найден");
    }

    /**
     * Метод считает количество товаров
     *
     * @param expectedCountProducts ожидаемое количество товаров
     */
    public void checkCountProductsFirstPage(int expectedCountProducts) {
        Assertions.assertTrue(getProductsOnPage().size() > expectedCountProducts,
                "Количество товаров меньше " + expectedCountProducts + " и равно " + getProductsOnPage().size());
    }

    /**
     * Метод вставляет значение в текстовое поле в фильтре
     *
     * @param filterRange содержит название фильтра и его значений для текстового поля
     */
    public void insertValueInInputRange(Map<String, List<String>> filterRange) {
        for (Map.Entry<String, List<String>> entry : filterRange.entrySet()) {
            String titleFilter = entry.getKey();
            List<String> value = entry.getValue();

            WebElement blockFilterElement = driver.findElement(
                    By.xpath(selectorBlockFilter + "//h4[text()='" + titleFilter + "']/../../following-sibling::div")
            );

            WebElement minInputElement = blockFilterElement.findElement(By.xpath(
                    "//span[@data-auto='filter-range-min']/label[contains(text(),'" + titleFilter + "')]/following-sibling::div/div/input"));
            minInputElement.sendKeys(value.get(0));

            WebElement maxInputElement = blockFilterElement.findElement(By.xpath(
                    "//span[@data-auto='filter-range-max']/label[contains(text(),'" + titleFilter + "')]/following-sibling::div/div/input"));
            maxInputElement.sendKeys(value.get(1));
        }
    }

    /**
     * Метод выбирает чекбокс в фильтре
     *
     * @param filterCheckbox содержит название фильтра и его значений для чекбокса
     */
    public void selectCheckboxFields(Map<String, List<String>> filterCheckbox) {
        for (Map.Entry<String, List<String>> entry : filterCheckbox.entrySet()) {
            String titleFilter = entry.getKey();
            List<String> namesCheckbox = entry.getValue();
            for (String nameCheckbox : namesCheckbox) {
                String selectorCheckboxInput = "//div[@data-zone-name='FilterValue']//span[text()='" + nameCheckbox + "']";
                WebElement blockFilterElement = driver.findElement(By.xpath(
                        selectorBlockFilter + "//h4[text()='" + titleFilter + "']/../../following-sibling::div"));
                wait.until(ExpectedConditions.visibilityOf(blockFilterElement));

                String selectorLoadFilterLink = "//div[@data-zone-name='LoadFilterValues']/button";
                if (blockFilterElement.findElements(By.xpath(selectorLoadFilterLink)).isEmpty()) {
                    WebElement checkboxInputElement = blockFilterElement.findElement(By.xpath(selectorCheckboxInput));
                    checkboxInputElement.click();
                } else {
                    blockFilterElement.findElement(By.xpath(selectorLoadFilterLink)).click();
                    WebElement checkboxInputElement = blockFilterElement.findElement(By.xpath(selectorCheckboxInput));
                    wait.until(ExpectedConditions.visibilityOf(checkboxInputElement));
                    checkboxInputElement.click();
                }
            }
        }
    }

    /**
     * Метод собирает все товары на текущей странице
     *
     * @return возвращает товары
     */
    public List<WebElement> getProductsOnPage() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        return driver.findElements(By.xpath("//article[@data-zone-name='snippet-card']"));
    }

    /**
     * Метод проверяет соответсвие цены и заголовки у товаров
     *
     * @param expectedWordsInTitle ожидаемые слова для заголовка
     * @param expectedPrices       ожидаемые цены
     */
    public void checkFiltersPriceAndTitle(List<String> expectedWordsInTitle, List<Integer> expectedPrices) {
        Instant startTime = Instant.now();
        List<String> errors = new ArrayList<>();
        while (paginationPageNext() && checkLoopIsNotInfinite(startTime)) {
            if (!checkFilterCheckboxInTitleProduct(expectedWordsInTitle)) {
                errors.add("В названии товара не содержится фильтр чекбокса " + expectedWordsInTitle + "; ");
            }
            if (!checkOfProductFilterPrice(expectedPrices)) {
                errors.add("Цена товара не соответсвует фильтру" + expectedPrices + "; ");
            }
            paginationPageNext();
        }
        Assertions.assertTrue(errors.isEmpty(), errors.toString());
    }

    /**
     * Метод перемещается на первую страницу
     */
    public void paginationPageFirst() {
        Instant startTime = Instant.now();
        while (paginationPagePrev() && checkLoopIsNotInfinite(startTime)) {
            paginationPagePrev();
        }
    }

    /**
     * Метод перемещается назад по страницам
     */
    public boolean paginationPagePrev() {
        List<WebElement> button = driver.findElements(By.xpath("//div[@data-auto='pagination-prev']"));
        if (button.isEmpty()) {
            return false;
        }
        button.get(0).click();
        wait.until(ExpectedConditions.stalenessOf(button.get(0)));
        return true;
    }

    /**
     * Метод ищет название первого товара в поиске и проверяет нашелся ли он на странице
     */
    public void searchByTitleFirstProduct() {
        WebElement firstProduct = getProductsOnPage().get(0).findElement(By.xpath(selectorTitleProduct));
        String firstProductTitle = firstProduct.getText();
        WebElement inputHeaderSearchElement = driver.findElement(By.xpath("//input[@id='header-search']"));
        inputHeaderSearchElement.sendKeys(firstProduct.getText());

        WebElement buttonHeaderSearchElement = driver.findElement(By.xpath("//button[@data-auto='search-button']"));
        buttonHeaderSearchElement.click();
        wait.until(ExpectedConditions.stalenessOf(buttonHeaderSearchElement));

        String firstFoundProduct = getProductsOnPage().get(0).findElement(By.xpath(selectorTitleProduct)).getText();

        Assertions.assertEquals(firstFoundProduct, firstProductTitle,
                "Первый найденный товар содержит название '" + firstFoundProduct + "', а искомый товар содержит '" + firstProductTitle + "'");
    }

    /**
     * Метод проверяет соответствует ли название товара чекбоксу выбранным в фильтре
     *
     * @param filterCheckboxNames названия чекбоксов
     */
    public boolean checkFilterCheckboxInTitleProduct(List<String> filterCheckboxNames) {
        boolean contains = false;
        List<WebElement> results = getProductsOnPage();
        for (WebElement product : results) {
            String title = product.findElement(By.xpath(selectorTitleProduct)).getText();
            for (String filterName : filterCheckboxNames) {
                if (title.contains(filterName)) {
                    contains = true;
                    break;
                }
            }
        }
        return contains;
    }

    /**
     * Метод проверяет соответствует ли товар текстовому полю выбранным в фильтре
     *
     * @param filterTextValues значения текстовых полей
     */
    public boolean checkOfProductFilterPrice(List<Integer> filterTextValues) {
        for (WebElement product : getProductsOnPage()) {
            int price = Integer.parseInt(product.findElement(By.xpath(".//div[@data-zone-name='price']//h3")).getText().replaceAll("\\D+", ""));
            if (price <= filterTextValues.get(0) && price >= filterTextValues.get(1)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Метод перемещается вперед по страницам
     */
    public boolean paginationPageNext() {
        List<WebElement> button = driver.findElements(By.xpath("//div[@data-auto='pagination-next']"));
        if (button.isEmpty()) {
            return false;
        } else {
            button.get(0).click();
            wait.until(ExpectedConditions.stalenessOf(button.get(0)));
            return true;
        }
    }

    /**
     * Метод проверяет работает ли цикл больше 5 минут
     */
    private boolean checkLoopIsNotInfinite(Instant startTime) {
        Instant endTime = Instant.now();
        Duration duration = Duration.between(startTime, endTime);
        if (duration.toMinutes() > 5) {
            Assertions.fail("Цикл работает больше 5 минут");
            return false;
        }
        return true;
    }
}