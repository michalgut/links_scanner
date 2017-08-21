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
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Dummy {


    public static void main(String[] args) throws IOException, InterruptedException {
        File file = new File(Dummy.class.getClassLoader().getResource("test_nomad.yml").getFile());
        LinksConfig linksConfig = new ObjectMapper(new YAMLFactory()).readValue(new FileReader(file), LinksConfig.class);

        ChromeDriverService service = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File(Dummy.class.getClassLoader().getResource("drivers/chromedriver").getFile()))
                .usingAnyFreePort().build();
        service.start();

        RemoteWebDriver driver = new RemoteWebDriver(service.getUrl(), DesiredCapabilities.chrome());

        driver.manage().timeouts().pageLoadTimeout(linksConfig.getPageLoadTimeout(), TimeUnit.SECONDS);
        driver.executeScript("document.body.style.zoom='75%'");
        driver.get(linksConfig.getUrl().toString());


        for (int i = 0; i < linksConfig.getScanDepth(); i++) {
            for (String window : driver.getWindowHandles()) {
                driver.switchTo().window(window);
                if (!driver.getCurrentUrl().contains(linksConfig.getUrl().getHost()) && !driver.getCurrentUrl().equals("about:blank")) {
                    System.out.println("Visited " + driver.getCurrentUrl() + " and closed since it's not from this page.");
                    driver.close();
                }
            }

            List<WebElement> elements = driver.findElements(By.xpath("//a[@href|@ng-href]|//button"));

            for (WebElement element : elements) {
                String selectLinkOpeninNewTab = Keys.chord(Keys.COMMAND, Keys.RETURN);
                element.sendKeys(selectLinkOpeninNewTab);
            }

            String currentTab = driver.getWindowHandle();
            String nextTab = driver.getWindowHandles().toArray()[1].toString();
            for (String window : driver.getWindowHandles()) {
                driver.switchTo().window(window);
            }
            driver.switchTo().window(currentTab).close();
            driver.switchTo().window(nextTab);
        }

        driver.quit();
        service.stop();
    }
}
