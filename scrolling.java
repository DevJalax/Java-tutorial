import org.openqa.selenium.JavascriptExecutor;  
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;  
  
 
  
public class scrolling {  
  
    public static void main(String[] args) {  
          
        System.setProperty("webdriver.chrome.driver", "D:\\sel\\chromedriver.exe");  
        WebDriver driver=new ChromeDriver();  
        driver.navigate().to("https://www.tutorialspoint.com/python/index.htm");
        JavascriptExecutor js = (JavascriptExecutor)driver;  
        js.executeScript("scrollBy(0, 4500)");  
  
    }  
}  
