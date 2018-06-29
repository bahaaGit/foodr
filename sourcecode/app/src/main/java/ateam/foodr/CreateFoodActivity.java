package ateam.foodr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

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
}
