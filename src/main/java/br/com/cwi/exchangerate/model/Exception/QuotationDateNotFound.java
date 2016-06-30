package br.com.cwi.exchangerate.model.Exception;

import java.time.LocalDate;

/**
 *
 * @author Willian Kirschner (willkev@gmail.com)
 */
public class QuotationDateNotFound extends ExchangeRateException {

    public QuotationDateNotFound(String string, Exception ex) {
        super(string, ex);
    }

    public QuotationDateNotFound(LocalDate date) {
        super("Unable to access the CSV file to the specified date: " + date.toString());
    }

}
