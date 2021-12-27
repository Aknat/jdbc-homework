package ua.kiev.prog.currencyRate;

import ua.kiev.prog.shared.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;

import static ua.kiev.prog.currencyRate.RateHelper.*;

public class Main {
    public static void main(String[] args) {

//        try (Connection conn = ConnectionFactory.getConnection()) {
//            CurrencyRateDAO dao = new CurrencyRate(conn);
//            dao.createTable();
//        } catch (SQLException throwable) {
//            throwable.printStackTrace();
//        }

        String currency = defineCurrency();
        int rate = defineRateType();

        if (rate == 1) {
            try (Connection conn = ConnectionFactory.getConnection()) {
                CurrencyRateDAO dao = new CurrencyRate(conn);
                dao.getRate(defineDate(), currency);
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        } else if (rate == 2) {
            try (Connection conn = ConnectionFactory.getConnection()) {
                CurrencyRateDAO dao = new CurrencyRate(conn);
                dao.selectAverageRate(definePeriod(), currency);
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        }
    }
}
