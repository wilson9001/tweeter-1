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
import edu.byu.cs.tweeter.net.request.ChangeRelationshipRequest;
import edu.byu.cs.tweeter.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.net.request.SearchRequest;
import edu.byu.cs.tweeter.net.request.SignOutRequest;
import edu.byu.cs.tweeter.net.response.ChangeRelationshipResponse;
import edu.byu.cs.tweeter.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.net.response.SearchResponse;
import edu.byu.cs.tweeter.net.response.SignOutResponse;
import edu.byu.cs.tweeter.presenter.ChangeRelationshipPresenter;
import edu.byu.cs.tweeter.presenter.MainPresenter;
import edu.byu.cs.tweeter.presenter.PostStatusPresenter;
import edu.byu.cs.tweeter.presenter.SearchPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.ChangeRelationshipTask;
import edu.byu.cs.tweeter.view.asyncTasks.LoadImageTask;
import edu.byu.cs.tweeter.view.asyncTasks.PostStatusTask;
import edu.byu.cs.tweeter.view.asyncTasks.SearchTask;
import edu.byu.cs.tweeter.view.asyncTasks.SignOutTask;
import edu.byu.cs.tweeter.view.cache.ImageCache;
import edu.byu.cs.tweeter.view.main.feed.feedFragment;
import edu.byu.cs.tweeter.view.main.login.LoginActivity;
import edu.byu.cs.tweeter.view.main.story.storyFragment;

public class MainActivity extends AppCompatActivity implements LoadImageTask.LoadImageObserver, MainPresenter.View, SignOutTask.SignOutObserver, PostStatusFragment.PostStatusFragmentListener, PostStatusTask.PostStatusObserver, PostStatusPresenter.View, SearchFragment.SearchFragmentListener, SearchTask.SearchObserver, SearchPresenter.View, ChangeRelationshipPresenter.View, ChangeRelationshipTask.ChangeRelationshipObserver, feedFragment.FeedFragmentListener, storyFragment.StoryFragmentListener
{
    private MainPresenter presenter;
    private PostStatusPresenter postStatusPresenter;
    private SearchPresenter searchPresenter;
    private ChangeRelationshipPresenter changeRelationshipPresenter;
    private User user;
    private User userBeingViewed;
    private ImageView userImageView;
    private PopupWindow popupWindow;
    private Button closeDialogButton, executeDialogActionButton;
    private boolean userFollowsUserBeingViewed, userSignedIn;

    private View.OnClickListener changeUserRelationship = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Button thisButton = (Button) v;

            String buttonText = thisButton.getText().toString();

            ChangeRelationshipTask changeRelationshipTask = new ChangeRelationshipTask(changeRelationshipPresenter, MainActivity.this);

            ChangeRelationshipRequest changeRelationshipRequest = new ChangeRelationshipRequest(user, userBeingViewed,
                                                                                                buttonText.equals("Follow") ? ChangeRelationshipRequest.RelationshipChange.FOLLOW : ChangeRelationshipRequest.RelationshipChange.UNFOLLOW);

            changeRelationshipTask.execute(changeRelationshipRequest);
        }
    };

    private View.OnClickListener changeSignInState = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if(userSignedIn)
            {
                SignOutTask signoutTask = new SignOutTask(presenter, MainActivity.this);
                SignOutRequest signOutRequest = new SignOutRequest(user);
                signoutTask.execute(signOutRequest);
            }
            else
            {
                toLoginScreen();
            }
        }
    };

    private void toLoginScreen()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private View.OnClickListener postStatus = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            EditText statusRaw = popupWindow.getContentView().findViewById(R.id.status);
            String statusText = statusRaw.getText().toString();

            PostStatusTask postStatusTask = new PostStatusTask(postStatusPresenter, MainActivity.this);
            PostStatusRequest postStatusRequest = new PostStatusRequest(user, statusText);
            postStatusTask.execute(postStatusRequest);
        }
    };

    private View.OnClickListener executeSearch = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            EditText searchRaw = popupWindow.getContentView().findViewById(R.id.searchField);
            String searchString = searchRaw.getText().toString();

            SearchTask searchTask = new SearchTask(searchPresenter, MainActivity.this);
            SearchRequest searchRequest = new SearchRequest(searchString);
            searchTask.execute(searchRequest);
        }
    };

    private View.OnClickListener closeWindow = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            popupWindow.dismiss();
        }
    };

    private View.OnClickListener openSearchDialog = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View postStatusView = layoutInflater.inflate(R.layout.fragment_search, (ViewGroup) findViewById(R.id.mainActivityOuterView), false);

            closeDialogButton = postStatusView.findViewById(R.id.cancelSearchButton);
            executeDialogActionButton = postStatusView.findViewById(R.id.executeSearchButton);

            popupWindow = new PopupWindow(postStatusView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

            popupWindow.showAsDropDown(findViewById(R.id.mainActivityOuterView));

            closeDialogButton.setOnClickListener(closeWindow);
            executeDialogActionButton.setOnClickListener(executeSearch);
        }
    };

    private View.OnClickListener openPostTweetDialog = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View postStatusView = layoutInflater.inflate(R.layout.fragment_post_status, (ViewGroup) findViewById(R.id.mainActivityOuterView), false);

            closeDialogButton = postStatusView.findViewById(R.id.closePostStatusButton);
            executeDialogActionButton = postStatusView.findViewById(R.id.postStatusButton);

            popupWindow = new PopupWindow(postStatusView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

            popupWindow.showAsDropDown(findViewById(R.id.mainActivityOuterView));

            closeDialogButton.setOnClickListener(closeWindow);
            executeDialogActionButton.setOnClickListener(postStatus);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        userSignedIn = getIntent().getBooleanExtra("userSignedIn", false);

        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(this);
        postStatusPresenter = new PostStatusPresenter(this);
        searchPresenter = new SearchPresenter(this);
        changeRelationshipPresenter = new ChangeRelationshipPresenter(this);

        FloatingActionButton fab = findViewById(R.id.fab);

        if(userSignedIn)
        {
            fab.setOnClickListener(openPostTweetDialog);
        }
        else
        {
            fab.hide();
        }

        FloatingActionButton searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(openSearchDialog);

        userImageView = findViewById(R.id.userImage);

        user = presenter.getCurrentUser();
        userFollowsUserBeingViewed = presenter.getUserFollowsUserBeingViewed();

        Button changeSignInStateButton = findViewById(R.id.changeSignInStateButton);
        changeSignInStateButton.setOnClickListener(changeSignInState);

        if(!userSignedIn)
        {
            changeSignInStateButton.setText("Sign In");
        }

        findViewById(R.id.changeRelationshipButton).setOnClickListener(changeUserRelationship);
        refreshHomeScreen();
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

        ViewPager viewPager = findViewById(R.id.view_pager);

        if(userSignedIn && user.equals(userBeingViewed))
        {
            Log.d("refreshHomeScreen", "about to re-initialize adapter as SectionsPagerAdapter");
            SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
            viewPager.setAdapter(sectionsPagerAdapter);
        }
        else
        {
            Log.d("refreshHomeScreen", "about to re-initialize adapter as PartialSectionsPagerAdapter");
            PartialSectionsPagerAdapter partialSectionsPagerAdapter = new PartialSectionsPagerAdapter(this, getSupportFragmentManager());
            viewPager.setAdapter(partialSectionsPagerAdapter);
        }

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        setChangeRelationshipButton();
    }

    private void setChangeRelationshipButton()
    {
        Button changeRelationshipButton = findViewById(R.id.changeRelationshipButton);

        if (user.equals(userBeingViewed) || !userSignedIn)
        {
            changeRelationshipButton.setVisibility(View.GONE);
        }
        else if (userFollowsUserBeingViewed)
        {
            changeRelationshipButton.setText("Unfollow");
            changeRelationshipButton.setVisibility(View.VISIBLE);
        }
        else
        {
            changeRelationshipButton.setText("Follow");
            changeRelationshipButton.setVisibility(View.VISIBLE);
        }
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

        if (message == null)
        {
            userSignedIn = false;
            toLoginScreen();
        }
        else
        {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    public void closeDialogWindow()
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
            closeDialogWindow();
        }
        else
        {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void searchRetrieved(SearchResponse searchResponse)
    {
        User searchedUser = searchResponse.getSearchedUser();

        if (searchedUser == null)
        {
            Toast.makeText(this, searchResponse.getMessage(), Toast.LENGTH_LONG).show();
        }
        else
        {
            userFollowsUserBeingViewed = searchResponse.getUserFollowsSearchedUser();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("userSignedIn", userSignedIn);
            startActivity(intent);
            //refreshHomeScreen();
            //closeDialogWindow();
        }
    }

    @Override
    public void changeRelationshipRetrieved(ChangeRelationshipResponse changeRelationshipResponse)
    {
        String message = changeRelationshipResponse.getMessage();

        if(message == null)
        {
            ChangeRelationshipResponse.RelationshipChanged relationshipChanged = changeRelationshipResponse.getRelationshipChanged();

            userFollowsUserBeingViewed = relationshipChanged == ChangeRelationshipResponse.RelationshipChanged.FOLLOWED;

            setChangeRelationshipButton();
        }
        else
        {
            Toast.makeText(this, changeRelationshipResponse.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void aliasClicked(SearchRequest searchRequest)
    {
        new SearchTask(searchPresenter, this).execute(searchRequest);
    }
}