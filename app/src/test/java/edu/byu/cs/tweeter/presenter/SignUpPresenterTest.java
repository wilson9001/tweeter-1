package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.services.SignUpService;
import edu.byu.cs.tweeter.net.request.SignUpRequest;
import edu.byu.cs.tweeter.net.response.SignUpResponse;

import static org.junit.jupiter.api.Assertions.*;

class SignUpPresenterTest implements SignUpPresenter.View
{
    private SignUpPresenter signUpPresenterSpy;
    private SignUpRequest signUpRequest = new SignUpRequest("","","","","");

    @BeforeEach
    void setup()
    {
        signUpPresenterSpy = Mockito.spy(new SignUpPresenter(this));
    }

    private SignUpResponse mockSignUp(SignUpRequest signUpRequest, SignUpResponse signUpResponse)
    {
        SignUpService signUpServiceMock = Mockito.mock(SignUpService.class);
        Mockito.when(signUpServiceMock.signUp(Mockito.any(SignUpRequest.class))).thenReturn(signUpResponse);
        Mockito.when(signUpPresenterSpy.getService()).thenReturn(signUpServiceMock);

        return signUpPresenterSpy.signUp(signUpRequest);
    }

    @Test
    void signUpSuccess()
    {
        SignUpResponse signUpResponse = mockSignUp(signUpRequest, new SignUpResponse(new User("", "", ""), null));

        assertNotNull(signUpResponse.getUser());
        assertNull(signUpResponse.getMessage());
    }

    @Test
    void signUpFail()
    {
        SignUpResponse signUpResponse = mockSignUp(signUpRequest, new SignUpResponse(""));

        assertNull(signUpResponse.getUser());
        assertNotNull(signUpResponse.getMessage());
    }
}