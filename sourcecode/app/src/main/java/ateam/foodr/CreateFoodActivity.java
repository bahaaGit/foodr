package ateam.foodr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateFoodActivity extends AppCompatActivity
{
    @BindView(R.id.nameTextbox)  EditText nameTextbox;
    @BindView(R.id.priceTextbox) EditText priceTextbox;
    @BindView(R.id.descTextbox)  EditText descTextbox;

    @BindView(R.id.photoButton)  Button photoButton;
    @BindView(R.id.createButton) Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_food);
        ButterKnife.bind(this);
    }

    public void onCreateButtonClick(View v)
    {
        // Take all the information from the form and put it in an object
        Food f = new Food
        (
                -1,     // TODO: Fill this in
                nameTextbox.getText().toString(),
                priceTextbox.getText().toString(),     // TODO: Make this an int
                descTextbox.getText().toString()
        );

        // TODO: Send that object to firebase
    }
}
