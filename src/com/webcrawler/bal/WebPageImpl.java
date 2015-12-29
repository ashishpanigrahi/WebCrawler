/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webcrawler.bal;

import java.util.Set;
import java.util.HashSet;
import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author AshishPanigrahi
 */
public class WebPageImpl implements WebPageDAO{
    private static final String ROOT_QUERY_URL
            = "https://www.google.com.au/search?q=";
    /**
     *
     * @param queryString
     */
    public void setWebPagesFromQueryString(String queryString) {
        try {

            Set<WebPage> webPageList = (Set<WebPage>) this.getDomainsFromGoogle(ROOT_QUERY_URL,queryString
                    + "&num=50&cr=AU");
            for (WebPage webPage : webPageList) {
                WebPage.setPages(webPage.getDomain().getDomainUrl());
            }
        } catch (IllegalArgumentException exc) {
            exc.printStackTrace();
        } catch (HttpStatusException exc) {
            exc.printStackTrace();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    /**
     *
     * @param queryString
     * @param url
     * @return
     * @throws Exception
     */
    public Set<WebPage> getDomainsFromGoogle(String url, String queryString)
            throws Exception {
        String requestUrl = url + queryString;
        Domain domain = new Domain(requestUrl);
        WebPage webPage = new WebPage(domain);
        webPage.LoadDocumentFomWeb();

        Set<WebPage> result = (Set<WebPage>) new HashSet();

        try {
            webPage.getDocument();
            if (webPage.getDocument() != null) {
                // get the list of domain names
                Elements links = webPage.getDocument().select("a[href]");
                for (Element link : links) {
                    String temp = link.attr("href");
                    if (temp.startsWith("/url?q=http://")
                            && (!temp.contains("google"))
                            && (temp.contains(".com")
                            || temp.contains(".au"))) {
                        result.add(new WebPage(new Domain(Domain.getDomainName(temp))));
                    }
                }
            }
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
        return result;
    }

    @Override
    public boolean insertWebPage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteWebPage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateWebPage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public WebPage getWebpage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<WebPage> getAllWebPages() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
