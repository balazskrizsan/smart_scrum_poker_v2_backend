package org.kbalazs.smart_scrum_poker_backend_native.helpers;

import org.kbalazs.smart_scrum_poker_backend_native.config.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.ssl.TrustSelfSignedStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

@Service
@RequiredArgsConstructor
public class InsecureKeyStoreService
{
    private final ApplicationProperties applicationProperties;

    private KeyStore keyStore(String file, char[] password) throws Exception
    {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        File key = ResourceUtils.getFile(file);
        try (InputStream in = new FileInputStream(key))
        {
            keyStore.load(in, password);
        }

        return keyStore;
    }

    public SSLContext getSslContext() throws Exception
    {
        char[] password = "password".toCharArray();

        return SSLContextBuilder
            .create()
            .loadKeyMaterial(
                keyStore(applicationProperties.siteP12KeyStoreFilePath(), password),
                password
            )
            .loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
    }
}
