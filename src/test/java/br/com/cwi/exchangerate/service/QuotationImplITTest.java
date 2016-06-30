package br.com.cwi.exchangerate.service;

import br.com.cwi.exchangerate.datasource.BrazilianCentralBankDataSource;
import br.com.cwi.exchangerate.model.Exception.ExchangeRateException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Integration test with set of values
 *
 * @author Willian Kirschner (willkev@gmail.com)
 */
@RunWith(Parameterized.class)
public class QuotationImplITTest {

    private final String from;
    private final String to;
    private final Number valueToConvert;
    private final String quotationDate;
    private final String expected;

    @Parameterized.Parameters(name = "Try convert value {2} from {0} to {1} for date {3}")
    public static List<Object[]> params() {
        return Arrays.asList(
                new Object[][]{
                    //                    {"EUR", "BRL", 77.00, "20160627", "287.90"},
                    {"EUR", "MXN", 77.00, "20160627", "1624.78"},
                    //                    {"GBP", "BRL", 32.00, "20160627", "143.43"},
                    {"USD", "EUR", 100.00, "20141120", "79.69"},
                    {"EUR", "USD", 100.00, "20141120", "125.46"},});
    }

    public QuotationImplITTest(String from, String to, Number valueToConvert, String quotationDate, String expected) {
        this.from = from;
        this.to = to;
        this.valueToConvert = valueToConvert;
        this.quotationDate = quotationDate;
        this.expected = expected;
    }

    @Test
    public void testValues() throws ExchangeRateException {
        QuotationImpl quotationImpl = new QuotationImpl(new BrazilianCentralBankDataSource());
        BigDecimal currencyQuotation = quotationImpl.currencyQuotation(from, to, valueToConvert, quotationDate);
        Assert.assertEquals(expected, currencyQuotation.toString());
    }

}
