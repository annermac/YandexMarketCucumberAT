package helpers;

import org.junit.jupiter.params.provider.Arguments;

import java.util.*;
import java.util.stream.Stream;

/**
 * Класс DataProvider содержит тестовые данные
 *
 * @author Ермаченкова Анна
 * @version 1.0
 */
public class DataProvider {
    public static Stream<Arguments> providerCheckingCategory() {
        int expectedCountProducts = 12;

        Map<String, List<String>> filerCheckbox = new HashMap<>();
        filerCheckbox.put("Производитель", Arrays.asList("HP", "Lenovo"));

        Map<String, List<String>> filterRange = new HashMap<>();
        filterRange.put("Цена, ₽", Arrays.asList("10000", "30000"));

        List<String> expectedWordsInTitle = new ArrayList<>();
        expectedWordsInTitle.add("HP");
        expectedWordsInTitle.add("Lenovo");

        List<Integer> expectedPrices = new ArrayList<>();
        expectedPrices.add(10000);
        expectedPrices.add(30000);

        return Stream.of(Arguments.of(
                "Ноутбуки и компьютеры",
                "Ноутбуки",
                expectedCountProducts,
                filerCheckbox,
                filterRange,
                expectedWordsInTitle,
                expectedPrices));
    }
}
