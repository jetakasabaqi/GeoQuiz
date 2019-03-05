package jeta.android.geoquiz;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {


    private static final String EXTRA_ANSWER_IS_TRUE = "jeta.android.geoquiz.answer_is_true";

    private static final String EXTRA_ANSWER_SHOWN = "jeta.android.geoquiz.answer_shown";


    private boolean mAnswerIsTrue;

    private TextView mAnswerrTextView;
    private Button mShowAnswerButton;
private TextView apiLevelTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false);

        mAnswerrTextView = findViewById(R.id.answer_text_view);

        apiLevelTextView = findViewById(R.id.api_level_text_view);

        apiLevelTextView.setText("API Level "+Build.VERSION.SDK);

        mShowAnswerButton = findViewById(R.id.show_answer_button);

        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAnswerIsTrue){
                    mAnswerrTextView.setText(R.string.true_button);
                }else{
                    mAnswerrTextView.setText(R.string.false_button);
                }

                setAnswerShown(true);

                int cx = mShowAnswerButton.getWidth() / 2;
                int cy = mShowAnswerButton.getHeight() /2 ;
                float radius = mShowAnswerButton.getWidth();
                Animator anim = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    anim = ViewAnimationUtils.createCircularReveal(mShowAnswerButton,cx,cy,radius,0);
                    anim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {

                            mShowAnswerButton.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    anim.start();

                } else { mShowAnswerButton.setVisibility(View.INVISIBLE); }


            }
        });
    }

    private void setAnswerShown( boolean isAnswerShown ){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN,isAnswerShown);
        setResult(RESULT_OK,data);
    }
}
