package tech.threekilogram.network.state.manager;

import static tech.threekilogram.network.state.manager.NetWorkStateValue.RECEIVER_UNREGISTER;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import java.util.ArrayList;

/**
 * 为 app 监听网络状态变化并且通知注册的观察者
 *
 * @author: Liujin
 * @version: V1.0
 * @date: 2018-07-23
 * @time: 18:44
 */
public class NetWorkStateChangeManager implements OnNetWorkStateChangedListener {

      private static final String TAG = NetWorkStateChangeManager.class.getSimpleName();

      // ========================= singleTon =========================

      private NetWorkStateChangeManager () {

      }

      public static NetWorkStateChangeManager getInstance () {

            return SingletonHolder.INSTANCE;
      }

      private static class SingletonHolder {

            private static final NetWorkStateChangeManager INSTANCE = new NetWorkStateChangeManager();
      }

      @NetWorkStateValue
      private int mCurrentNetState = RECEIVER_UNREGISTER;

      private ArrayList<OnNetWorkStateChangedListener> mListeners          = new ArrayList<>();
      private StateChangeHandler                       mStateChangeHandler = new StateChangeHandler();
      private NetWorkStateReceiver mNetWorkStateReceiver;

      // ========================= receiver =========================

      private void setNetWorkStateReceiver (
          NetWorkStateReceiver netWorkStateReceiver) {

            mNetWorkStateReceiver = netWorkStateReceiver;
      }

      private NetWorkStateReceiver getNetWorkStateReceiver () {

            return mNetWorkStateReceiver;
      }

      // ========================= 注册 =========================

      /**
       * 注册一个网络变化的receiver
       *
       * @param context context
       */
      public static void registerReceiver (Context context) {

            /* 如果没有注册过 receiver 注册一个新的 */

            NetWorkStateReceiver netWorkStateReceiver =
                NetWorkStateChangeManager.getInstance().mNetWorkStateReceiver;

            if(netWorkStateReceiver == null) {

                  /* 注册 */

                  NetWorkStateReceiver receiver = new NetWorkStateReceiver();
                  receiver.setNetWorkStateChangeManager(NetWorkStateChangeManager.getInstance());
                  NetWorkStateChangeManager.getInstance().setNetWorkStateReceiver(receiver);

                  IntentFilter filter = new IntentFilter();
                  filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                  context.registerReceiver(receiver, filter);
            }
      }

      /**
       * 解除已经注册的receiver
       *
       * @param context context
       */
      public static void unRegisterReceiver (Context context) {

            NetWorkStateReceiver netWorkStateReceiver =
                NetWorkStateChangeManager.getInstance()
                                         .getNetWorkStateReceiver();

            if(netWorkStateReceiver != null) {

                  context.unregisterReceiver(netWorkStateReceiver);
            }
      }

      // ========================= state =========================

      @NetWorkStateValue
      public int getCurrentNetState () {

            return mCurrentNetState;
      }

      @Override
      public void onNetWorkStateChanged (int state) {

            mCurrentNetState = state;
            mStateChangeHandler.sendStateChanged(state);
      }

      // ========================= notify =========================

      public void registerListener (OnNetWorkStateChangedListener listener) {

            if(listener != null) {
                  mListeners.add(listener);
            }
      }

      public void unregisterListener (OnNetWorkStateChangedListener listener) {

            if(listener != null) {
                  mListeners.remove(listener);
            }
      }

      void notifySateChanged (@NetWorkStateValue int state) {

            for(OnNetWorkStateChangedListener listener : mListeners) {
                  listener.onNetWorkStateChanged(state);
            }
      }

      // ========================= 内部类 =========================

      /**
       * 因为 {@link NetWorkStateReceiver#onReceive(Context, Intent)}需要尽快结束,所以使用handler转发一下消息
       */
      private static class StateChangeHandler extends Handler {

            void sendStateChanged (@NetWorkStateValue int newState) {

                  sendEmptyMessage(newState);
            }

            @Override
            public void handleMessage (Message msg) {

                  NetWorkStateChangeManager.getInstance().notifySateChanged(msg.what);
            }
      }
}
