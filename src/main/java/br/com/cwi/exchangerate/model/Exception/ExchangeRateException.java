package br.com.cwi.exchangerate.model.Exception;

/**
 *
 * @author Willian Kirschner (willkev@gmail.com)
 */
public class ExchangeRateException extends Exception {

    public ExchangeRateException(String string) {
        super(string);
    }

    public ExchangeRateException(String string, Exception ex) {
        super(string, ex);
    }

}
