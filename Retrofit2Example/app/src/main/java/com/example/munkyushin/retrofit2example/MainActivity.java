package com.example.munkyushin.retrofit2example;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.recycler)
    RecyclerView mRecyclerView;

    private RepositoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        request();
    }

    private void request() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GithubService service = retrofit.create(GithubService.class);
        Call<List<Repository>> repositoryCall = service.listRepos("skylershin");

        repositoryCall.enqueue(new Callback<List<Repository>>() {
            @Override
            public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {
                List<Repository> repositories = response.body();

                setRepositories(repositories);
            }

            @Override
            public void onFailure(Call<List<Repository>> call, Throwable t) {

            }
        });
    }

    private void setRepositories(List<Repository> repositories) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new RepositoryAdapter(getApplicationContext(), repositories);
        mRecyclerView.setAdapter(mAdapter);
    }

    private class RepositoryAdapter extends RecyclerView.Adapter {
        private List<Repository> mRepositoryList;
        private Context mContext;

        public RepositoryAdapter(Context context, List<Repository> repositoryList) {
            mContext = context;
            mRepositoryList = repositoryList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return RepositoryViewHolder.create(mContext, parent);
        }

        public Repository getItem(int position) {
            return mRepositoryList.get(position);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((RepositoryViewHolder)holder).bind(getItem(position));
        }

        @Override
        public int getItemCount() {
            return mRepositoryList.size();
        }
    }
}
