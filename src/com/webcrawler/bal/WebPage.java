/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webcrawler.bal;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author AshishPanigrahi
 */
public class WebPage {
    private final Anchor anchor;
    private String webPageHash;
    private int anchorParseStatus;
    private int emailParseStatus;
    private Document document;
    
    public WebPage(Anchor anchor) throws Exception{
        this.anchor             =   anchor;
        this.webPageHash        =   Hasher.toSha256(anchor.getAnchorHash());
        this.anchorParseStatus  =   0;
        this.emailParseStatus   =   0;
    }
    public void LoadDocumentFomWeb()
    {
        try {
            document    =   Jsoup.connect(anchor.getAnchorUrl()).userAgent(
			  "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
			.timeout(10000).get();
        } catch (IOException ex) {
            Logger.getLogger(WebPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Document getDocument() {
        return document;
    }
    
}
