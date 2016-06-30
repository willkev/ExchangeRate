package br.com.cwi.exchangerate.model.Exception;

/**
 *
 * @author Willian Kirschner (willkev@gmail.com)
 */
public class QuotationDateTimeParseException extends ExchangeRateException {

    public QuotationDateTimeParseException(String string, Exception ex) {
        super(string, ex);
    }

}
