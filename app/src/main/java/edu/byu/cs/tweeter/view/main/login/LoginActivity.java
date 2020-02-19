package edu.byu.cs.tweeter.view.main.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.request.SignInRequest;
import edu.byu.cs.tweeter.net.response.SignInResponse;
import edu.byu.cs.tweeter.presenter.SignInPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.SignInTask;
import edu.byu.cs.tweeter.view.main.MainActivity;

public class LoginActivity extends AppCompatActivity implements SignInPresenter.View, SignInTask.SignInObserver
{
    private SignInPresenter signInPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInPresenter = new SignInPresenter(this);

        findViewById(R.id.signInButton).setOnClickListener(attemptSignIn);
        findViewById(R.id.signUpButton).setOnClickListener(toSignUpPage);
    }

    private View.OnClickListener attemptSignIn = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            String signInAlias = v.findViewById(R.id.alias).toString();
            String signInPassword = v.findViewById(R.id.password).toString();

            SignInTask signInTask = new SignInTask(signInPresenter, LoginActivity.this);
            SignInRequest signInRequest = new SignInRequest(signInAlias, signInPassword);
            signInTask.execute(signInRequest);
        }
    };

    private View.OnClickListener toSignUpPage = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            transitionToSignUp();
        }
    };

    public void transitionToSignUp()
    {
        Toast.makeText(this, "This will transition to the sign up activity", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void signedIn(User signedInUser)
    {
        Toast.makeText(this, "This will transition to the main activity", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void signInRetrieved(SignInResponse signInResponse)
    {
        User signedInUser = signInResponse.getUser();

        if (signedInUser != null)
        {
            signedIn(signedInUser);
        }
        else
        {
            Toast.makeText(this, signInResponse.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
