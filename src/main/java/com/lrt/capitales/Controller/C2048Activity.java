package com.lrt.capitales.Controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.lrt.capitales.Model.Game2048;
import com.lrt.capitales.R;
import com.lrt.capitales.View.OnSwipeListener;
import static java.lang.Math.pow;

public class C2048Activity extends AppCompatActivity implements View.OnTouchListener {
    private static final String TAG = "C2048Activity";

    private Game2048 m_Game2048;

    private TextView m_2048btn[] = new TextView[16];

    private TextView m_txtScore, m_txtBestScore;
    private Button m_btnRestart;
    private LinearLayout m_2048blocsLayout;

    private GestureDetector m_gestureDetector;


    private int m_colorArray[] = new int[]{R.color.btn2048_1,R.color.btn2048_2,R.color.btn2048_3,
            R.color.btn2048_4,R.color.btn2048_5,R.color.btn2048_6,
            R.color.btn2048_7,R.color.btn2048_8,R.color.btn2048_9,
            R.color.btn2048_10,R.color.btn2048_11};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2048);

        m_Game2048 = new Game2048();


        m_txtScore = (TextView) findViewById(R.id.activity_2048_txtScore);
        m_txtBestScore = (TextView) findViewById(R.id.activity_2048_txtBestScore);

        int i = 0;
        m_2048btn[i++] = (TextView) findViewById(R.id.activity_2048_btn00);
        m_2048btn[i++] = (TextView) findViewById(R.id.activity_2048_btn10);
        m_2048btn[i++] = (TextView) findViewById(R.id.activity_2048_btn20);
        m_2048btn[i++] = (TextView) findViewById(R.id.activity_2048_btn30);
        m_2048btn[i++] = (TextView) findViewById(R.id.activity_2048_btn01);
        m_2048btn[i++] = (TextView) findViewById(R.id.activity_2048_btn11);
        m_2048btn[i++] = (TextView) findViewById(R.id.activity_2048_btn21);
        m_2048btn[i++] = (TextView) findViewById(R.id.activity_2048_btn31);
        m_2048btn[i++] = (TextView) findViewById(R.id.activity_2048_btn02);
        m_2048btn[i++] = (TextView) findViewById(R.id.activity_2048_btn12);
        m_2048btn[i++] = (TextView) findViewById(R.id.activity_2048_btn22);
        m_2048btn[i++] = (TextView) findViewById(R.id.activity_2048_btn32);
        m_2048btn[i++] = (TextView) findViewById(R.id.activity_2048_btn03);
        m_2048btn[i++] = (TextView) findViewById(R.id.activity_2048_btn13);
        m_2048btn[i++] = (TextView) findViewById(R.id.activity_2048_btn23);
        m_2048btn[i]   = (TextView) findViewById(R.id.activity_2048_btn33);


        m_btnRestart = (Button) findViewById(R.id.activity_2048_btnRestart);

        m_Game2048.creationBloc();
        _majAffichage();

        m_2048blocsLayout = (LinearLayout) findViewById(R.id.activity_2048_btnLayout);
        m_gestureDetector = new GestureDetector(this, new OnSwipeListener() {
            @Override
            public boolean onSwipe(Direction direction) {
                Log.d(TAG, "appel de Overriden on swipe");
                if (direction == Direction.up) {
                    m_Game2048.mvtHaut();
                }
                if (direction == Direction.down) {
                    m_Game2048.mvtBas();
                }
                if (direction == Direction.left) {
                    m_Game2048.mvtGauche();
                }
                if (direction == Direction.right) {
                    m_Game2048.mvtDroite();
                }
                _majAffichage();
                return true;
            }
        });
        m_2048blocsLayout.setOnTouchListener(this);

        // onClickListener restart
        m_btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_Game2048.restart();
                m_Game2048.creationBloc();
                _majAffichage();
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        m_gestureDetector.onTouchEvent(event);
        return true;
    }

    private void _majAffichage() {
        Log.d(TAG,"appel de majAffichage");
        // Plateau
        int w_plateau[] = m_Game2048.getM_plateau();
        for(int i=0; i<16; i++) {
            Log.d(TAG,"-- i = "+i+" -> "+w_plateau[i]);
            if(w_plateau[i]==0) {
                m_2048btn[i].setText("");
                m_2048btn[i].setBackgroundResource(R.color.btn2048_0);
            } else {
                m_2048btn[i].setText(""+ ((int) pow(2, w_plateau[i])));
                m_2048btn[i].setBackgroundResource(m_colorArray[w_plateau[i]%12]);

            }
        }

        // Score
        String w_strScore = "Score : \n\n"+m_Game2048.getM_score();
        String w_strBestScore = "Best score : \n\n"+m_Game2048.getM_bestScore();
        m_txtScore.setText(w_strScore);
        m_txtBestScore.setText(w_strBestScore);
    }
}
