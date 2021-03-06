package ateam.foodr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {

    //The variable declarations for UI
    private EditText regEmail;
    private EditText regPassword;
    private Button regButton;
    private Switch regTog;
    private android.support.v7.widget.Toolbar toolbar;

    //Progress Dialog that will apper when something loads
    private ProgressDialog mRegProgress;

    //FireBase Authentication Linker
    private FirebaseAuth mAuth;

    //This is the link to the database
    private DatabaseReference mDatabase;

    //The user type that is going to be registered
    private String user_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Initialize the progress dialog
        mRegProgress = new ProgressDialog(this);

        //Get the user info from the UI
        regEmail =  (EditText) findViewById(R.id.reg_emailBox);
        regPassword = (EditText) findViewById(R.id.reg_passwordBox);
        regButton = (Button) findViewById(R.id.reg_regButton);
        regTog = (Switch) findViewById(R.id.reg_tog);
        toolbar =  findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        //This puts the arrow to go back to the view that we set in the AndroidManifest.xml file specificly for this view
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set the user type to be regular
        user_type = "normal";

        regTog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //In order to register as a admin user
                user_type = "admin";
            }
        });

        //This will initialize the authentication State
        mAuth = FirebaseAuth.getInstance();


        //When the registration button is clicked
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get the text from the TextInputLayouts
                final String email = regEmail.getText().toString();
                final String password = regPassword.getText().toString();

                //To check if any fields that is entered is empty
                if ( TextUtils.isEmpty(email) || TextUtils.isEmpty(password) )
                {
                    //Print the text to say that there is a error
                    Toast.makeText(RegistrationActivity.this, "Please fill all the required fields",Toast.LENGTH_LONG).show();
                }
                else {

                    //Registering User will be the title with this now
                    mRegProgress.setTitle("Registering User");

                    //This will be the info that will be told the user whats going on
                    mRegProgress.setMessage("Please wait while we create your account !");

                    //When user clicks on to the progress dialog we don't want him to cancel the progress
                    mRegProgress.setCanceledOnTouchOutside(false);

                    //This will show the progress dialog
                    mRegProgress.show();

                    //Finally register the user
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>()
                            {
                                @Override
                                public void onSuccess(AuthResult authResult) {

                                    /** From here the user info will be sent to the database **/
                                    //We need to get the id of the user to store the user private info on database accordingly.
                                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();

                                    //We get the current userID to store unique user info on database
                                    final String uid = current_user.getUid();

                                    //To store the info to the database like a tree this is what you do, that's why you have child code
                                    //This code only creates the content like the header, the real values are stored in the hashmap.
                                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                                    //In database the values are stored like <key, value> consequently you have to send the values like that
                                    //That is why we are using a hashmap
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    hashMap.put("email", email);
                                    hashMap.put("user_type", user_type);
                                    hashMap.put("image", "default");
                                    hashMap.put("password_hash", password);

                                    //Get the username

                                    int index = email.indexOf("@");
                                    String username =  email.substring(0,index);
                                    hashMap.put("user_name",username);

                                    //This is what really sends the values to the database
                                    mDatabase.setValue(hashMap)
                                            .addOnCompleteListener((task) ->
                                            {
                                                if (task.isSuccessful())
                                                {

                                                    //Initialize the user object
                                                    SimplerLoginActivity.user.setId(uid);
                                                    SimplerLoginActivity.user.setEmail(email);
                                                    SimplerLoginActivity.user.setPassword_hash(password);
                                                    SimplerLoginActivity.user.setUser_type(user_type);
                                                    SimplerLoginActivity.user.setUser_type(username);

                                                    //This will dismiss the progress dialog
                                                    mRegProgress.dismiss();

                                                    //if the user is an admin switch to the admin page
                                                    if (user_type.equals("admin"))
                                                    {
                                                        //Check if the current user is marked as a admin
                                                        Intent ownerPageIntent = new Intent(RegistrationActivity.this, OwnersResturantsActivity.class);

                                                        //This line of code makes sure that the user can't go back to the registration page using the phone back button
                                                        ownerPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                                        //Actually switches the UI
                                                        startActivity(ownerPageIntent);

                                                        finish();
                                                    }

                                                    if (user_type.equals("normal"))
                                                    {
                                                        //Check if the current user is marked as a admin
                                                        Intent ownerPageIntent = new Intent(RegistrationActivity.this, UserMapViewActivity.class);

                                                        //This line of code makes sure that the user can't go back to the registration page using the phone back button
                                                        ownerPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                                        //Actually switches the UI
                                                        startActivity(ownerPageIntent);

                                                        finish();
                                                    }
                                                }
                                                else
                                                {
                                                    //If there is a error than get rod of the progress dialog
                                                    mRegProgress.hide();
                                                    //Print the text to say that there is a error
                                                    Toast.makeText(RegistrationActivity.this, "There is a error",Toast.LENGTH_LONG).show();
                                                }
                                            })
                                            .addOnFailureListener((Exception e) ->
                                            {
                                                Utils.showToast(RegistrationActivity.this, e.getMessage());
                                                mRegProgress.dismiss();
                                            });


                                }
                            })

                            .addOnFailureListener((Exception e) -> Utils.showToast(RegistrationActivity.this, e.getMessage()));

                }
            }
        });
    }
}
