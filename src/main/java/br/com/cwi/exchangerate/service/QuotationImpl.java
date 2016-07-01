package br.com.cwi.exchangerate.service;

import br.com.cwi.exchangerate.datasource.BrazilianCentralBankDataSource;
import br.com.cwi.exchangerate.model.Exception.ExchangeRateException;
import br.com.cwi.exchangerate.model.Exception.NegativeValueException;
import br.com.cwi.exchangerate.model.QuotationDaily;
import br.com.cwi.exchangerate.model.QuotationDailyItem;
import static br.com.cwi.exchangerate.model.QuotationDailyItem.CURRENCY_BRL;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 *
 * http://www4.bcb.gov.br/pec/conversao/conversao.asp
 *
 * @author Willian Kirschner (willkev@gmail.com)
 */
public class QuotationImpl implements Quotation {

    /**
     * The return value should be rounded to two decimal places
     */
    private final int roundDecimalDigits = 2;

    private final BrazilianCentralBankDataSource brazilianCBDS;

    public QuotationImpl(BrazilianCentralBankDataSource brazilianCBDS) {
        this.brazilianCBDS = brazilianCBDS;
    }

    @Override
    public BigDecimal currencyQuotation(String from, String to, Number value, String quotation) throws ExchangeRateException {
        BigDecimal valueToConvert = new BigDecimal(value.toString());
        if (valueToConvert.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException("Value to convert must be greather or egual to Zero!", value);
        }
        // When 'from' is equal 'to'
        if (from.equals(to)) {
            return valueToConvert;
        }
        QuotationDaily quotationDaily = brazilianCBDS.findQuotationByDate(quotation);
        QuotationDailyItem fromItem = quotationDaily.getQuotationDailyItem(from);

        // BRL not exist em CSV and it should be treated differently
        if (to.equals(CURRENCY_BRL)) {
            return convertToReal(fromItem, valueToConvert);
        }
        QuotationDailyItem toItem = quotationDaily.getQuotationDailyItem(to);
        return convert(fromItem, toItem, valueToConvert);
    }

    private BigDecimal convertToReal(QuotationDailyItem fromItem, BigDecimal valueToConvert) {
        BigDecimal fromBuyingRate = fromItem.getBuyingRate();
        BigDecimal valueInReal = valueToConvert.multiply(fromBuyingRate, MathContext.DECIMAL128);
        return valueInReal.divide(BigDecimal.ONE, roundDecimalDigits, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal convert(QuotationDailyItem fromItem, QuotationDailyItem toItem, BigDecimal valueToConvert) {
        BigDecimal fromBuyingRate = fromItem.getBuyingRate();
//        System.out.println(fromItem.getAbbreviationName() + " " + fromBuyingRate);

        BigDecimal toBuyingRate = toItem.getBuyingRate();
//        System.out.println(toItem.getAbbreviationName() + " " + toBuyingRate);

        BigDecimal valueInReal = valueToConvert.multiply(fromBuyingRate, MathContext.DECIMAL128);
//        System.out.println(valueInReal);

        BigDecimal divide = valueInReal.divide(toBuyingRate, roundDecimalDigits, BigDecimal.ROUND_HALF_UP);
//        System.out.println(divide);
        return divide;
    }

}
