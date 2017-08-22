package com.jamfnow.qa.links;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Dummy {

    private RemoteWebDriver driver;
    private String hostUrl;

    public static void main(String[] args) throws IOException {
        new Dummy().run();
    }

    public void run() throws IOException {
        File file = new File(Dummy.class.getClassLoader().getResource("test_nomad.yml").getFile());
        LinksConfig linksConfig = new ObjectMapper(new YAMLFactory()).readValue(new FileReader(file), LinksConfig.class);

        ChromeDriverService service = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File(Dummy.class.getClassLoader().getResource("drivers/chromedriver").getFile()))
                .usingAnyFreePort().build();
        service.start();

        driver = new RemoteWebDriver(service.getUrl(), DesiredCapabilities.chrome());
        hostUrl = linksConfig.getUrl().getHost();

        driver.manage().timeouts().pageLoadTimeout(linksConfig.getPageLoadTimeout(), TimeUnit.SECONDS);
        driver.executeScript("document.body.style.zoom='75%'");
        driver.get(linksConfig.getUrl().toString());
        try {
            checkLinks(new LinkedList<>(), linksConfig.getScanDepth());
        }
        finally {
            driver.quit();
            service.stop();
        }
    }

    private void checkLinks(final LinkedList<String> windowsStack, final int depthLevelToCheck) {
        final String nextWindow = driver.getWindowHandles().stream()
                                        .filter(handle -> !windowsStack.contains(handle))
                                        .findFirst().get();
        windowsStack.add(nextWindow);
        driver.switchTo().window(nextWindow);
        waitUntilPageStartsLoading();
        if (!driver.getCurrentUrl().contains(hostUrl) && !driver.getCurrentUrl().equals("about:blank")) {
            System.out.println("Visited " + driver.getCurrentUrl() + " and closed since it's not from this page.");
        }
        else if (depthLevelToCheck < 1) {
            System.out.println("Visited " + driver.getCurrentUrl() + " and closed since it's the lowest checked depth.");
        }
        else {
            final List<WebElement> elements = driver.findElements(By.xpath("//a[@href|@ng-href]|//button"));
            for (WebElement element : elements) {
                final String selectLinkOpenInNewTab = Keys.chord(Keys.COMMAND, Keys.RETURN);
                element.sendKeys(selectLinkOpenInNewTab);
                checkLinks(windowsStack, depthLevelToCheck - 1);
            }
        }
        driver.close();
        windowsStack.removeLast();
        if (!windowsStack.isEmpty()) {
            driver.switchTo().window(windowsStack.getLast());
        }
    }

    private void waitUntilPageStartsLoading() {
        //TODO timeout and then rewriting it properly
        try {
            while (driver.getCurrentUrl().equals("about:blank")) {
                Thread.sleep(200);
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}