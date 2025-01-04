import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BlockedFundsDebit {
    private double blockedAmount;
    private double debitAmount;
    private String purposeCode;
    private String debitRule;
    private LocalDate lastDebitDate; // Track the date of the last debit

    public BlockedFundsDebit(double blockedAmount, double debitAmount, String purposeCode, String debitRule) {
        this.blockedAmount = blockedAmount;
        this.debitAmount = debitAmount;
        this.purposeCode = purposeCode;
        this.debitRule = debitRule;
        this.lastDebitDate = LocalDate.now(); // Initialize with today's date
    }

    public void executeDebits(String frequency) {
        // Execute debits based on frequency until the blocked amount is fully utilized
        while (blockedAmount >= debitAmount) {
            // Perform the debit
            performDebit();
            
            // Check if we can continue debiting based on frequency
            if (!canContinueDebiting(frequency)) {
                break; // Exit if we cannot continue debiting based on frequency
            }
        }
        System.out.println("Blocked amount fully utilized. Remaining blocked amount: " + blockedAmount);
    }

    private void performDebit() {
        System.out.println("Executing debit of " + debitAmount + " on " + LocalDate.now());
        System.out.println("Purpose Code: " + purposeCode);
        System.out.println("Debit Rule: " + debitRule);
        blockedAmount -= debitAmount; // Deduct the debit amount from blocked funds
    }

    private boolean canContinueDebiting(String frequency) {
        LocalDate today = LocalDate.now();
        
        switch (frequency.toLowerCase()) {
            case "daily":
                return !lastDebitDate.isEqual(today); // Allow a new debit if it's a different day
            case "weekly":
                return ChronoUnit.WEEKS.between(lastDebitDate, today) >= 1; // Allow a new debit if a week has passed
            case "fortnightly":
                return ChronoUnit.WEEKS.between(lastDebitDate, today) >= 2; // Allow a new debit if two weeks have passed
            case "monthly":
                return ChronoUnit.MONTHS.between(lastDebitDate, today) >= 1; // Allow a new debit if a month has passed
            case "quarterly":
                return ChronoUnit.MONTHS.between(lastDebitDate, today) >= 3; // Allow a new debit if three months have passed
            case "half-yearly":
                return ChronoUnit.MONTHS.between(lastDebitDate, today) >= 6; // Allow a new debit if six months have passed
            case "annually":
                return ChronoUnit.YEARS.between(lastDebitDate, today) >= 1; // Allow a new debit if a year has passed
            default:
                System.out.println("Invalid frequency specified.");
                return false; // Invalid frequency means we cannot continue debiting
        }
    }

    public static void main(String[] args) {
        // Example usage
        double totalBlockedFunds = 1000.0; // Total blocked amount
        double debitPerTransaction = 100.0; // Amount to be debited per transaction
        String purposeCode = "76"; // Purpose code
        String debitRule = "MAX"; // Debit rule

        BlockedFundsDebit fundsDebitor = new BlockedFundsDebit(totalBlockedFunds, debitPerTransaction, purposeCode, debitRule);

        // Execute debits based on the desired frequency
        String frequency = "monthly"; // Change this to daily, weekly, fortnightly, etc.
        fundsDebitor.executeDebits(frequency);
    }
}
