package eu.genome.fpx.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.genome.domain.TransactionStatus;
import eu.genome.fpx.SignatureBuilder;
import eu.genome.fpx.SignatureGenerator;
import eu.genome.fpx.SignatureMode;
import eu.genome.fpx.domain.PaymentMethodType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;

/**
 * Jackson-bound implementation of callback DTO.
 * In order to use this DTO, Jackson library must be on classpath.
 */
@JsonRootName("callback")
public class Callback {
    /**
     * Parses given string data using standard Jackson JSON mapper.
     *
     * @param data Data to parse.
     * @return Callback DTO object.
     * @throws JsonProcessingException On processing error.
     */
    public static Callback parseJSON(String data) throws JsonProcessingException {
        return parse(new ObjectMapper(), data);
    }

    /**
     * Parses given string data using given object mapper.
     *
     * @param mapper Object mapper to use, mandatory.
     * @param data   Data to parse.
     * @return Callback DTO object.
     * @throws JsonProcessingException On processing error.
     */
    public static Callback parse(ObjectMapper mapper, String data) throws JsonProcessingException {
        return mapper.readValue(data, Callback.class);
    }

    // General part
    @JsonProperty("isTest")
    private boolean test;
    @JsonProperty("apiKey")
    private String pixelKey;
    @JsonProperty("apiSignature")
    private String pixelSignature;
    @JsonProperty("sessionId")
    private String sessionId;

    // Transaction part
    @JsonProperty("transactionId")
    private long transactionId;
    @JsonProperty("transactionStatus")
    private TransactionStatus transactionStatus;
    @JsonProperty("transactionErrorCode")
    private long transactionErrorCode;
    @JsonProperty("transactionTimeUnixSeconds")
    private long transactionTime;

    // Card/Billing tokens part
    @JsonProperty("cardToken")
    private String cardToken;
    @JsonProperty("billToken")
    private String billToken;

    // Amount part
    @JsonProperty("paymentMethodType")
    private PaymentMethodType paymentMethodType;
    @JsonProperty("amount")
    private BigDecimal amount;
    @JsonProperty("currency")
    private String currency;

    // Identification part
    @JsonProperty("orderId")
    private String orderId;
    @JsonProperty("userId")
    private String userId;

    // Custom fields
    @JsonProperty("custom")
    private Map<String, String> custom;

    public boolean isTest() {
        return this.test;
    }

    public String getAPIKey() {
        return this.pixelKey;
    }

    public String getSignature() {
        return this.pixelSignature;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public long getTransactionId() {
        return this.transactionId;
    }

    public TransactionStatus getTransactionStatus() {
        return this.transactionStatus;
    }

    public long getTransactionErrorCode() {
        return this.transactionErrorCode;
    }

    public Instant getTransactionTime() {
        return Instant.ofEpochSecond(this.transactionTime);
    }

    public String getCardToken() {
        return this.cardToken;
    }

    public String getBillToken() {
        return this.billToken;
    }

    public PaymentMethodType getPaymentMethodType() {
        return this.paymentMethodType;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public String getCurrencyISO() {
        return this.currency.toUpperCase(Locale.ROOT);
    }

    public Currency getCurrency() {
        return Currency.getInstance(getCurrencyISO());
    }

    public String getOrderId() {
        return this.orderId;
    }

    public String getUserId() {
        return this.userId;
    }

    /**
     * Verifies signature using given signature generator.
     *
     * @param secret API secret to use.
     * @return True if signature for this callback is correct.
     */
    public boolean verify(String secret) {
        return this.verify(new SignatureGenerator(secret));
    }

    /**
     * Verifies signature using given signature generator.
     *
     * @param generator Signature generator to use.
     * @return True if signature for this callback is correct.
     */
    public boolean verify(SignatureGenerator generator) {
        return new SignatureBuilder()
                .mode(SignatureMode.CALLBACK)
                .sessionId(getSessionId())
                .transactionId(getTransactionId())
                .amount(getAmount().doubleValue(), getCurrencyISO())
                .orderId(orderId)
                .verify(generator, getSignature());
    }
}
