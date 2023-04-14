package com.digital.solution.generalservice.security.aes;

import com.digital.solution.generalservice.exception.GenericException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant.ERROR_CODE_ENC_INVALID_ALGORITHM;
import static com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant.ERROR_CODE_ENC_INVALID_CIPHER;
import static com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant.ERROR_CODE_ENC_INVALID_KEY;
import static com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant.ERROR_CODE_GENERAL;
import static com.digital.solution.generalservice.domain.constant.error.SourceSystemConstant.SOURCE_SYSTEM_CLICKS;

@Slf4j
public class AesCrypto extends Crypto {

    private static final String KEY_FACTORY = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    public AesCrypto(byte[] key) {
        super(key);
    }

    public byte[] decrypt(byte[] cipher) {
        byte[] ret;
        try {
            byte[] key = getKey(KEY_LEN);
            if (null == key) {
                throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_ENC_INVALID_KEY);
            }

            @SuppressWarnings("all")
            Cipher cipherAlgorithm = Cipher.getInstance(TRANSFORMATION);
            if (ObjectUtils.isEmpty(cipherAlgorithm)) {
                throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_ENC_INVALID_CIPHER);
            }

            SecretKey secKey = new SecretKeySpec(key, AesCrypto.KEY_FACTORY);
            cipherAlgorithm.init(Cipher.DECRYPT_MODE, secKey);
            ret = cipherAlgorithm.doFinal(cipher);
        } catch (NoSuchAlgorithmException e) {
            log.error("Error NoSuchAlgorithmException decrypt : {}", e.getMessage(), e);
            throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_ENC_INVALID_ALGORITHM);
        } catch (InvalidKeyException e) {
            log.error("Error InvalidKeyException decrypt : {}", e.getMessage(), e);
            throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_ENC_INVALID_KEY);
        } catch (BadPaddingException | NoSuchPaddingException | IllegalBlockSizeException e) {
            log.error("Error BadPaddingException | NoSuchPaddingException | IllegalBlockSizeException decrypt : {}", e.getMessage(), e);
            throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_GENERAL);
        }

        return ret;
    }

    public byte[] encrypt(byte[] clear) {
        byte[] ret;
        try {
            byte[] key = getKey(KEY_LEN);
            if (null == key) {
                throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_ENC_INVALID_KEY);
            }

            @SuppressWarnings("all")
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            if (ObjectUtils.isEmpty(cipher)) {
                throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_ENC_INVALID_CIPHER);
            }

            SecretKey secKey = new SecretKeySpec(key, AesCrypto.KEY_FACTORY);
            cipher.init(Cipher.ENCRYPT_MODE, secKey);
            ret = cipher.doFinal(clear);
        } catch (NoSuchAlgorithmException e) {
            log.error("Error NoSuchAlgorithmException encrypt : {}", e.getMessage(), e);
            throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_ENC_INVALID_ALGORITHM);
        } catch (InvalidKeyException e) {
            log.error("Error InvalidKeyException encrypt : {}", e.getMessage(), e);
            throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_ENC_INVALID_KEY);
        } catch (BadPaddingException | NoSuchPaddingException | IllegalBlockSizeException e) {
            log.error("Error BadPaddingException | NoSuchPaddingException | IllegalBlockSizeException encrypt : {}", e.getMessage(), e);
            throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_GENERAL);
        }

        return ret;
    }
}
