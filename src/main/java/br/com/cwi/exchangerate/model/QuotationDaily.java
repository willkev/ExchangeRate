package br.com.cwi.exchangerate.model;

import br.com.cwi.exchangerate.model.Exception.CurrencyNotFoundException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Willian Kirschner (willkev@gmail.com)
 */
public class QuotationDaily {

    private final LocalDate date;
    private final Map<String, QuotationDailyItem> quotationDailyItems;

    public QuotationDaily(LocalDate date) {
        this.date = date;
        this.quotationDailyItems = new HashMap<>();
    }

    public void addItem(QuotationDailyItem quotationDailyItem) {
        quotationDailyItems.put(quotationDailyItem.getAbbreviationName(), quotationDailyItem);
    }

    public LocalDate getDate() {
        return date;
    }

    public QuotationDailyItem getQuotationDailyItem(String abbreviationName) throws CurrencyNotFoundException {
        if (abbreviationName == null || abbreviationName.isEmpty()) {
            throw new CurrencyNotFoundException("Currency is null or empty!", abbreviationName);
        }
        QuotationDailyItem get = quotationDailyItems.get(abbreviationName);
        if (get == null) {
            throw new CurrencyNotFoundException("Currency not found!", abbreviationName);
        }
        return get;
    }

}
