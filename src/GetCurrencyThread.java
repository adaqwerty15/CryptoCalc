import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class GetCurrencyThread extends Thread {
    private String current;
    private String crypto;
    Double res;

    public GetCurrencyThread(String current, String crypto) {
        this.current = current;
        this.crypto = crypto;
    }

    public Double getRes() {
        return res;
    }

    public void run() {
        System.setProperty("https.proxyHost","proxy.isu.ru");
        System.setProperty("https.proxyPort","3128");
        URL url = null;
        try {
            url = new URL("https://api.coinmarketcap.com/v2/ticker/"+crypto+"/?convert="+current);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Scanner sc = null;
        try {
            sc = new Scanner((InputStream) url.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String res ="";
        while (sc.hasNext()){
            res+=(sc.next());
        }

        JSONObject n = new JSONObject();
        try {
             n = new JSONObject(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        double price = 0;

        try {
            price =  n.getJSONObject("data").getJSONObject("quotes").getJSONObject(current).getDouble("price");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.res = price;
    }
}
