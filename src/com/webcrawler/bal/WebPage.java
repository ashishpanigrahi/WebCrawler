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
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author AshishPanigrahi
 */
public class WebPage {

    private Domain domain;
    private String webPageHash;
    private Document document;

    private static int rowIndex = 1;
    private static final DB DATABASE_OBJECT = new DB();

    public WebPage(Domain domain) throws Exception {
        this.domain = domain;
        this.webPageHash = Hasher.toSha256(domain.getDomainHash());
    }

    public void LoadDocumentFomWeb() throws IOException {
        try {
            this.document = Jsoup.connect(domain.getDomainUrl()).userAgent(
                    "Mozilla/5.0 (compatible; Googlebot/2.1; "
                            + "+http://www.google.com/bot.html)").timeout(5000).get();
        } catch (HttpStatusException exc) {
            exc.printStackTrace();
        } 
    }

    public Document getDocument() {
        return document;
    }

    public Domain getDomain() {
        return domain;
    }

    public String getWebPageHash() {
        return webPageHash;
    }

    public static int getRowIndex() {
        return rowIndex;
    }

    /**
     * setPages
     *
     * @param Url
     * @throws SQLException
     * @throws IOException
     */
    public void setPages(String Url) throws SQLException, IOException {
        //db.runSql2("TRUNCATE Record;");
        try {
            //check if the given Url is already in database
            String sql = "select URL from Record where URL = '" + Url + "'";
            ResultSet resultSet = DATABASE_OBJECT.runSql(sql);
            this.LoadDocumentFomWeb();
            if ((!resultSet.next())) {
                //get useful information
                String docStr = this.getDocument().text().toLowerCase();
                //get all links and recursively call the setPages method
                if (docStr.contains("sale")
                        || docStr.contains("deal")
                        || docStr.contains("clearance")
                        || docStr.contains("outlet")
                        || docStr.contains("special")
                        || docStr.contains("offer")) {
                    try {
                        //store the Url to database to avoid parsing again
                        sql = "INSERT INTO  `Crawler`.`Record` " + " VALUES " + "(?,?);";
                        PreparedStatement stmt
                                = DATABASE_OBJECT.conn.prepareStatement(sql,
                                        Statement.RETURN_GENERATED_KEYS);
                        stmt.setString(1, String.valueOf(rowIndex++));
                        stmt.setString(2, Url);
                        stmt.execute();
                    } catch (SQLException exc) {
                        exc.printStackTrace();
                    } catch (Exception exc) {
                        exc.printStackTrace();
                    }
                }
            }
        } catch (NullPointerException | SQLException exc) {
            exc.printStackTrace();
        }
    }
}
