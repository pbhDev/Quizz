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

    private int currentQuestion = 1;
    private String _currentQuestion = Integer.toString(currentQuestion);
    private LinearLayout layout_questions;
    private LinearLayout mIntro;
    private LinearLayout previous_card_question;
    private LinearLayout current_card_question;
    private Button submit_button;
    private Button reset_button;
    private TextView counter;
    private int scoreThisQuestion = 0;
    private final String maxScorePerQuestion = "5";
    private String radioGroupAnswer;
    private String yourAnswer;

    private TextView viewScore;

    private int runningScore = 0;
    private String _runningScore = "0";

    private int answered = 0; //if a box is checked or unchecked, updates to +1 or -1 respectively.
    //Submit button displays a toast if this variable = 0 inviting the user to provide answer.

    private CharSequence text_toast = "";

    private String name; //player's name
    private Fade mFade; //same kind of animation for all questions
    private boolean isAnswerCorrect;
    private String correctAnswer;
    private String announcement;


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
        mIntro.setVisibility(View.GONE);
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
    private void moveToNext(Boolean isAnswerCorrect, String announcement,
                            String yourAnswer, String correctAnswer, int scoreForThisQuestion,
                            ViewGroup layout_questions, View current_card_question) {

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        TextView text = layout.findViewById(R.id.text);

        //prepare the message that will show in the toast
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

        //this tells where the toast will pop up-It will fill the screen.
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


    private void updateScoreTotal(int scoreThisQuestion) {

        //update score
        runningScore += scoreThisQuestion;

        _runningScore = Integer.toString(runningScore);

        //display score

        viewScore.setText(_runningScore);
    }

    public void submit(View view) {


        switch (currentQuestion) {

            case 1: {
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


                    isAnswerCorrect = radioGroupAnswer.equalsIgnoreCase("Caracas");
                    correctAnswer = "Caracas";

                    if (isAnswerCorrect) {

                        scoreThisQuestion += 5;
                        announcement = "You rock!";


                        answered = 0;

                    } else {

                        announcement = "Nice try!";
                    }

                    moveToNext(isAnswerCorrect,
                            announcement,
                            radioGroupAnswer, correctAnswer,
                            scoreThisQuestion, layout_questions, current_card_question);

                    //update score
                    updateScoreTotal(scoreThisQuestion);


                }
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


                correctAnswer = "Ottawa\nRome\n";

                yourAnswer = "";
                answered = 0;
                if (!sao_paolo.isChecked() && !barcelona.isChecked() &&
                        ottawa.isChecked() && rome.isChecked() ) {

                    answered += 6;

                    //gets the player's  answer to be displayed in the toast
                    yourAnswer += (rome.getText().toString() + "\n" + (ottawa.getText().toString() + "\n"));

                    announcement = "You rock!!!";
                    scoreThisQuestion = 5;
                    isAnswerCorrect = true;



                }else {

                    //each "if" statement gets the player's  answer to be displayed in the toast
                    if (sao_paolo.isChecked()) {
                        yourAnswer += (sao_paolo.getText().toString() + "\n");

                        answered = 1;

                    }

                    if (barcelona.isChecked()) {
                        yourAnswer += (barcelona.getText().toString() + "\n");
                        answered = 1;

                    }

                    if (ottawa.isChecked()) {
                        answered += 3;
                        yourAnswer += (ottawa.getText().toString() + "\n");


                    }

                    if (rome.isChecked()) {
                        answered += 3;
                        yourAnswer += (rome.getText().toString() + "\n");


                    }

                    if (answered == 3) {

                        announcement = "You got only one correct.";
                        scoreThisQuestion = 0;
                        isAnswerCorrect = false; //to triggers "show" correct answer

                    } else if ((answered >3 && answered !=6) || ( answered>=1 && answered <3)) {

                        announcement = "Nice try!";
                        scoreThisQuestion = 0;
                        isAnswerCorrect = false;
                    }
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


                    //update score
                    updateScoreTotal(scoreThisQuestion);


                }


                answered = 0;


                break;


            }

            case 3: {
                {
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

                    hideKeypad(); //does exactly what it says

                    scoreThisQuestion = 0;
                    yourAnswer = "";

                    correctAnswer = "Google it :-)";


                    counter.setText(_currentQuestion);

                    EditText editText_answer3 = findViewById(R.id.answer3_editText_cap_in_africa);
                    yourAnswer = editText_answer3.getText().toString();

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


                    isAnswerCorrect = capitals.contains(yourAnswer.toLowerCase());

                    if (yourAnswer.isEmpty()) {
                        answered = 0;
                    } else {

                        answered = 1;

                    }

                    if (isAnswerCorrect) {

                        scoreThisQuestion = 5;
                        announcement = "Good job!";

                        previous_card_question = findViewById(R.id.text_card_q3);
                        previous_card_question.setVisibility(View.GONE);

                        //find next question
                        current_card_question = findViewById(R.id.text_card_q4);

                        //update score
                        updateScoreTotal(scoreThisQuestion);

                        moveToNext(isAnswerCorrect,
                                announcement,
                                yourAnswer, correctAnswer,
                                scoreThisQuestion, layout_questions, current_card_question);

                        answered = 0;


                    } else {

                        // of course in this case isAnswerCorrect = false;
                        scoreThisQuestion = 0;

                        if (answered == 0) {

                            Toast.makeText(getApplicationContext(), "You didn't answer!",
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            announcement = "Mmm...wrong answer";

                            previous_card_question = findViewById(R.id.text_card_q3);
                            previous_card_question.setVisibility(View.GONE);

                            //find next question
                            current_card_question = findViewById(R.id.text_card_q4);

                            //update score
                            updateScoreTotal(scoreThisQuestion);

                            answered = 0;

                            moveToNext(isAnswerCorrect,
                                    announcement,
                                    yourAnswer, correctAnswer,
                                    scoreThisQuestion, layout_questions, current_card_question);


                        }


                    }


                }


                break;


            }

            case 4: {
                if (answered == 0) {

                    Toast.makeText(getApplicationContext(), "You didn't answer!",
                            Toast.LENGTH_SHORT).show();
                } else {


                    //Question 4: "Which one of these cities is a nation capital?"
                    //Answer 4: "Kathmandu"

                    scoreThisQuestion = 0;

                    String correctAnswer = "Kathmandu";

                    previous_card_question = findViewById(R.id.text_card_q4);
                    previous_card_question.setVisibility(View.GONE);
                    yourAnswer = "";

                    //find next question
                    current_card_question = findViewById(R.id.text_card_q5);


                    //set the editText to empty
                    EditText editText_answer3 = findViewById(R.id.answer3_editText_cap_in_africa);
                    editText_answer3.setText("");


                    counter.setText(_currentQuestion);


                    isAnswerCorrect = radioGroupAnswer.equalsIgnoreCase(correctAnswer);

                    if (isAnswerCorrect) {

                        announcement = "You rock!";

                        scoreThisQuestion += 5;


                        //update score
                        updateScoreTotal(scoreThisQuestion);

                        answered = 0;

                    } else {

                        announcement = "Nice try!";

                        //update score
                        updateScoreTotal(scoreThisQuestion);

                        answered = 0;


                    }

                    moveToNext(isAnswerCorrect,
                            announcement,
                            radioGroupAnswer, correctAnswer,
                            scoreThisQuestion, layout_questions, current_card_question);
                }


                break;


            }

            case 5: {
                if (answered == 0) {

                    Toast.makeText(getApplicationContext(), "You didn't answer!",
                            Toast.LENGTH_SHORT).show();
                } else {

                    //Question 5: "Damascus is the capital of Turkey"
                    //Answer 5: "True"

                    scoreThisQuestion = 0;

                    yourAnswer = "";

                    correctAnswer = "True";


                    previous_card_question = findViewById(R.id.text_card_q5);
                    previous_card_question.setVisibility(View.GONE);


                    //find next question
                    current_card_question = findViewById(R.id.text_card_q6);

                    //clear previous answer
                    RadioGroup rd_buttons4 = findViewById(R.id.rg_question4);
                    rd_buttons4.clearCheck();

                    //update the question counter
                    counter.setText(_currentQuestion);

                    //compare entry with known answer, return a boolean value
                    isAnswerCorrect = radioGroupAnswer.equalsIgnoreCase(correctAnswer);

                    if (isAnswerCorrect) {

                        announcement = "You rock!";

                        scoreThisQuestion += 5;

                        answered = 0;

                    } else {

                        announcement = "Nice try!";

                        answered = 0;

                    }

                    moveToNext(isAnswerCorrect,
                            announcement,
                            radioGroupAnswer, correctAnswer,
                            scoreThisQuestion, layout_questions, current_card_question);


                    //update score
                    updateScoreTotal(scoreThisQuestion);

                    answered = 0;


                }
                break;
            }

            case 6: {
                //Question 6: "Which 2 of these cities can you find in South America?"
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

                correctAnswer = "Lima\nGeorgetown\n";

                yourAnswer = "";

                if(!porto_novo.isChecked() && !lisbon.isChecked() &&
                        lima.isChecked() && georgetown.isChecked() ){
                    answered += 6;
                    yourAnswer += correctAnswer;

                    announcement = "You rock!!!";
                    scoreThisQuestion = 5;
                    isAnswerCorrect = true;

                }else {

                    //each "if" statement gets the player's  answer to be displayed in the toast
                    if (lima.isChecked()) {
                        answered = 3;
                        yourAnswer += (lima.getText().toString() + "\n");
                    }

                    if (georgetown.isChecked()) {
                        answered = 3;
                        yourAnswer += (georgetown.getText().toString() + "\n");
                    }

                    if (porto_novo.isChecked()) {
                        yourAnswer += (porto_novo.getText().toString() + "\n");
                        answered = 1;

                    }

                    if (lisbon.isChecked()) {
                        yourAnswer += (lisbon.getText().toString() + "\n");

                        answered = 1;

                    }

                    if (answered==3) {

                        announcement = "You got only correct";
                        scoreThisQuestion = 0;
                        isAnswerCorrect = false; //triggers to display the correct answer

                    }  else if((answered > 3 && answered !=6) || ( answered>=1 && answered <3)){

                        announcement = "...not really";
                        scoreThisQuestion = 0;
                        isAnswerCorrect = false;
                    }

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

        //clear answer in question 6
        CheckBox porto_novo = findViewById(R.id.answer6_porto_novo_in_sa);
        porto_novo.setChecked(false);
        CheckBox lisbon = findViewById(R.id.answer6_lisbon_in_sa);
        lisbon.setChecked(false);
        CheckBox lima = findViewById(R.id.answer6_lima_in_sa);
        lima.setChecked(false);
        CheckBox georgetown = findViewById(R.id.answer6_georgetown_in_sa);
        georgetown.setChecked(false);

        //Set current question to question 1
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



