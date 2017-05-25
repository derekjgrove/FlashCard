package edu.iup.flashcard.quiz;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import edu.iup.flashcard.R;

/**
 * Final activity to provide user feedback on performance.
 *
 * @since 2014
 * @author Ryan C. Gray & Team
 * @since 2015
 * @author Derek J. Grove & Team
 *
 */
public class ReportCardActivity extends Activity {
    //member variables for singletons
    private UserAnswer[] userAnswers;
    private LinkedList<Question> questions;
    private int incorrectCount;
    private int correctCount;
    private int questionCount;
    private int score;
    private char grade;
    private TextView answerSummaryText;
    private Button returnButton;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportcard);
        userAnswers = UserAnswerSingleton.getInstance().getUserAnswers(); //retrieve array of UserAnswers
        questions = QuestionSingleton.getInstance().getQuestions();		  //retrieve list of Questions
        String [] questionSet = new String[50];

        answerSummaryText = (TextView) findViewById(R.id.answerSummary);

        //count questions and number correct
        for(int i = 0; i < userAnswers.length; i++){
            if(userAnswers[i].getAnswerNumber()==questions.get(i).getcAnswer()){
                correctCount++;
                questionSet[i] = "Correct";
            }
            if(userAnswers[i].getAnswerNumber()!=questions.get(i).getcAnswer()){
                incorrectCount++;
                questionSet[i] = "Wrong";
            }
            questionCount++;
        }

        //calculate and output score
        score = 100 * correctCount / questionCount;
        if(score > 90){
            grade = 'A';
        }
        if(score > 80){
            grade = 'B';
        }
        if(score > 70){
            grade = 'C';
        }
        if(score > 60){
            grade = 'D';
        }
        else{
            grade = 'F';
        }

        String text = "";

        if (score > 60) {
            text = "<font color=#ffffff>You obtained the grade </font> <font color=#45fc7d>" + grade + "</font> <font color=#ffffff> in this lesson</font><br>";
            text += "<font color=#ffffff>Score: </font> <font color=#45fc7d>" + score + "</font> <font color=#ffffff>%</font><br>";
        } else {
            text = "<font color=#ffffff>You obtained the grade </font> <font color=#f45555>" + grade + "</font> <font color=#ffffff> in this lesson</font><br>";
            text += "<font color=#ffffff>Score: </font> <font color=#f45555>" + score + "</font> <font color=#ffffff>%</font><br>";
        }

        text += "<font color=#ffffff>Correct Answers: </font> <font color=#45fc7d>" + correctCount + "</font><br>";
        text += "<font color=#ffffff>Incorrect Answers: </font> <font color=#f45555>" + incorrectCount + "</font><br><br><br>";

        for (int i = 0; i < questionCount; i++) {
            if (questionSet[i] == "Correct") {
                text += "<font color=#ffffff>Question #" + i + "</font> <font color=#45fc7d>" + questionSet[i] + "</font><br>";
            } else {
                text += "<font color=#ffffff>Question #" + i + "</font> <font color=#f45555>" + questionSet[i] + "</font><br>";
            }
        }
        Spanned spannedCode = Html.fromHtml(text.toString());
        answerSummaryText.setText(spannedCode);
        answerSummaryText.setTextSize(14);
        answerSummaryText.setMovementMethod(new ScrollingMovementMethod());

        //offers a button to go back to lesson menu, clearing the userAnswers array and questions list
        returnButton = (Button) findViewById(R.id.reportCardReturnButton);
        returnButton.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                for(int i = 0; i<userAnswers.length; i++){
                    userAnswers[i] = null;
                }
                questions.clear();
                Intent intent = new Intent(ReportCardActivity.this, ChooseLessonActivity.class);
                startActivity(intent);

            }

        });

    }
}
