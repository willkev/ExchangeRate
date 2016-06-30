package br.com.cwi.exchangerate.model.Exception;

/**
 *
 * @author Willian Kirschner (willkev@gmail.com)
 */
public class CurrencyNotFoundException extends ExchangeRateException {

    public CurrencyNotFoundException(String msg, String abbreviationName) {
        super(msg + " Name: " + abbreviationName);
    }

}
