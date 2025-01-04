import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.print.PrintOptions;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.openqa.selenium.Pdf;


public class wikipedia {

	public static void main(String[] args) throws IOException {
		

        System.setProperty("webdriver.chrome.driver", "D:\\sel\\chromedriver.exe"); 
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        ChromeDriver driver= new ChromeDriver(chromeOptions);
        
        driver.navigate().to("https://www.wikipedia.org/");
        driver.findElement(By.xpath("//input[@id='searchInput']")).sendKeys("gandhi");
        driver.findElement(By.xpath("//i[@class='sprite svg-search-icon']")).click();
       
        
        Pdf pdf = driver.print(new PrintOptions());
        Files.write(Paths.get("D://wiki.pdf"), OutputType.BYTES.convertFromBase64Png(pdf.getContent()));
        
        String str = driver.getPageSource();
        PrintWriter out = new PrintWriter("wiki.html");
        out.println(str);
        out.close();
        
	}

}
