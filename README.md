# 监听网络变化库

## 引入

**Step 1.** Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

**Step 2.** Add the dependency

```
	dependencies {
	        implementation 'com.github.threekilogram:NetWorkStateChangeListener:1.2.2'
	}
```

## NetWorkStateChangeManager

1. 注册一个监听网络变化的BroadCastReceiver

```
<receiver android:name="tech.threekilogram.network.state.manager.NetStateReceiver">
</receiver>
```

```
// 推荐app创建时注册
NetWorkStateChangeManager.registerReceiver(context);
```

```
// 不要忘记app退出时解注册
NetWorkStateChangeManager.unRegisterReceiver(context);
```

2. 在需要响应网络变化的类中实现 OnNetWorkStateChangedListener 并添加给NetWorkStateChangeManager

```
MainActivity extends AppCompatActivity implements OnNetWorkStateChangedListener
```

```
@Override
public void onNetWorkStateChanged (int state) {
      Log.e(TAG, "onNetWorkStateChanged : " + state);
      
      // 当网络连接状态改变了做出响应动作
}
```

3. 或者只在需要的时候获取连接状态

```
NetWorkStateChangeManager instance = NetWorkStateChangeManager.getInstance();
int currentNetState = instance.getCurrentNetState();

//状态值为一下其一
int RECEIVER_UNREGISTER    = -1; --> 没有注测 receiver
int WIFI_MOBILE_DISCONNECT = 0; --> 没有连接
int ONLY_MOBILE_CONNECT    = 1; --> 只有手机数据连接
int ONLY_WIFI_CONNECT      = 2; --> 只有wifi连接
int WIFI_MOBILE_CONNECT    = 3; --> wifi 和 手机 全部处于连接状态
```



## NetWorkStateUtils

用于主动测试是否连接

```
// 测试是否有网络连接
boolean networkConnected = NetWorkStateUtils.isNetworkConnected(this);
Log.e(TAG, "isConnect : network " + networkConnected);

// 测试wifi是否连接
boolean wifiConnected = NetWorkStateUtils.isWifiConnected(this);
Log.e(TAG, "isConnect : wifi " + wifiConnected);

// 测试手机是否连接
boolean mobileConnected = NetWorkStateUtils.isMobileConnected(this);
Log.e(TAG, "isConnect : mobile " + mobileConnected);
```