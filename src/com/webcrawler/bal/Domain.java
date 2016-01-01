/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webcrawler.bal;

import java.sql.Timestamp;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author AshishPanigrahi
 */
public class Domain {

    private String domainHash;
    private String domainUrl;
    private Timestamp created;

    

    public String getDomainHash() {
        return domainHash;
    }

    public String getDomainUrl() {
        return domainUrl;
    }


    public Timestamp getCreated() {
        return created;
    }

    public Domain(String domainUrl) throws Exception {
        this.domainHash = Hasher.toSha256(domainUrl);
        this.domainUrl = domainUrl;
        this.created = CommonBal.getTimeStamp();
    }

    public Domain(String domainHash, String domainUrl, boolean activated, Timestamp created) {
        this.domainHash = domainHash;
        this.domainUrl = domainUrl;
        this.created = created;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        Domain domain = (Domain) obj;
        return (domain.getDomainHash().equals(this.getDomainHash()));
    }

    @Override
    public int hashCode() {
        return this.getDomainUrl().length(); //To change body of generated methods, choose Tools | Templates.
    }
    /**
     * getDomainName
     *
     * @param url
     * @return
     */
    public static String getDomainName(String url) {
        Matcher matcher;
        String DOMAIN_FORMAT
            = "(([a-zA-Z0-9:/]{7,8}))+"
            + "([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,63}[a-zA-Z0-9])?\\.)"
            + "+[a-zA-Z]{2,6}";
        
        Pattern PATTERN_NAME = Pattern.compile(DOMAIN_FORMAT);
    
        String domainName = "";
        matcher = PATTERN_NAME.matcher(url);
        if (matcher.find()) 
            domainName = matcher.group(0).toLowerCase().trim();
        return domainName;
    }
       /**
     * showDomainNames
     *
     * @param domainList
     */
    public static void showDomainNames(Set<Domain> domainList) {
        // display domains in console
        System.out.println("\nList of Domains found:\n");
        for (Domain temp : domainList) {
            System.out.println(temp.getDomainUrl());
        }
    }
}
