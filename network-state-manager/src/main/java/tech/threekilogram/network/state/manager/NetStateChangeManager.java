package tech.threekilogram.network.state.manager;

import static tech.threekilogram.network.state.manager.NetStateValue.RECEIVER_UNREGISTER;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * 为 app 监听网络状态变化并且通知注册的观察者
 *
 * @author: Liujin
 * @version: V1.0
 * @date: 2018-07-23
 * @time: 18:44
 */
public class NetStateChangeManager implements OnNetStateChangedListener {

      private static final String TAG = NetStateChangeManager.class.getSimpleName();

      // ========================= singleTon =========================
      @NetStateValue
      private int                                                 mCurrentNetState    = RECEIVER_UNREGISTER;
      private ArrayList<WeakReference<OnNetStateChangedListener>> mListeners          = new ArrayList<>();
      private StateChangeHandler                                  mStateChangeHandler = new StateChangeHandler();
      private NetStateReceiver mNetStateReceiver;

      private NetStateChangeManager () {

      }

      /**
       * 注册一个网络变化的receiver,和 app 生命周期绑定
       *
       * @param context context
       */
      public static void registerReceiver (Context context) {

            /* 如果没有注册过 receiver 注册一个新的 */

            NetStateReceiver netStateReceiver =
                NetStateChangeManager.getInstance().mNetStateReceiver;

            if(netStateReceiver == null) {

                  /* 注册 */

                  NetStateReceiver receiver = new NetStateReceiver();
                  receiver.setNetStateChangeManager(NetStateChangeManager.getInstance());
                  NetStateChangeManager.getInstance().setNetStateReceiver(receiver);

                  IntentFilter filter = new IntentFilter();
                  filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                  context.getApplicationContext().registerReceiver(receiver, filter);
            }
      }

      public static NetStateChangeManager getInstance () {

            return SingletonHolder.INSTANCE;
      }

      // ========================= receiver =========================

      /**
       * 解除已经注册的receiver,并不会清空{@link NetStateChangeManager#mListeners},需要自己{@link
       * NetStateChangeManager#removeListener(OnNetStateChangedListener)}
       *
       * @param context context
       */
      public static void unRegisterReceiver (Context context) {

            NetStateReceiver netStateReceiver =
                NetStateChangeManager.getInstance()
                                     .getNetStateReceiver();

            if(netStateReceiver != null) {

                  context.getApplicationContext().unregisterReceiver(netStateReceiver);
                  NetStateChangeManager.getInstance().setNetStateReceiver(null);
                  NetStateChangeManager.getInstance().mCurrentNetState = RECEIVER_UNREGISTER;
            }
      }

      private NetStateReceiver getNetStateReceiver () {

            return mNetStateReceiver;
      }

      // ========================= 注册 =========================

      private void setNetStateReceiver (
          NetStateReceiver netStateReceiver) {

            mNetStateReceiver = netStateReceiver;
      }

      /**
       * 获取当前网络状态
       *
       * @return one of {@link NetStateValue}
       */
      @NetStateValue
      public int getCurrentNetState () {

            return mCurrentNetState;
      }

      // ========================= state =========================

      /**
       * 因为该方法在{@link NetStateReceiver#onReceive(Context, Intent)}中回调,因为 onReceive
       * 方法需要尽快处理,所以使用Handler转发一下
       *
       * @param state one of {@link NetStateValue}
       */
      @Override
      public void onNetWorkStateChanged (int state) {

            mCurrentNetState = state;
            mStateChangeHandler.sendStateChanged(state);
      }

      /**
       * 添加一个网络状态变化的监听,当{@link NetStateChangeManager}收到网络变化之后会通知监听者们
       *
       * @param listener 新添加的listener
       */
      public void addListener (OnNetStateChangedListener listener) {

            if(listener != null) {
                  for(WeakReference<OnNetStateChangedListener> reference : mListeners) {
                        if(reference.get() == listener) {
                              return;
                        }
                  }
                  mListeners.add(new WeakReference<OnNetStateChangedListener>(listener));
            }
      }

      // ========================= notify =========================

      public void removeListener (OnNetStateChangedListener listener) {

            if(listener != null) {
                  for(WeakReference<OnNetStateChangedListener> reference : mListeners) {
                        if(reference.get() == listener) {
                              mListeners.remove(reference);
                              return;
                        }
                  }
            }
      }

      void notifySateChanged (@NetStateValue int state) {

            for(WeakReference<OnNetStateChangedListener> reference : mListeners) {
                  reference.get().onNetWorkStateChanged(state);
            }
      }

      private static class SingletonHolder {

            private static final NetStateChangeManager INSTANCE = new NetStateChangeManager();
      }

      // ========================= 内部类 =========================

      /**
       * 因为 {@link NetStateReceiver#onReceive(Context, Intent)}需要尽快结束,所以使用handler转发一下消息
       */
      private static class StateChangeHandler extends Handler {

            void sendStateChanged (@NetStateValue int newState) {

                  sendEmptyMessage(newState);
            }

            @Override
            public void handleMessage (Message msg) {

                  NetStateChangeManager.getInstance().notifySateChanged(msg.what);
            }
      }
}
