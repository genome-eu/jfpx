package eu.genome.fpx;

import java.time.Instant;
import java.util.Currency;
import java.util.Objects;

/**
 * Mutable signature builder helper.
 */
public class SignatureBuilder {
    private SignatureMode mode;
    private Instant instant;
    private double amount;
    private String currencyISO;
    private String orderId;
    private String userId;
    private String mcc;

    /**
     * Adds ORDER ID value to builder.
     *
     * @param value ORDER ID value.
     * @return Builder instance.
     */
    public SignatureBuilder orderId(String value) {
        this.orderId = value;
        return this;
    }

    /**
     * Adds USER ID value to builder.
     *
     * @param value USER ID value.
     * @return Builder instance.
     */
    public SignatureBuilder userId(String value) {
        this.userId = value;
        return this;
    }

    /**
     * Adds MCC code to builder.
     *
     * @param value MCC code.
     * @return Builder instance.
     */
    public SignatureBuilder mcc(String value) {
        this.mcc = value;
        return this;
    }

    /**
     * Adds Unix timestamp nonce to builder.
     *
     * @param value Instance that will be used as nonce.
     * @return Builder instance.
     */
    public SignatureBuilder nonce(Instant value) {
        this.instant = value;
        return this;
    }

    /**
     * Adds transaction amount to builder.
     *
     * @param amount        Amount value.
     * @param currencyISOA3 Currency in ISO Alpha-3 format.
     * @return Builder instance.
     */
    public SignatureBuilder amount(double amount, String currencyISOA3) {
        this.amount = amount;
        this.currencyISO = currencyISOA3;
        return this;
    }

    /**
     * Adds transaction amount to builder.
     *
     * @param amount   Amount value.
     * @param currency Currency.
     * @return Builder instance.
     */
    public SignatureBuilder amount(double amount, Currency currency) {
        return this.amount(amount, currency.getCurrencyCode());
    }

    /**
     * Adds signature mode to builder.
     *
     * @param value Signature mode.
     * @return Builder instance.
     */
    public SignatureBuilder mode(SignatureMode value) {
        this.mode = value;
        return this;
    }

    /**
     * Builds signature using builder data and given signature generator.
     *
     * @param signatureGenerator Signature generator to use.
     * @return Generated signature.
     */
    public String build(SignatureGenerator signatureGenerator) {
        Objects.requireNonNull(signatureGenerator, "signatureGenerator");
        if (mode == null) {
            throw new IllegalStateException("Signature mode not provided");
        }

        switch (mode) {
            case MODE_A:
                return signatureGenerator.MODE_A(amount, currencyISO, orderId, userId, mcc);
            case MODE_A_TS:
                return signatureGenerator.MODE_A_TS(instant, amount, currencyISO, orderId, userId, mcc);
            default:
                throw new IllegalStateException("Unsupported mode " + mode);
        }
    }

    /**
     * Builds signature using builder data and given secret.
     *
     * @param secret Application secret.
     * @return Generated signature.
     */
    public String build(String secret) {
        return this.build(new SignatureGenerator(secret));
    }

    /**
     * Verifies given hash candidate.
     *
     * @param signatureGenerator Signature generator to use.
     * @param candidate          Signature hash candidate.
     * @return True if hash candidate is valid.
     */
    public boolean verify(SignatureGenerator signatureGenerator, String candidate) {
        return candidate != null
                && !candidate.isEmpty()
                && candidate.trim().equalsIgnoreCase(build(signatureGenerator));
    }

    /**
     * Verifies given hash candidate.
     *
     * @param secret    Application secret.
     * @param candidate Signature hash candidate.
     * @return True if hash candidate is valid.
     */
    public boolean verify(String secret, String candidate) {
        return candidate != null
                && !candidate.isEmpty()
                && candidate.trim().equalsIgnoreCase(build(secret));
    }
}
