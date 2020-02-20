package edu.byu.cs.tweeter.view.main;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.request.SignOutRequest;
import edu.byu.cs.tweeter.net.response.SignOutResponse;
import edu.byu.cs.tweeter.presenter.MainPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.LoadImageTask;
import edu.byu.cs.tweeter.view.asyncTasks.SignOutTask;
import edu.byu.cs.tweeter.view.cache.ImageCache;
import edu.byu.cs.tweeter.view.main.login.LoginActivity;

public class MainActivity extends AppCompatActivity implements LoadImageTask.LoadImageObserver, MainPresenter.View, SignOutTask.SignOutObserver
{
    private MainPresenter presenter;
    private User user;
    private User userBeingViewed;
    private ImageView userImageView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        userImageView = findViewById(R.id.userImage);

        user = presenter.getCurrentUser();

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

        findViewById(R.id.logOutButton).setOnClickListener(signOut);

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
            Toast.makeText(this, "This will transition to the sign in activity", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }
}