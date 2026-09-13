package org.kbalazs.smart_scrum_poker_backend_native.helpers;

import org.kbalazs.smart_scrum_poker_backend_native.SmartScrumPokerBackendNativeApplication;
import org.kbalazs.smart_scrum_poker_backend_native.config.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = SmartScrumPokerBackendNativeApplication.class)
@EnableAspectJAutoProxy
public abstract class AbstractTest
{
    @Autowired
    protected ApplicationProperties applicationProperties;
}
