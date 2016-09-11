import java.io.IOException;
import java.util.ArrayList;
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
        final String login = "http://202.195.206.35:8080/jsxsd/xk/LoginToXk";
        final String grade = "http://202.195.206.35:8080/jsxsd/kscj/cjcx_list";
        final String table = "http://202.195.206.35:8080/jsxsd/xskb/xskb_list.do";

        ArrayList<ClassBean> classList = new ArrayList<>();

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
            .get();

        Elements tables = doc.select("table[id=kbtable]");

        Elements trs = tables.select("tr");

        //周数
        for (int tr_index = 0; tr_index < trs.size(); tr_index++) {

            Elements divs = trs.get(tr_index).select("div[class=kbcontent]");

            // System.out.println(divs);

            for (int div_index = 0; div_index < divs.size(); div_index++) {

                String html = divs.get(div_index).outerHtml().replaceAll("<br>", "#~#");
                Document doc1 = Jsoup.parse(html.toString());
                String newHtml = doc1.text();

                //去除没有课程信息的课程
                if (newHtml.length() < 2) {
                    continue;
                }

                String[] ary = newHtml.split("#~#");

                //创建一个课程
                ClassBean bean = new ClassBean();

                //遍历数组
                for (int i = 0; i < ary.length; i++) {

                    //去除所有空格
                    ary[i] = ary[i].replaceAll("\\s+", "");

                    //课程坐标
                    bean.posX = div_index;

                    bean.posY = tr_index;

                    //一种课程选择情况
                    if (i % 5 == 0) {

                        bean.classNum = ary[i];

                    } else if (i % 5 == 1) {

                        bean.className = ary[i];

                    } else if (i % 5 == 2) {

                        bean.techName = ary[i];

                    } else if (i % 5 == 3) {

                        String time = ary[i];

                        time = time.substring(0, time.length() - 3);

                        String[] dots = time.split(",");

                        bean.classTime = dots;

                    } else if (i % 5 == 4) {
                        bean.classAddr = ary[i];
                    }

                    System.out.println(ary[i].replaceAll("\\s+", ""));

                }

                //把课程加入课表
                classList.add(bean);

            }

        }

        System.out.println(classList.size());

        for (ClassBean bean : classList) {

            System.out.println(bean.posY);

        }

    }
}
