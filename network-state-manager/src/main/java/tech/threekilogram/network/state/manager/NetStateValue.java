package tech.threekilogram.network.state.manager;

import static tech.threekilogram.network.state.manager.NetStateValue.ONLY_MOBILE_CONNECT;
import static tech.threekilogram.network.state.manager.NetStateValue.ONLY_WIFI_CONNECT;
import static tech.threekilogram.network.state.manager.NetStateValue.RECEIVER_UNREGISTER;
import static tech.threekilogram.network.state.manager.NetStateValue.WIFI_MOBILE_CONNECT;
import static tech.threekilogram.network.state.manager.NetStateValue.WIFI_MOBILE_DISCONNECT;

import android.support.annotation.IntDef;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: Liujin
 * @version: V1.0
 * @date: 2018-07-25
 * @time: 9:53
 */
@IntDef(value = {WIFI_MOBILE_DISCONNECT,
                 ONLY_WIFI_CONNECT,
                 ONLY_MOBILE_CONNECT,
                 WIFI_MOBILE_CONNECT,
                 RECEIVER_UNREGISTER})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
public @interface NetStateValue {

      final int RECEIVER_UNREGISTER    = -1;
      final int WIFI_MOBILE_DISCONNECT = 0;
      final int ONLY_MOBILE_CONNECT    = 1;
      final int ONLY_WIFI_CONNECT      = 2;
      final int WIFI_MOBILE_CONNECT    = 3;
}
