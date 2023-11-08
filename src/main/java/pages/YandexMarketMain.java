package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Класс YandexMarketMain на главной странице ищет в Каталоге категорию и переходит в подкатегорию
 *
 * @author Ермаченкова Анна
 * @version 1.0
 */
public class YandexMarketMain {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public YandexMarketMain(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Метод открывает страницу подкатегории
     *
     * @param categoryName    название каталога
     * @param subCategoryName название подкаталога
     */
    public void openPageCategory(String categoryName, String subCategoryName) {
        String selectorButtonCatalog = "//header//button/span[text()='Каталог']";
        driver.findElement(By.xpath(selectorButtonCatalog)).click();
        Actions action = new Actions(driver);
        WebElement categoryLinkElement = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//li[@data-zone-name='category-link']//span[text()='" + categoryName + "']"))
        );

        WebElement subCategoryLinkElement = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//ul[@data-autotest-id='subItems']/li//a[text()='" + subCategoryName + "']"))
        );
        action.moveToElement(categoryLinkElement).pause(Duration.ofMillis(500)).moveToElement(subCategoryLinkElement);
        subCategoryLinkElement.click();
    }
}
