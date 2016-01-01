/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webcrawler.bal;

import java.util.Set;

/**
 *
 * @author AshishPanigrahi
 */
interface WebPageDAO {
    public void insertWebPage(WebPage webPage);
    public void deleteWebPage(WebPage webPage);
    public void updateWebPage(WebPage webPage);
    public WebPage getWebPage();
    public Set<WebPage> getAllWebPages();
}
