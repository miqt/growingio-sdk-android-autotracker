/*
 * Copyright (C) 2020 Beijing Yishu Technology Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.growingio.sdk.integrationtests;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Sortable;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URLClassLoader;
import java.util.List;

public class PluginAndroidJUnit4 extends Runner implements Filterable, Sortable {

    private static final String TAG = "AndroidJUnit4";

    private final Runner delegate;

    public PluginAndroidJUnit4(Class<?> klass) throws InitializationError {
        delegate = loadRunner(klass);
    }

    private static String getRunnerClassName() {
        String runnerClassName = System.getProperty("android.junit.runner", null);
        if (runnerClassName == null) {
            if (!System.getProperty("java.runtime.name").toLowerCase().contains("android")
                    && hasClass("org.robolectric.RobolectricTestRunner")) {
                return "org.robolectric.RobolectricTestRunner";
            } else {
                return "androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner";
            }
        }
        return runnerClassName;
    }

    private static boolean hasClass(String className) {
        try {
            return Class.forName(className) != null;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static Runner loadRunner(Class<?> testClass) throws InitializationError {
        String runnerClassName = getRunnerClassName();
        return loadRunner(testClass, runnerClassName);
    }

    @SuppressWarnings("unchecked")
    private static Runner loadRunner(Class<?> testClass, String runnerClassName)
            throws InitializationError {

        Class<? extends Runner> runnerClass = null;
        Thread.currentThread().setContextClassLoader(testClassLoader);
        try {
            runnerClass = (Class<? extends Runner>) testClassLoader.loadClass(runnerClassName);
        } catch (ClassNotFoundException e) {
            throwInitializationError(
                    String.format(
                            "Delegate runner %s for AndroidJUnit4 could not be found.\n", runnerClassName),
                    e);
        }

        Constructor<? extends Runner> constructor = null;
        try {
            constructor = runnerClass.getConstructor(Class.class);
        } catch (NoSuchMethodException e) {
            throwInitializationError(
                    String.format(
                            "Delegate runner %s for AndroidJUnit4 requires a public constructor that takes a"
                                    + " Class<?>.\n",
                            runnerClassName),
                    e);
        }

        try {
            return constructor.newInstance(testClass);
        } catch (IllegalAccessException e) {
            throwInitializationError(
                    String.format("Illegal constructor access for test runner %s\n", runnerClassName), e);
        } catch (InstantiationException e) {
            throwInitializationError(
                    String.format("Failed to instantiate test runner %s\n", runnerClassName), e);
        } catch (InvocationTargetException e) {
            String details = getInitializationErrorDetails(e, testClass);
            throwInitializationError(
                    String.format("Failed to instantiate test runner %s\n%s\n", runnerClass, details), e);
        }
        throw new IllegalStateException("Should never reach here");
    }

    private static void throwInitializationError(String details, Throwable cause)
            throws InitializationError {
        throw new InitializationError(new RuntimeException(details, cause));
    }

    private static String getInitializationErrorDetails(Throwable throwable, Class<?> testClass) {
        StringBuilder innerCause = new StringBuilder();
        final Throwable cause = throwable.getCause();

        if (cause == null) {
            return "";
        }

        final Class<? extends Throwable> causeClass = cause.getClass();
        if (causeClass == InitializationError.class) {
            final InitializationError initializationError = (InitializationError) cause;
            final List<Throwable> testClassProblemList = initializationError.getCauses();
            innerCause.append(
                    String.format(
                            "Test class %s is malformed. (%s problems):\n",
                            testClass, testClassProblemList.size()));
            for (Throwable testClassProblem : testClassProblemList) {
                innerCause.append(testClassProblem).append("\n");
            }
        }
        return innerCause.toString();
    }

    @Override
    public Description getDescription() {
        return delegate.getDescription();
    }

    @Override
    public void run(RunNotifier runNotifier) {
        delegate.run(runNotifier);
    }

    @Override
    public void filter(Filter filter) throws NoTestsRemainException {
        ((Filterable) delegate).filter(filter);
    }

    @Override
    public void sort(Sorter sorter) {
        ((Sortable) delegate).sort(sorter);
    }

    static final ClassLoader testClassLoader = new TestClassLoader();

    private Class<?> getFromTestClassloader(Class<?> clazz) throws InitializationError {
        try {
            return Class.forName(clazz.getName(), true, testClassLoader);
        } catch (ClassNotFoundException e) {
            throw new InitializationError(e);
        }
    }

    public static class TestClassLoader extends URLClassLoader {
        public TestClassLoader() {
            super(((URLClassLoader) getSystemClassLoader()).getURLs());
        }

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            System.err.println("xxx loadClass = " + name);
            if (name.startsWith("org.mypackages.")) {
                return super.findClass(name);
            }
            return super.loadClass(name);
        }

        @Override
        public InputStream getResourceAsStream(String name) {
            System.err.println("xxx getResourceAsStream  = " + name);
            return super.getResourceAsStream(name);
        }
    }
}

