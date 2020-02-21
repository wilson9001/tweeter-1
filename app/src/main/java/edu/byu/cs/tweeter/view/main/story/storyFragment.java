package edu.byu.cs.tweeter.view.main.story;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.net.request.SearchRequest;
import edu.byu.cs.tweeter.net.request.StoryRequest;
import edu.byu.cs.tweeter.net.response.StoryResponse;
import edu.byu.cs.tweeter.presenter.StoryPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.GetStoryTask;
import edu.byu.cs.tweeter.view.cache.ImageCache;

public class storyFragment extends Fragment implements StoryPresenter.View
{
    private static final int LOADING_DATA_VIEW = 0;
    private static final int ITEM_VIEW = 1;

    private static final int PAGE_SIZE = 10;

    private StoryPresenter storyPresenter;
    private StoryRecyclerViewAdapter storyRecyclerViewAdapter;
    private StoryFragmentListener storyFragmentListener;

    public interface StoryFragmentListener
    {
        void aliasClicked(SearchRequest searchRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_story, container, false);

        storyPresenter = new StoryPresenter(this);

        RecyclerView storyRecyclerView = view.findViewById(R.id.storyRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        storyRecyclerView.setLayoutManager(layoutManager);

        storyRecyclerViewAdapter = new StoryRecyclerViewAdapter();
        storyRecyclerView.setAdapter(storyRecyclerViewAdapter);

        storyRecyclerView.addOnScrollListener(new StoryRecyclerViewPaginationOnScrollListener(layoutManager));

        return view;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof StoryFragmentListener)
        {
            storyFragmentListener = (StoryFragmentListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                                       + " must implement FeedFragmentListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        storyFragmentListener = null;
    }

    private class StoryHolder extends RecyclerView.ViewHolder
    {
        private final ImageView userImage;
        private final TextView userAlias;
        private final TextView userName;
        private final TextView timestamp;
        private final TextView statusText;

        StoryHolder(@NonNull View itemView)
        {
            super(itemView);

            userImage = itemView.findViewById(R.id.userImage);
            userAlias = itemView.findViewById(R.id.userAlias);
            userName = itemView.findViewById(R.id.userName);
            timestamp = itemView.findViewById(R.id.timestamp);
            statusText = itemView.findViewById(R.id.statusText);
        }

        void bindStatus(Status status)
        {
            User poster = status.getPoster();
            userImage.setImageDrawable(ImageCache.getInstance().getImageDrawable(poster));
            userAlias.setText(poster.getAlias());
            userName.setText(poster.getName());
            timestamp.setText(status.getTimeStamp().toString());

            String statusString = status.getStatusText();

            SpannableString spannableString = new SpannableString(statusString);

            final List<Pair<String, Pair<Integer, Integer>>> references = status.getReferences();

            Log.d("bindStatus", "About to start creating spannable string for ".concat(status.getPoster().getAlias()).concat(", Entry count = ").concat(String.valueOf(references.size())));

            for (final Pair<String, Pair<Integer, Integer>> reference : references)
            {
                Log.d("bindStatus loop", "Creating new clickable span for ".concat(reference.first));
                spannableString.setSpan(new ClickableSpan()
                {
                    @Override
                    public void onClick(@NonNull View widget)
                    {
                        Log.d("aliasClicked", "alias activated for ".concat(reference.first.substring(1)));
                        storyFragmentListener.aliasClicked(new SearchRequest(reference.first.substring(1)));
                    }
                }, reference.second.first, reference.second.second, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            statusText.setText(spannableString);
            statusText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private class StoryRecyclerViewAdapter extends RecyclerView.Adapter<StoryHolder> implements GetStoryTask.GetStoryObserver
    {
        private final List<Status> statuses = new ArrayList<>();

        private Status lastStatus;

        private boolean hasMorePages;
        private boolean isLoading = false;

        StoryRecyclerViewAdapter()
        {
            loadMoreItems();
        }

        void addItems(List<Status> newStatuses)
        {
            int startInsertPosition = statuses.size();
            statuses.addAll(newStatuses);
            this.notifyItemRangeInserted(startInsertPosition, newStatuses.size());
        }

        void addItem(Status status)
        {
            statuses.add(status);
            this.notifyItemInserted(statuses.size() - 1);
        }

        void removeItem(Status status)
        {
            int position = statuses.indexOf(status);
            statuses.remove(position);
            this.notifyItemRemoved(position);
        }

        @NonNull
        @Override
        public StoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(storyFragment.this.getContext());
            View view;

            if (isLoading)
            {
                view = layoutInflater.inflate(R.layout.loading_row, parent, false);
            }
            else
            {
                view = layoutInflater.inflate(R.layout.status_row, parent, false);
            }

            return new StoryHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StoryHolder storyHolder, int position)
        {
            if (!isLoading)
            {
                storyHolder.bindStatus(statuses.get(position));
            }
        }

        @Override
        public int getItemCount()
        {
            return statuses.size();
        }

        @Override
        public int getItemViewType(int position)
        {
            return (position == statuses.size() - 1 && isLoading) ? LOADING_DATA_VIEW : ITEM_VIEW;
        }

        void loadMoreItems()
        {
            isLoading = true;
            addLoadingFooter();

            GetStoryTask getStoryTask = new GetStoryTask(storyPresenter, this);
            StoryRequest storyRequest = new StoryRequest(storyPresenter.getUserBeingViewed(), PAGE_SIZE, lastStatus);
            getStoryTask.execute(storyRequest);
        }

        @Override
        public void StoryRetrieved(StoryResponse storyResponse)
        {
            List<Status> statuses = storyResponse.getStatuses();

            lastStatus = statuses.isEmpty() ? null : statuses.get(statuses.size() - 1);
            hasMorePages = storyResponse.hasMorePages();

            isLoading = false;
            removeLoadingFooter();
            storyRecyclerViewAdapter.addItems(statuses);
        }

        private void addLoadingFooter()
        {
            addItem(new Status("Lorem ipsum", new User("Dummy", "User", "")));
        }

        private void removeLoadingFooter()
        {
            removeItem(statuses.get(statuses.size() - 1));
        }
    }

    private class StoryRecyclerViewPaginationOnScrollListener extends RecyclerView.OnScrollListener
    {
        private final LinearLayoutManager linearLayoutManager;

        StoryRecyclerViewPaginationOnScrollListener(LinearLayoutManager linearLayoutManager)
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

            if (!storyRecyclerViewAdapter.isLoading && storyRecyclerViewAdapter.hasMorePages)
            {
                if ((visibleItemCount + firstVisibleItemPosition) >=
                    totalItemCount && firstVisibleItemPosition >= 0)
                {
                    storyRecyclerViewAdapter.loadMoreItems();
                }
            }
        }
    }
}
