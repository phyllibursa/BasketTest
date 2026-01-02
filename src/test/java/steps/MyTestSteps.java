package steps;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.After;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.edge.EdgeDriver;


public class MyTestSteps {
    WebDriver driver;

    @Given("Jag är på registreringssidan")
    public void jagArPaRegistreringssidan() {

        if (driver == null) { //Ändrade om detta så att koden börjar med null och inte råkar krocka ihop när jag la dit edge webbläsaren,
            //så den ser till att ha chrome igång o redo och råkar inte öppna 2 st webbläsare samtidigt när vi kör testerna.
            driver = new ChromeDriver();
            driver.manage().window().maximize();
        }
        driver.get("file:///C:/Users/phyll/Downloads/Register/Register.html");
    }

    @When("Jag fyller i alla fält korrekt")
    public void jagFyllerIAllaFältKorrekt() {
        //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20)); <---Detta gör vi till kommentar och ersätter den med wait metoden längst ner
        //Detta gör så att vår kod ser renare ut och mindre att skriva.

        WebElement dateField = waitForElement(By.name("DateOfBirth")); //<-- Då skriver vi annorlunda på denna raden med. Har suddat bort lite och skrivit om.
        dateField.sendKeys("01/01/2001");
        dateField.sendKeys(Keys.TAB);


        // 2. För och Efternamn
        driver.findElement(By.name("Forename")).sendKeys("Barbie");
        driver.findElement(By.name("Surname")).sendKeys("Kensson");

        // 3. Unik e-postadress, för att om vi skriver samma email varje gång, kommer sidan säga "Redan registrerad" andra gången vi kör.
        //System.currentTimeMillis() är en klocka som ger oss en unik siffra varje gång vi trycker på play.
        String unikEmail = "user" + System.currentTimeMillis() + "@test.com";
        //Nu skriver vi in den unika adressen i båda rutorna

        driver.findElement(By.name("EmailAddress")).sendKeys(unikEmail);
        driver.findElement(By.name("ConfirmEmailAddress")).sendKeys(unikEmail);

        //4. Lösenord
        driver.findElement(By.name("Password")).sendKeys("Password000!");
        driver.findElement(By.name("ConfirmPassword")).sendKeys("Password000!");

        //5. "Terms and Conditions"- checkbox och åldern checkboxen, fick lägga Javascript för det funkade inte med det förra.
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.name("TermsAccept")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.name("AgeAccept")));

        //Checkbox 2: (åldern)
        //driver.findElement(By.name("AgeAccept")).click(); <--funkade inte med detta

    }

    @And("Jag klickar på Join-knappen")
    public void jagKlickarPåJoinKnappen() {

        //Checkbox nr 3 som är längst ner för att kunna joina:
       //driver.findElement(By.name("AgreeToCodeOfEthicsAndConduct")).click(); <-- Den här funkade inte riktigt heller så får ersätta med JSE
        WebElement codeBox = driver.findElement(By.name("AgreeToCodeOfEthicsAndConduct"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", codeBox);

       WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        //Sen vänta på att knappen syns och är klickbar
        WebElement joinButton = wait.until(ExpectedConditions.elementToBeClickable(By.name("join")));
        joinButton.click();
    }

    @Then("Jag ska få ett meddelande om att kontot har skapats")
    public void jagSkaFåEttMeddelandeOmAttKontotHarSkapats() throws InterruptedException {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        //Hämtar all text som finns i ("body"), det var innan och det var för vagt. Lägger med h2
        WebElement successHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h2")));

        String actualText = successHeader.getText();
        String ExpectedText = "THANK YOU FOR CREATING AN ACCOUNT WITH BASKETBALL ENGLAND";

        //Det funkade inte alls med Assert och jag höll på i flera dagar så jag ersätter med if/else-sats
        if (actualText.contains(ExpectedText)) {
            System.out.println("The test was a success 🤩✔️ Found the text " + ExpectedText);
        } else {
            System.out.println("The test fail😪❌ Could not find " + ExpectedText);
            //Nu tvingar vi testet att bli rött om den misslyckas så att vi inte får något som heter "false positive" och tar därför med 'throw new'
            throw new RuntimeException("The test failed because the text was not found");
        }

    }

    @When("Jag fyller i alla fält men lämnar efternamn tomt")
    public void jagFyllerIAllaFältMenLämnarEfternamnTomt() {
        //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        //Fyller i datumet
        WebElement dateField = waitForElement(By.name("DateOfBirth"));
        dateField.sendKeys("01/01/2001");
        dateField.sendKeys(Keys.TAB);

        //Fyller i förnamnet men skippar efternamnet
        driver.findElement(By.name("Forename")).sendKeys("Barbie");

        //Fyller i email
        String unikEmail = "user" + System.currentTimeMillis() + "@test.com";
        driver.findElement(By.name("EmailAddress")).sendKeys(unikEmail);
        driver.findElement(By.name("ConfirmEmailAddress")).sendKeys(unikEmail);

        //Fyller i lösenordet
        driver.findElement(By.name("Password")).sendKeys("Password000!");
        driver.findElement(By.name("ConfirmPassword")).sendKeys("Password000!");

        //Fick hjälp att få fram dessa för att kunna få rätt checkboxar inkryssade.
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.name("TermsAccept")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.name("AgeAccept")));

    }

    @Then("Jag ska se ett felmeddelande om att efternamn krävs")
    public void jagSkaSeEttFelmeddelandeOmAttEfternamnKrävs() {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span[data-valmsg-for='Surname']")));

        String actualText = errorMsg.getText();
        String expectedText = "Last Name is required"; //Får detta felmeddelandet på sidan när jag skippar att fylla i efternamnet.

        Assertions.assertTrue(actualText.contains(expectedText), "Felmeddelandet visades inte!");

    }

    @When("Jag fyller i alla fält men skriver olika lösenord")
    public void jagFyllerIAllaFältMenSkriverOlikaLösenord() {
        //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        //Fyller i datumet
        WebElement dateField = waitForElement(By.name("DateOfBirth"));
        dateField.sendKeys("01/01/2001");
        dateField.sendKeys(Keys.TAB);

        //Fyller i för o efternamnet
        driver.findElement(By.name("Forename")).sendKeys("Barbie");
        driver.findElement(By.name("Surname")).sendKeys("Kensson");

        //Fyller i email
        String unikEmail = "user" + System.currentTimeMillis() + "@test.com";
        driver.findElement(By.name("EmailAddress")).sendKeys(unikEmail);
        driver.findElement(By.name("ConfirmEmailAddress")).sendKeys(unikEmail);

        // Nu ska vi fylla i fel lösenord:
        driver.findElement(By.name("Password")).sendKeys("Password000!");
        driver.findElement(By.name("ConfirmPassword")).sendKeys("Password777!");

        //Klickar i dem gömda checkboxar som vi hade svårt att hitta. Men vi sparar join knappen till nästa steg:
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.name("TermsAccept")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.name("AgeAccept")));

    }

    @Then("Jag ska se ett felmeddelande om att lösenorden inte matchar")
    public void jagSkaSeEttFelmeddelandeOmAttLösenordenInteMatchar() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        //Dags att leta efter cssSelectorn igen som är mer specifik.
        WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span[data-valmsg-for='ConfirmPassword']")));

        String actualText = errorMsg.getText();
        String expectedText = "Password did not match";

        Assertions.assertTrue(actualText.contains(expectedText), "Felmeddelandet visades inte");

    }

    @When("Jag fyller i alla fält men kryssar inte i Terms and Conditions")
    public void jagFyllerIAllaFältMenKryssarInteITermsAndConditions() {
        //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement dateField = waitForElement(By.name("DateOfBirth"));
        dateField.sendKeys("01/01/2001");
        dateField.sendKeys(Keys.TAB);

        String unikEmail = "user" + System.currentTimeMillis() + "@test.com";
        driver.findElement(By.name("EmailAddress")).sendKeys(unikEmail);
        driver.findElement(By.name("ConfirmEmailAddress")).sendKeys(unikEmail);

        driver.findElement(By.name("Password")).sendKeys("Password000!");
        driver.findElement(By.name("ConfirmPassword")).sendKeys("Password000!");

        //Nu ska testet utföras, Vi klickar inte på "TearmsAccept", utan bara på den andra (AgeAccept).
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.name("AgeAccept")));
    }

    @Then("Jag ska se ett felmeddelande om att villkoren måste godkännas")
    public void jagSkaSeEttFelmeddelandeOmAttVillkorenMåsteGodkännas() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        //Skriver i felmeddelandet för Terms-boxen:
        WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span[data-valmsg-for='TermsAccept']")));

        String actualText = errorMsg.getText();
        Assertions.assertTrue(actualText.contains("You must confirm that you have read and accepted our Terms and Conditions"), "Felmeddelandet för Terms visades inte!");

    }
    @After
    public void stängWebbläsaren() {
        // Denna körs ALLTID efter varje scenario, oavsett om det blev rött eller grönt.
        if (driver != null) {
            driver.quit();
        }
    }
    private WebElement waitForElement(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    @When("Jag fyller i alla fält men med lösenord {string} och bekräftelse {string}")
    public void jagFyllerIAllaFältMenMedLösenordOchBekräftelse(String pass, String confirm) {
        WebElement dateField = waitForElement(By.name("DateOfBirth"));
        dateField.sendKeys("01/01/2001");
        dateField.sendKeys(Keys.TAB);

        driver.findElement(By.name("Forename")).sendKeys("Barbie");
        driver.findElement(By.name("Surname")).sendKeys("Kensson");

        String unikEmail = "user" + System.currentTimeMillis() + "@test.com";
        driver.findElement(By.name("EmailAddress")).sendKeys(unikEmail);
        driver.findElement(By.name("ConfirmEmailAddress")).sendKeys(unikEmail);

        driver.findElement(By.name("Password")).sendKeys(pass);
        driver.findElement(By.name("ConfirmPassword")).sendKeys(confirm);

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.name("TermsAccept")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.name("AgeAccept")));
    }

    @Given("Jag startar webbläsaren {string}")
    public void jagStartarWebbläsaren(String browserName) {
        //Här lägger jag min Scenario Outline så användaren kan välja att öppna i två olika browsers:Edge eller Chrome, med hjälp av if-sats.
        if (browserName.equalsIgnoreCase("Edge")) {
            driver = new EdgeDriver();
        } else {
            driver = new ChromeDriver();
        }
        driver.manage().window().maximize();
    }
}


