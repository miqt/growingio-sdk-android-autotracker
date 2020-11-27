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

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;
import org.robolectric.internal.bytecode.UrlResourceProvider;

import java.io.InputStream;
import java.net.URL;

@Implements(UrlResourceProvider.class)
public class ShadowUrlResourceProvider {
    @RealObject
    public UrlResourceProvider realUrlResourceProvider;

    @Implementation
    protected void __constructor__(URL... urls) {
        System.err.println("xxx __constructor__ " + urls);
    }

    @Implementation
    protected InputStream getResourceAsStream(String resName) {
        System.err.println("xxx getResourceAsStream " + resName);
        return realUrlResourceProvider.getResourceAsStream(resName);
    }
}
