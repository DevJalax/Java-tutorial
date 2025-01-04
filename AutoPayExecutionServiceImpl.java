import java.math.BigDecimal;
import java.util.Date;

import com.npci.upi.exception.NpciErrorException;
import com.npci.upi.model.CardDetails;
import com.npci.upi.model.Mandate;
import com.npci.upi.model.RequestPayload;
import com.npci.upi.service.PinValidationService;
import com.npci.upi.util.UPIErrorCodes;

public class AutoPayExecutionServiceImpl implements AutoPayExecutionService {

    private static final long min_time = 5;
    private static final int min_amount = 15000;

    private PinValidationService pinValidation;

    public Mandate executeAutoPayment(Mandate mandate, RequestPayload reqPay, CardDetails cardDetails) {
        if (mandate.getPurpose().equals("14")) {
            int ruleValue = Integer.valueOf(mandate.getRecurrenceRuleValue());
            // validate rule match
            if (SIExecutionValidationService.isRuleMatches(date, mandate.getRecurrencePattern(),
                    mandate.getRecurrenceRuleType(), ruleValue)) {
                throw new NpciErrorException(UPIErrorCodes.VI.getCode(), UPIErrorCodes.VI.getMessage());
            }

            // validate sequence no
            int seq_no = Integer.parseInt(reqPay.getPayer().getSeqNum());
            int calculatedSeqNo = SIExecutionValidationService.getSequenceNumber(mandate.getRecurrencePattern(),
                    ruleValue, mandate.getStartDate(), date);
            if (seq_no != calculatedSeqNo) {
                throw new NpciErrorException(UPIErrorCodes.V1.getCode(), UPIErrorCodes.V1.getMessage());
            }
            // validate month is valid for BI, Quar, half, Yearly
            if (SIExecutionValidationService.validateMonthForBiQuarterHalfYearly(mandate.getRecurrencePattern(),
                    mandate.getStartDate(), date)) {
                throw new NpciErrorException(UPIErrorCodes.VI.getCode(), UPIErrorCodes.VI.getMessage());
            }

            // validate UPI PIN if Amount is Greater than 15K and for Sequence 1 if it is
            boolean validateUPIpin = true;

            if (seq_no == 1 && getMandateCreationAnd1stDebitTimeDifference(mandate.getStartDatetime(), date) <= min_time
                    && mandate.getAmount().compareTo(new BigDecimal(min_amount)) <= 15000) {

                validateUPIpin = false;
            }
            pinValidation.validUpiPinAndMandateSignature(reqPay.getTxn().getOrgTxnId(), cardDetails,
                    reqPay.getPayer().getCreds(), mandate, validateUPIpin);
        }

        return mandate;
    }

    public long getMandateCreationAnd1stDebitTimeDifference(Date createDate, Date txnDate) {
        long timeDifferenceInMillis = txnDate.getTime() - createDate.getTime();
        long timeDifferenceInMinutes = timeDifferenceInMillis / (60 * 1000);
        return timeDifferenceInMinutes;
    }
}
