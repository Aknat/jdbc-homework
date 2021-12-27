package ua.kiev.prog.currencyRate;

import java.math.BigDecimal;
import java.sql.*;

public class CurrencyRate implements CurrencyRateDAO {
    private final Connection conn;

    private String baseCurrency;
    private String currency;
    private BigDecimal saleRateNB;
    private BigDecimal purchaseRateNB;
    private BigDecimal saleRate;
    private BigDecimal purchaseRate;

    public CurrencyRate(Connection conn) {
        this.conn = conn;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getSaleRateNB() {
        return saleRateNB;
    }

    public void setSaleRateNB(BigDecimal saleRateNB) {
        this.saleRateNB = saleRateNB;
    }

    public BigDecimal getPurchaseRateNB() {
        return purchaseRateNB;
    }

    public void setPurchaseRateNB(BigDecimal purchaseRateNB) {
        this.purchaseRateNB = purchaseRateNB;
    }

    public BigDecimal getSaleRate() {
        return saleRate;
    }

    public void setSaleRate(BigDecimal saleRate) {
        this.saleRate = saleRate;
    }

    public BigDecimal getPurchaseRate() {
        return purchaseRate;
    }

    public void setPurchaseRate(BigDecimal purchaseRate) {
        this.purchaseRate = purchaseRate;
    }


    @Override
    public void createTable() {
        try {
            try (Statement st = conn.createStatement()) {
                st.execute("DROP TABLE IF EXISTS Rates");
                st.execute("CREATE TABLE Rates (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                        "baseCurrency VARCHAR(10) NOT NULL, currency VARCHAR(10) NOT NULL, " +
                        "saleRateNB DECIMAL(9,7), purchaseRateNB DECIMAL(9,7), " +
                        "saleRate DECIMAL (9,7), purchaseRate DECIMAL(9,7), date VARCHAR(10))");
                st.execute("CREATE INDEX ratePerDay ON Rates(currency, date)");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }


    @Override
    public void addRate(String baseCurrency, String currency,
                        BigDecimal saleRateNB, BigDecimal purchaseRateNB,
                        BigDecimal saleRate, BigDecimal purchaseRate, String date) {
        try {
            try (PreparedStatement st = conn.prepareStatement("INSERT INTO Rates " +
                    "(baseCurrency, currency, saleRateNB, purchaseRateNB, saleRate, purchaseRate, date) VALUES(?, ?, ?, ?, ?, ?, ?)")) {
                st.setString(1, baseCurrency);
                st.setString(2, currency);
                st.setBigDecimal(3, saleRateNB);
                st.setBigDecimal(4, purchaseRateNB);
                st.setBigDecimal(5, saleRate);
                st.setBigDecimal(6, purchaseRate);
                st.setString(7, date);
                st.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }


    @Override
    public void addRate(CurrencyRate rate, String date) {
        try {
            try (PreparedStatement st = conn.prepareStatement("INSERT INTO Rates " +
                    "(baseCurrency, currency, saleRateNB, purchaseRateNB, saleRate, purchaseRate, date) VALUES(?, ?, ?, ?, ?, ?, ?)")) {
                st.setString(1, rate.baseCurrency);
                st.setString(2, rate.currency);
                st.setBigDecimal(3, rate.saleRateNB);
                st.setBigDecimal(4, rate.purchaseRateNB);
                st.setBigDecimal(5, rate.saleRate);
                st.setBigDecimal(6, rate.purchaseRate);
                st.setString(7, date);
                st.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }


    @Override
    public void getRate(String date, String currency) {
        CurrencyRate rate = selectRate(date, currency);

        if (rate.isEmpty()) {
            CurrencyRateList list = new CurrencyRateList();
            list.getRateListFromSource(date);
            rate = selectRate(date, currency);
        }
        System.out.println("Requested exchange rate for the date [" + date + "] for [" + currency + "] is the following:" + rate);
    }


    private CurrencyRate selectRate(String date, String currency) {
        CurrencyRate rate = new CurrencyRate(conn);

        try (Statement st = conn.createStatement()) {

            try (ResultSet rs = st.executeQuery("SELECT * FROM Rates WHERE date='" + date + "' AND currency='" + currency + "'")) {
                while (rs.next()) {
                    rate.setBaseCurrency(rs.getString(2));
                    rate.setCurrency(rs.getString(3));
                    rate.setSaleRateNB(rs.getBigDecimal(4));
                    rate.setPurchaseRateNB(rs.getBigDecimal(5));
                    rate.setSaleRate(rs.getBigDecimal(6));
                    rate.setPurchaseRate(rs.getBigDecimal(7));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return rate;
    }


    @Override
    public String selectAverageRate(String[] dates, String currency) {
        String startDate = dates[0];
        String endDate = dates[1];
        String average = null;
        try (Statement st = conn.createStatement()) {

            try (ResultSet rs = st.executeQuery("SELECT CAST(AVG(saleRate) AS DECIMAL(9, 7)) FROM Rates " +
                    "WHERE currency='" + currency +
                    "' AND date<='" + endDate + "' AND date>='" + startDate + "'")) {
                while (rs.next()) {
                    average = rs.getString(1);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        System.out.println("Requested average rate for the period " +
                "from [" + startDate + "] to [" + endDate + "] for [" + currency + "] is the following: " + average);
        return average;
    }


    public boolean isEmpty() {
        return (this.baseCurrency == null || this.currency == null || this.saleRateNB == null || this.purchaseRateNB == null);
    }


    @Override
    public String toString() {
        return "\n" +
                "baseCurrency='" + baseCurrency + '\'' +
                ", currency='" + currency + '\'' +
                ", saleRateNB=" + saleRateNB +
                ", purchaseRateNB=" + purchaseRateNB +
                ", saleRate=" + saleRate +
                ", purchaseRate=" + purchaseRate;
    }
}
