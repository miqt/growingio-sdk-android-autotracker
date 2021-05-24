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

package com.growingio.android.sdk.track;

import android.text.TextUtils;

import com.growingio.android.sdk.AppGioModule;
import com.growingio.sdk.annotation.GIOConfig;
import com.growingio.sdk.annotation.GIOModule;

/**
 * <p>
 *
 * @author cpacm 4/28/21
 */
@GIOModule()
public final class GrowingAppModule extends AppGioModule {

    @GIOConfig(tracker = CdpTracker.class,
            config = CdpTrackConfiguration.class)
    public void config(CdpTrackConfiguration configuration) {
        if (TextUtils.isEmpty(configuration.getDataSourceId())) {
            throw new IllegalStateException("DataSourceId is NULL");
        }
    }
}