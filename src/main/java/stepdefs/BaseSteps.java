package stepdefs;

import org.openqa.selenium.WebDriver;
import stash.Context;
import stash.TestContext;

/**
 * Базовый класс Steps
 */
public class BaseSteps {

    /**
     * Класс-хранилище тестовых данных
     */
    public TestContext testContext;

    /**
     * Хромдрайвер
     */
    public WebDriver chromeDriver;

    public BaseSteps() {
        this.testContext = TestContext.getInstance();
        // драйвер берется из хранилища в классе контеста
        this.chromeDriver = (WebDriver) this.testContext.get(Context.CHROMEDRIVER.toString());
    }

}
