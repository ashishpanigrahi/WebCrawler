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

            Set<Domain> domainList = (Set<Domain>) this.getDomainsFromGoogle(queryString
                    + "&num=50&cr=AU", ROOT_QUERY_URL);
            Domain.showDomainNames(domainList);
            System.out.println("\n\nTraversing through the domains to get links\n");
            for (Domain domain : domainList) {
                WebPage.setPages(domain.getDomainUrl());
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
    public Set<Domain> getDomainsFromGoogle(String queryString, String url)
            throws Exception {
        String requestUrl = url + queryString;
        Domain domain = new Domain(requestUrl);
        WebPage webPage = new WebPage(domain);
        webPage.LoadDocumentFomWeb();

        Set<Domain> result = (Set<Domain>) new HashSet();

        try {
            Document doc = webPage.getDocument();
            if (doc != null) {
                // get the list of domain names
                Elements links = doc.select("a[href]");
                for (Element link : links) {
                    String temp = link.attr("href");
                    if (temp.startsWith("/url?q=http://")
                            && (!temp.contains("google"))
                            && (temp.contains(".com")
                            || temp.contains(".au"))) {
                        result.add(new Domain(Domain.getDomainName(temp)));
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
