package tech.threekilogram.network.state.manager;

import static tech.threekilogram.network.state.manager.NetStateValue.RECEIVER_UNREGISTER;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
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
public class NetStateChangeManager {

      /**
       * 当前网络状态
       */
      @NetStateValue
      private int mCurrentNetState = RECEIVER_UNREGISTER;

      /**
       * 注册的网络变化监听
       */
      private ArrayList<WeakReference<OnNetStateChangedListener>> mListeners = new ArrayList<>();

      /**
       * 辅助转发消息到主线程
       */
      private StateChangeHandler mStateChangeHandler = new StateChangeHandler();
      /**
       * 监听系统网络变化的receiver
       */
      private NetStateReceiver mNetStateReceiver;

      /**
       * single ton
       */
      private NetStateChangeManager ( ) { }

      /**
       * 注册一个网络变化的receiver,和 app 生命周期绑定,当不需要监听网络变化时解除注册{@link #unRegisterReceiver(Context)}
       *
       * @param context context
       */
      public static void registerReceiver ( Context context ) {

            /* 如果没有注册过 receiver 注册一个新的 */

            NetStateReceiver netStateReceiver =
                NetStateChangeManager.getInstance().mNetStateReceiver;

            if( netStateReceiver == null ) {

                  /* 注册 */

                  NetStateReceiver receiver = new NetStateReceiver();
                  receiver.setNetStateChangeManager( NetStateChangeManager.getInstance() );
                  NetStateChangeManager.getInstance().mNetStateReceiver = receiver;

                  IntentFilter filter = new IntentFilter();
                  filter.addAction( ConnectivityManager.CONNECTIVITY_ACTION );
                  context.getApplicationContext().registerReceiver( receiver, filter );
            }
      }

      /**
       * 解除已经注册的receiver,
       * 并不会清空{@link NetStateChangeManager#mListeners},需要自己{@link
       * NetStateChangeManager#removeListener(OnNetStateChangedListener)}
       *
       * @param context context
       */
      public static void unRegisterReceiver ( Context context ) {

            NetStateReceiver netStateReceiver =
                NetStateChangeManager.getInstance().mNetStateReceiver;

            if( netStateReceiver != null ) {

                  context.getApplicationContext().unregisterReceiver( netStateReceiver );
                  NetStateChangeManager.getInstance().mNetStateReceiver = null;
                  NetStateChangeManager.getInstance().mCurrentNetState = RECEIVER_UNREGISTER;
            }
      }

      /**
       * 获取单一实例
       *
       * @return 单实例
       */
      public static NetStateChangeManager getInstance ( ) {

            return SingletonHolder.INSTANCE;
      }

      /**
       * 获取当前网络状态
       *
       * @return one of {@link NetStateValue}
       */
      @NetStateValue
      public int getCurrentNetState ( ) {

            return mCurrentNetState;
      }

      /**
       * 因为该方法在{@link NetStateReceiver#onReceive(Context, Intent)}中回调,因为 onReceive
       * 方法需要尽快处理,所以使用Handler转发一下
       *
       * @param state one of {@link NetStateValue}
       */

      void onNetWorkStateChanged ( int state ) {

            mCurrentNetState = state;
            mStateChangeHandler.sendStateChanged( state );
      }

      /**
       * 添加一个网络状态变化的监听,当{@link NetStateChangeManager}收到网络变化之后会通知监听者们
       *
       * @param listener 新添加的listener
       */
      public void addListener ( OnNetStateChangedListener listener ) {

            if( listener != null ) {
                  for( WeakReference<OnNetStateChangedListener> reference : mListeners ) {

                        if( reference.get() == null ) {
                              mListeners.remove( reference );
                              continue;
                        }

                        if( reference.get() == listener ) {
                              return;
                        }
                  }
                  mListeners.add( new WeakReference<>( listener ) );
                  listener.onNetWorkStateChanged( getCurrentNetState() );
            }
      }

      /**
       * 移除监听
       *
       * @param listener 之前设置的监听
       */
      public void removeListener ( OnNetStateChangedListener listener ) {

            if( listener != null ) {
                  for( WeakReference<OnNetStateChangedListener> reference : mListeners ) {

                        if( reference.get() == null ) {
                              mListeners.remove( reference );
                              continue;
                        }

                        if( reference.get() == listener ) {
                              mListeners.remove( reference );
                              return;
                        }
                  }
            }
      }

      /**
       * 通知网络状态改变了
       *
       * @param state 新网络状态
       */
      void notifySateChanged ( @NetStateValue int state ) {

            for( WeakReference<OnNetStateChangedListener> reference : mListeners ) {

                  if( reference.get() == null ) {
                        mListeners.remove( reference );
                        continue;
                  }

                  reference.get().onNetWorkStateChanged( state );
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

            StateChangeHandler ( ) {

                  super( Looper.getMainLooper() );
            }

            void sendStateChanged ( @NetStateValue int newState ) {

                  sendEmptyMessage( newState );
            }

            @Override
            public void handleMessage ( Message msg ) {

                  NetStateChangeManager.getInstance().notifySateChanged( msg.what );
            }
      }
}
