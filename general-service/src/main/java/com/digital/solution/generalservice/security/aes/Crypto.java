package com.digital.solution.generalservice.security.aes;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public abstract class Crypto {

    protected static final int KEY_LEN = 16;
    protected static byte[] defaultKey;
    protected byte[] key;
    protected Crypto(byte[] key) {
        this.key = key;
    }

    static {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = Crypto.class.getResourceAsStream("default.key")){
            if (inputStream != null) {
                IOUtils.copy(inputStream, byteArrayOutputStream);
            }
        } catch (IOException e) {
            log.error("Error IOException : {}", e.getMessage(), e);
        }

        byte[] keys = byteArrayOutputStream.toByteArray();
        if (keys.length > 8) {
            defaultKey = byteArrayOutputStream.toByteArray();
        } else {
            defaultKey = new byte[128];
        }
    }

    public abstract byte[] encrypt(byte[] clear);
    public abstract byte[] decrypt(byte[] cipher);

    @SuppressWarnings("all")
    protected byte[] getKey(int len) {
        byte[] result;
        if (null == key) {
            result = new byte[len];
            System.arraycopy(Crypto.defaultKey, 0, result, 0, len);
        } else {
            result = key;
        }

        return result;
    }
}
