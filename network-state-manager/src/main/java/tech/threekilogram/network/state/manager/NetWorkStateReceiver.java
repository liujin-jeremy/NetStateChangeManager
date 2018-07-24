package tech.threekilogram.network.state.manager;

import static tech.threekilogram.network.state.manager.OnNetWorkStateChangedListener.ONLY_MOBILE_CONNECT;
import static tech.threekilogram.network.state.manager.OnNetWorkStateChangedListener.ONLY_WIFI_CONNECT;
import static tech.threekilogram.network.state.manager.OnNetWorkStateChangedListener.WIFI_MOBILE_CONNECT;
import static tech.threekilogram.network.state.manager.OnNetWorkStateChangedListener.WIFI_MOBILE_DISCONNECT;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;

/**
 * 监听网络变化
 *
 * @author: Liujin
 * @version: V1.0
 * @date: 2018-07-23
 * @time: 18:04
 */

public class NetWorkStateReceiver extends BroadcastReceiver {

      private NetWorkStateChangeManager mNetWorkStateChangeManager;

      void setNetWorkStateChangeManager (
          NetWorkStateChangeManager netWorkStateChangeManager) {

            mNetWorkStateChangeManager = netWorkStateChangeManager;
      }

      public NetWorkStateChangeManager getNetWorkStateChangeManager () {

            return mNetWorkStateChangeManager;
      }

      @Override
      public void onReceive (Context context, Intent intent) {

            //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
            if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

                  getNetWorkState(context);
            } else {

                  getNetWorkStateApi23(context);
            }
      }

      /**
       * api 23 之前使用此方法读取网络状态
       *
       * @param context context
       */
      private void getNetWorkState (Context context) {

            //获得ConnectivityManager对象
            ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //获取ConnectivityManager对象对应的NetworkInfo对象
            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo =
                connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            //获取移动数据连接的信息
            NetworkInfo dataNetworkInfo =
                connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if(wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {

                  if(mNetWorkStateChangeManager != null) {
                        mNetWorkStateChangeManager.onNetWorkStateChanged(
                            WIFI_MOBILE_CONNECT);
                  }
            } else if(wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {

                  if(mNetWorkStateChangeManager != null) {
                        mNetWorkStateChangeManager
                            .onNetWorkStateChanged(ONLY_WIFI_CONNECT);
                  }
            } else if(!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {

                  if(mNetWorkStateChangeManager != null) {
                        mNetWorkStateChangeManager.onNetWorkStateChanged(ONLY_MOBILE_CONNECT);
                  }
            } else {

                  if(mNetWorkStateChangeManager != null) {
                        mNetWorkStateChangeManager.onNetWorkStateChanged(WIFI_MOBILE_DISCONNECT);
                  }
            }
      }

      /**
       * api 23 之后使用此方法读取网络状态
       *
       * @param context context
       */
      @RequiresApi(api = VERSION_CODES.LOLLIPOP)
      private void getNetWorkStateApi23 (Context context) {

            //API大于23时使用下面的方式进行网络监听

            //获得ConnectivityManager对象
            ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if(connMgr == null) {
                  return;
            }

            //获取所有网络连接的信息
            Network[] networks = connMgr.getAllNetworks();

            boolean wifiConnect = false;
            boolean dataNetworkConnect = false;

            //通过循环将网络信息逐个取出来
            for(int i = 0; i < networks.length; i++) {

                  //获取ConnectivityManager对象对应的NetworkInfo对象
                  NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
                  int type = networkInfo.getType();

                  if(type == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected()) {

                        wifiConnect = true;
                  } else if(type == ConnectivityManager.TYPE_MOBILE &&
                      networkInfo.isConnected()) {

                        dataNetworkConnect = true;
                  }
            }

            if(wifiConnect) {

                  if(dataNetworkConnect) {

                        if(mNetWorkStateChangeManager != null) {
                              mNetWorkStateChangeManager.onNetWorkStateChanged(
                                  WIFI_MOBILE_CONNECT);
                        }
                  } else {
                        if(mNetWorkStateChangeManager != null) {
                              mNetWorkStateChangeManager
                                  .onNetWorkStateChanged(ONLY_WIFI_CONNECT);
                        }
                  }
            } else {
                  if(dataNetworkConnect) {

                        if(mNetWorkStateChangeManager != null) {
                              mNetWorkStateChangeManager.onNetWorkStateChanged(ONLY_MOBILE_CONNECT);
                        }
                  } else {
                        if(mNetWorkStateChangeManager != null) {
                              mNetWorkStateChangeManager
                                  .onNetWorkStateChanged(WIFI_MOBILE_DISCONNECT);
                        }
                  }
            }
      }
}
