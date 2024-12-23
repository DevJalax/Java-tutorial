import org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI;

public class EncryptProperties {
    public static void main(String[] args) {
        JasyptPBEStringEncryptionCLI cli = new JasyptPBEStringEncryptionCLI();
        cli.setPassword("your_secret_password"); // Set your encryption password
        String encryptedValue = cli.encrypt("your_sensitive_value");
        System.out.println("Encrypted value: " + encryptedValue);
    }
}
