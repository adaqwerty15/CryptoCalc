import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class MainThread {
    public static void main(String[] args) throws IOException, JSONException, InterruptedException {
        System.setProperty("https.proxyHost","proxy.isu.ru");
        System.setProperty("https.proxyPort","3128");
        URL url = new URL("https://api.coinmarketcap.com/v2/listings/");
        Scanner sc =  new Scanner((InputStream) url.getContent());
        String res ="";
        while (sc.hasNext()){
            res+=(sc.next());
        }

        Scanner in = new Scanner(System.in);
        String crypto = in.next();

        JSONObject n = new JSONObject(res);
        int code = -1;
        JSONArray data = n.getJSONArray("data");
        for (int i=0; i<data.length(); i++) {
            if (data.getJSONObject(i).getString("symbol").equals(crypto))
                code = data.getJSONObject(i).getInt("id");
        }

        if (code!=-1) {
            HashMap<String, Double> zav = new HashMap<>();
            ArrayList<String> fiat = new ArrayList<String>(Arrays.asList("AUD", "BRL", "CAD", "CHF", "CLP", "CNY",
                    "CZK", "DKK", "EUR", "GBP", "HKD", "HUF", "IDR", "ILS", "INR", "JPY", "KRW", "MXN", "MYR", "NOK",
                    "NZD", "PHP", "PKR", "PLN", "RUB", "SEK", "SGD", "THB", "TRY", "TWD", "ZAR"));
            for (int i=0; i<fiat.size(); i++) {
                GetCurrencyThread tr = new GetCurrencyThread(fiat.get(i), String.valueOf(code));
                tr.start();
                tr.join();
                zav.put(fiat.get(i), tr.getRes());
            }

            for (Map.Entry<String, Double> pair : zav.entrySet()) {
                System.out.println(crypto+"\\"+pair.getKey() + " - "+pair.getValue());
            }

        }

    }
}
