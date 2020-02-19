package edu.byu.cs.tweeter.view.main.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signUpPresenter = new SignUpPresenter(this);

        findViewById(R.id.signUpButton).setOnClickListener(attemptSignUp);
        findViewById(R.id.signInButton).setOnClickListener(toSignInPage);
    }

    private View.OnClickListener attemptSignUp = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            String firstName = v.findViewById(R.id.firstName).toString();
            String lastName = v.findViewById(R.id.lastName).toString();
            String alias = v.findViewById(R.id.alias).toString();
            String password = v.findViewById(R.id.password).toString();
            String imageURL = v.findViewById(R.id.imageURL).toString();

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
            signedUp(signedInUser);
        }
        else
        {
            Toast.makeText(this, signUpResponse.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
