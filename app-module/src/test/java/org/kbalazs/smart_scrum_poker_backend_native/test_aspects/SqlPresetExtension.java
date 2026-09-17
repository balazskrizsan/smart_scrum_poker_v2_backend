package org.kbalazs.smart_scrum_poker_backend_native.test_aspects;

import lombok.NonNull;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.IInsert;
import org.kbalazs.smart_scrum_poker_backend_native.domain_common.services.JooqService;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Method;
import java.util.Arrays;

public class SqlPresetExtension implements BeforeAllCallback, BeforeEachCallback, AfterAllCallback, AfterEachCallback, ParameterResolver
{
    @Override
    public void beforeAll(ExtensionContext context) throws Exception
    {
        handleSqlPreset(context, context.getRequiredTestClass());
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception
    {
        handleSqlPreset(context, context.getRequiredTestMethod());
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception
    {
        handleSqlPresetAfter(context, context.getRequiredTestClass());
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception
    {
        handleSqlPresetAfter(context, context.getRequiredTestMethod());
    }

    private void handleSqlPreset(@NonNull ExtensionContext context, @NonNull Class<?> annotatedClass) throws Exception
    {
        SqlPreset annotation = annotatedClass.getAnnotation(SqlPreset.class);
        if (annotation != null)
        {
            executePresets(context, annotation);
        }
    }

    private void handleSqlPreset(@NonNull ExtensionContext context, @NonNull Method annotatedMethod) throws Exception
    {
        SqlPreset annotation = annotatedMethod.getAnnotation(SqlPreset.class);
        if (annotation != null)
        {
            executePresets(context, annotation);
        }
    }

    private void handleSqlPresetAfter(@NonNull ExtensionContext context, @NonNull Class<?> annotatedClass) throws Exception
    {
        SqlPreset annotation = annotatedClass.getAnnotation(SqlPreset.class);
        if (annotation != null && annotation.truncateAfter())
        {
            executeTruncate(context);
        }
    }

    private void handleSqlPresetAfter(@NonNull ExtensionContext context, @NonNull Method annotatedMethod) throws Exception
    {
        SqlPreset annotation = annotatedMethod.getAnnotation(SqlPreset.class);
        if (annotation != null && annotation.truncateAfter())
        {
            executeTruncate(context);
        }
    }

    private void executePresets(@NonNull ExtensionContext context, @NonNull SqlPreset annotation) throws Exception
    {
        JooqService jooqService = getJooqService(context);
        PresetService presetService = new PresetService();
        
        // Use reflection to set jooqService
        java.lang.reflect.Field field = PresetService.class.getDeclaredField("jooqService");
        field.setAccessible(true);
        field.set(presetService, jooqService);

        if (annotation.truncate())
        {
            presetService.truncateDb();
        }

        Class<? extends IInsert>[] presets = annotation.presets();
        if (presets.length > 0)
        {
            presetService.setupDb(presets);
        }
    }

    private void executeTruncate(@NonNull ExtensionContext context) throws Exception
    {
        JooqService jooqService = getJooqService(context);
        PresetService presetService = new PresetService();
        
        // Use reflection to set jooqService
        java.lang.reflect.Field field = PresetService.class.getDeclaredField("jooqService");
        field.setAccessible(true);
        field.set(presetService, jooqService);

        presetService.truncateDb();
    }

    private JooqService getJooqService(@NonNull ExtensionContext context)
    {
        return SpringExtension.getApplicationContext(context).getBean(JooqService.class);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
    {
        return false;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
    {
        return null;
    }
}
