package com.example.munkyushin.okhttpexample;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.recycler)
    RecyclerView mRecyclerView;

    private final OkHttpClient mClient = new OkHttpClient.Builder()
            .addNetworkInterceptor(new StethoInterceptor())
            .build();
    private final Gson mGson = new Gson();

    private RepositoryAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.title)
    void onClickTitle() {
        Request request = new Request.Builder()
                .url("https://api.github.com/users/skylershin/repos")
                .build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                Type listType = new TypeToken<List<Repository>>() {
                }.getType();

                final List<Repository> repositories = mGson.fromJson(response.body().charStream(), listType);

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            setRepositories(repositories);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void setRepositories(List<Repository> repositories) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
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
            ((RepositoryViewHolder) holder).bind(getItem(position));
        }

        @Override
        public int getItemCount() {
            return mRepositoryList.size();
        }
    }
}
