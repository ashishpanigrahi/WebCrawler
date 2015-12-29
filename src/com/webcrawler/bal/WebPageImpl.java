/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webcrawler.bal;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.HashSet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

/**
 *
 * @author AshishPanigrahi
 */
public class WebPageImpl {

    private static int rowIndex;
    private static final DB DB_OBJ = new DB();
    private static final String ROOT_QUERY_URL
            = "https://www.google.com.au/search?q=";
    /**
     *
     * @param queryString
     */
    public void setWebPagesFromDomain(String queryString) {
        try {
            WebPageImpl obj = new WebPageImpl();
            Set<Domain> domainList = (Set<Domain>) obj.getUrlsFromGoogle(queryString
                    + "&num=50&cr=AU", ROOT_QUERY_URL);
            Domain.showDomainNames(domainList);
            System.out.println("\n\nTraversing through the domains to get links\n");
            for (Domain domain : domainList) {
                setPages(domain.getDomainUrl());
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
     * getUrlsFromGoogle
     *
     * @param queryString
     * @return
     * @throws Exception
     */
    private Set<Domain> getUrlsFromGoogle(String queryString, String url)
            throws Exception {
        String requestUrl = url + queryString;

        Domain domain = new Domain(requestUrl);
        Anchor anchor = new Anchor(domain, requestUrl);

        WebPage webPage = new WebPage(anchor);
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

    /**
     * setPages
     *
     * @param Url
     * @throws SQLException
     * @throws IOException
     */
    private void setPages(String Url) throws SQLException, IOException {
        //db.runSql2("TRUNCATE Record;");
        try {
            //check if the given Url is already in database
            String sql = "select URL from Record where URL = '" + Url + "'";
            ResultSet resultSet = DB_OBJ.runSql(sql);

            if (!resultSet.next()) {
                //get useful information
                Document doc = Jsoup.connect(Url).get();
                String docStr = doc.text().toLowerCase();
                //get all links and recursively call the setPages method
                if (docStr.contains("sale")
                        || docStr.contains("deal")
                        || docStr.contains("clearance")
                        || docStr.contains("outlet")
                        || docStr.contains("special")
                        || docStr.contains("offer")) {
                    try {
                        System.out.println(Url);
                        //store the Url to database to avoid parsing again
                        sql = "INSERT INTO  `Crawler`.`Record` " + " VALUES " + "(?,?);";
                        PreparedStatement stmt
                                = DB_OBJ.conn.prepareStatement(sql,
                                        Statement.RETURN_GENERATED_KEYS);
                        stmt.setString(1, String.valueOf(rowIndex++));
                        stmt.setString(2, Url);
                        stmt.execute();
                        //setPages(link.attr("abs:href"));
                    } catch (SQLException exc) {
                    } catch (Exception exc) {
                        exc.printStackTrace();
                    }
                }
            }

        } catch (IllegalArgumentException | IOException exc) {
            System.out.println(exc.getMessage());
        } catch (SQLException exc) {
        }
    }
}
