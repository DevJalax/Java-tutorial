import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.print.PrintOptions;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.openqa.selenium.Pdf;

public class pdfsel {  
  
    public static void main(String[] args) throws IOException {  
    
    	
    	int n,ns=9; 
        Scanner s = new Scanner(System.in);
        System.out.println("Enter n value");
        n = s.nextInt();
        
        System.out.println("Enter list value");
        ArrayList<ArrayList<String>> x = new ArrayList<ArrayList<String>>();
        for(int i=0;i<n;i++)
        {
        	ArrayList<String> f = new ArrayList<>();
        	for(int j=0;j<ns;j++) 
        	{
        		f.add(s.next());
        	}
        	x.add(i, new ArrayList<>(f)); 	 
        }
        
        
         System.setProperty("webdriver.chrome.driver", "D:\\sel\\chromedriver.exe"); 
         ChromeOptions chromeOptions = new ChromeOptions();
         chromeOptions.addArguments("--headless");
         ChromeDriver driver= new ChromeDriver(chromeOptions);
         
         
         
        /* looping x */
        
        for (int i = 0; i <x.size(); i++)
        {
          
            	 driver.navigate().to("file:///C:/Users/RPATraining/Downloads/form.html");
                driver.findElement(By.xpath("/html/body/form/div/input[1]")).sendKeys(x.get(i).get(0));  /* first name */
                driver.findElement(By.xpath("/html/body/form/div/input[2]")).sendKeys(x.get(i).get(1));  /* middle name*/
                driver.findElement(By.xpath("/html/body/form/div/input[3]")).sendKeys(x.get(i).get(2));  /* last name */
                driver.findElement(By.xpath("/html/body/form/div/input[5]")).sendKeys(x.get(i).get(5));  /* phone number */
                driver.findElement(By.xpath("/html/body/form/div/textarea")).sendKeys(x.get(i).get(6));  /* address */
                driver.findElement(By.xpath("/html/body/form/div/input[6]")).sendKeys(x.get(i).get(7));  /* e-mail */
                driver.findElement(By.xpath("/html/body/form/div/input[7]")).sendKeys(x.get(i).get(8));  /* password */
                driver.findElement(By.xpath("/html/body/form/div/input[8]")).sendKeys(x.get(i).get(8));  /* re-enter password*/
                
                /* course */
                
                /* Select Course = new Select(driver.findElement(By.xpath("/html/body/form/div/div[1]/select")));

                Course.selectByVisibleText("x.get(i).get(3)");
                
                
                WebElement radio = driver.findElement(By.xpath("//input[@value='x.get(i).get(4)']"));
                
                radio.click(); */
                
                /* HTML -> PDF */
             
            Pdf pdf = driver.print(new PrintOptions());
            
            Files.write(Paths.get("D://webpdf"+i+"rem.pdf"), OutputType.BYTES.convertFromBase64Png(pdf.getContent()));
           
            driver.findElement(By.xpath("/html/body/form/div/button[2]")).click();
        } 
    s.close();
}
}
