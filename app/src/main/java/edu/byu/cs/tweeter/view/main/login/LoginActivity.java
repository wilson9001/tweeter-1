package edu.byu.cs.tweeter.view.main.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

    private View.OnClickListener skipLoginPressed = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            skipLogin();
        }
    };

    private void skipLogin()
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userSignedIn", false);
        startActivity(intent);
    }

    private View.OnClickListener attemptSignIn = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            EditText signInAliasRaw = findViewById(R.id.alias);
            EditText signInPasswordRaw = findViewById(R.id.password);

            String signInAlias = signInAliasRaw.getText().toString();
            String signInPassword = signInPasswordRaw.getText().toString();

            SignInTask signInTask = new SignInTask(signInPresenter, LoginActivity.this);
            SignInRequest signInRequest = new SignInRequest(signInAlias, signInPassword);
            signInTask.execute(signInRequest);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInPresenter = new SignInPresenter(this);

        findViewById(R.id.signInButton).setOnClickListener(attemptSignIn);
        findViewById(R.id.signUpButton).setOnClickListener(toSignUpPage);
        findViewById(R.id.skipLoginButton).setOnClickListener(skipLoginPressed);
    }

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
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void signedIn(User signedInUser)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userSignedIn", true);
        startActivity(intent);
    }

    @Override
    public void signInRetrieved(SignInResponse signInResponse)
    {
        User signedInUser = signInResponse.getUser();

        if (signedInUser != null)
        {
            Log.d("LoginActivity", "User retrieved from sign in is ".concat(signedInUser.toString()));
            signedIn(signedInUser);
        }
        else
        {
            Log.d("LoginActivity", "User retrieved from sign in is null");
            Toast.makeText(this, signInResponse.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
