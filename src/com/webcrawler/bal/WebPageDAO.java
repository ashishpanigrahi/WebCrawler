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
    public boolean insertWebPage();
    public boolean deleteWebPage();
    public boolean updateWebPage();
    public WebPage getWebpage();
    public Set<WebPage> getAllWebPages();
}
