import org.openqa.selenium.By;  
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;  
import java.util.*;
import java.io.*;

public class mobilechoice {  
  
    public static void main(String[] args) throws FileNotFoundException {  
          
    System.setProperty("webdriver.chrome.driver", "D:\\sel\\chromedriver.exe");  
    WebDriver driver=new ChromeDriver();  
    driver.navigate().to("http://www.amazon.in/");    
    driver.findElement(By.xpath("//*[@id=\"twotabsearchtextbox\"]")).sendKeys("mobile phones"); 
    driver.findElement(By.id("nav-search-submit-button")).click();
    driver.findElement(By.xpath("//*[@id=\"p_89/Redmi\"]/span/a/div/label/i")).click();
    List<WebElement> mn = driver.findElements(By.cssSelector(".a-size-medium.a-color-base.a-text-normal"));
    PrintWriter out = new PrintWriter("redmisamsungoppo.txt"); 
    for(int i = 0; i< mn.size(); i++) {
        String s = mn.get(i).getText();
            out.println(s);
     }
    out.close();
    
    driver.findElement(By.xpath("//*[@id=\"p_89/Samsung\"]/span/a/div/label/i")).click();
    List<WebElement> sm = driver.findElements(By.cssSelector(".a-size-medium.a-color-base.a-text-normal"));
    PrintWriter ou = new PrintWriter("redmisamsungoppo.txt"); 
    for(int i = 0; i< sm.size(); i++) {
        String s = sm.get(i).getText();
            ou.println(s);
     }
    ou.close();
    
}
}
