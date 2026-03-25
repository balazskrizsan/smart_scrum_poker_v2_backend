package org.kbalazs.smart_scrum_poker_backend_native.helpers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.kbalazs.smart_scrum_poker_backend_native.common.servies.Slf4jLongTermLoggerService;
import org.kbalazs.smart_scrum_poker_backend_native.helpers.exceptions.ServiceFactoryException;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.repositories.InsecureUserRepository;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.account_module.repositories.InsecureUserSessionsRepository;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.repositories.InGamePlayersRepository;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.repositories.PokerRepository;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.repositories.TicketRepository;
import org.kbalazs.smart_scrum_poker_backend_native.socket_domain.poker_module.repositories.VoteRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ServiceFactory
{
    protected final SimpMessagingTemplate simpMessagingTemplate;
    protected final PokerRepository pokerRepository;
    protected final InsecureUserRepository insecureUserRepository;
    protected final TicketRepository ticketRepository;
    protected final VoteRepository voteRepository;
    protected final InGamePlayersRepository inGamePlayersRepository;
    protected final InsecureUserSessionsRepository insecureUserSessionsRepository;
    protected final Slf4jLongTermLoggerService slf4jLongTermLoggerService;

    Map<String, Map<String, Object>> oneTimeMocks = new HashMap<>();

    public void setOneTimeMock(
        @NonNull Class<?> newClass,
        @NonNull Object mock
    )
    {
        oneTimeMocks.put(newClass.getName(), new HashMap<>()
        {{
            put(mock.getClass().getName(), mock);
        }});
    }

    public void resetMockContainer()
    {
        oneTimeMocks = new HashMap<>();
    }

    public <T> T createInstance(@NonNull Class<T> clazz) throws ServiceFactoryException
    {
        try
        {
            return createInstanceLogic(clazz);
        }
        catch (InvocationTargetException | InstantiationException | IllegalAccessException e)
        {
            throw new ServiceFactoryException("Service instance creation error: " + e.getMessage(), e);
        }
    }

    private <T> T createInstanceLogic(@NonNull Class<T> clazz)
        throws InvocationTargetException, InstantiationException, IllegalAccessException, ServiceFactoryException
    {
        List<Field> diFields = getPrivateFinalNotStaticFields(clazz);
        Constructor<T> constructor = getLombokAllRequiredArgsConstructor(clazz, diFields);

        List<Object> diObjects = new ArrayList<>();
        for (Field f : diFields)
        {
            try
            {
                Object dependency = getDiObject(clazz, f, getField(f));

                diObjects.add(dependency);
            }
            catch (NoSuchFieldException e)
            {
                Object dependency = getDependency(clazz, f.getType(), createInstance(f.getType()));

                diObjects.add(getDependency(clazz, f.getType(), dependency));
            }
        }

        return constructor.newInstance(diObjects.toArray());
    }

    private <T> Object getDiObject(@NonNull Class<T> clazz, @NonNull Field f, @NonNull Field fieldObject)
    {
        try
        {
            return getDependency(clazz, f.getType(), fieldObject.get(this));
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    private Field getField(@NonNull Field f) throws NoSuchFieldException
    {
        return this.getClass().getDeclaredField(f.getName());
    }

    protected <T> T getDependency(@NonNull Class<?> newClass, @NonNull Class<?> dependencyClass, @NonNull T diObject)
    {
        var newClassFound = oneTimeMocks.getOrDefault(newClass.getName(), null);
        if (newClassFound != null && !newClassFound.isEmpty())
        {
            var dependencyClassFound = (T) newClassFound.getOrDefault(dependencyClass.getName(), null);
            if (dependencyClassFound != null)
            {
                newClassFound.remove(dependencyClass.getName());

                return dependencyClassFound;
            }
        }

        return diObject;
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
