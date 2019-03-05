package jeta.android.geoquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {


    private static final String EXTRA_ANSWER_IS_TRUE = "jeta.android.geoquiz.answer_is_true";

    private static final String EXTRA_ANSWER_SHOWN = "jeta.android.geoquiz.answer_shown";


    private boolean mAnswerIsTrue;

    private TextView mAnswerrTextView;
    private Button mShowAnswerButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false);

        mAnswerrTextView = findViewById(R.id.answer_text_view);

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
            }
        });
    }

    private void setAnswerShown( boolean isAnswerShown ){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN,isAnswerShown);
        setResult(RESULT_OK,data);
    }
}
