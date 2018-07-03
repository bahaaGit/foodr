package ateam.foodr;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class AddFoodToMenuActivity extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 1;

    private Button chooseImageBtn;
    private Button summitButton;
    private EditText enterFileName;
    private ImageView choosenImageView;
    private ProgressBar progressBar;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_to_menu);


        chooseImageBtn = findViewById(R.id.idAddFoodchooseImageBtn);
        summitButton = findViewById(R.id.idAddFoodSummitBtn);
        enterFileName = findViewById(R.id.idAddFoodenterFileName);
        choosenImageView = findViewById(R.id.idAddFoodcChoosenImageView);
        progressBar = findViewById(R.id.idAddFoodcProgressBar);

        chooseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        summitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        /*mTextViewShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();

            Picasso.with(this).load(imageUri).into(choosenImageView);
        }
    }
}

