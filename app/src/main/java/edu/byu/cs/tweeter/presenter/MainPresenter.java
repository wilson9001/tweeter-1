package edu.byu.cs.tweeter.presenter;

import edu.byu.cs.tweeter.model.services.LoginService;
import edu.byu.cs.tweeter.net.request.SignOutRequest;
import edu.byu.cs.tweeter.net.response.SignOutResponse;

public class MainPresenter extends Presenter
{

    private final View view;

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View
    {

    }

    public SignOutResponse signOut(SignOutRequest signOutRequest)
    {
        return LoginService.getInstance().signOut(signOutRequest);
    }

    public MainPresenter(View view)
    {
        this.view = view;
    }
}
