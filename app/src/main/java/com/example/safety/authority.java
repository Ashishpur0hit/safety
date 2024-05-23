package com.example.safety;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

class authorityActivity extends AppCompatActivity {

    private EditText departmentNumberEditText;
    private EditText employeeIdEditText;
    private EditText employeeEmailEditText;
    private EditText passwordEditText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Corrected line

        // Initialize UI elements
        departmentNumberEditText = findViewById(R.id.departmentNumber);
        employeeIdEditText = findViewById(R.id.employeeId);
        employeeEmailEditText = findViewById(R.id.employeeEmail);
        passwordEditText = findViewById(R.id.password);

        // Set a click listener for the login button
        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String departmentNumber = departmentNumberEditText.getText().toString();
                String employeeId = employeeIdEditText.getText().toString();
                String employeeEmail = employeeEmailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Validate input fields
                if (TextUtils.isEmpty(departmentNumber) || TextUtils.isEmpty(employeeId)
                        || TextUtils.isEmpty(employeeEmail) || TextUtils.isEmpty(password)) {
                    Toast.makeText(authorityActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Perform the login process
                performLogin(departmentNumber, employeeId, employeeEmail, password);
            }
        });
    }

    private void performLogin(String departmentNumber, String employeeId, String employeeEmail, String password) {
        // Here you can implement the actual login process, such as making a network request
        // to your server or validating the credentials locally.
        // For the sake of simplicity, I'll just print the input values to the console.

        Log.d("Login", "Department Number: " + departmentNumber);
        Log.d("Login", "Employee Id: " + employeeId);
        Log.d("Login", "Employee Email: " + employeeEmail);
        Log.d("Login", "Password: ******");

        // After the login process, you can navigate the user to another screen or show a success message.
    }
}