package com.github.md;

import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.apache.commons.logging.impl.SimpleLog;

@SuppressWarnings("unused")
@TargetClass(LogFactory.class)
final class LogFactorySubstituted {
    @Substitute
    protected static LogFactory newFactory(final String factoryClass,
                                           final ClassLoader classLoader,
                                           final ClassLoader contextClassLoader) {
        return new LogFactoryImpl();
    }
}

@SuppressWarnings("unused")
@TargetClass(LogFactoryImpl.class)
final class LogFactoryImplSubstituted {
    @Substitute
    private Log discoverLogImplementation(String logCategory) {
        return new SimpleLog(logCategory);
    }
}