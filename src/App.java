import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by eccolala on 16-9-7.
 */


public class App {
    public static void main(String arg[]) throws IOException {
        String login = "http://202.195.206.35:8080/jsxsd/xk/LoginToXk";
        String grade = "http://202.195.206.35:8080/jsxsd/kscj/cjcx_list";
        String table = "http://jwgl.just.edu.cn:8080/jsxsd/xskb/xskb_list.do";

        Map<String, String> map = new HashMap<String, String>();
        map.put("USERNAME", "1341901213");
        map.put("PASSWORD", "112358gyx");

        Connection conn = Jsoup.connect(login);
        conn.data(map);
        conn.timeout(30000);
        conn.method(Connection.Method.POST);
        conn.userAgent("Mozilla");
        Connection.Response response = conn.execute();
        Map<String, String> cookies = response.cookies();
        Document doc = Jsoup.connect(table)
            .cookies(cookies)
            .timeout(3000)
            .get();
        Elements allTd = doc.select("td");

        for (org.jsoup.nodes.Element element : allTd) {
            String td = element.text();
            System.out.println(td);
        }

    }
}
