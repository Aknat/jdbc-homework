package ua.kiev.prog.currencyRate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ua.kiev.prog.shared.ConnectionFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class CurrencyRateList {
    private final Gson gson;
    String date;
    String bank;
    int baseCurrency;
    String baseCurrencyLit;
    ArrayList<CurrencyRate> exchangeRate;

    public CurrencyRateList() {
        gson = new GsonBuilder().create();
    }

    public void getRateListFromSource(String date) {

        URL url = null;
        HttpsURLConnection connection = null;
        CurrencyRateList list = null;
        try {
            url = new URL("https://api.privatbank.ua/p24api/exchange_rates?json&date=" + date);
            System.out.println("APP LOG: the request to privat-API is the following: " + url);
            connection = (HttpsURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();
            byte[] buf = responseBodyToArray(is);
            String strBuf = new String(buf, StandardCharsets.UTF_8);

            list = gson.fromJson(strBuf, CurrencyRateList.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Connection conn = ConnectionFactory.getConnection()) {
            CurrencyRateDAO dao = new CurrencyRate(conn);

            for (int i = 0; i < list.exchangeRate.size(); i++) {
                if (!list.exchangeRate.get(i).isEmpty()) dao.addRate(list.exchangeRate.get(i), list.date);
            }
            System.out.println("APP LOG: wow! data from privat-API is received and stored to db)");

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }


    public void getMultipleRateListFromSource(ArrayList<String> dates) {
        for (String s : dates) {
            getRateListFromSource(s);
        }
    }


    private static byte[] responseBodyToArray(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[10240];
        int r;

        do {
            r = is.read(buf);
            if (r > 0) bos.write(buf, 0, r);
        } while (r != -1);
        return bos.toByteArray();
    }


    @Override
    public String toString() {
        return "CurrencyRateList{" +
                "date='" + date + '\'' +
                ", bank='" + bank + '\'' +
                ", baseCurrency=" + baseCurrency +
                ", baseCurrencyLit='" + baseCurrencyLit + '\'' + "," +
                "\n" + "exchangeRate=" + exchangeRate + '}';
    }
}
