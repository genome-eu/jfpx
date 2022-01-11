package eu.genome.fpx;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Instant;
import java.util.Currency;

public class SignatureBuilderTest {
    @Test
    public void testBuild() {
        Assert.assertEquals(
                new SignatureBuilder()
                        .mode(SignatureMode.MODE_A_TS)
                        .nonce(Instant.ofEpochSecond(32456789))
                        .amount(4.99, Currency.getInstance("EUR"))
                        .orderId("foo")
                        .userId("bar")
                        .mcc("baz")
                        .build("somesecret"),
                "2eec82d8857b5a08669411e993eaef194353248973f4f099f21c7819277a67e6"
        );
    }
}