package th.ac.ku.book;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddBookTests {

    @LocalServerPort
    private Integer port;

    private static WebDriver driver;
    private static WebDriverWait wait;

    // เป็นการกำหนดว่า field นี้อยู่ที่ id ไหน โดยไม่ต้องกำหนดในแต่ละ test case
    @FindBy(id = "nameInput")
    private WebElement nameField;

    @FindBy(id = "authorInput")
    private WebElement authorField;

    @FindBy(id = "priceInput")
    private WebElement priceField;

    @FindBy(id = "submitButton")
    private WebElement submitButton;

    // ทำก่อนทุก test case
    @BeforeAll
    public static void beforeAll() {
        // เปิด chrome แล้วรอให้ browser เปิดเสร็จเพื่อเข้าหน้าเว็บ
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 1000);
    }

    // ทำก่อนในแต่ละ test case
    @BeforeEach
    public void beforeEach() {
        // เปิดหน้าเว็บ
        driver.get("http://localhost:" + port + "/book/add");
        PageFactory.initElements(driver, this);
    }

    // ทำหลังจากแต่ละ test case เสร็จ
    @AfterEach
    public void afterEach() throws InterruptedException {
        // บอกให้หยุดเพื่อดูผลลัพธ์
        Thread.sleep(3000);
    }

    @AfterAll
    public static void afterAll() {
        // ปิด browser
        driver.quit();
    }

    // test case
    @Test
    void testAddBook() {
        // หา field ที่ต้องใส่ข้อมูล (id เหมือนใน book_add.html)
        // ไม่ต้องหาแล้ว เพราะใช้ @FindBy ข้างนอก test case
//        WebElement nameField = wait.until(webDriver ->
//                webDriver.findElement(By.id("nameInput")));
//        WebElement authorField = driver.findElement(By.id("authorInput"));
//        WebElement priceField = driver.findElement(By.id("priceInput"));

        // หาปุ่ม submit
        WebElement submitButton = driver.findElement(By.id("submitButton"));

        // ใส่ข้อมูลในแต่ละ field
        nameField.sendKeys("Clean Code");
        authorField.sendKeys("Robert Martin");
        priceField.sendKeys("600");

        // กดปุ่ม submit เพื่อเพิ่มข้อมูล
        submitButton.click();

        // ตรวจสอบว่าหนังสือที่เพิ่มไปมีใน database หรือไม่
        // หาจาก <td> ใน books.html
//        WebElement firstTd = wait.until(webDriver ->
//                webDriver.findElement(By.tagName("td")));
//        assertEquals("Clean Code", firstTd.getText());

        WebElement name = wait.until(webDriver -> webDriver
                .findElement(By.xpath("//table/tbody/tr[1]/td[1]")));
        WebElement author = driver
                .findElement(By.xpath("//table/tbody/tr[1]/td[2]"));
        WebElement price = driver
                .findElement(By.xpath("//table/tbody/tr[1]/td[3]"));

        // ตรวจสอบข้อมูลในแต่ละ field ว่าตรงกับที่ใส่ไปหรือไม่
        assertEquals("Clean Code", name.getText());
        assertEquals("Robert Martin", author.getText());
        assertEquals("600.00", price.getText());

    }

}

