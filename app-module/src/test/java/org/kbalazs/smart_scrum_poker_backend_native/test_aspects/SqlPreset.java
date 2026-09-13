package org.kbalazs.smart_scrum_poker_backend_native.test_aspects;

import org.junit.platform.commons.annotation.Testable;
import org.kbalazs.smart_scrum_poker_backend_native.db_presets.IInsert;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Testable
public @interface SqlPreset
{
    Class<? extends IInsert>[] presets() default {};

    // @todo: implement
    boolean transactional() default true;

    boolean truncate() default true;

    boolean truncateAfter() default true;
}
