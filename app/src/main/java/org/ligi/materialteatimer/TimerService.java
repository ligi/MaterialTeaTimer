package org.ligi.materialteatimer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class TimerService extends Service {

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }
}
