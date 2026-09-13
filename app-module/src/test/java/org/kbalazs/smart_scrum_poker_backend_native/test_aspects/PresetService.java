package org.kbalazs.smart_scrum_poker_backend_native.test_aspects;

import lombok.NonNull;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.IInsert;
import org.kbalazs.smart_scrum_poker_backend_native.domain_common.services.JooqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static org.kbalazs.smart_scrum_poker_backend_native.db.Public.PUBLIC;

@Service
public class PresetService
{
    @Autowired
    private JooqService jooqService;

    public void truncateDb()
    {
        PUBLIC.getTables()
            .stream()
            .filter(t -> !t.asTable().getName().contains("flyway_schema_history"))
            .forEach(t -> jooqService.getDbContext().truncate(t).restartIdentity().cascade().execute());
    }

    public void setupDb(@NonNull Class<? extends IInsert>[] presets)
        throws InvocationTargetException, InstantiationException, IllegalAccessException
    {
        for (Class<? extends IInsert> clazz : presets)
        {
            List<Field> diFields = getPrivateFinalNotStaticFields(clazz);
            Constructor<? extends IInsert> constructor = getLombokAllRequiredArgsConstructor(clazz, diFields);
            IInsert newInstance = constructor.newInstance();
            newInstance.run(jooqService.getDbContext());
        }
    }

    private <T> List<Field> getPrivateFinalNotStaticFields(@NonNull Class<T> clazz)
    {
        return Arrays.stream(clazz.getDeclaredFields())
            .filter(f -> {
                int modifiers = f.getModifiers();

                return Modifier.isPrivate(modifiers) && Modifier.isFinal(modifiers) && !Modifier.isStatic(modifiers);
            })
            .toList();
    }

    private <T> Constructor<T> getLombokAllRequiredArgsConstructor(
        @NonNull Class<T> clazz,
        @NonNull List<Field> diFields
    )
    {
        List<String> diFieldFqns = diFields.stream().map(f -> f.getType().getName()).toList();

        return (Constructor<T>) Arrays.stream(clazz.getConstructors())
            .filter(c -> {
                var constructorParamFqns = Arrays.stream(c.getParameterTypes()).map(Class::getName).toList();

                return constructorParamFqns.equals(diFieldFqns);
            })
            .toList()
            .getFirst();
    }
}
