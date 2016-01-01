/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler.ut;

import com.webcrawler.bal.WebPage;
import com.webcrawler.bal.WebPageImpl;
import java.util.Set;

/**
 *
 * @author AshishPanigrahi
 */
public class WebCrawlerUT {

    /**
     * @param args the command line arguments
     */

    public static void main(String args[]) {
        WebPageImpl obj = new WebPageImpl();
        Set<WebPage> webPageList = obj.setWebPagesFromQueryString("fashion+online+store");
        obj.getAllImagesFromUrl(webPageList);
    }
}
