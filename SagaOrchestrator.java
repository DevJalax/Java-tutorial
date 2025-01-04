public class SagaOrchestrator {

    public void startSaga() {
        try {
            reserveInventory();
            makePayment();
            confirmOrder();
        } catch (Exception e) {
            rollback();
        }
    }
    
    private void reserveInventory() {
        // Code for reserving inventory
    }

    private void makePayment() {
        // Code for payment processing
    }

    private void confirmOrder() {
        // Code for order confirmation
    }

    private void rollback() {
        // Rollback all previous steps
    }
}
