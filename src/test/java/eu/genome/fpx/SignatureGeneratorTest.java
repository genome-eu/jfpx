package eu.genome.fpx;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Instant;

public class SignatureGeneratorTest {
    @DataProvider
    public Object[][] dataProviderMODE_A() {
        return new Object[][]{
                {"609170bb538b2800f205b1a4fccc3810e066c6150a42be9db50f0b6ede8b17d1", 19.99, "EUR", null, null, null},
                {"609170bb538b2800f205b1a4fccc3810e066c6150a42be9db50f0b6ede8b17d1", 19.99123, "EUR", null, null, null},
                {"609170bb538b2800f205b1a4fccc3810e066c6150a42be9db50f0b6ede8b17d1", 19.99, "eur", null, null, null},
                {"480d4c2e0bb790217388b073634547c581112ffd69a63915dde473eb3cec8737", 19.99, "eur", "order-1", null, null},
                {"9f16857877877d657925ba4970259da97d7b610371d44ab16c62903006bc5c40", 19.99, "EUR", null, "user-124", null},
                {"0181d4f1c211b1b8d40727e4036350438790e8853328880af3eb2d0bc6862ee4", 19.99, "eur", null, null, "5198"},
                {"c9d1df874a7afa5cb73bcf590a4037b0b1b695a5f5cf3dc6ab1b38a48b2934c7", 19.99, "eur", "foo", "bar", "baz"},
        };
    }

    @Test(dataProvider = "dataProviderMODE_A")
    public void testMODE_A(
            String expected,
            double amount,
            String currencyISOA3,
            String orderID,
            String userID,
            String MCC
    ) {
        SignatureGenerator generator = new SignatureGenerator("somesecret");
        Assert.assertEquals(generator.MODE_A(amount, currencyISOA3, orderID, userID, MCC), expected);
    }

    @DataProvider
    public Object[][] dataProviderMODE_A_TS() {
        return new Object[][]{
                {"aa1a89183ab1d047d17f493a0f6043f350498e33c8a4f0397581bfb22d42cf13", Instant.ofEpochSecond(123456), 12.33, "USD", null, null, null},
                {"aa1a89183ab1d047d17f493a0f6043f350498e33c8a4f0397581bfb22d42cf13", Instant.ofEpochSecond(123456), 12.3333333, "USD", null, null, null},
                {"aa1a89183ab1d047d17f493a0f6043f350498e33c8a4f0397581bfb22d42cf13", Instant.ofEpochSecond(123456), 12.33, "usd", null, null, null},
                {"da74ad85e32a63a187ce0b4a0968295950a010f4c53ad54619bd33a31483c4e1", Instant.ofEpochSecond(123456), 12.33, "usd", "order-1", null, null},
                {"82fdb01d4881b6df87528ef48d1bf00db1c7617a4940a68569d833cbc3ef5d4c", Instant.ofEpochSecond(123456), 12.33, "usd", null, "user-124", null},
                {"cebc4891b77979fdcd0dc020edce142708f41a934293c0c05e21ccb6ff867f70", Instant.ofEpochSecond(123456), 12.33, "usd", null, null, "5198"},
                {"2988a1eb1d09cd7e2e9571ae2ea05f4402f0d1895d0a93845ad87af95cf3c55f", Instant.ofEpochSecond(123456), 12.33, "usd", "foo", "bar", "baz"},
        };
    }

    @Test(dataProvider = "dataProviderMODE_A_TS")
    public void testMODE_A_TS(
            String expected,
            Instant time,
            double amount,
            String currencyISOA3,
            String orderID,
            String userID,
            String MCC
    ) {
        SignatureGenerator generator = new SignatureGenerator("somesecret");
        Assert.assertEquals(generator.MODE_A_TS(time, amount, currencyISOA3, orderID, userID, MCC), expected);
    }
}