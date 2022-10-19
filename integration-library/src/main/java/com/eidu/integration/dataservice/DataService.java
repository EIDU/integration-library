package com.eidu.integration.dataservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import androidx.annotation.NonNull;
import java.util.Map;
import org.json.JSONObject;

public final class DataService {

    static String TAG = "DataService";
    static IDataServiceInterface service = null;

    private DataService() {}

    private static final ServiceConnection connection =
            new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder binder) {
                    Log.i(TAG, "bindDataService.onServiceConnected");
                    service = IDataServiceInterface.Stub.asInterface(binder);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    Log.i(TAG, "bindDataService.onServiceDisconnected");
                    service = null;
                }
            };

    public static void bindDataService(@NonNull Context applicationContext) {
        applicationContext.bindService(
                new Intent("com.eidu.dataservicedemo.service.BIND")
                        .setPackage("com.eidu.dataservicedemo"),
                connection,
                Context.BIND_AUTO_CREATE);
    }

    public static void recordEvent(
            @NonNull String id,
            @NonNull Map<String, Object> segmentation,
            @NonNull Long time,
            @NonNull Long duration) {
        try {
            if (service != null)
                service.recordEvent(id, new JSONObject(segmentation).toString(), time, duration);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
