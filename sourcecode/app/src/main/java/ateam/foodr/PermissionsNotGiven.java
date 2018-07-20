package ateam.foodr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PermissionsNotGiven extends AppCompatActivity {

    private Button permissionsButton;
    private Button perReqBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions_not_given);

        permissionsButton = findViewById(R.id.PermissionButton);
        perReqBtn = findViewById(R.id.reqPerBtn);

        perReqBtn.setOnClickListener((View v) -> PermissionsUtils.checkAndRequestPermissions(PermissionsNotGiven.this));

        permissionsButton.setOnClickListener((View v) -> {
            if (PermissionsUtils.checkAndRequestPermissions(PermissionsNotGiven.this))
            {
                Intent userPageIntent = new Intent(PermissionsNotGiven.this, SimplerLoginActivity.class);
                userPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(userPageIntent);
                finish();
            }
        });

    }
}
