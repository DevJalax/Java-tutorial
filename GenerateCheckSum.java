import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenerateCheckSum {
   private static final Logger log = LoggerFactory.getLogger(GenerateCheckSum.class);

   public static String generateChecksumMerchant(String concatenatedString, String checksumkey) throws IOException {
      String inputString = concatenatedString + checksumkey;
      StringBuffer sb = null;

      try {
         MessageDigest md = MessageDigest.getInstance("SHA-256");
         md.update(inputString.getBytes());
         byte[] byteData = md.digest();
         sb = new StringBuffer();

         for(int i = 0; i < byteData.length; ++i) {
            sb.append(Integer.toString((byteData[i] & 255) + 256, 16).substring(1));
         }
      } catch (NoSuchAlgorithmException var7) {
         log.error("Exception occur during execution : {}", var7);
      }

      return sb.toString();
   }

   public static void main(String[] args) throws IOException {
      String request = "CHAYOOS001|api|3004294066713|9971345344|CHAYOOS929|CHAYOOS TEA PVT LTD|Canara Bank";
      String headerKey = "hgfgfds976535667967";

      try {
         System.out.println("checksum=" + generateChecksumMerchant(request, headerKey));
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
