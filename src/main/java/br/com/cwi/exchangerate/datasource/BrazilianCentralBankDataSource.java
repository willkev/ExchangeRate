package br.com.cwi.exchangerate.datasource;

import br.com.cwi.exchangerate.model.Exception.ExchangeRateException;
import br.com.cwi.exchangerate.model.Exception.QuotationDateNotFound;
import br.com.cwi.exchangerate.model.Exception.QuotationDateTimeParseException;
import br.com.cwi.exchangerate.model.QuotationDaily;
import br.com.cwi.exchangerate.model.QuotationDailyItem;
import br.com.cwi.exchangerate.model.QuotationDailyItem.Type;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.DayOfWeek;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Willian Kirschner (willkev@gmail.com)
 */
public class BrazilianCentralBankDataSource {

    private static final Logger LOG = Logger.getLogger(BrazilianCentralBankDataSource.class.getName());

    /**
     * URL template to acessing CVS files from Brasilian Central Bank
     */
    private static final String URL_TEMPLATE_CSV = "http://www4.bcb.gov.br/Download/fechamento/DATE.csv";
    private static final String URL_DATE_REPLACE = "DATE";

    /**
     * Time out to try access URL
     */
    private static final int CONNECTION_TIME_OUT = 13000;

    /**
     * Pattern of date/time format
     */
    private static final String DATE_TIME_PATTERN = "yyyyMMdd";

    /**
     * DateTimeFormatter for {@link #DATE_TIME_PATTERN}
     */
    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    public QuotationDaily findQuotationByDate(String quotationDate) throws ExchangeRateException {
        LocalDate businessDay = getBusinessDay(quotationDate);
        LOG.log(Level.INFO, "Date requested: {0}", businessDay.toString());

        return createQuotationDaily(businessDay);
    }

    private QuotationDaily createQuotationDaily(LocalDate date) throws ExchangeRateException {
        InputStream CSVContentFile = downloadCSVContentFileForDate(date);

        QuotationDaily quotationDaily = new QuotationDaily(date);
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(CSVContentFile))) {
            String line;
            String[] coluns;
            Long code;
            Type type;
            String abbreviationName;
            BigDecimal buyingRate;
            BigDecimal seleRate;
            BigDecimal buyingParity;
            BigDecimal seleParity;
            QuotationDailyItem quotationDailyItem;
            // line content example: 20/11/2014;139;A;MAD;0,28820000;0,29080000;8,75270000;8,82670000
            while ((line = bufferedReader.readLine()) != null) {
                coluns = line.split(";");
                // ignore date
                code = Long.parseLong(coluns[1]);
                type = Type.valueOf(coluns[2]);
                abbreviationName = coluns[3];
                buyingRate = converteCommaFormat(coluns[4]);
                seleRate = converteCommaFormat(coluns[4]);
                buyingParity = converteCommaFormat(coluns[4]);
                seleParity = converteCommaFormat(coluns[4]);

                // TODO: maybe here, it is better to use a 'Builder' 
                quotationDailyItem = new QuotationDailyItem(code, type, abbreviationName,
                        buyingRate, seleRate, buyingParity, seleParity);
                quotationDaily.addItem(quotationDailyItem);
            }
        } catch (Exception ex) {
            throw new ExchangeRateException("Contet of CSV file is invalid to parse for date: " + date, ex);
        }
        return quotationDaily;
    }

    private BigDecimal converteCommaFormat(String value) {
        return new BigDecimal(value.replaceFirst(",", "."));
    }

    private InputStream downloadCSVContentFileForDate(LocalDate date) throws ExchangeRateException {
        String urlToDownload = generateURLForDate(date);

        LOG.log(Level.INFO, "Requested: {0}", urlToDownload);
        HttpURLConnection httpCon = null;
        try {
            httpCon = (HttpURLConnection) new URL(urlToDownload).openConnection();
            httpCon.setDoInput(true);
            httpCon.setRequestMethod("GET");
            httpCon.setReadTimeout(CONNECTION_TIME_OUT);
            httpCon.setConnectTimeout(CONNECTION_TIME_OUT);
            httpCon.connect();
            int respondeCode = httpCon.getResponseCode();
            if (respondeCode == HttpURLConnection.HTTP_NOT_FOUND) {
                throw new QuotationDateNotFound(date);
            }
            if (respondeCode != HttpURLConnection.HTTP_OK) {
                throw new ExchangeRateException("Response code: " + respondeCode + "; From: " + urlToDownload);
            }
            LOG.log(Level.INFO, "Acessing... {0}", urlToDownload);
            return httpCon.getInputStream();
        } catch (IOException ex) {
            // To ensure that it will terminate the connection failed!
            try {
                httpCon.disconnect();
            } catch (Exception e) {
            }
            throw new ExchangeRateException("Unable to connect in: " + urlToDownload, ex);
        }
    }

    private String generateURLForDate(LocalDate date) {
        return URL_TEMPLATE_CSV.replaceFirst(URL_DATE_REPLACE, date.format(DATE_TIME_FORMAT));
    }

    private LocalDate getBusinessDay(String date) throws QuotationDateTimeParseException {
        try {
            LocalDate formatDate = LocalDate.parse(date, DATE_TIME_FORMAT);
            DayOfWeek dayOfWeek = formatDate.getDayOfWeek();
            if (dayOfWeek.equals(SATURDAY)) {
                formatDate = formatDate.minusDays(1);
            } else if (dayOfWeek.equals(SUNDAY)) {
                formatDate = formatDate.minusDays(2);
            }
            return formatDate;
        } catch (Exception ex) {
            throw new QuotationDateTimeParseException("Could not parse date " + date + " from expected format " + DATE_TIME_PATTERN, ex);
        }
    }

}
