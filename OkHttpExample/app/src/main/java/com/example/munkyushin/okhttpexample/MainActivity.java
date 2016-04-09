package com.example.munkyushin.okhttpexample;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.munkyushin.okhttpexample.dto.Bank;
import com.example.munkyushin.okhttpexample.dto.Doc;
import com.example.munkyushin.okhttpexample.dto.Repository;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.recycler)
    RecyclerView mRecyclerView;

    private final OkHttpClient mClient = new OkHttpClient.Builder()
            .addNetworkInterceptor(new StethoInterceptor())
            .build();
    private final Gson mGson = new Gson();
    private final JsonUtil mJsonUtil = new JsonUtil();

    private RepositoryAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.request_remote)
    void onClickRemoteButton() {

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

                mJsonUtil.readValue(response.body().string(), new TypeReference<List<Repository>>() {
                }).subscribeOn(Schedulers.io())
                        .doOnSuccess(new Action1<List<Repository>>() {
                            @Override
                            public void call(List<Repository> repositories) {
                                System.out.println("doOnSuccess received "
                                        + repositories + " on "
                                        + Thread.currentThread().getName());
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<List<Repository>>() {
                            @Override
                            public void call(List<Repository> repositories) {
                                setRepositories(repositories);
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        });
            }
        });
    }

    @OnClick(R.id.request_local)
    void onClickLocalButton() {
        InputStream inputStream = getResources().openRawResource(R.raw.test);
        mJsonUtil.readValue(inputStream, Bank.class)
                .subscribeOn(Schedulers.newThread())
                .doOnSuccess(new Action1<Bank>() {
                    @Override
                    public void call(Bank bank) {
                        System.out.println("doOnSuccess received "
                                + bank + " on "
                                + Thread.currentThread().getName());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bank>() {
                    @Override
                    public void call(Bank bank) {
                        System.out.println("Subscriber received "
                                + bank + " on "
                                + Thread.currentThread().getName());

                        setProjectDocs(bank.getProjectDocs());

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        System.out.println("err"
                                + throwable + " on "
                                + Thread.currentThread().getName());
                    }
                });
    }

    private void setRepositories(List<Repository> repositories) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mAdapter = new RepositoryAdapter(getApplicationContext(), repositories);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setProjectDocs(List<Doc> docs) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        mRecyclerView.setAdapter(new ProjectDocsAdapter(getApplicationContext(), docs));
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
            return TextViewHolder.create(mContext, parent);
        }

        public Repository getItem(int position) {
            return mRepositoryList.get(position);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((TextViewHolder) holder).bind(getItem(position));
        }

        @Override
        public int getItemCount() {
            return mRepositoryList.size();
        }
    }

    private class ProjectDocsAdapter extends RecyclerView.Adapter {
        private List<Doc> mDocs;
        private Context mContext;

        public ProjectDocsAdapter(Context context, List<Doc> docs) {
            mContext = context;
            mDocs = docs;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return TextViewHolder.create(mContext, parent);
        }

        public Doc getItem(int position) {
            return mDocs.get(position);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((TextViewHolder) holder).bind(getItem(position));
        }

        @Override
        public int getItemCount() {
            return mDocs.size();
        }
    }

}
