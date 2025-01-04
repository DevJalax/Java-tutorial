public class AccountNumberMasker {

    public static String maskAccountNumber(String accountNumber) {
        // Number of digits to show at the end
        int digitsToShow = 4;

        if (accountNumber == null || accountNumber.length() <= digitsToShow) {
            return accountNumber; // Nothing to mask, return the original
        }

        // Calculate the number of digits to mask
        int maskLength = accountNumber.length() - digitsToShow;

        // Create the masked part with asterisks
        String maskedPart = "*".repeat(maskLength);

        // Get the last few digits
        String visiblePart = accountNumber.substring(maskLength);

        // Combine masked and visible parts
        return maskedPart + visiblePart;
    }

    public static void main(String[] args) {
        String accountNumber = "1234567890123456";
        String maskedAccountNumber = maskAccountNumber(accountNumber);

        System.out.println("Original: " + accountNumber);
        System.out.println("Masked: " + maskedAccountNumber);
    }
}
