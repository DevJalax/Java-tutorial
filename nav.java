import org.openqa.selenium.By;    
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;    

    
public class nav {    
    
    public static void main(String[] args) {    
            
        
            System.setProperty("webdriver.chrome.driver","D:\\sel\\chromedriver.exe");  
                    
            WebDriver driver= new ChromeDriver();    
                   
            driver.navigate().to("https://www.javatpoint.com/software-testing-tutorial");     
     
            driver.findElement(By.linkText("https://www.javatpoint.com/manual-testing")).click();  
    
            driver.navigate().back();   
    
            driver.navigate().forward();  
    
            driver.navigate().to("https://www.google.com");  
    
            driver.navigate().refresh();  
          
            driver.close();   
    }  
}  
