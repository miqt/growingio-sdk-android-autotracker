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

package com.growingio.android.sdk.track.events;

import com.growingio.android.sdk.track.events.base.BaseAttributesEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomEvent extends BaseAttributesEvent {
    private static final long serialVersionUID = 1L;

    private final String mEventName;

    protected CustomEvent(Builder eventBuilder) {
        super(eventBuilder);
        mEventName = eventBuilder.mEventName;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject json = super.toJSONObject();
        try {
            json.put("eventName", getEventName());
        } catch (JSONException ignored) {
        }
        return json;
    }

    public String getEventName() {
        return checkValueSafe(mEventName);
    }

    public static class Builder extends BaseAttributesEvent.Builder<CustomEvent> {
        private String mEventName;

        public Builder() {
            super();
        }

        @Override
        public String getEventType() {
            return TrackEventType.CUSTOM;
        }

        public Builder setEventName(String eventName) {
            mEventName = eventName;
            return this;
        }

        @Override
        public Builder setAttributes(Map<String, String> attributes) {
            super.setAttributes(attributes);
            return this;
        }

        @Override
        public CustomEvent build() {
            return new CustomEvent(this);
        }
    }

    public static class AttributesBuilder {
        Map<String, String> attributes;
        private static final String LIST_SPLIT = "||";

        private AttributesBuilder() {
            attributes = new HashMap<>();
        }

        public static AttributesBuilder getAttributesBuilder() {
            return new AttributesBuilder();
        }

        public AttributesBuilder addAttribute(String key, String value) {
            if (key != null && value != null) {
                attributes.put(key, value);
            }

            return this;
        }

        public <T> AttributesBuilder addAttribute(String key, List<T> value) {
            if (key != null && value != null && !value.isEmpty()) {
                StringBuilder valueBuilder = new StringBuilder();
                valueBuilder.append(value.get(0));
                for (int i = 1; i < value.size(); ++i) {
                    valueBuilder.append(LIST_SPLIT).append(value.get(i));
                }
                attributes.put(key, valueBuilder.toString());
            }

            return this;
        }

        public Map<String, String> getAttributes() {
            return attributes;
        }
    }
}
