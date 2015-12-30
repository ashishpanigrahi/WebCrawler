/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webcrawler.bal;

import java.io.IOException;
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
            System.out.println(exc.getMessage());
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
}
