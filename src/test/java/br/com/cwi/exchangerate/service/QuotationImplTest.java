package br.com.cwi.exchangerate.service;

import br.com.cwi.exchangerate.datasource.BrazilianCentralBankDataSource;
import br.com.cwi.exchangerate.model.Exception.CurrencyNotFoundException;
import br.com.cwi.exchangerate.model.Exception.NegativeValueException;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Willian Kirschner (willkev@gmail.com)
 */
public class QuotationImplTest {

    public QuotationImplTest() {
    }

    /**
     * If the from or to parameters are not valid, an exception must be thrown
     */
    @Test
    public void testCurrencyQuotationFailValues() throws Exception {
        QuotationImpl quotationImpl = new QuotationImpl(new BrazilianCentralBankDataSource());
        try {
            quotationImpl.currencyQuotation("REAL", "USD", 145, "20160627");
            Assert.fail();
        } catch (CurrencyNotFoundException e) {
        }
        try {
            quotationImpl.currencyQuotation("EUR", "LIBRA", 100, "20160627");
            Assert.fail();
        } catch (CurrencyNotFoundException e) {
        }
        try {
            quotationImpl.currencyQuotation("EUR", "", 111, "20160627");
            Assert.fail();
        } catch (CurrencyNotFoundException e) {
        }
        try {
            quotationImpl.currencyQuotation("1234", "USD", 111, "20160627");
            Assert.fail();
        } catch (CurrencyNotFoundException e) {
        }
        try {
            quotationImpl.currencyQuotation("EUR", "USD", -1, "20141120");
            Assert.fail();
        } catch (NegativeValueException e) {
        }
        try {
            quotationImpl.currencyQuotation("EUR", "USD", -0.666, "20141120");
            Assert.fail();
        } catch (NegativeValueException e) {
        }
    }

}
