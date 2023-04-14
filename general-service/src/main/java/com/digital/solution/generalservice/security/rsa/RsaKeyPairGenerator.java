package com.digital.solution.generalservice.security.rsa;

import com.digital.solution.generalservice.exception.GenericException;
import lombok.extern.slf4j.Slf4j;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static com.digital.solution.generalservice.domain.constant.GeneralConstant.GENERATED_RSA_PRIVATE_KEY;
import static com.digital.solution.generalservice.domain.constant.GeneralConstant.GENERATED_RSA_PUBLIC_KEY;
import static com.digital.solution.generalservice.domain.constant.GeneralConstant.RSA_ALGORITHM;
import static com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant.ERROR_CODE_ENC_INVALID_ALGORITHM;
import static com.digital.solution.generalservice.domain.constant.error.SourceSystemConstant.SOURCE_SYSTEM_CLICKS;

@Slf4j
public class RsaKeyPairGenerator {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public RsaKeyPairGenerator() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(RSA_ALGORITHM);
            keyGen.initialize(2048);
            KeyPair pair = keyGen.generateKeyPair();
            privateKey = pair.getPrivate();
            publicKey = pair.getPublic();
        } catch (NoSuchAlgorithmException e) {
            log.error("Error NoSuchAlgorithmException RSAKeyPairGenerator: {}", e.getMessage(), e);
            throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_ENC_INVALID_ALGORITHM);
        }
    }

    public static Map<String,String> generateRsaKeyPair() {
        RsaKeyPairGenerator keyPairGenerator = new RsaKeyPairGenerator();
        Map<String, String> rsaKeyPair = new HashMap<>();
        rsaKeyPair.put(GENERATED_RSA_PRIVATE_KEY, Base64.getEncoder().encodeToString(keyPairGenerator.getPrivateKey().getEncoded()));
        rsaKeyPair.put(GENERATED_RSA_PUBLIC_KEY, Base64.getEncoder().encodeToString(keyPairGenerator.getPublicKey().getEncoded()));
        return rsaKeyPair;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
