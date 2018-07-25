package tech.threekilogram.networkstatemanagerlib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import tech.threekilogram.network.state.manager.NetStateChangeManager;

/**
 * @author liujin
 */
public class Main2Activity extends AppCompatActivity {

      public static void start (Context context) {

            Intent starter = new Intent(context, Main2Activity.class);
            context.startActivity(starter);
      }

      @Override
      protected void onCreate (Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main2);
      }

      public void unRegister (View view) {

            NetStateChangeManager.unRegisterReceiver(this);
      }
}
