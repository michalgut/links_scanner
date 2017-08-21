package com.jamfnow.qa.links;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;

public class LinksConfig {

    @JsonProperty("browser")
    private Browser browser;
    @JsonProperty("page-load-timeout-seconds")
    private long pageLoadTimeout = 10;
    @JsonProperty("login")
    private LoginPage login = null;
    @JsonProperty("url")
    private URL url;
    @JsonProperty("scan-depth")
    private int scanDepth = 1;
    @JsonProperty("is-angular")
    private boolean isAngular;

    public Browser getBrowser() {
        return browser;
    }

    public void setBrowser(final Browser browser) {
        this.browser = browser;
    }

    public long getPageLoadTimeout() {
        return pageLoadTimeout;
    }

    public void setPageLoadTimeout(final long pageLoadTimeout) {
        this.pageLoadTimeout = pageLoadTimeout;
    }

    public LoginPage getLogin() {
        return login;
    }

    public void setLogin(final LoginPage login) {
        this.login = login;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(final URL url) {
        this.url = url;
    }

    public int getScanDepth() {
        return scanDepth;
    }

    public void setScanDepth(final int scanDepth) {
        this.scanDepth = scanDepth;
    }

    public boolean isAngular() {
        return isAngular;
    }

    public void setAngular(final boolean angular) {
        isAngular = angular;
    }
}
