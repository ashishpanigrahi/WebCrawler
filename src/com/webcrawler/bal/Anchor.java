/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webcrawler.bal;

import java.sql.Timestamp;

/**
 *
 * @author AshishPanigrahi
 */
public class Anchor {
    private Domain domain;
    private String anchorHash;
    private String anchorUrl;
    private boolean activated;
    private int scanStatus;
    private Timestamp created;
    private Timestamp modified;

    public Domain getDomain() {
        return domain;
    }

    public String getAnchorHash() {
        return anchorHash;
    }

    public String getAnchorUrl() {
        return anchorUrl;
    }

    public boolean isActivated() {
        return activated;
    }

    public int getScanStatus() {
        return scanStatus;
    }

    public Timestamp getCreated() {
        return created;
    }

    public Timestamp getModified() {
        return modified;
    }

    public Anchor(Domain domain, String anchorUrl) throws Exception{
        this.domain     =   domain;
        this.anchorUrl  =   anchorUrl;
        this.activated  =   true;
        this.anchorHash =   Hasher.toSha256(anchorUrl);
        this.created    =   CommonBal.getTimeStamp();
        this.modified   =   CommonBal.getTimeStamp();
        this.scanStatus =   0;
    }

    public Anchor(Domain domain, String anchorHash, String anchorUrl) {
        this.domain     =   domain;
        this.anchorHash =   anchorHash;
        this.anchorUrl  =   anchorUrl;
    }

    public Anchor(Domain domain, String anchorHash, String anchorUrl, boolean activated, int scanStatus, Timestamp created, Timestamp modified) {
        this.domain     =   domain;
        this.anchorHash =   anchorHash;
        this.anchorUrl  =   anchorUrl;
        this.activated  =   activated;
        this.scanStatus =   scanStatus;
        this.created    =   created;
        this.modified   =   modified;
    }
    
}
