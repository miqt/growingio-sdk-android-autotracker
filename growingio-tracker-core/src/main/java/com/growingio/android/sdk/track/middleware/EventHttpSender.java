/*
 *   Copyright (C) 2020 Beijing Yishu Technology Co., Ltd.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.growingio.android.sdk.track.middleware;

import android.os.Build;
import android.text.TextUtils;

import com.growingio.android.sdk.CoreConfiguration;
import com.growingio.android.sdk.TrackerContext;
import com.growingio.android.sdk.track.events.base.BaseEvent;
import com.growingio.android.sdk.track.http.EventEncoder;
import com.growingio.android.sdk.track.http.EventResponse;
import com.growingio.android.sdk.track.http.EventUrl;
import com.growingio.android.sdk.track.log.Logger;
import com.growingio.android.sdk.track.modelloader.ModelLoader;
import com.growingio.android.sdk.track.providers.ConfigurationProvider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class EventHttpSender implements IEventNetSender {
    private static final String TAG = "EventHttpSender";

    private final String mProjectId;
    private final String mServerHost;

    public EventHttpSender() {
        CoreConfiguration configuration = ConfigurationProvider.core();
        mProjectId = configuration.getProjectId();
        mServerHost = configuration.getDataCollectionServerHost();
    }

    private ModelLoader<EventUrl, EventResponse> getNetworkModelLoader() {
        return TrackerContext.get().getRegistry().getModelLoader(EventUrl.class, EventResponse.class);
    }

    @Override
    public SendResponse send(byte[] events, String mediaType) {
        if (events == null || events.length == 0) {
            return new SendResponse(false, 0);
        }
        if (getNetworkModelLoader() == null) {
            Logger.e(TAG, "please register http request component first");
            return new SendResponse(false, 0);
        }
        long time = System.currentTimeMillis();
        EventUrl eventUrl = new EventUrl(mServerHost, time)
                .addPath("v3")
                .addPath("projects")
                .addPath(mProjectId)
                .addPath("collect")
                .addParam("stm", String.valueOf(time))
                .setBodyData(events);
        if (!TextUtils.isEmpty(mediaType)) eventUrl.setMediaType(mediaType);
        //data encoder - https://codes.growingio.com/w/api_v3_interface/
        EventEncoder encoder = TrackerContext.get().executeData(new EventEncoder(eventUrl), EventEncoder.class, EventEncoder.class);
        if (encoder != null) {
            eventUrl = encoder.getEventUrl();
        }

        byte[] data = eventUrl.getRequestBody();


        ModelLoader.LoadData<EventResponse> loadData = getNetworkModelLoader().buildLoadData(eventUrl);
        if (!loadData.fetcher.getDataClass().isAssignableFrom(EventResponse.class)) {
            Logger.e(TAG, new IllegalArgumentException("illegal data class for http response."));
            return new SendResponse(false, 0);
        }
        EventResponse response = loadData.fetcher.executeData();

        boolean successful = response != null && response.isSucceeded();
        if (successful) {
            Logger.d(TAG, "Send events successfully");
        } else {
            Logger.d(TAG, "Send events failed, response = " + response);
        }
        long totalUsed = data == null ? 0L : data.length;
        return new SendResponse(successful, totalUsed);
    }

    public byte[] defaultDataTransfer(List<GEvent> events) {
        if (events == null || events.isEmpty()) {
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        for (GEvent event : events) {
            if (event instanceof BaseEvent) {
                JSONObject eventJson = ((BaseEvent) event).toJSONObject();
                jsonArray.put(eventJson);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return jsonArray.toString().getBytes(StandardCharsets.UTF_8);
        } else {
            return jsonArray.toString().getBytes();
        }
    }
}
