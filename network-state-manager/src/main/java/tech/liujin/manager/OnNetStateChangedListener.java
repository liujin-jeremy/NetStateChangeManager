package tech.liujin.manager;

import tech.liujin.manager.NetStateChangeManager.NetStateValue;

/**
 * 监听网络状态的回调
 *
 * @author: Liujin
 * @version: V1.0
 * @date: 2018-07-23
 * @time: 18:45
 */
public interface OnNetStateChangedListener {

      /**
       * 网络连接状态改变的回调
       *
       * @param state one of {@link NetStateValue}
       */
      void onNetWorkStateChanged ( @NetStateValue int state );
}
