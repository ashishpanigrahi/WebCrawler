/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webcrawler.bal;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.HashSet;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author AshishPanigrahi
 */
public class WebPageImpl implements WebPageDAO {

    private static final DB DATABASE_OBJECT = new DB();
    private static final String ROOT_QUERY_URL
            = "https://www.google.com.au/search?q=";

    /**
     *
     * @param queryString
     * @return
     */
    public Set<WebPage> setWebPagesFromQueryString(String queryString) {
        Set<WebPage> webPageList = null;
        try {
            webPageList = (Set<WebPage>) this.getUrlsFromGoogleSearch(ROOT_QUERY_URL, queryString
                    + "&num=5&cr=AU");
            for (WebPage webPage : webPageList) {
                setPages(webPage, webPage.getDomain().getDomainUrl());

            }
        } catch (IOException exc) {
            exc.printStackTrace();
        } catch (IllegalArgumentException exc) {
            exc.printStackTrace();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return webPageList;
    }

    /**
     *
     * @param queryString
     * @param url
     * @return
     * @throws Exception
     */
    private Set<WebPage> getUrlsFromGoogleSearch(String url, String queryString)
            throws Exception {
        String requestUrl = url + queryString;
        Domain domain = new Domain(requestUrl);
        WebPage webPage = new WebPage(domain);
        webPage.LoadDocumentFomWeb();

        Set<WebPage> webPageList = (Set<WebPage>) new HashSet();

        try {
            if (webPage.getDocument() != null) {
                // get the list of domain names
                Elements links = webPage.getDocument().select("a[href]");
                for (Element link : links) {
                    String temp = link.attr("href");
                    if (temp.startsWith("/url?q=http://")
                            && (!temp.contains("google"))
                            && (temp.contains(".com")
                            || temp.contains(".au"))) {
                        webPageList.add(new WebPage(new Domain(Domain.getDomainName(temp))));
                    }
                }
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return webPageList;
    }

    /**
     * setPages
     *
     * @param Url
     * @throws SQLException
     * @throws IOException
     */
    private void setPages(WebPage webPage, String Url) throws SQLException, IOException {
        //db.runSql2("TRUNCATE Record;");
        try {
            //check if the given Url is already in database
            String sql = "select URL from WEBPAGE where URL = '" + Url + "'";
            ResultSet resultSet = DATABASE_OBJECT.runSql(sql);
            webPage.LoadDocumentFomWeb();
            if ((!resultSet.next())) {
                //get useful information
                String docStr = webPage.getDocument().text().toLowerCase();
                //get all links and recursively call the setPages method
                if (docStr.contains("sale")
                        || docStr.contains("deal")
                        || docStr.contains("clearance")
                        || docStr.contains("outlet")
                        || docStr.contains("special")
                        || docStr.contains("offer")) {
                    insertWebPage(webPage);
                }
            }
        } catch (NullPointerException | SQLException exc) {
            System.out.println(exc.getMessage());
        }
    }

    @Override
    public void insertWebPage(WebPage webPage) {
        try {
            //store the Url to database to avoid parsing again
            String sql = "INSERT INTO  `Crawler`.`WEBPAGE` " + " VALUES " + "(?,?,?,?,?);";
            PreparedStatement stmt
                    = DATABASE_OBJECT.conn.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, webPage.getWebPageHash());
            stmt.setString(2, webPage.getDomain().getDomainUrl());
            stmt.setString(3, webPage.getDomain().getDomainHash());
            stmt.setString(4, webPage.getDomain().getCreated().toString());
            stmt.setString(5, webPage.getDocument().html());
            stmt.execute();

        } catch (SQLException exc) {
            exc.printStackTrace();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public void deleteWebPage(WebPage webPage) {
        try {
            String sql = "Delete from Crawler.WEBPAGE where WEBPAGEHASH = " + webPage.getWebPageHash() + "AND URL = " + webPage.getDomain().getDomainUrl();
            PreparedStatement stmt
                    = DATABASE_OBJECT.conn.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);
            stmt.execute();
        } catch (SQLException exc) {
            exc.printStackTrace();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public void updateWebPage(WebPage webPage) {
    }

    @Override
    public WebPage getWebPage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<WebPage> getAllWebPages() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void insertImageUrl(WebPage webPage, String imageUrl) {
        try {
            //store the Url to database to avoid parsing again
            String sql = "INSERT INTO  `Crawler`.`PAGEIMAGE` " + " VALUES " + "(?,?);";
            PreparedStatement stmt
                    = DATABASE_OBJECT.conn.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

            stmt.setString(2, webPage.getWebPageHash());
            stmt.setString(1, imageUrl);

            stmt.execute();

        } catch (MySQLIntegrityConstraintViolationException exc) {
            exc.printStackTrace();
        } catch (SQLException exc) {
            exc.printStackTrace();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public String getImageUrl(String imageUrl) {
        try {
            //check if the given Url is already in database
            String sql = "select URL from PAGEIMAGE where  IMAGEURL= '" + imageUrl + "'";
            ResultSet resultSet = DATABASE_OBJECT.runSql(sql);

            if (resultSet.next()) 
                return resultSet.getString(1);
            else 
                return "";
            
        } catch (NullPointerException | SQLException exc) {
            System.out.println(exc.getMessage());
            return "";
        }
    }

    public void getAllImagesFromUrl(Set<WebPage> webPageList) {
        for (WebPage webPage : webPageList) {
            try {
                String imageUrl = "";
                webPage.LoadDocumentFomWeb();
                Elements elements = webPage.getDocument().select("img");

                for (Element element : elements) {
                    imageUrl = element.attr("src");
                    if (imageUrl.toLowerCase().contains("sale")) {
                        if (imageUrl.startsWith("http://")) {
                            insertImageUrl(webPage, imageUrl);
                        }
                    }
                }
            } catch (IOException | NullPointerException exc) {
                System.out.println(exc.getMessage());
            }
        }
    }

}
