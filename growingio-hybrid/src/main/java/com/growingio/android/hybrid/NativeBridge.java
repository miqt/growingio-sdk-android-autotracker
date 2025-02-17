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

import com.growingio.android.sdk.track.TrackMainThread;
import com.growingio.android.sdk.track.providers.UserInfoProvider;

class NativeBridge {
    private static final String TAG = "NativeBridge";

    private final HybridTransformer mHybridTransformer;

    NativeBridge() {
        mHybridTransformer = new HybridTransformerImp();
    }

    void dispatchEvent(String event) {
        TrackMainThread.trackMain().postEventToTrackMain(mHybridTransformer.transform(event));
    }

    void setNativeUserId(String userId) {
        TrackMainThread.trackMain().postActionToTrackMain(new Runnable() {
            @Override
            public void run() {
                UserInfoProvider.get().setLoginUserId(userId);
            }
        });
    }

    void clearNativeUserId() {
        TrackMainThread.trackMain().postActionToTrackMain(new Runnable() {
            @Override
            public void run() {
                UserInfoProvider.get().setLoginUserId(null);
            }
        });
    }

    void setNativeUserIdAndUserKey(String userId, String userKey) {
        TrackMainThread.trackMain().postActionToTrackMain(new Runnable() {
            @Override
            public void run() {
                UserInfoProvider.get().setLoginUserId(userId, userKey);
            }
        });
    }

    void clearNativeUserIdAndUserKey() {
        TrackMainThread.trackMain().postActionToTrackMain(new Runnable() {
            @Override
            public void run() {
                UserInfoProvider.get().setLoginUserId(null, null);
            }
        });
    }
}
