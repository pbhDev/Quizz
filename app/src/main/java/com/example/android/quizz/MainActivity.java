package com.example.android.quizz;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    int currentQuestion = 1;
    String _currentQuestion = Integer.toString(currentQuestion);
    LinearLayout layout_questions;
    RelativeLayout mIntro;
    LinearLayout previous_card_question;
    LinearLayout current_card_question;
    Button submit_button;
    Button reset_button;
    TextView counter;
    int scoreThisQuestion = 0;
    String maxScorePerQuestion = "5";
    String radioGroupAnswer;
    String yourAnswer = "";

    TextView viewScore;

    int runningScore = 0;
    String _runningScore = "0";

    int answered = 0; //if a box is checked or unchecked, updates to +1 or -1 respectively.
    //Submit button displays a toast if this variable = 0 inviting the user to provide answer.

    CharSequence text_toast = "";

    String name; //player's name
    Fade mFade; //same kind of animation for all questions


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this view will show the question count.
        counter = findViewById(R.id.counter);

        //this layout will be present most of the time
        layout_questions = findViewById(R.id.layout_questions);

        viewScore = findViewById(R.id.score);

        submit_button = findViewById(R.id.button_submit);
        reset_button = findViewById(R.id.button_reset);

        answered = 0;


    }


    private void hideKeypad() {
        EditText editView = findViewById(R.id.name_field);

        InputMethodManager myInputM =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert myInputM != null;//to prevent the null pointer exception
        myInputM.hideSoftInputFromWindow(editView.getWindowToken(), 0);
    }

    public void startQuiz(View view) {

        mIntro = findViewById(R.id.layout_intro);
        mIntro.setVisibility(View.INVISIBLE);
        layout_questions = findViewById(R.id.layout_questions);

        EditText name_player = findViewById(R.id.name_field);
        name = name_player.getText().toString();
        TextView tvName_player = findViewById(R.id.name_display);
        tvName_player.setText(name);

        layout_questions.setVisibility(View.VISIBLE);
        submit_button = findViewById(R.id.button_submit);
        submit_button.setVisibility(View.VISIBLE);

        hideKeypad();

        currentQuestion = 1;

    }


    //Prepares and displays the toast, also updates question count
    public void moveToNext(Boolean isAnswerCorrect, String announcement,
                           String yourAnswer, String correctAnswer, int scoreForThisQuestion,
                           ViewGroup layout_questions, View current_card_question) {

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        TextView text = layout.findViewById(R.id.text);

        //prepare the message that will in the toast
        if (isAnswerCorrect) {
            text_toast = "Question " + currentQuestion + " Score: "
                    + Integer.toString(scoreForThisQuestion) + "/" + maxScorePerQuestion + "\n"
                    + announcement;

        } else {

            text_toast = "Question " + currentQuestion +
                    " Score: " + Integer.toString(scoreForThisQuestion) + "/" + maxScorePerQuestion
                    + "\n\n" + announcement + "\n\n" + "Your answer\n" + yourAnswer + "\n\n"
                    + "Correct Answer\n" + correctAnswer;
        }

        //load the message
        text.setText(text_toast);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);

        //this tells where the toast will pop up
        toast.setGravity(Gravity.FILL, 0, 0);

        toast.setView(layout);
        toast.show();
        toast.show();


        //update question count value.
        currentQuestion++;


        //Listen to any change inside ViewGroup layout_questions
        TransitionManager.beginDelayedTransition(layout_questions, mFade);

        if (currentQuestion <= 6) {
            _currentQuestion = Integer.toString(currentQuestion);
            counter.setText(_currentQuestion);

            //make next question visible. TransitionManager will notice the change
            //and apply the animation
            current_card_question.setVisibility(View.VISIBLE);

        }


    }


    public void updateScoreTotal(int scoreThisQuestion) {

        //update score
        runningScore += scoreThisQuestion;

        _runningScore = Integer.toString(runningScore);

        //display score

        viewScore.setText(_runningScore);
    }

    public void submit(View view) {


        switch (currentQuestion) {

            case 1:
                if (answered == 0) {

                    Toast.makeText(getApplicationContext(), "You didn't answer!",
                            Toast.LENGTH_SHORT).show();
                } else {

                    //Question : What's the capital of venezuela
                    //Answer : Caracas
                    scoreThisQuestion = 0;


                    //the current card becomes previous then it's set to visibility GONE.
                    previous_card_question = findViewById(R.id.text_card_q1);
                    previous_card_question.setVisibility(View.GONE);


                    //show next question
                    current_card_question = findViewById(R.id.text_card_q2);


                    boolean isAnswerCorrect = radioGroupAnswer.equalsIgnoreCase("Caracas");
                    String correctAnswer = "Caracas";

                    if (isAnswerCorrect) {

                        scoreThisQuestion += 5;
                        moveToNext(true,
                                "You rock!",
                                radioGroupAnswer, correctAnswer,
                                scoreThisQuestion, layout_questions, current_card_question);

                    } else {

                        moveToNext(false,
                                "Nice try!",
                                radioGroupAnswer, correctAnswer, scoreThisQuestion,
                                layout_questions, current_card_question);


                    }

                    //update score
                    updateScoreTotal(scoreThisQuestion);
                    answered = 0;


                    break;


                }

            case 2: {
                //Question 2 : Select all nation capitals
                //Answer 2 : Lima, Georgetown

                scoreThisQuestion = 0;


                //Clear the answer to the previous. That will for the reset later
                RadioGroup rd_buttons = findViewById(R.id.rg_question1);
                rd_buttons.clearCheck();


                //sao paolo, barcelona, ottawa, rome
                CheckBox sao_paolo = findViewById(R.id.answer2_sao_paolo);
                CheckBox barcelona = findViewById(R.id.answer2_barcelona);
                CheckBox ottawa = findViewById(R.id.answer2_ottawa);
                CheckBox rome = findViewById(R.id.answer2_rome);

                String announcement = "";
                String correctAnswer = "Ottawa\nRome\n";

                yourAnswer = "";

                if (sao_paolo.isChecked()) {
                    yourAnswer += (sao_paolo.getText().toString() + "\n");

                    answered = 1;

                } else {
                    if (answered > 0)
                        answered -= 1;
                }

                if (barcelona.isChecked()) {
                    yourAnswer += (barcelona.getText().toString() + "\n");
                    answered = 1;

                } else {
                    if (answered > 0)
                        answered -= 1;
                }

                if (ottawa.isChecked()) {
                    answered++;
                    yourAnswer += (ottawa.getText().toString() + "\n");

                    answered = 1;

                } else {
                    if (answered > 0)
                        answered -= 1;
                }

                if (rome.isChecked()) {
                    answered++;
                    yourAnswer += (rome.getText().toString() + "\n");


                } else {
                    if (answered > 0)
                        answered -= 1;
                }

                boolean isAnswerCorrect = false;

                if (answered == 1) {

                    announcement = "Good job!";
                    scoreThisQuestion = 2;
                    isAnswerCorrect = false; //to triggers "show" correct answer

                } else if (answered == 2) {

                    announcement = "You rock!!!";
                    scoreThisQuestion = 5;
                    isAnswerCorrect = true;
                } else if (answered == 0) {

                    announcement = "That was tricky";
                    scoreThisQuestion = 5;
                    isAnswerCorrect = false;
                }

                if (yourAnswer.equals("")) {

                    Toast.makeText(getApplicationContext(), "You didn't answer!",
                            Toast.LENGTH_SHORT).show();
                } else {

                    previous_card_question = findViewById(R.id.text_card_q2);
                    previous_card_question.setVisibility(View.GONE);

                    //find next question
                    current_card_question = findViewById(R.id.text_card_q3);


                    counter.setText(_currentQuestion);

                    moveToNext(isAnswerCorrect,
                            announcement,
                            yourAnswer, correctAnswer,
                            scoreThisQuestion, layout_questions, current_card_question);

                }

                //update score
                updateScoreTotal(scoreThisQuestion);

                answered = 0;


                break;


            }

            case 3: {
                //Question : name_a_capital_in_africa
                //Answer : pick one from the list below


                //reset checkboxes or radio buttons
                CheckBox sao_paolo = findViewById(R.id.answer2_sao_paolo);
                sao_paolo.setChecked(false);
                CheckBox barcelona = findViewById(R.id.answer2_barcelona);
                barcelona.setChecked(false);
                CheckBox ottawa = findViewById(R.id.answer2_ottawa);
                ottawa.setChecked(false);
                CheckBox rome = findViewById(R.id.answer2_rome);
                rome.setChecked(false);

                hideKeypad();

                scoreThisQuestion = 0;
                yourAnswer = "";

                String correctAnswer = "Google it :-)";


                counter.setText(_currentQuestion);

                EditText editText_answer3 = findViewById(R.id.answer3_editText_cap_in_africa);
                yourAnswer += editText_answer3.getText().toString();

                List<String> capitals = Arrays.asList("algiers", "luanda", "porto-novo", "gaborone",
                        "ouagadougou", "bujumbura", "praia", "yaounde", "bangui", "n'djamena",
                        "moroni", "kinshasa", "brazzaville", "yamoussoukro", "djibouti", "cairo",
                        "malabo", "asmara", "addis ababa", "libreville", "banjul", "accra",
                        "conakry", "bissau", "nairobi", "maseru", "monrovia", "tripoli",
                        "antananarivo", "lilongwe", "bamako", "nouakchott", "port louis", "rabat",
                        "maputo", "windhoek", "niamey", "abuja", "kigali", "sao tome", "dakar",
                        "victoria", "freetown", "mogadishu", "pretoria", "juba", "khartoum",
                        "mbabane", "dodoma", "lome", "tunis", "kampala", "lusaka", "harare",
                        "porto novo", "ndjamena");


                boolean isAnswerCorrect = false;

                if (yourAnswer.equals("")) {
                    answered = 0;
                } else {
                    isAnswerCorrect = capitals.contains(yourAnswer.toLowerCase());
                    answered = 1;

                }

                if (isAnswerCorrect) {

                    scoreThisQuestion = 5;

                    moveToNext(true,
                            "Good job!",
                            yourAnswer, correctAnswer,
                            scoreThisQuestion, layout_questions, current_card_question);


                } else {

                    // of course in this case isAnswerCorrect = false;
                    scoreThisQuestion = 0;

                    if (answered == 0) {

                        Toast.makeText(getApplicationContext(), "You didn't answer!",
                                Toast.LENGTH_SHORT).show();
                    } else {

                        previous_card_question = findViewById(R.id.text_card_q3);
                        previous_card_question.setVisibility(View.GONE);

                        //find next question
                        current_card_question = findViewById(R.id.text_card_q4);

                        moveToNext(false,
                                "Mmm...wrong answer",
                                yourAnswer, correctAnswer,
                                scoreThisQuestion, layout_questions, current_card_question);

                    }


                }


                //update score
                updateScoreTotal(scoreThisQuestion);

                answered = 0;

                break;


            }

            case 4:
                if (answered == 0) {

                    Toast.makeText(getApplicationContext(), "You didn't answer!",
                            Toast.LENGTH_SHORT).show();
                } else {


                    //Question 4: "Which one of these cities is a nation capital?"
                    //Answer 4: "Kathmandu"

                    scoreThisQuestion = 0;
                    yourAnswer = "";
                    boolean isAnswerCorrect;
                    String correctAnswer = "Kathmandu";

                    previous_card_question = findViewById(R.id.text_card_q4);
                    previous_card_question.setVisibility(View.GONE);

                    //find next question
                    current_card_question = findViewById(R.id.text_card_q5);


                    //set the editText to empty
                    EditText editText_answer3 = findViewById(R.id.answer3_editText_cap_in_africa);
                    editText_answer3.setText("");


                    counter.setText(_currentQuestion);


                    isAnswerCorrect = radioGroupAnswer.equalsIgnoreCase(correctAnswer);

                    if (isAnswerCorrect) {

                        scoreThisQuestion += 5;
                        moveToNext(true,
                                "You rock!",
                                radioGroupAnswer, correctAnswer,
                                scoreThisQuestion, layout_questions, current_card_question);

                    } else {


                        moveToNext(false,
                                "Nice try!",
                                radioGroupAnswer, correctAnswer, scoreThisQuestion,
                                layout_questions, current_card_question);


                    }

                    //update score
                    updateScoreTotal(scoreThisQuestion);

                    answered = 0;


                    break;


                }

            case 5:
                if (answered == 0) {

                    Toast.makeText(getApplicationContext(), "You didn't answer!",
                            Toast.LENGTH_SHORT).show();
                } else {

                    //Question 5: "Damascus is the capital of Turkey"
                    //Answer 5: "True"

                    scoreThisQuestion = 0;

                    yourAnswer = "";
                    boolean isAnswerCorrect;
                    String correctAnswer = "True";


                    previous_card_question = findViewById(R.id.text_card_q5);
                    previous_card_question.setVisibility(View.GONE);


                    //find next question
                    current_card_question = findViewById(R.id.text_card_q6);


                    RadioGroup rd_buttons4 = findViewById(R.id.rg_question4);
                    rd_buttons4.clearCheck();


                    counter.setText(_currentQuestion);

                    isAnswerCorrect = radioGroupAnswer.equalsIgnoreCase(correctAnswer);

                    if (isAnswerCorrect) {

                        scoreThisQuestion += 5;
                        moveToNext(true,
                                "You rock!",
                                radioGroupAnswer, correctAnswer,
                                scoreThisQuestion, layout_questions, current_card_question);

                    } else {


                        moveToNext(false,
                                "Nice try!",
                                radioGroupAnswer, correctAnswer, scoreThisQuestion,
                                layout_questions, current_card_question);


                    }


                    //update score
                    updateScoreTotal(scoreThisQuestion);

                    answered = 0;


                    break;


                }

            case 6: {
                //Question 6: "Which of these cities can you find in South America?"
                //Answer 6: "Lima", "Georgetown"

                scoreThisQuestion = 0;


                RadioButton rd_buttons5a = findViewById(R.id.answer5_false_damascus);
                rd_buttons5a.setChecked(false);
                RadioButton rd_buttons5b = findViewById(R.id.answer5_true_damascus);
                rd_buttons5b.setChecked(false);

                //sao paolo, barcelona, ottawa, rome
                CheckBox porto_novo = findViewById(R.id.answer6_porto_novo_in_sa);
                CheckBox lisbon = findViewById(R.id.answer6_lisbon_in_sa);
                CheckBox lima = findViewById(R.id.answer6_lima_in_sa);
                CheckBox georgetown = findViewById(R.id.answer6_georgetown_in_sa);

                String announcement = "";
                String correctAnswer = "Lima\nGeorgetown\n";

                yourAnswer = "";

                if (porto_novo.isChecked()) {
                    yourAnswer += (porto_novo.getText().toString() + "\n");
                    answered = 1;

                } else {

                    answered = 0;
                }

                if (lisbon.isChecked()) {
                    yourAnswer += (lisbon.getText().toString() + "\n");

                    answered = 1;

                } else {

                    answered = 0;
                }

                if (lima.isChecked()) {
                    answered++;
                    yourAnswer += (lima.getText().toString() + "\n");


                } else {

                    answered = 0;
                }

                if (georgetown.isChecked()) {
                    answered++;
                    yourAnswer += (georgetown.getText().toString() + "\n");


                } else {

                    answered = 0;
                }

                boolean isAnswerCorrect = false;

                if (answered == 1) {

                    announcement = "You got one correct!";
                    scoreThisQuestion = 2;
                    isAnswerCorrect = false; //triggers to display the correct answer

                } else if (answered == 2) {

                    announcement = "You rock!!!";
                    scoreThisQuestion = 5;
                    isAnswerCorrect = true;
                } else if (answered == 0) {

                    announcement = "...not really";
                    scoreThisQuestion = 5;
                    isAnswerCorrect = false;
                }

                if (yourAnswer.equals("")) {

                    Toast.makeText(getApplicationContext(), "You didn't answer!",
                            Toast.LENGTH_SHORT).show();
                } else {

                    moveToNext(isAnswerCorrect,
                            announcement,
                            yourAnswer, correctAnswer,
                            scoreThisQuestion, layout_questions, current_card_question);

                    //update score
                    updateScoreTotal(scoreThisQuestion);

                    //submit_button =  findViewById(R.id.button_submit);
                    submit_button.setVisibility(View.GONE);
                    //reset_button = findViewById(R.id.button_reset);
                    reset_button.setVisibility(View.VISIBLE);

                }


                break;

            }

            default:

        }


    }

    public void onRb1Clicked(View view) {

        answered = 1;

        RadioButton myRbutton1;

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.q1_buenos_aires:
                if (checked)

                    answered++;
                myRbutton1 = findViewById(R.id.q1_buenos_aires);

                radioGroupAnswer = myRbutton1.getText().toString();

                break;
            case R.id.q1_santa_maria:
                if (checked)

                    answered++;
                myRbutton1 = findViewById(R.id.q1_santa_maria);

                radioGroupAnswer = myRbutton1.getText().toString();

                break;


            case R.id.q1_caracas:
                if (checked)

                    answered++;
                myRbutton1 = findViewById(R.id.q1_caracas);

                radioGroupAnswer = myRbutton1.getText().toString();

                break;

            default:
        }


    }


    public void onRb4Clicked(View view) {

        answered = 1;

        RadioButton myRbutton4;

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.answer4_kathmandu:
                if (checked)

                    answered++;
                myRbutton4 = findViewById(R.id.answer4_kathmandu);

                radioGroupAnswer = myRbutton4.getText().toString();

                break;
            case R.id.answer4_kyoto:
                if (checked)

                    answered++;
                myRbutton4 = findViewById(R.id.answer4_kyoto);

                radioGroupAnswer = myRbutton4.getText().toString();

                break;


            case R.id.answer4_Amara:
                if (checked)

                    answered++;
                myRbutton4 = findViewById(R.id.answer4_Amara);

                radioGroupAnswer = myRbutton4.getText().toString();

                break;

            default:
        }

    }


    public void onRb5Clicked(View view) {

        answered = 1;
        RadioButton myRbutton5 = findViewById(R.id.answer5_true_damascus);

        // Is the button  checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.answer5_true_damascus:
                if (checked)


                    myRbutton5 = findViewById(R.id.answer5_true_damascus);

                //get the answer on the checked button
                radioGroupAnswer = myRbutton5.getText().toString();

                break;
            case R.id.answer5_false_damascus:
                if (checked)


                    myRbutton5 = findViewById(R.id.answer5_false_damascus);

                radioGroupAnswer = myRbutton5.getText().toString();

                break;

            default:


        }

    }


    public void reset(View view) {

        answered = 0;
        scoreThisQuestion = 0;
        currentQuestion = 1; //question counter


        //update score
        runningScore += scoreThisQuestion;

        _runningScore = Integer.toString(scoreThisQuestion);

        //display score
        viewScore = findViewById(R.id.score);
        viewScore.setText(_runningScore);

        //set current card_question to GONE
        previous_card_question = findViewById(R.id.text_card_q6);
        previous_card_question.setVisibility(View.GONE);

        //
        CheckBox porto_novo = findViewById(R.id.answer6_porto_novo_in_sa);
        porto_novo.setChecked(false);
        CheckBox lisbon = findViewById(R.id.answer6_lisbon_in_sa);
        lisbon.setChecked(false);
        CheckBox lima = findViewById(R.id.answer6_lima_in_sa);
        lima.setChecked(false);
        CheckBox georgetown = findViewById(R.id.answer6_georgetown_in_sa);
        georgetown.setChecked(false);

        current_card_question = findViewById(R.id.text_card_q1);
        current_card_question.setVisibility(View.VISIBLE);

        String _currentQuestion = Integer.toString(currentQuestion);

        counter = findViewById(R.id.counter);
        counter.setText(_currentQuestion);

        //layout_questions = findViewById(R.id.layout_questions);
        layout_questions.setVisibility(View.VISIBLE);

        //reset total score
        runningScore = 0;


        //display  score on board
        //viewScore = findViewById(R.id.score);
        viewScore.setText(_runningScore);


        //reset_button = findViewById(R.id.button_reset);
        reset_button.setVisibility(View.GONE);
        //submit_button =  findViewById(R.id.button_submit);
        submit_button.setVisibility(View.VISIBLE);

    }


}



