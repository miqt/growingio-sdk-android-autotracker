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

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.powermock.reflect.Whitebox;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.internal.AndroidSandbox;
import org.robolectric.internal.bytecode.ResourceProvider;
import org.robolectric.internal.bytecode.SandboxClassLoader;

import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

public class PluginRobolectricTestRunner extends RobolectricTestRunner {
    public PluginRobolectricTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected AndroidSandbox getSandbox(FrameworkMethod method) {
        AndroidSandbox sandbox = super.getSandbox(method);
        SandboxClassLoader robolectricClassLoader = (SandboxClassLoader) sandbox.getRobolectricClassLoader();
        URLClassLoader resourceProvider = Whitebox.getInternalState(robolectricClassLoader, "resourceProvider");
        MyUrlResourceProvider myUrlResourceProvider = new MyUrlResourceProvider(new URL[0], resourceProvider);
        Whitebox.setInternalState(robolectricClassLoader, "resourceProvider", myUrlResourceProvider);
        return sandbox;
    }

    public class MyUrlResourceProvider extends URLClassLoader implements ResourceProvider {
        public MyUrlResourceProvider(URL[] urls, ClassLoader parent) {
            super(urls, parent);
        }

        @Override
        public InputStream getResourceAsStream(String name) {
            InputStream resourceAsStream = super.getResourceAsStream(name);
            if (resourceAsStream == null) {
                System.err.println("xxx getResourceAsStream name " + name);
            }
            return resourceAsStream;
        }
    }
}
