package com.digital.solution.generalservice.security.rsa;

import com.digital.solution.generalservice.exception.GenericException;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static com.digital.solution.generalservice.domain.constant.GeneralConstant.RSA_ALGORITHM;
import static com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant.ERROR_CODE_ENC_INVALID_ALGORITHM;
import static com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant.ERROR_CODE_ENC_INVALID_KEY;
import static com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant.ERROR_CODE_GENERAL;
import static com.digital.solution.generalservice.domain.constant.error.SourceSystemConstant.SOURCE_SYSTEM_CLICKS;

@Slf4j
public class RsaSecurity {

    private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";

    private static PublicKey getPublicKey(String base64PublicKey) {
        PublicKey publicKey;
        try{
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            publicKey = keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            log.error("Error NoSuchAlgorithmException getPublicKey : {}", e.getMessage(), e);
            throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_ENC_INVALID_ALGORITHM);
        } catch (InvalidKeySpecException e) {
            log.error("Error InvalidKeySpecException getPublicKey : {}", e.getMessage(), e);
            throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_ENC_INVALID_KEY);
        }

        return publicKey;
    }

    private static PrivateKey getPrivateKey(String base64PrivateKey) {
        PrivateKey privateKey;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
        try {
            var keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            log.error("Error NoSuchAlgorithmException getPrivateKey : {}", e.getMessage(), e);
            throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_ENC_INVALID_ALGORITHM);
        } catch (InvalidKeySpecException e) {
            log.error("Error InvalidKeySpecException getPrivateKey : {}", e.getMessage(), e);
            throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_ENC_INVALID_KEY);
        }

        return privateKey;
    }

    public static byte[] encrypt(String data, String publicKey) {
        try {
            @SuppressWarnings("all")
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
            return cipher.doFinal(data.getBytes());
        } catch (NoSuchAlgorithmException e) {
            log.error("Error NoSuchAlgorithmException encrypt : {}", e.getMessage(), e);
            throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_ENC_INVALID_ALGORITHM);
        } catch (InvalidKeyException e) {
            log.error("Error InvalidKeyException encrypt : {}", e.getMessage(), e);
            throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_ENC_INVALID_KEY);
        } catch (NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            log.error("Error NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException encrypt : {}", e.getMessage(), e);
            throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_GENERAL);
        }
    }

    private static String decrypt(byte[] data, PrivateKey privateKey) {
        try {
            @SuppressWarnings("all")
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(cipher.doFinal(data));
        } catch (NoSuchAlgorithmException e) {
            log.error("Error NoSuchAlgorithmException decrypt : {}", e.getMessage(), e);
            throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_ENC_INVALID_ALGORITHM);
        } catch (InvalidKeyException e) {
            log.error("Error InvalidKeyException decrypt : {}", e.getMessage(), e);
            throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_ENC_INVALID_KEY);
        } catch (NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            log.error("Error NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException decrypt : {}", e.getMessage(), e);
            throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_GENERAL);
        }
    }

    public static String decrypt(String data, String base64PrivateKey) {
        return decrypt(Base64.getDecoder().decode(data.getBytes()), getPrivateKey(base64PrivateKey));
    }
}
