package edu.byu.cs.tweeter.view.main.feed;
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
import edu.byu.cs.tweeter.net.request.FeedRequest;
import edu.byu.cs.tweeter.net.request.SearchRequest;
import edu.byu.cs.tweeter.net.response.FeedResponse;
import edu.byu.cs.tweeter.presenter.FeedPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.GetFeedTask;
import edu.byu.cs.tweeter.view.asyncTasks.SearchTask;
import edu.byu.cs.tweeter.view.cache.ImageCache;
import edu.byu.cs.tweeter.view.main.MainActivity;


public class feedFragment extends Fragment implements FeedPresenter.View {
    private static final int LOADING_DATA_VIEW = 0;
    private static final int ITEM_VIEW = 1;

    private static final int PAGE_SIZE = 10;

    private FeedPresenter feedPresenter;

    private FeedRecyclerViewAdapter feedRecyclerViewAdapter;

    private FeedFragmentListener feedFragmentListener;

    public interface FeedFragmentListener
    {
        void aliasClicked(SearchRequest searchRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        feedPresenter = new FeedPresenter(this);

        RecyclerView feedRecyclerView = view.findViewById(R.id.feedRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        feedRecyclerView.setLayoutManager(linearLayoutManager);

        feedRecyclerViewAdapter = new FeedRecyclerViewAdapter();
        feedRecyclerView.setAdapter(feedRecyclerViewAdapter);

        feedRecyclerView.addOnScrollListener(new FeedRecyclerViewPaginationOnScrollListener(linearLayoutManager));

        return view;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof FeedFragmentListener)
        {
            feedFragmentListener = (FeedFragmentListener) context;
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
        feedFragmentListener = null;
    }

    private class FeedHolder extends RecyclerView.ViewHolder
    {
        private final ImageView userImage;
        private final TextView userAlias;
        private final TextView userName;
        private final TextView timestamp;
        private final TextView statusText;

        FeedHolder(@NonNull View itemView)
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
                spannableString.setSpan(new ClickableSpan()
                {
                    @Override
                    public void onClick(@NonNull View widget)
                    {
                        feedFragmentListener.aliasClicked(new SearchRequest(reference.first.substring(1)));
                    }
                }, reference.second.first, reference.second.second, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            statusText.setText(spannableString);
            statusText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private class FeedRecyclerViewAdapter extends RecyclerView.Adapter<FeedHolder> implements GetFeedTask.GetFeedObserver
    {
        private final List<Status> statuses = new ArrayList<>();

        private Status lastStatus;

        private boolean hasMorePages;
        private boolean isLoading = false;

        FeedRecyclerViewAdapter()
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
        public FeedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(feedFragment.this.getContext());
            View view;

            if (isLoading)
            {
                view = layoutInflater.inflate(R.layout.loading_row, parent, false);
            }
            else
            {
                view = layoutInflater.inflate(R.layout.status_row, parent, false);
            }

            return new FeedHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FeedHolder feedHolder, int position)
        {
            if (!isLoading)
            {
                feedHolder.bindStatus(statuses.get(position));
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

            GetFeedTask getFeedTask = new GetFeedTask(feedPresenter, this);
            FeedRequest feedRequest = new FeedRequest(feedPresenter.getUserBeingViewed(), PAGE_SIZE, lastStatus);
            getFeedTask.execute(feedRequest);
        }

        @Override
        public void feedRetrieved(FeedResponse feedResponse)
        {
            List<Status> statuses = feedResponse.getStatuses();

            lastStatus = statuses.isEmpty() ? null : statuses.get(statuses.size() - 1);
            hasMorePages = feedResponse.hasMorePages();

            isLoading = false;
            removeLoadingFooter();
            feedRecyclerViewAdapter.addItems(statuses);
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

    private class FeedRecyclerViewPaginationOnScrollListener extends RecyclerView.OnScrollListener
    {
        private final LinearLayoutManager linearLayoutManager;

        FeedRecyclerViewPaginationOnScrollListener(LinearLayoutManager linearLayoutManager)
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

            if(!feedRecyclerViewAdapter.isLoading && feedRecyclerViewAdapter.hasMorePages)
            {
                if ((visibleItemCount + firstVisibleItemPosition) >=
                    totalItemCount && firstVisibleItemPosition >= 0)
                {
                    feedRecyclerViewAdapter.loadMoreItems();
                }
            }
        }
    }
}
