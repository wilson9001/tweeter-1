package edu.byu.cs.tweeter.view.main.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.request.SignUpRequest;
import edu.byu.cs.tweeter.net.response.SignUpResponse;
import edu.byu.cs.tweeter.presenter.SignUpPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.SignUpTask;
import edu.byu.cs.tweeter.view.main.MainActivity;

public class SignupActivity extends AppCompatActivity implements SignUpPresenter.View, SignUpTask.SignUpObserver
{
    private SignUpPresenter signUpPresenter;

    private View.OnClickListener attemptSignUp = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            EditText firstNameRaw = findViewById(R.id.firstName);
            EditText lastNameRaw = findViewById(R.id.lastName);
            EditText aliasRaw = findViewById(R.id.alias);
            EditText passwordRaw = findViewById(R.id.password);
            EditText imageURLRaw = findViewById(R.id.imageURL);

            String firstName = firstNameRaw.getText().toString();
            String lastName = lastNameRaw.getText().toString();
            String alias = aliasRaw.getText().toString();
            String password = passwordRaw.getText().toString();
            String imageURL = imageURLRaw.getText().toString();

            SignUpTask signUpTask = new SignUpTask(signUpPresenter, SignupActivity.this);
            SignUpRequest signUpRequest = new SignUpRequest(firstName, lastName, alias, password, imageURL);
            signUpTask.execute(signUpRequest);
        }
    };

    private View.OnClickListener toSignInPage = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            transitionToSignIn();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signUpPresenter = new SignUpPresenter(this);

        findViewById(R.id.signUpButton).setOnClickListener(attemptSignUp);
        findViewById(R.id.signInButton).setOnClickListener(toSignInPage);
    }

    public void transitionToSignIn()
    {
        Toast.makeText(this, "This will transition to the sign in activity", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void signedUp(User signedInUser)
    {
        Toast.makeText(this, "This will transition to the main activity", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void signUpRetrieved(SignUpResponse signUpResponse)
    {
        User signedInUser = signUpResponse.getUser();

        if(signedInUser != null)
        {
            Toast.makeText(this, signedInUser.getAlias(), Toast.LENGTH_SHORT).show();
            signedUp(signedInUser);
        }
        else
        {
            Toast.makeText(this, signUpResponse.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
