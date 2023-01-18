import com.google.common.io.Files;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;
import java.io.File;
import java.io.IOException;
import static org.testng.Assert.*;



public class LoginTest {
    int count =0;
    WebDriver driver;
    @BeforeClass
    public void Setup(){
        WebDriverManager.firefoxdriver().setup();
        driver =new FirefoxDriver();
        driver.get("https://www.demo.guru99.com/V4/");
    }
    @Test(dataProvider = "logInData")
    public void Logintest(String userid, String password) throws IOException {
        driver.findElement(By.name("uid")).clear();
        driver.findElement(By.name("uid")).sendKeys(userid);
        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("btnLogin")).click();
        count++;
        var camera = (TakesScreenshot) driver;
        try{
            String alertText = driver.switchTo().alert().getText();
            driver.switchTo().alert().accept();
            assertEquals(alertText,"User or Password is not valid","result text is incorrect");
        }
        catch (NoAlertPresentException Ex){
            String text=driver.findElement(By.xpath("//table/tbody/tr/td/table/tbody/tr[3]/td")).getText();
            assertEquals("Manger Id : "+userid,text);
            File screenshot = camera.getScreenshotAs(OutputType.FILE);
            Files.move(screenshot, new File("resources/screenshots/" + count + ".png"));
        }

    }



    @DataProvider
    public Object [] [] logInData ()
    {
        Object [] [] data = new Object [4] [2];

        data [0] [0] = Util.validUserid;		data [0] [1] = Util.validPassword;
        data [1] [0] = Util.validUserid;		data [1] [1] = Util.inValidPassword;
        data [2] [0] = Util.inValidUserid;		data [2] [1] = Util.validPassword;
        data [3] [0] = Util.inValidUserid;		data [3] [1] = Util.inValidPassword;

        return data;
    }


    @AfterMethod
    public void goBack(){
        driver.navigate().back();
    }


    @AfterClass
    public void tearDown(){
        driver.quit();
    }

}
