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

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.gio.test.three.autotrack.activity.NestedFragmentActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

@RunWith(PluginRobolectricTestRunner.class)
@Config(sdk = {28}, shadows = {ShadowUrlResourceProvider.class})
public class ExampleTest {
    @Test
    public void one() {
        ShadowLog.stream = System.err;
        ActivityScenario<NestedFragmentActivity> launch = ActivityScenario.launch(NestedFragmentActivity.class);
        System.err.println("xxx one");
        launch.close();
    }

    @Test
    public void two() {
        System.err.println("xxx two");
    }


}
