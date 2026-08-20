package org.kbalazs.smart_scrum_poker_backend_native.test_aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.IInsert;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;

@Aspect
public class SqlPresetAspect
{
    @Autowired
    private PresetService presetService;

    @Pointcut("execution(* org.kbalazs.smart_scrum_poker_backend_native..*(..))")
    protected void findAllSocketDomainTestClasses()
    {
    }

    @Pointcut("@annotation(org.kbalazs.smart_scrum_poker_backend_native.test_aspects.SqlPreset)")
    protected void findSqlPresetAnnotatedTests()
    {
    }

    @Around("findAllSocketDomainTestClasses() && findSqlPresetAnnotatedTests()")
    public Object setup(ProceedingJoinPoint joinPoint) throws Throwable
    {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SqlPreset annotation = method.getAnnotation(SqlPreset.class);

        if (annotation.truncate())
        {
            presetService.truncateDb();
        }

        Class<? extends IInsert>[] presets = annotation.presets();
        if (presets.length > 0)
        {
            presetService.setupDb(presets);
        }

        var proceeded =  joinPoint.proceed();

        if (annotation.truncateAfter())
        {
            presetService.truncateDb();
        }

        return proceeded;
    }
}
