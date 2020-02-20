package edu.byu.cs.tweeter.view.main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.jetbrains.annotations.NotNull;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.presenter.PostStatusPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.PostStatusTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostStatusFragment extends Fragment //implements PostStatusTask.PostStatusObserver, PostStatusPresenter.View
{
    public interface PostStatusFragmentListener
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_status, container, false);
    }
}
