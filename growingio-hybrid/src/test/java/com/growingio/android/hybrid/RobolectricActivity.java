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

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.growingio.android.sdk.track.listener.event.ActivityLifecycleEvent;

import static android.widget.LinearLayout.VERTICAL;
import static com.growingio.android.sdk.track.listener.event.ActivityLifecycleEvent.EVENT_TYPE.ON_CREATED;
import static com.growingio.android.sdk.track.listener.event.ActivityLifecycleEvent.EVENT_TYPE.ON_DESTROYED;
import static com.growingio.android.sdk.track.listener.event.ActivityLifecycleEvent.EVENT_TYPE.ON_NEW_INTENT;
import static com.growingio.android.sdk.track.listener.event.ActivityLifecycleEvent.EVENT_TYPE.ON_PAUSED;
import static com.growingio.android.sdk.track.listener.event.ActivityLifecycleEvent.EVENT_TYPE.ON_RESUMED;
import static com.growingio.android.sdk.track.listener.event.ActivityLifecycleEvent.EVENT_TYPE.ON_SAVE_INSTANCE_STATE;
import static com.growingio.android.sdk.track.listener.event.ActivityLifecycleEvent.EVENT_TYPE.ON_STARTED;
import static com.growingio.android.sdk.track.listener.event.ActivityLifecycleEvent.EVENT_TYPE.ON_STOPPED;

public class RobolectricActivity extends FragmentActivity {

    private ActivityLifecycleEvent.EVENT_TYPE state;

    private TextView textView;
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        state = ON_CREATED;
        super.onCreate(savedInstanceState);
        textView = new TextView(this);
        textView.setText("this is cpacm");

        imageView = new ImageView(this);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(VERTICAL);
        linearLayout.addView(textView);
        linearLayout.addView(imageView);
        setContentView(linearLayout);
        WindowManager.LayoutParams wlp = new WindowManager.LayoutParams();
        wlp.packageName = "com.cpacm.test";
        getWindow().getDecorView().setLayoutParams(wlp);
    }

    public android.app.Fragment attachFragment(android.app.Fragment appFragment) {
        getFragmentManager().beginTransaction()
                .add(appFragment, "app")
                .commit();
        return appFragment;
    }

    public void attachFragmentX(Fragment supportFragment) {
        getSupportFragmentManager().beginTransaction()
                .add(supportFragment, "androidX")
                .commit();
    }

    public ActivityLifecycleEvent.EVENT_TYPE getState() {
        return state;
    }

    public TextView getTextView() {
        return textView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    @Override
    protected void onResume() {
        state = ON_RESUMED;
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        state = ON_NEW_INTENT;
        super.onNewIntent(intent);
    }

    @Override
    protected void onStart() {
        state = ON_STARTED;
        super.onStart();
    }

    @Override
    protected void onPause() {
        state = ON_PAUSED;
        super.onPause();
    }

    @Override
    protected void onStop() {
        state = ON_STOPPED;
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        state = ON_SAVE_INSTANCE_STATE;
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        state = ON_DESTROYED;
        super.onDestroy();
    }
}
