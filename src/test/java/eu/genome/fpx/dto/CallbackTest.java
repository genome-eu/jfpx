package eu.genome.fpx.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.genome.domain.TransactionStatus;
import eu.genome.fpx.domain.PaymentMethodType;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

public class CallbackTest {
    @Test
    public void testParse() throws JsonProcessingException {
        String source = "{\n" +
                "  \"isTest\" : true,\n" +
                "  \"apiKey\" : \"Frr9R8qALB8fovpjxVa5csQ38LP9EvnRL07TRTXxMeznLZsUSIlXtMus39FZQq2E\",\n" +
                "  \"apiSignature\" : \"9a3b9345845d6001de4c98789ebf91dbc0d693f43708890c73728fc5591a5883\",\n" +
                "  \"sessionId\" : \"O6WgjIvFy7BCf4f9ZHjUsgp8JpxifSNGFz8zK2UwPeoXOtK9I40SjgzVloChsqzB\",\n" +
                "  \"transactionId\" : 5121270,\n" +
                "  \"transactionStatus\" : \"SUCCESS\",\n" +
                "  \"transactionErrorCode\" : 0,\n" +
                "  \"transactionTimeUnixSeconds\" : 1638550119,\n" +
                "  \"cardToken\" : null,\n" +
                "  \"billToken\" : \"5c7e5555-0000-0000-1001-5affdcf91001\",\n" +
                "  \"paymentMethodType\" : \"TEST_TRX_SUCCESS\",\n" +
                "  \"amount\" : 9.000000000,\n" +
                "  \"currency\" : \"XTS\",\n" +
                "  \"orderId\" : \"\",\n" +
                "  \"userId\" : \"\",\n" +
                "  \"custom\" : { \"foo\":\"bar\" }\n" +
                "}";

        Callback callback = Callback.parseJSON(source);
        Assert.assertTrue(callback.isTest());
        Assert.assertEquals(callback.getAPIKey(), "Frr9R8qALB8fovpjxVa5csQ38LP9EvnRL07TRTXxMeznLZsUSIlXtMus39FZQq2E");
        Assert.assertEquals(callback.getSignature(), "9a3b9345845d6001de4c98789ebf91dbc0d693f43708890c73728fc5591a5883");
        Assert.assertEquals(callback.getSessionId(), "O6WgjIvFy7BCf4f9ZHjUsgp8JpxifSNGFz8zK2UwPeoXOtK9I40SjgzVloChsqzB");
        Assert.assertEquals(callback.getTransactionId(), 5121270);
        Assert.assertEquals(callback.getTransactionStatus(), TransactionStatus.SUCCESS);
        Assert.assertEquals(callback.getTransactionErrorCode(), 0);
        Assert.assertEquals(callback.getTransactionTime(), Instant.ofEpochSecond(1638550119));
        Assert.assertNull(callback.getCardToken());
        Assert.assertEquals(callback.getBillToken(), "5c7e5555-0000-0000-1001-5affdcf91001");
        Assert.assertEquals(callback.getPaymentMethodType(), PaymentMethodType.TEST_TRX_SUCCESS);
        Assert.assertEquals(callback.getAmount().compareTo(BigDecimal.valueOf(9.)), 0);
        Assert.assertEquals(callback.getCurrencyISO(), "XTS");
        Assert.assertEquals(callback.getOrderId(), "");
        Assert.assertEquals(callback.getUserId(), "");

        Currency currency = callback.getCurrency();

        Assert.assertTrue(callback.verify("eeeeeeeeeeeeeeeeeeeeee"));
    }
}