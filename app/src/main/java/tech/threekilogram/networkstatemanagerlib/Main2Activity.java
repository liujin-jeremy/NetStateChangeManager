package tech.threekilogram.networkstatemanagerlib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import tech.threekilogram.network.state.manager.NetStateChangeManager;
import tech.threekilogram.network.state.manager.OnNetStateChangedListener;

/**
 * @author liujin
 */
public class Main2Activity extends AppCompatActivity implements OnNetStateChangedListener {

      public static void start (Context context) {

            Intent starter = new Intent(context, Main2Activity.class);
            context.startActivity(starter);
      }

      @Override
      protected void onCreate (Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main2);
      }

      @Override
      protected void onResume () {

            super.onResume();
            NetStateChangeManager.getInstance().addListener(this);
      }

      @Override
      protected void onPause () {

            super.onPause();
            NetStateChangeManager.getInstance().removeListener(this);
      }

      public void unRegister (View view) {

            NetStateChangeManager.unRegisterReceiver(this);
      }

      @Override
      public void onNetWorkStateChanged (int state) {

            Toast.makeText(this, "当前网络状态" + state, Toast.LENGTH_SHORT).show();
      }
}
