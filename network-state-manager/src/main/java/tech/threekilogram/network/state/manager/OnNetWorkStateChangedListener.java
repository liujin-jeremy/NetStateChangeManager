package tech.threekilogram.network.state.manager;

/**
 * 监听网络状态的回调
 *
 * @author: Liujin
 * @version: V1.0
 * @date: 2018-07-23
 * @time: 18:45
 */
public interface OnNetWorkStateChangedListener {

      /**
       * wifi 连接状态改变的回调
       *
       * @param state one of {@link NetWorkStateValue}
       */
      void onNetWorkStateChanged (@NetWorkStateValue int state);
}
