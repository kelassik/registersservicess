package com.digital.solution.generalservice.security.aes;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import static com.digital.solution.generalservice.security.aes.Crypto.KEY_LEN;

@SuppressWarnings("all")
public class AesSecurity {

    public static String encrypt(String literal) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBin = new byte[KEY_LEN];
        secureRandom.nextBytes(keyBin);
        Crypto crypto = new AesCrypto(keyBin);
        byte[] litBin = literal.getBytes(StandardCharsets.UTF_8);
        byte[] chipBin = crypto.encrypt(litBin);
        byte[] retBin = new byte[keyBin.length + chipBin.length];
        System.arraycopy(chipBin, 0, retBin, 0, chipBin.length);
        System.arraycopy(keyBin, 0, retBin, chipBin.length, keyBin.length);
        byte[] encoded = Base64.encodeBase64(retBin, false);
        return new String(encoded, StandardCharsets.UTF_8);
    }

    public static String decrypt(String chipper) {
        byte[] chipBin = chipper.getBytes(StandardCharsets.UTF_8);
        byte[] decoded = Base64.decodeBase64(chipBin);
        byte[] keyBin = ArrayUtils.subarray(decoded, decoded.length - KEY_LEN, decoded.length);
        byte[] clrBin = ArrayUtils.subarray(decoded, 0, decoded.length - KEY_LEN);
        Crypto crypto = new AesCrypto(keyBin);
        byte[] retBin = crypto.decrypt(clrBin);
        return new String(retBin, StandardCharsets.UTF_8);
    }
}
