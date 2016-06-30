package br.com.cwi.exchangerate.model.Exception;

/**
 * @author Willian Kirschner (willkev@gmail.com)
 */
public class NegativeValueException extends ExchangeRateException {

    public NegativeValueException(String msg, Number value) {
        super(msg + " Value: " + value);
    }

}
