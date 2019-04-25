package tech.liujin.manager;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.IntDef;
import android.support.v4.util.ArraySet;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
       * {@link NetStateChangeManager}没有注册
       */
      public static final int RECEIVER_UNREGISTER    = 0;
      /**
       * wifi mobile 都没有连接
       */
      public static final int WIFI_MOBILE_DISCONNECT = 1;
      /**
       * 只有mobile连接
       */
      public static final int ONLY_MOBILE_CONNECT    = 2;
      /**
       * 只有wifi连接
       */
      public static final int ONLY_WIFI_CONNECT      = 3;
      /**
       * wifi mobile都连接了
       */
      public static final int WIFI_MOBILE_CONNECT    = 4;
      /**
       * 当前网络状态
       */
      @NetStateValue
      private static      int sCurrentNetState       = RECEIVER_UNREGISTER;

      /**
       * 注册的网络变化监听
       */
      private static ArraySet<OnNetStateChangedListener> sListeners = new ArraySet<>();

      /**
       * 监听系统网络变化的receiver
       */
      private static NetStateReceiver sNetStateReceiver;

      /**
       * 注册一个网络变化的receiver,和 app 生命周期绑定,当不需要监听网络变化时解除注册{@link #destroy(Context)}
       *
       * @param context context
       */
      public static void create ( Context context ) {

            /* 如果没有注册过 receiver 注册一个新的 */
            if( sNetStateReceiver == null ) {
                  sNetStateReceiver = new NetStateReceiver();
                  IntentFilter filter = new IntentFilter();
                  filter.addAction( ConnectivityManager.CONNECTIVITY_ACTION );
                  context.getApplicationContext().registerReceiver( sNetStateReceiver, filter );
            }
      }

      /**
       * 注册一个网络变化的receiver,和 app 生命周期绑定,当不需要监听网络变化时解除注册{@link #destroy(Context)}
       *
       * @param context context
       */
      public static void create ( Context context, NetStateReceiver receiver ) {

            /* 如果没有注册过 receiver 注册一个新的 */
            if( sNetStateReceiver == null ) {
                  sNetStateReceiver = receiver;

                  IntentFilter filter = new IntentFilter();
                  filter.addAction( ConnectivityManager.CONNECTIVITY_ACTION );
                  context.getApplicationContext().registerReceiver( sNetStateReceiver, filter );
            } else {

                  if( sNetStateReceiver != receiver ) {
                        destroy( context );
                        create( context, receiver );
                  }
            }
      }

      /**
       * 解除已经注册的receiver,
       * 并不会清空{@link NetStateChangeManager#sListeners},需要自己{@link
       * NetStateChangeManager#removeListener(OnNetStateChangedListener)}
       *
       * @param context context
       */
      public static void destroy ( Context context ) {

            if( sNetStateReceiver != null ) {
                  sCurrentNetState = RECEIVER_UNREGISTER;
                  context.getApplicationContext().unregisterReceiver( sNetStateReceiver );
                  sNetStateReceiver = null;
            }
      }

      /**
       * 获取当前网络状态
       *
       * @return one of {@link NetStateValue}
       */
      @NetStateValue
      public static int getCurrentNetState ( ) {

            return sCurrentNetState;
      }

      /**
       * 因为该方法在{@link NetStateReceiver#onReceive(Context, Intent)}中回调,因为 onReceive
       * 方法需要尽快处理,所以使用Handler转发一下
       *
       * @param state one of {@link NetStateValue}
       */

      static void onNetWorkStateChanged ( int state ) {

            sCurrentNetState = state;
            notifySateChanged( state );
      }

      /**
       * 添加一个网络状态变化的监听,当{@link NetStateChangeManager}收到网络变化之后会通知监听者们
       *
       * @param listener 新添加的listener
       */
      public static void addListener ( OnNetStateChangedListener listener ) {

            if( listener != null ) {

                  if( sListeners.contains( listener ) ) {
                        return;
                  }

                  sListeners.add( listener );
                  if( sCurrentNetState != RECEIVER_UNREGISTER ) {
                        listener.onNetWorkStateChanged( sCurrentNetState );
                  }
            }
      }

      /**
       * 移除监听
       *
       * @param listener 之前设置的监听
       */
      public static void removeListener ( OnNetStateChangedListener listener ) {

            if( listener != null ) {
                  sListeners.remove( listener );
            }
      }

      /**
       * 移除所有监听
       */
      public static void clearListener ( ) {

            sListeners.clear();
      }

      /**
       * 通知网络状态改变了
       *
       * @param state 新网络状态
       */
      private static void notifySateChanged ( @NetStateValue int state ) {

            for( OnNetStateChangedListener listener : sListeners ) {
                  listener.onNetWorkStateChanged( state );
            }
      }

      /**
       * 网络状态值
       *
       * @author: Liujin
       * @version: V1.0
       * @date: 2018-07-25
       * @time: 9:53
       */
      @IntDef(value = { WIFI_MOBILE_DISCONNECT,
                        ONLY_WIFI_CONNECT,
                        ONLY_MOBILE_CONNECT,
                        WIFI_MOBILE_CONNECT,
                        RECEIVER_UNREGISTER })
      @Retention(RetentionPolicy.RUNTIME)
      @Inherited
      @Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
      public static @interface NetStateValue { }
}
