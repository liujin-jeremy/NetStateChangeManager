package tech.threekilogram.networkstatemanagerlib;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import tech.threekilogram.network.state.manager.NetStateChangeManager;
import tech.threekilogram.network.state.manager.NetStateUtils;
import tech.threekilogram.network.state.manager.OnNetStateChangedListener;

/**
 * @author liujin
 */
public class MainActivity extends AppCompatActivity implements OnNetStateChangedListener {

      private static final String TAG = MainActivity.class.getSimpleName();

      @Override
      protected void onCreate ( Bundle savedInstanceState ) {

            super.onCreate( savedInstanceState );
            setContentView( R.layout.activity_main );
      }

      @Override
      protected void onDestroy ( ) {

            super.onDestroy();
      }

      @Override
      public void onNetWorkStateChanged ( int state ) {

            Log.e( TAG, "onNetWorkStateChanged : " + state );
      }

      public void getCurrentState ( View view ) {

            NetStateChangeManager instance = NetStateChangeManager.getInstance();
            int currentNetState = instance.getCurrentNetState();
            Log.e( TAG, "getCurrentState : " + currentNetState );
      }

      public void isConnect ( View view ) {

            boolean networkConnected = NetStateUtils.isNetworkConnected( this );
            Log.e( TAG, "isConnect : network " + networkConnected );

            boolean wifiConnected = NetStateUtils.isWifiConnected( this );
            Log.e( TAG, "isConnect : wifi " + wifiConnected );

            boolean mobileConnected = NetStateUtils.isMobileConnected( this );
            Log.e( TAG, "isConnect : mobile " + mobileConnected );
      }

      public void register ( View view ) {

            NetStateChangeManager.registerReceiver( MainActivity.this );
            NetStateChangeManager.getInstance().addListener( MainActivity.this );
      }

      public void unRegister ( View view ) {

            NetStateChangeManager.unRegisterReceiver( this );
            NetStateChangeManager.getInstance().removeListener( this );
      }

      public void toMain2 ( View view ) {

            Main2Activity.start( this );
      }
}
