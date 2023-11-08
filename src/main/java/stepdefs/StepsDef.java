package stepdefs;

import config.Properties;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.YandexMarketCategory;
import pages.YandexMarketMain;

import java.util.*;

public class StepsDef extends BaseSteps {

    YandexMarketMain yandexMarketMain = new YandexMarketMain(chromeDriver);
    YandexMarketCategory yandexMarketCategory = new YandexMarketCategory(chromeDriver);

    @Given("открываем сайт Яндекс Маркета")
    public void открываемСайтЯндексМаркета() {
        chromeDriver.get(Properties.testsProperties.yandexMarketURL());
    }

    @When("выбираем категорию {string} и подкатегорию {string}")
    public void выбираемКатегориюНоутбукиИКомпьютерыИПодкатегориюНоутбуки(String categoryName, String subCategoryName) {
        yandexMarketMain.openPageCategory(categoryName, subCategoryName);
    }

    @Then("проверяем содержит ли страница заголовок {string}")
    public void проверяемСодержитЛиСтраницаЗаголовокНоутбуки(String subCategoryName) {
        yandexMarketCategory.checkCorrectPage(subCategoryName);
    }

    @Then("проверяем на первой странице соответствует ли количество товаров {string}")
    public void проверяемНаПервойСтраницеСоответствуетЛиКоличествоТоваров(String expectedCountProducts) {
        yandexMarketCategory.checkCountProductsFirstPage(Integer.parseInt(expectedCountProducts));
    }

    @When("вставляем в фильтр от и до:")
    public void вставляемВФильтрОтИДо(DataTable table) {
        yandexMarketCategory.insertValueInInputRange(tableConverterFilter(table));
    }

    @And("выбираем фильтр чекбокс:")
    public void выбираемФильтрЧекбокс(DataTable table) {
        yandexMarketCategory.selectCheckboxFields(tableConverterFilter(table));
    }

    private Map<String, List<String>> tableConverterFilter(DataTable table) {
        List<Map<String, String>> data = table.asMaps(String.class, String.class);
        Map<String, List<String>> filter = new HashMap<>();
        for (Map<String, String> entry : data) {
            entry.forEach((k, v) -> filter.put(k, Arrays.asList(v.split(", "))));
        }
        return filter;
    }

    @Then("проверяем соответствие фильтра заголовку товара {string} и цены {string}")
    public void проверяемСоответствиеФильтраЗаголовкуТовараОжидаемаемыеЗаголовкиТоваровИЦеныОжидаемыеЦены(String words, String prices) {
        List<String> expectedWordsInTitle = new ArrayList<>(Arrays.asList(words.split(", ")));
        List<Integer> expectedPrices = new ArrayList<>();
        for (String s : prices.split(", ")) {
            expectedPrices.add(Integer.valueOf(s));
        }
        yandexMarketCategory.checkFiltersPriceAndTitle(expectedWordsInTitle, expectedPrices);
    }

    @When("возвращаемся на 1-ю страницу")
    public void возвращаемсяНаПервуюСтраницу() {
        yandexMarketCategory.paginationPageFirst();
    }

    @Then("берем название первого товара, вводим в поиск и проверяем находится ли искомый товар на странице")
    public void беремНазваниеПервогоТовараВводимВПоискИПроверяемНаходитсяЛиИскомыйТоварНаСтранице() {
        yandexMarketCategory.searchByTitleFirstProduct();
    }
}
