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

package com.growingio.android.hybrid;

import com.growingio.android.sdk.track.log.Logger;

import org.json.JSONException;
import org.json.JSONObject;

class WebViewJavascriptBridgeConfiguration {
    private static final String TAG = "WebViewJavascriptBridgeConfiguration";

    private final String mProjectId;
    private final String mAppId;
    private final String mAppPackage;
    private final String mNativeSdkVersion;
    private final int mNativeSdkVersionCode;

    WebViewJavascriptBridgeConfiguration(String projectId, String appId, String appPackage, String nativeSdkVersion, int nativeSdkVersionCode) {
        mProjectId = projectId;
        mAppId = appId;
        mAppPackage = appPackage;
        mNativeSdkVersion = nativeSdkVersion;
        mNativeSdkVersionCode = nativeSdkVersionCode;
    }

    JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("projectId", mProjectId);
            jsonObject.put("appId", mAppId);
            jsonObject.put("appPackage", mAppPackage);
            jsonObject.put("nativeSdkVersion", mNativeSdkVersion);
            jsonObject.put("nativeSdkVersionCode", mNativeSdkVersionCode);
            return jsonObject;
        } catch (JSONException e) {
            Logger.e(TAG, e.getMessage(), e);
        }
        return jsonObject;
    }
}
