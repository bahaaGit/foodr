package ateam.foodr;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.content.Intent;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SimplerLoginActivity extends AppCompatActivity
{

    //Define the UI components
    private Button switchRegBtn;
    private EditText lUserEmail;
    private EditText lUserPassword;
    private Switch lAdmnBtn;

    //The User Object
    public static User user;

    //FireBase Authentication Linker
    private FirebaseAuth mAuth;

    //This is the link to the database
    private DatabaseReference mDatabase;
    private DatabaseReference uDatabase;

    //Progress Dialog that will apper when something loads
    private ProgressDialog mLoginProgress;

    //The variable to check if there is a error
    public int error;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simpler_login);

        //Set the error to be 0
        error = 0;

        //Request Permissions
        PermissionsUtils.checkAndRequestPermissions(SimplerLoginActivity.this);

        //Initialize the registration button for the login page
        switchRegBtn = (Button) findViewById(R.id.log_regPgBtn);
        lUserEmail = (EditText) findViewById(R.id.login_email);
        lUserPassword = (EditText) findViewById(R.id.login_password);
        lAdmnBtn = (Switch) findViewById(R.id.login_ownerToggle);

        //Initialize the progress dialog
        mLoginProgress = new ProgressDialog(this);

        //Initialize the first state of the user object (without identification of user)
        user = new User();
        userInit();

        //This will initialize the authentication State

        mAuth = FirebaseAuth.getInstance();

        //Check if a user is logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //mAuth.signOut();
        //When the user wants to switch to the registration view
        switchRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Switch to the registration Intent
                Intent intent = new Intent(SimplerLoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        //If a user exists
        if (currentUser != null)
        {

            String uid = currentUser.getUid();

            uDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

            uDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (!dataSnapshot.hasChild("user_type"))
                        return;

                    String status = dataSnapshot.child("user_type").getValue().toString();
                    userInit();
                    user.setUser_type(status);
                    if (status.equals("admin"))
                    {
                        Intent intent = new Intent(SimplerLoginActivity.this,OwnersResturantsActivity.class);

                        //This line of code makes sure that the user can't go back to the registration page using the phone back button
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        //Actually switches the UI
                        startActivity(intent);

                        finish();
                    }

                    if (status.equals("normal"))
                    {
                        Intent intent = new Intent(SimplerLoginActivity.this,UserMapViewActivity.class);

                        //This line of code makes sure that the user can't go back to the registration page using the phone back button
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        //Actually switches the UI
                        startActivity(intent);

                        finish();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }

    public void onLoginClick(View view)
    {

        //Get the parameters for sign in
        String email = lUserEmail.getText().toString();
        String password = lUserPassword.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
        {
            //Print the text to say that there is a error
            Toast.makeText(SimplerLoginActivity.this, "Please fill all the required fields",Toast.LENGTH_LONG).show();
        }
        else {


            //Loging the User will be the title with this now
            mLoginProgress.setTitle("Logging in User");

            //This will be the info that will be told the user whats going on
            mLoginProgress.setMessage("Please wait while we check your credentials !");

            //This will show the progress dialog
            mLoginProgress.show();

            loginUser(email, password);

        }

        // TODO: Do something different if the user isn't
        // marked as an owner.
    }

    private void loginUser(String email, String password) {

        //To sign in the user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    //When the task is complete
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            //Get rid of the progress dialog
                            mLoginProgress.dismiss();

                            String uid = mAuth.getUid();

                            if (uid == null)
                                return;

                            //The database reference for the user
                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                            //Check if the current user is marked as a admin
                            mDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    String uid1 = mAuth.getUid();
                                    //Check the user type
                                    String user_type = dataSnapshot.child("user_type").getValue().toString();

                                    user.setUser_type(user_type);

                                    //Initialize the user object
                                    user.setEmail(dataSnapshot.child("email").getValue().toString());

                                    // Set the password hash (if it exists)
                                    if (dataSnapshot.hasChild("password_hash"))
                                    {
                                        user.setPassword_hash(dataSnapshot.child("password_hash").getValue().toString());
                                    }
                                    user.setId(uid1);

                                    // If the user marked themselves as an owner, take them to the owner page
                                    Switch ownerToggle = findViewById(R.id.login_ownerToggle);
                                    if (ownerToggle.isChecked() && user_type.equals("admin")) {

                                        //Check if the current user is marked as a admin
                                        Intent ownerPageIntent = new Intent(SimplerLoginActivity.this, OwnersResturantsActivity.class);

                                        //Reverse the toggle back to its state
                                        ownerToggle.setChecked(false);

                                        //This line of code makes sure that the user can't go back to the registration page using the phone back button
                                        ownerPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                        //Actually switches the UI
                                        startActivity(ownerPageIntent);

                                        finish();
                                        return;
                                    }

                                    if (!ownerToggle.isChecked() && user_type.equals("normal") && (mAuth.getCurrentUser() != null))
                                    {
                                        // They did not mark themselves as an admin, so go to the map view
                                        Intent userPageIntent = new Intent(SimplerLoginActivity.this, UserMapViewActivity.class);
                                        userPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                        startActivity(userPageIntent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        } else {

                            //Get rid of the progress dialog
                            mLoginProgress.hide();

                            error = 1;

                            //Print the text to say that there is a error
                            Toast.makeText(SimplerLoginActivity.this, "We couldn't find your account!",Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
    //Initialize the user object
    public static void userInit()
    {
        user.setId("0");
        user.user_type = "null";
        user.setPassword_hash("0");
        String[] restArray = {"null"};
        user.setRestaurant(restArray);
        user.setEmail("root@root.com");
    }
}
