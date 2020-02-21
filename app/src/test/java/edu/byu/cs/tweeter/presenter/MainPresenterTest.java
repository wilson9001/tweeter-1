package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.services.LoginService;
import edu.byu.cs.tweeter.net.request.SignOutRequest;
import edu.byu.cs.tweeter.net.response.SignOutResponse;

import static org.junit.jupiter.api.Assertions.*;

class MainPresenterTest implements MainPresenter.View
{
    private MainPresenter mainPresenterSpy;
    private static SignOutRequest signOutRequest = new SignOutRequest(new User("User", "a", ""));

    @BeforeEach
    void setup()
    {
        mainPresenterSpy = Mockito.spy(new MainPresenter(this));
    }

    @Test
    void signOutSuccess()
    {
        SignOutResponse signOutResponse = mockSignOut(signOutRequest, new SignOutResponse());

        assertNull(signOutResponse.getMessage());
    }

    @Test
    void signOutFailure()
    {
        SignOutResponse signOutResponse = mockSignOut(signOutRequest, new SignOutResponse("User not signed in"));

        assertNotNull(signOutResponse.getMessage());
    }

    private SignOutResponse mockSignOut(SignOutRequest signOutRequest, SignOutResponse signOutResponse)
    {
        LoginService loginServiceMock = Mockito.mock(LoginService.class);
        Mockito.when(loginServiceMock.signOut(Mockito.any(SignOutRequest.class))).thenReturn(signOutResponse);
        Mockito.when(mainPresenterSpy.getService()).thenReturn(loginServiceMock);

        return mainPresenterSpy.signOut(signOutRequest);
    }
}