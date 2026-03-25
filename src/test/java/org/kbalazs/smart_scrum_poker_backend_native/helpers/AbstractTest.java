package org.kbalazs.smart_scrum_poker_backend_native.helpers;

import lombok.NonNull;
import org.junit.jupiter.api.AfterEach;
import org.kbalazs.smart_scrum_poker_backend_native.SmartScrumPokerBackendNativeApplication;
import org.kbalazs.smart_scrum_poker_backend_native.config.ApplicationProperties;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.exceptions.ServiceFactoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = SmartScrumPokerBackendNativeApplication.class)
@EnableAspectJAutoProxy
public abstract class AbstractTest
{
    @Autowired
    protected ApplicationProperties applicationProperties;
    @Autowired
    private ServiceFactory serviceFactory;

    @AfterEach
    public void after()
    {
        serviceFactory.resetMockContainer();
    }

    protected <T> T createInstance(@NonNull Class<T> clazz, @NonNull List<Object> mocks) throws ServiceFactoryException
    {
        mocks.forEach(m -> setOneTimeMock(clazz, m));

        return createInstance(clazz);
    }

    protected <T> T createInstance(@NonNull Class<T> clazz, @NonNull Object mock) throws ServiceFactoryException
    {
        setOneTimeMock(clazz, mock);

        return createInstance(clazz);
    }

    protected <T> T createInstance(@NonNull Class<T> clazz) throws ServiceFactoryException
    {
        return serviceFactory.createInstance(clazz);
    }

    protected void setOneTimeMock(@NonNull Class<?> newClass, @NonNull Object mock)
    {
        serviceFactory.setOneTimeMock(newClass, mock);
    }
}
