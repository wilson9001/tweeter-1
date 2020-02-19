package edu.byu.cs.tweeter.presenter;

import edu.byu.cs.tweeter.model.services.SignUpService;
import edu.byu.cs.tweeter.net.request.SignUpRequest;
import edu.byu.cs.tweeter.net.response.SignUpResponse;

public class SignUpPresenter extends Presenter
{
    private final View view;

    public interface View
    {
    }

    public SignUpPresenter(View view)
    {
        this.view = view;
    }

    public SignUpResponse signUp(SignUpRequest signUpRequest)
    {
        return SignUpService.getInstance().signUp(signUpRequest);
    }
}
