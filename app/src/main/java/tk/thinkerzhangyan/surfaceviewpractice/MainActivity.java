package tk.thinkerzhangyan.surfaceviewpractice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
*@author zhangyan
*@date 2017/10/26 
*/
public class MainActivity extends AppCompatActivity {

    private SimpleDraw mSimpleDraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSimpleDraw = (SimpleDraw) findViewById(R.id.simple_draw);

        Button bt_clear_screen = (Button) findViewById(R.id.bt_clear_screen);
        bt_clear_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSimpleDraw.setClearScreen(true);
            }
        });
    }
}
