package net.test.perfect.number.security;

import net.test.perfect.number.exception.PerfectNumberException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Formatter;

public class HmacGenerator {

    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    public static String generate(String data, String key)
            throws NoSuchAlgorithmException, InvalidKeyException {

        if (key == null || key.isEmpty()) {
            throw new PerfectNumberException("Invalid security key");
        }

        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA256_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
        mac.init(signingKey);

        return toHexString(mac.doFinal(data.getBytes()));
    }

    public static String createHmacFromData(String applicationName, String method, String key, Instant expiry)
            throws InvalidKeyException, NoSuchAlgorithmException {
        if (expiry.isAfter(Instant.now().plusSeconds(HmacData.MAX_EXPIRY_IN_SECONDS))) {
            throw new PerfectNumberException("Too long expiry time!!");
        }
        return HmacGenerator.generate(HmacData.createHmacString(applicationName, method, expiry), key);
    }

    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }
}
