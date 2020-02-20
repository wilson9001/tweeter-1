package edu.byu.cs.tweeter.view.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.net.request.SignOutRequest;
import edu.byu.cs.tweeter.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.net.response.SignOutResponse;
import edu.byu.cs.tweeter.presenter.MainPresenter;
import edu.byu.cs.tweeter.presenter.PostStatusPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.LoadImageTask;
import edu.byu.cs.tweeter.view.asyncTasks.PostStatusTask;
import edu.byu.cs.tweeter.view.asyncTasks.SignOutTask;
import edu.byu.cs.tweeter.view.cache.ImageCache;
import edu.byu.cs.tweeter.view.main.login.LoginActivity;

public class MainActivity extends AppCompatActivity implements LoadImageTask.LoadImageObserver, MainPresenter.View, SignOutTask.SignOutObserver, PostStatusFragment.PostStatusFragmentListener, PostStatusTask.PostStatusObserver, PostStatusPresenter.View
{
    private MainPresenter presenter;
    private PostStatusPresenter postStatusPresenter;
    private User user;
    private User userBeingViewed;
    private ImageView userImageView;
    private PopupWindow popupWindow;
    private Button closePostStatusButton, postStatusButton;

    private View.OnClickListener signOut = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            SignOutTask signoutTask = new SignOutTask(presenter, MainActivity.this);
            SignOutRequest signOutRequest = new SignOutRequest(user);
            signoutTask.execute(signOutRequest);
        }
    };

    private View.OnClickListener postStatus = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            EditText statusRaw = popupWindow.getContentView().findViewById(R.id.status);
            String statusText = statusRaw.getText().toString();

            Log.d("PostStatus Listener", statusText);

            PostStatusTask postStatusTask = new PostStatusTask(postStatusPresenter, MainActivity.this);
            PostStatusRequest postStatusRequest = new PostStatusRequest(user, statusText);
            postStatusTask.execute(postStatusRequest);
        }
    };

    private View.OnClickListener closeWindow = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Log.d("Close status listener", "Close status listener activated");
            popupWindow.dismiss();
        }
    };

    private View.OnClickListener openPostTweetDialog = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();

            Log.d("openPostTweetDialog", "Inside onClickListener for open tweet");
            LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View postStatusView = layoutInflater.inflate(R.layout.fragment_post_status, (ViewGroup) findViewById(R.id.mainActivityOuterView), false);

            closePostStatusButton = postStatusView.findViewById(R.id.closePostStatusButton);
            postStatusButton = postStatusView.findViewById(R.id.postStatusButton);

            popupWindow = new PopupWindow(postStatusView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

            popupWindow.showAsDropDown(findViewById(R.id.mainActivityOuterView));

            closePostStatusButton.setOnClickListener(closeWindow);
            postStatusButton.setOnClickListener(postStatus);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(this);
        postStatusPresenter = new PostStatusPresenter(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(openPostTweetDialog);

        userImageView = findViewById(R.id.userImage);

        user = presenter.getCurrentUser();

        refreshHomeScreen();

        findViewById(R.id.logOutButton).setOnClickListener(signOut);
    }

    private void refreshHomeScreen()
    {
        userBeingViewed = presenter.getUserBeingViewed();

        // Asynchronously load the user's image
        LoadImageTask loadImageTask = new LoadImageTask(this);
        loadImageTask.execute(userBeingViewed.getImageUrl());

        TextView userName = findViewById(R.id.userName);
        userName.setText(userBeingViewed.getName());

        TextView userAlias = findViewById(R.id.userAlias);
        userAlias.setText(userBeingViewed.getAlias());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        findViewById(R.id.followButton).setVisibility(user.equals(userBeingViewed) ? View.GONE : View.VISIBLE);
    }

    @Override
    public void imageLoadProgressUpdated(Integer progress)
    {
        // We're just loading one image. No need to indicate progress.
    }

    @Override
    public void imagesLoaded(Drawable[] drawables)
    {
        ImageCache.getInstance().cacheImage(user, drawables[0]);

        if (drawables[0] != null)
        {
            userImageView.setImageDrawable(drawables[0]);
        }
    }

    @Override
    public void signOutRetrieved(SignOutResponse signOutResponse)
    {
        String message = signOutResponse.getMessage();

        if(message == null)
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    public void closeStatusPost()
    {
        popupWindow.dismiss();
    }

    @Override
    public void postStatusRetrieved(PostStatusResponse postStatusResponse)
    {
        String message = postStatusResponse.getMessage();

        if (message == null)
        {
            refreshHomeScreen();
            closeStatusPost();
        }
        else
        {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }
}