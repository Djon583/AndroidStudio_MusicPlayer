package com.example.myaudioplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import static com.example.myaudioplayer.PlayerActivity.mediaPlayer;

public class BecomingNoisyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (AudioManager.ACTION_HEADSET_PLUG.equals(intent.getAction())) {
            Log.e("asd", "ok");

        }
    }


}
