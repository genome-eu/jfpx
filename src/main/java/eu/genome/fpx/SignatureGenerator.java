package eu.genome.fpx;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Locale;

/**
 * Component to generate signatures.
 */
public class SignatureGenerator {
    private final String secret;

    /**
     * Constructs Signer instance with given secret.
     *
     * @param secret Application secret.
     */
    public SignatureGenerator(String secret) {
        if (secret == null || secret.isEmpty()) {
            throw new IllegalArgumentException("Empty secret");
        }
        this.secret = secret;
    }

    /**
     * @return Constructed SHA256 message digest.
     */
    private MessageDigest getDigest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts given bytes to hexadecimal representation.
     *
     * @param source Source bytes to convert.
     * @return Hexadecimal string.
     */
    private String toHex(byte[] source) {
        StringBuilder sb = new StringBuilder(source.length * 2);
        for (byte b : source) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    /**
     * Verifies amount & currency data.
     *
     * @param amount   Amount to verify.
     * @param currency Currency ISO A3 to verify.
     */
    private void verifyAmount(double amount, String currency) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Incorrect amount " + amount);
        }
        if (currency == null || currency.isEmpty()) {
            throw new IllegalArgumentException("Empty currency ISO code");
        }
        if (currency.length() != 3) {
            throw new IllegalArgumentException("Currency ISO A3 code expected, but " + currency + " given");
        }
    }

    /**
     * Constructs CALLBACK signature.
     *
     * @param sessionID     FPX/HPP session identifier.
     * @param transactionID Transaction identifier.
     * @param amount        Transaction amount.
     * @param currencyISOA3 Transaction currency ISO.
     * @param orderID       Order identifier, optional.
     * @return Generated signature.
     */
    public String CALLBACK(
            String sessionID,
            long transactionID,
            double amount,
            String currencyISOA3,
            String orderID
    ) {
        verifyAmount(amount, currencyISOA3);
        if (sessionID == null || sessionID.isEmpty()) {
            throw new IllegalArgumentException("Empty session ID");
        }
        if (transactionID == 0) {
            throw new IllegalArgumentException("Empty transaction ID");
        }

        MessageDigest digest = getDigest();
        digest.update(String.format(
                Locale.ROOT,
                "%s|%s|%s|%d|%.02f|%s",
                secret,
                sessionID,
                orderID == null ? "" : orderID,
                transactionID,
                amount,
                currencyISOA3
        ).getBytes(StandardCharsets.UTF_8));
        return toHex(digest.digest());
    }

    /**
     * Constructs MODE_A signature.
     *
     * @param amount        Transaction amount.
     * @param currencyISOA3 Transaction currency ISO.
     * @param orderID       Order identifier, optional.
     * @param userID        User identifier, optional.
     * @param MCC           Operation MCC code, optional.
     * @return Generated signature.
     * @deprecated Use MODE_A_TS instead.
     */
    @Deprecated
    public String MODE_A(
            double amount,
            String currencyISOA3,
            String orderID,
            String userID,
            String MCC
    ) {
        verifyAmount(amount, currencyISOA3);
        MessageDigest digest = getDigest();
        digest.update(String.format(
                Locale.ROOT,
                "%s|%s|%.2f|%s|%s|%s|%s",
                secret,
                SignatureMode.MODE_A,
                amount,
                currencyISOA3.toUpperCase(Locale.ROOT),
                orderID == null ? "" : orderID,
                userID == null ? "" : userID,
                MCC == null ? "" : MCC
        ).getBytes(StandardCharsets.UTF_8));
        return toHex(digest.digest());
    }

    /**
     * Constructs MODE_A_TS signature.
     *
     * @param time          Timestamp nonce.
     * @param amount        Transaction amount.
     * @param currencyISOA3 Transaction currency ISO.
     * @param orderID       Order identifier, optional.
     * @param userID        User identifier, optional.
     * @param MCC           Operation MCC code, optional.
     * @return Generated signature.
     */
    public String MODE_A_TS(
            Instant time,
            double amount,
            String currencyISOA3,
            String orderID,
            String userID,
            String MCC
    ) {
        verifyAmount(amount, currencyISOA3);
        MessageDigest digest = getDigest();
        digest.update(String.format(
                Locale.ROOT,
                "%s|%s|%d|%.2f|%s|%s|%s|%s",
                secret,
                SignatureMode.MODE_A_TS,
                time.getEpochSecond(),
                amount,
                currencyISOA3.toUpperCase(Locale.ROOT),
                orderID == null ? "" : orderID,
                userID == null ? "" : userID,
                MCC == null ? "" : MCC
        ).getBytes(StandardCharsets.UTF_8));
        return toHex(digest.digest());
    }
}
