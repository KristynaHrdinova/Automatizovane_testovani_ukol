package cz.czechitas.selenium;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestyPrihlasovaniNaKurzy {

    public static final String URL = "https://cz-test-jedna.herokuapp.com";
    public static final String JMENO = "Jana Nová";
    public static final String HESLO = "Jn1234";
    public static final String EMAIL = "jana@nova.com";
    public static final String JMENO_ZAKA = "Tomáš";
    public static final String PRIJMENI_ZAKA = "Nový";
    public static final String DATUM_NAROZENI_ZAKA = "12.10.2000";
//    public static final String JMENO_ZAKA_2 = "Lucie";
//    public static final String PRIJMENI_ZAKA_2 = "Levá";
//    public static final String DATUM_NAROZENI_ZAKA_2 = "2.5.2005";
    WebDriver prohlizec;
    WebDriverWait wait;

    @BeforeEach
    public void setUp() {
//      System.setProperty("webdriver.gecko.driver", System.getProperty("user.home") + "/Java-Training/Selenium/geckodriver");
        System.setProperty("webdriver.gecko.driver", "C:\\Java-Training\\Selenium\\geckodriver.exe");
        prohlizec = new FirefoxDriver();
//        prohlizec.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        wait = new WebDriverWait(prohlizec, 10);
    }

    @Test
    public void registrovanyUzivatelSeUspesnePrihlasi() {
        prohlizec.navigate().to(URL);
        klikniNaNavigatorPrihlasit();
        cekejNaPrihlasovaciDialog();
        vyplnEmail(EMAIL);
        vyplnHeslo(HESLO);
        vlozPrihlasovaciUdaje();
        cekejNaPrihlaseni(JMENO);


    }

    @Test
    public void uzivatelZvoliKurzWebPrihlasiSeNasledneUspesnePrihlasiDiteDoKurzu() {
        prohlizec.navigate().to(URL);
        zvolitKurzWeb();
        klikniNaTlacitkoVytvoritPrihlaskuWeb();
        cekejNaPrihlasovaciDialog();
        vyplnEmail(EMAIL);
        vyplnHeslo(HESLO);
        vlozPrihlasovaciUdaje();
        cekejNaPrihlasku();
        vyplnPrihlasku(JMENO_ZAKA, PRIJMENI_ZAKA, DATUM_NAROZENI_ZAKA);
        prejdiNaPrihlasky();
        Assertions.assertTrue(prohlizec.findElement(By.xpath("//tr[contains(.,'Odhlášení účasti') and contains(.,'" + JMENO_ZAKA + " " + PRIJMENI_ZAKA + "')]")).isDisplayed());
        odhlasitPrihlasku(JMENO);

    }

    @Test
    public void uzivatelSePrihlasiZvoliKurzWebNasledneUspesnePrihlasiDiteDoKurzu() {
        prohlizec.navigate().to(URL);
        klikniNaNavigatorPrihlasit();
        cekejNaPrihlasovaciDialog();
        vyplnEmail(EMAIL);
        vyplnHeslo(HESLO);
        vlozPrihlasovaciUdaje();
        cekejNaPrihlaseni(JMENO);
        klikniNaTlacitkoVytvoritNovouPrihlasku();
        zvolitKurzWeb();
        klikniNaTlacitkoVytvoritPrihlaskuWeb();
        cekejNaPrihlasku();
        vyplnPrihlasku(JMENO_ZAKA, PRIJMENI_ZAKA, DATUM_NAROZENI_ZAKA);
        prejdiNaPrihlasky();
        Assertions.assertTrue(prohlizec.findElement(By.xpath("//td[1][contains(text(),'" + JMENO_ZAKA + " " + PRIJMENI_ZAKA + "')]")).isDisplayed());
        odhlasitPrihlasku(JMENO);
    }

    @Test
    public void uzivatelSePrihlasiPrihlasiDiteDoKurzuFormaPlatbyBankovniPrevodNasledneZmeniFormuPlatbySlozenkou() {
        prohlizec.navigate().to(URL);
        zvolitKurzWeb();
        klikniNaTlacitkoVytvoritPrihlaskuWeb();
        cekejNaPrihlasovaciDialog();
        vyplnEmail(EMAIL);
        vyplnHeslo(HESLO);
        vlozPrihlasovaciUdaje();
        cekejNaPrihlasku();
        vyplnPrihlasku(JMENO_ZAKA, PRIJMENI_ZAKA, DATUM_NAROZENI_ZAKA);
        prejdiNaPrihlasky();
        Assertions.assertEquals("Složenka", upravUhraduPrihlaskyNaSlozenku(JMENO_ZAKA,PRIJMENI_ZAKA));
        odhlasitPrihlasku(JMENO);

    }


    @AfterEach
    public void tearDown() {

        odhlasitUzivatele(JMENO);
        prohlizec.quit();
    }


    public void klikniNaTlacitkoVytvoritNovouPrihlasku() {
        WebElement tlacitkoVytvoritNovouPrihlasku = prohlizec.findElement(By.xpath("//a[contains(text(),'Vytvořit novou přihlášku')]"));
        tlacitkoVytvoritNovouPrihlasku.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[contains(text(),'Vyberte období akce')]")));
    }


    public void klikniNaNavigatorPrihlasit() {
        WebElement navigatorPrihlasit = prohlizec.findElement(By.xpath("//a[text()[contains(.,'Přihlásit')]]"));
        navigatorPrihlasit.click();
    }

    public void vyplnInputBox(String id, String vyplnovanyText) {
        WebElement inputBox = prohlizec.findElement(By.id(id));
        inputBox.sendKeys(vyplnovanyText);
    }

    public void vyplnEmail(String email) {
        vyplnInputBox("email", email);
    }

    public void vyplnHeslo(String heslo) {
        vyplnInputBox("password", heslo);
    }

    public void vlozPrihlasovaciUdaje() {
        WebElement tlacitkoPrihlasit = prohlizec.findElement(By.xpath("//button[@type='submit']"));
        tlacitkoPrihlasit.click();
    }

    public void cekejNaPrihlaseni(String jmeno) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'" + jmeno + "')]")));
    }

    public void zvolitKurzWeb() {
        WebElement tlacitkoKurzWeb = prohlizec.findElement(By.xpath("//a[contains(@href,'trimesicni-kurzy-webu')]"));
        tlacitkoKurzWeb.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(@class,'btn-sm')]")));
    }

    public void klikniNaTlacitkoVytvoritPrihlaskuWeb() {
        WebElement tlacitkoPrihlasit = prohlizec.findElement(By.xpath("//a[contains(@class,'btn-sm')]"));
        tlacitkoPrihlasit.click();
    }

    public void cekejNaPrihlasku() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[contains(text(),'Nová přihláška')]")));
    }

    public void cekejNaPrihlasovaciDialog() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[contains(text(),'Přihlášení')]")));
    }

    public void vyplnPrihlasku(String jmeno, String prijmeni, String datumNarozeni) {
        WebElement vyberTermin = prohlizec.findElement(By.xpath("//*[contains(text(),'Vyberte termín...')]"));
        vyberTermin.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("bs-select-1-0"))).click();
        WebElement jmenoZaka = prohlizec.findElement(By.id("forename"));
        jmenoZaka.sendKeys(jmeno);
        WebElement prijmeniZaka = prohlizec.findElement(By.id("surname"));
        prijmeniZaka.sendKeys(prijmeni);
        WebElement datumNarozeniZaka = prohlizec.findElement(By.id("birthday"));
        datumNarozeniZaka.sendKeys(datumNarozeni);
        WebElement zpusobUhrady = prohlizec.findElement(By.xpath("//label[@for='payment_transfer']"));
        zpusobUhrady.click();
        WebElement souhlasPodminky = prohlizec.findElement(By.xpath("//label[@for='terms_conditions']"));
        souhlasPodminky.click();
        WebElement vytvoritPrihlasku = prohlizec.findElement(By.xpath("//input[@value='Vytvořit přihlášku']"));
        vytvoritPrihlasku.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[contains(text(),'" + jmeno + " " + prijmeni + "')]")));
    }

    public void prejdiNaPrihlasky() {
        WebElement navigacePrihlasky = prohlizec.findElement(By.xpath("//a[contains(text(),'Přihlášky')]"));
        navigacePrihlasky.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[contains(text(),'Přihlášky')]")));
    }

    public String upravUhraduPrihlaskyNaSlozenku(String jmeno, String prijmeni) {
        WebElement tlacitkoUpravit = prohlizec.findElement(By.xpath("//a[contains(.,'Upravit')]"));
        tlacitkoUpravit.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[contains(text(),'úprava')]")));
        WebElement labelzpusobUhrady = prohlizec.findElement(By.xpath("//label[@for='payment_postal_order']"));
        labelzpusobUhrady.click();
        WebElement tlacitkoUpravPrihlasku = prohlizec.findElement(By.xpath("//input[@value='Upravit přihlášku']"));
        tlacitkoUpravPrihlasku.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[contains(text(),'Přihlášky')]")));
        WebElement tlacitkoDetail = prohlizec.findElement(By.xpath("//tr[contains(.,'" + jmeno + " " + prijmeni + "') and contains(.,'Upravit')]//a[contains(.,'Detail')]"));
        tlacitkoDetail.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[contains(text(),'" + jmeno + " " + prijmeni + "')]"))) ;
        WebElement textzpusobUhrady = prohlizec.findElement(By.xpath("//tr[contains(.,'Způsoby úhrady')]//td[2]"));
        String zpusobUhrady = textzpusobUhrady.getText();
        prejdiNaPrihlasky();
        return zpusobUhrady;

        //
    }

    public void odhlasitPrihlasku(String jmeno) {

            WebElement tlacitkoOdhlaseniUcasti = prohlizec.findElement(By.xpath("//tr//a[contains(.,'Odhlášení účasti')]"));
            tlacitkoOdhlaseniUcasti.click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[@for='logged_out_illness']")));
            WebElement tlacitkoNemoc = prohlizec.findElement(By.xpath("//label[@for='logged_out_illness']"));
            tlacitkoNemoc.click();
            WebElement tlacitkoOdhlasitZaka = prohlizec.findElement(By.xpath("//input[@value='Odhlásit žáka']"));
            tlacitkoOdhlasitZaka.click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='toast-message']")));

    }

    public void odhlasitUzivatele(String jmeno) {
        WebElement tlacitkoJmeno = prohlizec.findElement(By.xpath("//*[contains(text(),'" + jmeno + "')]"));
        tlacitkoJmeno.click();
        WebElement tlacitkoOdhlasit = prohlizec.findElement(By.id("logout-link"));
        tlacitkoOdhlasit.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()[contains(.,'Přihlásit')]]")));
    }
}
