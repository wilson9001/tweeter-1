package edu.byu.cs.tweeter.view.main;

import android.content.Context;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.view.main.followers.FollowersFragment;
import edu.byu.cs.tweeter.view.main.following.FollowingFragment;
import edu.byu.cs.tweeter.view.main.story.storyFragment;

public class PartialSectionsPagerAdapter extends FragmentPagerAdapter
{
    private static final int STORY_FRAGMENT_POSITION = 0;
    private static final int FOLLOWING_FRAGMENT_POSITION = 1;
    private static final int FOLLOWERS_FRAGMENT_POSITION = 2;

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.storyTabTitle, R.string.followingTabTitle, R.string.followersTabTitle};
    private final Context mContext;

    public PartialSectionsPagerAdapter(Context context, FragmentManager fm)
    {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case STORY_FRAGMENT_POSITION:
                return new storyFragment();

            case FOLLOWING_FRAGMENT_POSITION:
                return new FollowingFragment();

            case FOLLOWERS_FRAGMENT_POSITION:
                return new FollowersFragment();

            default:
                throw new WindowManager.InvalidDisplayException();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount()
    {
        return 3;
    }
}
