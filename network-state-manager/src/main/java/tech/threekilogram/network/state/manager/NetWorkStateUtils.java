package tech.threekilogram.network.state.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;

/**
 * @author: Liujin
 * @version: V1.0
 * @date: 2018-07-23
 * @time: 18:21
 */
public class NetWorkStateUtils {

      /**
       * 判断是否有网络连接
       */
      public static boolean isNetworkConnected (Context context) {

            if(context != null) {
                  // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
                  ConnectivityManager manager =
                      (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                  if(manager == null) {
                        return false;
                  }

                  // 获取NetworkInfo对象
                  NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                  //判断NetworkInfo对象是否为空
                  if(networkInfo != null) {
                        return networkInfo.isAvailable();
                  }
            }
            return false;
      }

      // ========================= 测试是否处于连接状态 =========================

      /**
       * 测试 wifi 是否连接
       *
       * @param context context
       *
       * @return true:连接 ,false没有连接
       */
      public static boolean isWifiConnected (Context context) {

            //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
            if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

                  return isWifiConnectedCommon(context);
            } else {

                  return isWifiConnectedApi23(context);
            }
      }

      /**
       * 测试 mobile 是否连接
       *
       * @param context context
       *
       * @return true:连接 ,false没有连接
       */
      public static boolean isMobileConnected (Context context) {

            //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
            if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

                  return isMobileConnectedCommon(context);
            } else {

                  return isMobileConnectedApi23(context);
            }
      }

      /**
       * api 23 之前使用此方法读取网络状态
       *
       * @param context context
       */
      private static boolean isWifiConnectedCommon (Context context) {

            //获得ConnectivityManager对象
            ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //获取ConnectivityManager对象对应的NetworkInfo对象
            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo =
                connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            return wifiNetworkInfo.isConnected();
      }

      /**
       * api 23 之前使用此方法读取网络状态
       *
       * @param context context
       */
      private static boolean isMobileConnectedCommon (Context context) {

            //获得ConnectivityManager对象
            ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //获取移动数据连接的信息
            NetworkInfo dataNetworkInfo =
                connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            return dataNetworkInfo.isConnected();
      }

      /**
       * api 23 之后使用此方法读取网络状态
       *
       * @param context context
       */
      @RequiresApi(api = VERSION_CODES.LOLLIPOP)
      private static boolean isWifiConnectedApi23 (Context context) {

            //API大于23时使用下面的方式进行网络监听

            //获得ConnectivityManager对象
            ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if(connMgr == null) {
                  return false;
            }

            //获取所有网络连接的信息
            Network[] networks = connMgr.getAllNetworks();

            //通过循环将网络信息逐个取出来
            for(Network network : networks) {

                  //获取ConnectivityManager对象对应的NetworkInfo对象
                  NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
                  int type = networkInfo.getType();

                  if(type == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected()) {

                        return true;
                  }
            }

            return false;
      }

      /**
       * api 23 之后使用此方法读取网络状态
       *
       * @param context context
       */
      @RequiresApi(api = VERSION_CODES.LOLLIPOP)
      private static boolean isMobileConnectedApi23 (Context context) {

            //API大于23时使用下面的方式进行网络监听

            //获得ConnectivityManager对象
            ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if(connMgr == null) {
                  return false;
            }

            //获取所有网络连接的信息
            Network[] networks = connMgr.getAllNetworks();

            //通过循环将网络信息逐个取出来
            for(Network network : networks) {

                  //获取ConnectivityManager对象对应的NetworkInfo对象
                  NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
                  int type = networkInfo.getType();

                  if(type == ConnectivityManager.TYPE_MOBILE && networkInfo.isConnected()) {

                        return true;
                  }
            }

            return false;
      }
}
