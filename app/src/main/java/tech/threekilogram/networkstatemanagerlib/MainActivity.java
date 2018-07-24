package tech.threekilogram.networkstatemanagerlib;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import tech.threekilogram.network.state.manager.NetWorkStateChangeManager;
import tech.threekilogram.network.state.manager.NetWorkStateUtils;
import tech.threekilogram.network.state.manager.OnNetWorkStateChangedListener;

/**
 * @author liujin
 */
public class MainActivity extends AppCompatActivity implements OnNetWorkStateChangedListener {

      private static final String TAG = MainActivity.class.getSimpleName();

      @Override
      protected void onCreate (Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            NetWorkStateChangeManager.registerReceiver(this);
            NetWorkStateChangeManager.getInstance().registerListener(this);
      }

      @Override
      protected void onDestroy () {

            super.onDestroy();

            NetWorkStateChangeManager.unRegisterReceiver(this);
            NetWorkStateChangeManager.getInstance().unregisterListener(this);
      }

      @Override
      public void onNetWorkStateChanged (int state) {

            Log.e(TAG, "onNetWorkStateChanged : " + state);
      }

      public void getCurrentState (View view) {

            NetWorkStateChangeManager instance = NetWorkStateChangeManager.getInstance();
            int currentNetState = instance.getCurrentNetState();
            Log.e(TAG, "getCurrentState : " + currentNetState);
      }

      public void isConnect (View view) {

            boolean networkConnected = NetWorkStateUtils.isNetworkConnected(this);
            Log.e(TAG, "isConnect : network " + networkConnected);

            boolean wifiConnected = NetWorkStateUtils.isWifiConnected(this);
            Log.e(TAG, "isConnect : wifi " + wifiConnected);

            boolean mobileConnected = NetWorkStateUtils.isMobileConnected(this);
            Log.e(TAG, "isConnect : mobile " + mobileConnected);
      }
}
