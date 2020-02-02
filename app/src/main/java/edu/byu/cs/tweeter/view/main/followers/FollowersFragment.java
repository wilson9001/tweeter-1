package edu.byu.cs.tweeter.view.main.followers;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.request.FollowersRequest;
import edu.byu.cs.tweeter.net.response.FollowersResponse;
import edu.byu.cs.tweeter.presenter.FollowersPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.GetFollowersTask;
import edu.byu.cs.tweeter.view.cache.ImageCache;

public class FollowersFragment extends Fragment implements FollowersPresenter.View
{
    private static final int LOADING_DATA_VIEW = 0;
    private static final int ITEM_VIEW = 1;

    private static final int PAGE_SIZE = 10;

    private FollowersPresenter followersPresenter;
    private FollowersRecyclerViewAdapter followersRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_followers, container, false);

        followersPresenter = new FollowersPresenter(this);

        RecyclerView followersRecyclerView = view.findViewById(R.id.followersRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        followersRecyclerView.setLayoutManager(linearLayoutManager);

        followersRecyclerViewAdapter = new FollowersRecyclerViewAdapter();
        followersRecyclerView.setAdapter(followersRecyclerViewAdapter);

        followersRecyclerView.addOnScrollListener(new FollowerRecyclerViewPaginationScrollListener(linearLayoutManager));

        return view;
    }

    private class FollowerHolder extends RecyclerView.ViewHolder
    {
        private final ImageView userImage;
        private final TextView userAlias;
        private final TextView userName;

        FollowerHolder(@NonNull View itemView)
        {
            super(itemView);

            userImage = itemView.findViewById(R.id.userImage);
            userAlias = itemView.findViewById(R.id.userAlias);
            userName = itemView.findViewById(R.id.userName);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Toast.makeText(getContext(), "You selected '" + userName.getText() + "'.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        void bindUser(User user)
        {
            userImage.setImageDrawable(ImageCache.getInstance().getImageDrawable(user));
            userAlias.setText(user.getAlias());
            userName.setText(user.getName());
        }
    }

    private class FollowersRecyclerViewAdapter extends RecyclerView.Adapter<FollowerHolder> implements GetFollowersTask.GetFollowersObserver
    {
        private final List<User> users = new ArrayList<>();

        private User lastFollower;

        private boolean hasMorePages;
        private boolean isLoading = false;

        FollowersRecyclerViewAdapter()
        {
            loadMoreItems();
        }

        void addItems(List<User> newUsers)
        {
            int startInsertPosition = users.size();
            users.addAll(newUsers);
            this.notifyItemRangeInserted(startInsertPosition, newUsers.size());
        }

        void addItem(User user)
        {
            users.add(user);
            this.notifyItemInserted(users.size() - 1);
        }

        void removeItem(User user)
        {
            int position = users.indexOf(user);
            users.remove(position);
            this.notifyItemRemoved(position);
        }

        @NonNull
        @Override
        public FollowerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(FollowersFragment.this.getContext());
            View view;

            if (isLoading)
            {
                view = layoutInflater.inflate(R.layout.loading_row, parent, false);
            }
            else
            {
                view = layoutInflater.inflate(R.layout.user_row, parent, false);
            }

            return new FollowerHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FollowerHolder followersHolder, int position)
        {
            if (!isLoading)
            {
                followersHolder.bindUser(users.get(position));
            }
        }

        @Override
        public int getItemCount()
        {
            return users.size();
        }

        @Override
        public int getItemViewType(int position)
        {
            return (position == users.size() - 1 && isLoading) ? LOADING_DATA_VIEW : ITEM_VIEW;
        }

        void loadMoreItems()
        {
            isLoading = true;
            addLoadingFooter();

            GetFollowersTask getFollowersTask = new GetFollowersTask(followersPresenter, this);
            FollowersRequest followersRequest = new FollowersRequest(followersPresenter.getCurrentUser(), PAGE_SIZE, lastFollower);
            getFollowersTask.execute(followersRequest);
        }

        @Override
        public void followersRetrieved(FollowersResponse followersResponse)
        {
            List<User> followers = followersResponse.getFollowers();

            lastFollower = followers.isEmpty() ? null : followers.get(followers.size() - 1);
            hasMorePages = followersResponse.hasMorePages();

            isLoading = false;
            removeLoadingFooter();
            followersRecyclerViewAdapter.addItems(followers);
        }

        private void addLoadingFooter()
        {
            addItem(new User("Dummy", "User", ""));
        }

        private void removeLoadingFooter()
        {
            removeItem(users.get(users.size() - 1));
        }
    }

    private class FollowerRecyclerViewPaginationScrollListener extends RecyclerView.OnScrollListener
    {
        private final LinearLayoutManager linearLayoutManager;

        FollowerRecyclerViewPaginationScrollListener(LinearLayoutManager linearLayoutManager)
        {
            this.linearLayoutManager = linearLayoutManager;
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy)
        {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = linearLayoutManager.getChildCount();
            int totalItemCount = linearLayoutManager.getItemCount();
            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

            if (!followersRecyclerViewAdapter.isLoading && followersRecyclerViewAdapter.hasMorePages)
            {
                if ((visibleItemCount + firstVisibleItemPosition) >=
                    totalItemCount && firstVisibleItemPosition >= 0)
                {
                    followersRecyclerViewAdapter.loadMoreItems();
                }
            }
        }
    }
}
