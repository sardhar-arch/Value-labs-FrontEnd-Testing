package com.valuelabs.owt.automation;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.*;

public class AmazonApplication {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 20);
    }

    @Test
    public void testAmazonShopping() {
        try {
            // Navigate to Amazon
            driver.get("https://www.amazon.com/");

            // Search for "toys"
            WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
            searchBox.sendKeys("toys");
            searchBox.submit();

            // Add first product to cart
            WebElement firstProduct = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".s-main-slot .s-result-item h2 a")));
            firstProduct.click();
            WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.name("submit.addToCart")));
            addToCartButton.click();

            // Navigate back to search results
            driver.navigate().back();
            driver.navigate().back();

            // Add second product to cart
            WebElement secondProduct = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".s-main-slot .s-result-item h2 a")));
            secondProduct.click();
            WebElement addToCartButton2 = wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-button")));
            addToCartButton2.click();

            // Validate price on Search Results page
            String priceOnSearchResults = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(@class, 'a-price-whole')]"))).getText();

            // Validate price on Product Details page
            WebElement priceElement = (WebElement) ((JavascriptExecutor) driver).executeScript(
                    "return document.querySelector('span[id*=\"priceblock\"]');");

            if (priceElement != null) {
                String priceOnProductDetails = priceElement.getText();
                System.out.println("Price on Product Details page: " + priceOnProductDetails);
            } else {
                System.out.println("Price element not found on product details page!");
            }

            // Validate total price in cart
            WebElement cart = driver.findElement(By.id("nav-cart-count-container"));
            cart.click();
            String totalPrice = driver.findElement(By.cssSelector(".sc-subtotal .a-size-medium")).getText();

            System.out.println("Price on Search Results page: " + priceOnSearchResults);
            System.out.println("Total Price in Cart: " + totalPrice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
