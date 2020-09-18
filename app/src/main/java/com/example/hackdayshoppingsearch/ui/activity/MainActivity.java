package com.example.hackdayshoppingsearch.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.hackdayshoppingsearch.ui.listener.MainActivityListener;
import com.example.hackdayshoppingsearch.common.SharedPreferenceManager;
import com.example.hackdayshoppingsearch.task.BaseAsyncTask;
import com.example.hackdayshoppingsearch.communication.network.NaverApi;
import com.example.hackdayshoppingsearch.communication.network.NaverApiClient;
import com.example.hackdayshoppingsearch.communication.network.OnLoginResponseListener;
import com.example.hackdayshoppingsearch.container.message.RetrieveUserProfileMessage;
import com.example.hackdayshoppingsearch.task.DeleteAndInsertSearchHistoryAsyncTask;
import com.example.hackdayshoppingsearch.container.QueryHistory;
import com.example.hackdayshoppingsearch.R;
import com.example.hackdayshoppingsearch.task.SelectSearchHistoryAsyncTask;
import com.example.hackdayshoppingsearch.container.LoginUser;
import com.example.hackdayshoppingsearch.container.UserProfile;
import com.example.hackdayshoppingsearch.ui.fragment.SearchResultFragment;
import com.example.hackdayshoppingsearch.container.ShoppingItem;
import com.example.hackdayshoppingsearch.ui.fragment.HomeFragment;
import com.google.android.material.navigation.NavigationView;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainActivityListener, OnLoginResponseListener, Callback<RetrieveUserProfileMessage> {
    private AppBarConfiguration appBarConfiguration;
    private ArrayAdapter<String> adapter;
    private OAuthLogin oAuthLogin;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.appbar_action, menu);
        setSearchAction(menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void initView() {
        setAppBar();
        setNavigationDrawer();
    }

    private void setAppBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setNavigationDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        setNavigationDrawerHeader();
    }

    private void setNavigationDrawerHeader() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        ImageView profileImage = view.findViewById(R.id.profile_image);
        TextView profileName = view.findViewById(R.id.profile_name);
        Button loginButton = view.findViewById(R.id.login_button);
        Button logoutButton = view.findViewById(R.id.logout_button);

        LoginUser loginUser = LoginUser.getInstance();
        UserProfile userProfile = loginUser.getUserProfile();
        Glide.with(this).load(R.drawable.users).into(profileImage);
        if (userProfile != null) {
            if(userProfile.getProfileImage() != null) {
                Glide.with(this).load(userProfile.getProfileImage()).into(profileImage);
            }
            String nameText = userProfile.getNickname() + " 님";
            profileName.setText(nameText);
        } else {
            String nameText = "로그인이 필요합니다.";
            profileName.setText(nameText);
        }


        if (LoginUser.getInstance().getAccessToken() == null) {
            loginButton.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.GONE);
        } else {
            loginButton.setVisibility(View.GONE);
            logoutButton.setVisibility(View.VISIBLE);
        }
    }

    private void setSearchAction(Menu menu) {
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        AutoCompleteTextView searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        adapter = new ArrayAdapter<>(this, R.layout.suggestion_item, new ArrayList<String>());
        setSearchHistory();
        searchAutoComplete.setAdapter(adapter);
        searchAutoComplete.setThreshold(0);
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectQuery = adapterView.getItemAtPosition(i).toString();
                submitQuery(selectQuery);
            }
        });
        searchView.setOnQueryTextListener(createQuerySubmitListener());
    }

    private void setSearchHistory() {
        UserProfile userProfile = LoginUser.getInstance().getUserProfile();
        if (userProfile != null) {
            SelectSearchHistoryAsyncTask asyncTask = new SelectSearchHistoryAsyncTask(this, userProfile.getId());
            asyncTask.setPostProcess(createSelectSearchHistoryTaskPostListener());
            asyncTask.execute();
        }
    }

    private BaseAsyncTask.PostProcess<List<QueryHistory>> createSelectSearchHistoryTaskPostListener() {
        return new BaseAsyncTask.PostProcess<List<QueryHistory>>() {
            @Override
            public void onPostProcess(List<QueryHistory> queryHistories) {
                List<String> histories = new ArrayList<>();
                for (QueryHistory queryHistory : queryHistories) {
                    histories.add(queryHistory.getQuery());
                }
                adapter.addAll(histories);
            }
        };
    }

    private SearchView.OnQueryTextListener createQuerySubmitListener() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                submitQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        };
    }

    private void submitQuery(String query) {
        Log.v("MainActivity", "submitQuery: " + query);
        saveHistory(query);
        searchView.setQuery(query, false);
        SearchResultFragment searchResultFragment = SearchResultFragment.newInstance(query);
        replaceFragment(searchResultFragment);
    }

    private void saveHistory(String query) {
        UserProfile userProfile = LoginUser.getInstance().getUserProfile();
        if (userProfile != null) {
            adapter.remove(query);
            adapter.insert(query, 0);
            QueryHistory history = new QueryHistory(userProfile.getId(), query, new Date().getTime());
            DeleteAndInsertSearchHistoryAsyncTask asyncTask = new DeleteAndInsertSearchHistoryAsyncTask(this, history);
            asyncTask.execute();
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content_frame, fragment).addToBackStack(null).commit();
    }


    public void onLogin(View view) {
        oAuthLogin = OAuthLogin.getInstance();
        oAuthLogin.init(
                this
                ,getString(R.string.client_id)
                ,getString(R.string.client_secret)
                ,"Hackday Shopping Search"
        );
        oAuthLogin.startOauthLoginActivity(this, new NaverLoginHandler(this));
    }

    public void onLogout(View view) {
        SharedPreferenceManager.removeKey(this, "refreshToken");
        LoginUser.clear();
        adapter.clear();
        setNavigationDrawerHeader();
    }

    @Override
    public void onLoginSuccess() {
        Context context = this;
        String refreshToken = oAuthLogin.getRefreshToken(context);
        SharedPreferenceManager.setString(context, "refreshToken", refreshToken);
        LoginUser loginUser = LoginUser.getInstance();
        loginUser.importFrom(oAuthLogin, context);
        requestUserProfile();
    }

    private void requestUserProfile() {
        final NaverApi naverApi = NaverApiClient.getNaverApiClient();
        LoginUser loginUser = LoginUser.getInstance();
        naverApi.retrieveUserProfile(loginUser.getAuthorization()).enqueue(this);
    }

    @Override
    public void onLoginFail() {
        Context context = this;
        String errorCode = oAuthLogin.getLastErrorCode(context).getCode();
        String errorDesc = oAuthLogin.getLastErrorDesc(context);
        Log.e("Error in Login", "errorCode:" + errorCode + ", errorDesc:" + errorDesc);
    }

    @Override
    public void onResponse(Call<RetrieveUserProfileMessage> call, Response<RetrieveUserProfileMessage> response) {
        if(response.isSuccessful()) {
            onRetrieveUserProfileSuccess(response);
        } else {
            onRetrieveUserProfileError(response);
        }
    }

    @Override
    public void onFailure(Call<RetrieveUserProfileMessage> call, Throwable t) {

    }

    private void onRetrieveUserProfileSuccess(Response<RetrieveUserProfileMessage> response) {
        RetrieveUserProfileMessage retrieveUserProfileMessage = response.body();
        if(retrieveUserProfileMessage == null) {
            return;
        }
        UserProfile userProfile = retrieveUserProfileMessage.getUserProfile();
        LoginUser loginUser = LoginUser.getInstance();
        loginUser.setUserProfile(userProfile);
        setNavigationDrawerHeader();

        Fragment fragment = getSupportFragmentManager().getPrimaryNavigationFragment().getChildFragmentManager().getPrimaryNavigationFragment();
        if(fragment instanceof HomeFragment) {
            ((HomeFragment) fragment).showPopularTrendList();
        }
        setSearchHistory();
    }

    private void onRetrieveUserProfileError(Response<RetrieveUserProfileMessage> response) {

    }

    @Override
    public void onShoppingItemClicked(ShoppingItem item) {
        Log.v("click", item.toString());
        Intent intent = new Intent(this, ShoppingItemDetailActivity.class);
        intent.putExtra("item", item);
        startActivity(intent);
    }

    @Override
    public void onTrendItemClicked(String item) {
        submitQuery(item);
    }

    private static class NaverLoginHandler extends OAuthLoginHandler {
        private OnLoginResponseListener listener;
        NaverLoginHandler(OnLoginResponseListener listener) {
            this.listener = listener;
        }
        @Override
        public void run(boolean success) {
            if (success) {
                listener.onLoginSuccess();
            } else {
                listener.onLoginFail();
            }
        }
    }
}