package pl.collage.friends;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.collage.base.BaseFragment;
import pl.collage.friendsearch.FriendSearchActivity;
import pl.collage.home.HomeActivity;
import pl.collage.util.adapters.FriendsAdapter;
import pl.collage.util.events.FriendDeletionEvent;
import pl.collage.util.events.FriendSelectedEvent;
import pl.collage.util.interactors.FirebaseDatabaseInteractor;
import pl.collage.util.interactors.FirebaseStorageInteractor;
import pl.collage.util.models.User;

public class FriendsFragment extends BaseFragment implements FriendsView {

    @BindView(pl.collage.R.id.friends_recycler_view)
    RecyclerView recyclerView;

    @BindView(pl.collage.R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(pl.collage.R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(pl.collage.R.id.content_fragment_friends)
    ViewGroup contentFragmentFriends;

    @BindView(pl.collage.R.id.layout_connection_error)
    ViewGroup layoutConnectionError;

    @BindView(pl.collage.R.id.no_items_text_view)
    TextView noItemsTextView;

    private FriendsPresenter friendsPresenter;
    private HomeActivity homeActivity;
    private FriendsAdapter friendsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friendsPresenter = new FriendsPresenter(this, new FirebaseDatabaseInteractor(),
                new FirebaseStorageInteractor());
        friendsAdapter = new FriendsAdapter(new ArrayList<User>(), friendsPresenter, getContext());
        homeActivity = (HomeActivity) getActivity();

        baseView = this;
        basePresenter = friendsPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(pl.collage.R.layout.fragment_friends, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),
                        layoutManager.getOrientation());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(friendsAdapter);

        friendsPresenter.populateFriendsList();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                friendsPresenter.populateFriendsList();
            }
        });

        return view;
    }

    @Override
    public void setMenuVisibility(boolean fragmentVisible) {
        super.setMenuVisibility(fragmentVisible);
        if (homeActivity != null) {
            ActionBar toolbar = homeActivity.getSupportActionBar();
            if (fragmentVisible && toolbar != null) {
                showSystemUI();
                homeActivity.showHomeNavigation();
                toolbar.setTitle(pl.collage.R.string.friends_screen_title);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar toolbar = homeActivity.getSupportActionBar();
        if (toolbar != null) toolbar.setTitle(pl.collage.R.string.friends_screen_title);
    }

    @OnClick(pl.collage.R.id.fab_add_friend)
    public void onAddFriendClicked() {
        startActivity(new Intent(getActivity(), FriendSearchActivity.class));
    }

    @Override
    public void showProgressBar() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showNoItemsInfo() {
        noItemsTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoItemsInfo() {
        noItemsTextView.setVisibility(View.GONE);
    }

    @Override
    public void updateRecyclerView(List<User> usersList) {
        friendsAdapter.setFriendList(usersList);
    }

    @Override
    public void showConnectionError() {
        contentFragmentFriends.setVisibility(View.GONE);
        layoutConnectionError.setVisibility(View.VISIBLE);
    }

    @OnClick(pl.collage.R.id.button_retry)
    public void onRetryClicked() {
        contentFragmentFriends.setVisibility(View.VISIBLE);
        layoutConnectionError.setVisibility(View.GONE);
        friendsPresenter.populateFriendsList();
    }

    @Override
    public void navigateToGalleryFragment() {
        homeActivity.navigateToGalleryFragment(1);
    }

    @Override
    public void postFriendSelectedEvent(FriendSelectedEvent friendSelectedEvent) {
        EventBus.getDefault().post(friendSelectedEvent);
    }

    @Override
    public void postFriendDeletionEvent(FriendDeletionEvent friendDeletionEvent) {
        EventBus.getDefault().post(friendDeletionEvent);
    }
}
