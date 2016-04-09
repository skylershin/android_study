package com.example.munkyushin.okhttpexample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.munkyushin.okhttpexample.dto.Doc;
import com.example.munkyushin.okhttpexample.dto.Repository;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by MunkyuShin on 3/12/16.
 */
public class TextViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.name)
    TextView mNameTextView;

    private Context mContext;
    private View mItemView;

    public static TextViewHolder create(Context context, ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.viewholder_repository, parent, false);
        TextViewHolder viewHolder = new TextViewHolder(context, itemView);
        return viewHolder;
    }

    private TextViewHolder(Context context, View itemView) {
        super(itemView);
        mContext = context;
        mItemView = itemView;
        ButterKnife.bind(this, itemView);
    }

    public void bind(Repository repository) {
        if (repository == null) {
            return;
        }

        String name = repository.getName();
        if (name == null || name.length() == 0) {
            return;
        }

        mNameTextView.setText(name);
    }

    public void bind(Doc doc) {
        if (doc == null) {
            return;
        }

        String url = doc.DocURL;
        if (url == null || url.length() == 0) {
            return;
        }

        mNameTextView.setText(url);
    }
}
