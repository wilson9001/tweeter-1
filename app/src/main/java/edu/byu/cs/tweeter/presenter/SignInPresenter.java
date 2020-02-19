package edu.byu.cs.tweeter.presenter;

import edu.byu.cs.tweeter.model.services.LoginService;
import edu.byu.cs.tweeter.net.request.SignInRequest;
import edu.byu.cs.tweeter.net.response.SignInResponse;

public class SignInPresenter extends Presenter
{
    private final View view;

    public interface View
    {
        // If needed, Specify methods here that will be called on the view in response to model updates
    }

    public SignInPresenter(View view)
    {
        this.view = view;
    }

    public SignInResponse signIn(SignInRequest signInRequest)
    {
        return LoginService.getInstance().signIn(signInRequest);
    }
}
