package tech.threekilogram.networkstatemanagerlib;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import tech.threekilogram.network.state.manager.NetStateChangeManager;
import tech.threekilogram.network.state.manager.NetStateUtils;
import tech.threekilogram.network.state.manager.OnNetStateChangedListener;

/**
 * @author liujin
 */
public class MainActivity extends AppCompatActivity implements OnNetStateChangedListener {

      private static final String TAG = MainActivity.class.getSimpleName();

      @Override
      public void onNetWorkStateChanged ( int state ) {

            Log.e( TAG, "onNetWorkStateChanged : " + state );
            Toast.makeText( this, getStateString( state ), Toast.LENGTH_SHORT ).show();
      }

      @Override
      protected void onCreate ( Bundle savedInstanceState ) {

            super.onCreate( savedInstanceState );
            setContentView( R.layout.activity_main );
      }

      @Override
      protected void onDestroy ( ) {

            super.onDestroy();
      }

      public void getCurrentState ( View view ) {

            int currentNetState = NetStateChangeManager.getCurrentNetState();
            Log.e( TAG, "getCurrentState : " + currentNetState );
            Toast.makeText( this, getStateString( currentNetState ), Toast.LENGTH_SHORT ).show();
      }

      public void isConnect ( View view ) {

            boolean networkConnected = NetStateUtils.isNetworkConnected( this );
            Log.e( TAG, "isConnect : network " + networkConnected );

            boolean wifiConnected = NetStateUtils.isWifiConnected( this );
            Log.e( TAG, "isConnect : wifi " + wifiConnected );

            boolean mobileConnected = NetStateUtils.isMobileConnected( this );
            Log.e( TAG, "isConnect : mobile " + mobileConnected );

            Toast.makeText( this, String.valueOf( networkConnected ), Toast.LENGTH_SHORT ).show();
      }

      public void register ( View view ) {

            NetStateChangeManager.addListener( MainActivity.this );
            NetStateChangeManager.create( MainActivity.this );
      }

      public void unRegister ( View view ) {

            NetStateChangeManager.destroy( this );
            NetStateChangeManager.removeListener( this );
      }

      public void toMain2 ( View view ) {

            Main2Activity.start( this );
      }

      private String getStateString ( int state ) {
//            /**
//             * {@link NetStateChangeManager}没有注册
//             */
//            final int RECEIVER_UNREGISTER    = 0;
//            /**
//             * wifi mobile 都没有连接
//             */
//            final int WIFI_MOBILE_DISCONNECT = 1;
//            /**
//             * 只有mobile连接
//             */
//            final int ONLY_MOBILE_CONNECT    = 2;
//            /**
//             * 只有wifi连接
//             */
//            final int ONLY_WIFI_CONNECT      = 3;
//            /**
//             * wifi mobile都连接了
//             */
//            final int WIFI_MOBILE_CONNECT    = 4;

            if( state == 0 ) {
                  return "未注册";
            }
            if( state == 1 ) {
                  return "没有连接";
            }
            if( state == 2 ) {
                  return "手机连接";
            }
            if( state == 3 ) {
                  return "wifi连接";
            }
            if( state == 4 ) {
                  return "都连接";
            }

            return null;
      }
}
