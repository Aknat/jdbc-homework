package ua.kiev.prog.currencyRate;

import java.math.BigDecimal;

public interface CurrencyRateDAO {
    void createTable();

    void addRate(String baseCurrency, String currency,
                 BigDecimal saleRateNB, BigDecimal purchaseRateNB,
                 BigDecimal saleRate, BigDecimal purchaseRate, String date);

    void addRate(CurrencyRate rate, String date);

    void getRate(String date, String currency);

    String selectAverageRate(String[] dates, String currency);

}
