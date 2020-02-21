package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.services.LoginService;
import edu.byu.cs.tweeter.net.request.SignInRequest;
import edu.byu.cs.tweeter.net.response.SignInResponse;

import static org.junit.jupiter.api.Assertions.*;

class SignInPresenterTest implements SignInPresenter.View
{
    private SignInPresenter signInPresenterSpy;
    private SignInRequest signInRequest = new SignInRequest("", "");

    @BeforeEach
    void setup()
    {
        signInPresenterSpy = Mockito.spy(new SignInPresenter(this));
    }

    private SignInResponse mockSignIn(SignInRequest signInRequest, SignInResponse signInResponse)
    {
        LoginService loginServiceMock = Mockito.mock(LoginService.class);
        Mockito.when(loginServiceMock.signIn(Mockito.any(SignInRequest.class))).thenReturn(signInResponse);
        Mockito.when(signInPresenterSpy.getService()).thenReturn(loginServiceMock);

        return signInPresenterSpy.signIn(signInRequest);
    }

    @Test
    void signInSuccess()
    {
        SignInResponse signInResponse = mockSignIn(signInRequest, new SignInResponse(new User("", "", "")));

        assertNotNull(signInResponse.getUser());
        assertNull(signInResponse.getMessage());
    }

    @Test
    void signInFailure()
    {
        SignInResponse signInResponse = mockSignIn(signInRequest, new SignInResponse(""));

        assertNull(signInResponse.getUser());
        assertNotNull(signInResponse.getMessage());
    }
}