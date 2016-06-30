package br.com.cwi.exchangerate.datasource;

import br.com.cwi.exchangerate.model.Exception.ExchangeRateException;
import br.com.cwi.exchangerate.model.Exception.QuotationDateNotFound;
import br.com.cwi.exchangerate.model.Exception.QuotationDateTimeParseException;
import br.com.cwi.exchangerate.model.QuotationDaily;
import java.io.File;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Willian Kirschner (willkev@gmail.com)
 */
public class BrazilianCentralBankDataSourceTest {

    private static final int FILE_BUFFER = 4096;

    private final File dirTest;

    public BrazilianCentralBankDataSourceTest() {
        dirTest = new File(System.getProperty("java.io.tmpdir"), "");
    }

    @Test()
    public void testGetFileFail() throws ExchangeRateException {
        BrazilianCentralBankDataSource brazilianCBDataSource = new BrazilianCentralBankDataSource();
        try {
            brazilianCBDataSource.findQuotationByDate(null);
            Assert.fail();
        } catch (QuotationDateTimeParseException ex) {
            ex.printStackTrace();
        }
        try {
            brazilianCBDataSource.findQuotationByDate("");
            Assert.fail();
        } catch (QuotationDateTimeParseException ex) {
            ex.printStackTrace();
        }
        try {
            brazilianCBDataSource.findQuotationByDate("00001100");
            Assert.fail();
        } catch (QuotationDateTimeParseException ex) {
            ex.printStackTrace();
        }
        try {
            brazilianCBDataSource.findQuotationByDate("20200101");
            Assert.fail();
        } catch (QuotationDateNotFound ex) {
            ex.printStackTrace();
        }
        try {
            brazilianCBDataSource.findQuotationByDate("20160132");
            Assert.fail();
        } catch (QuotationDateTimeParseException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testGetFileOk() throws ExchangeRateException {
        BrazilianCentralBankDataSource brazilianCBDataSource = new BrazilianCentralBankDataSource();
        // is wednesday
        QuotationDaily findQuotationByDate = brazilianCBDataSource.findQuotationByDate("20160629");
        Assert.assertNotNull(findQuotationByDate);
        // is monday
        findQuotationByDate = brazilianCBDataSource.findQuotationByDate("20150810");
        Assert.assertNotNull(findQuotationByDate);
        // is saturday!
        String date = "20160604";
        findQuotationByDate = brazilianCBDataSource.findQuotationByDate(date);
        Assert.assertEquals("2016-06-03", findQuotationByDate.getDate().toString());
        // is sunday!
        date = "20160515";
        findQuotationByDate = brazilianCBDataSource.findQuotationByDate(date);
        Assert.assertEquals("2016-05-13", findQuotationByDate.getDate().toString());
    }

}
