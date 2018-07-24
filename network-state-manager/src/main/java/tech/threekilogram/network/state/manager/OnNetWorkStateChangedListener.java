package tech.threekilogram.network.state.manager;

import android.support.annotation.IntDef;

/**
 * 监听网络状态的回调
 *
 * @author: Liujin
 * @version: V1.0
 * @date: 2018-07-23
 * @time: 18:45
 */
public interface OnNetWorkStateChangedListener {

      int RECEIVER_UNREGISTER    = -1;
      int WIFI_MOBILE_DISCONNECT = 0;
      int ONLY_MOBILE_CONNECT    = 1;
      int ONLY_WIFI_CONNECT      = 2;
      int WIFI_MOBILE_CONNECT    = 3;

      /**
       * wifi 连接状态改变的回调
       *
       * @param state
       */
      void onNetWorkStateChanged (@NetWorkStateValue int state);

      @IntDef(value = {WIFI_MOBILE_DISCONNECT,
                       ONLY_WIFI_CONNECT,
                       ONLY_MOBILE_CONNECT,
                       WIFI_MOBILE_CONNECT,
                       RECEIVER_UNREGISTER})
      public @interface NetWorkStateValue {}
}
