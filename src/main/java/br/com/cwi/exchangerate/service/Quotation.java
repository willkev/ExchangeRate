package br.com.cwi.exchangerate.service;

import br.com.cwi.exchangerate.model.Exception.ExchangeRateException;
import java.math.BigDecimal;

/**
 *
 * @author Willian Kirschner (willkev@gmail.com)
 */
public interface Quotation {

    public BigDecimal currencyQuotation(String from, String to, Number value, String quotation) throws ExchangeRateException;

}
