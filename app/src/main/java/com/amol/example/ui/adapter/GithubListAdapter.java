package com.amol.example.ui.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.amol.example.R;
import com.amol.example.data.local.entity.GithubEntity;
import com.amol.example.databinding.RepoListItemBinding;
import com.amol.example.ui.custom.recyclerview.RecyclerLayoutClickListener;
import com.amol.example.utils.AppUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GithubListAdapter extends RecyclerView.Adapter<GithubListAdapter.CustomViewHolder> implements Filterable {

    private Context context;
    private List<GithubEntity> items;
    private List<GithubEntity> filteredItems;
    private RecyclerLayoutClickListener listener;

    private String lastFilteredLanguage = "All";

    public GithubListAdapter(Context context, RecyclerLayoutClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.items = new ArrayList<>();
        this.filteredItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        RepoListItemBinding itemBinding = RepoListItemBinding.inflate(layoutInflater, parent, false);
        CustomViewHolder customItemViewHolder = new CustomViewHolder(itemBinding);

        itemBinding.cardView.setOnClickListener(v -> customItemViewHolder.onLayoutButtonClick());

        return customItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.bindTo(getItem(position));
    }

    public void setItems(List<GithubEntity> items) {
        this.filteredItems.addAll(items);
        getFilter().filter(lastFilteredLanguage);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public GithubEntity getItem(int position) {
        return items.get(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String language = charSequence.toString();
                lastFilteredLanguage = language;

                if (language.equalsIgnoreCase("All")) {
                    items = filteredItems;

                } else {
                    List<GithubEntity> list = new ArrayList<>();
                    for (GithubEntity githubEntity : filteredItems) {

                        if (language.equalsIgnoreCase(githubEntity.getLanguage())) {
                            list.add(githubEntity);
                        }
                    }
                    items = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = items;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                items = (List<GithubEntity>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    protected class CustomViewHolder extends RecyclerView.ViewHolder {

        private RepoListItemBinding binding;

        public CustomViewHolder(RepoListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindTo(GithubEntity githubEntity) {
            Picasso.get().load(githubEntity.getOwner().getAvatarUrl())
                    .placeholder(R.drawable.default_profile_icon_purple)
                    .into(binding.itemProfileImg);

            binding.itemTitle.setText(githubEntity.getFullName());
            binding.itemTime.setText(String.format(context.getString(R.string.item_date),
                    AppUtils.getDate(githubEntity.getCreatedAt()),
                    AppUtils.getTime(githubEntity.getCreatedAt())));

            binding.itemDesc.setText(githubEntity.getDescription());

            if (githubEntity.getLanguage() != null) {
                binding.itemImgLanguage.setVisibility(View.VISIBLE);
                binding.itemLikes.setVisibility(View.VISIBLE);
                binding.itemLikes.setText(githubEntity.getLanguage());

                GradientDrawable drawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.ic_circle);
                drawable.setColor(AppUtils.getColorByLanguage(context, githubEntity.getLanguage()));
                binding.itemImgLanguage.setImageDrawable(drawable);

            } else {
                binding.itemLikes.setVisibility(View.GONE);
                binding.itemImgLanguage.setVisibility(View.GONE);
            }

        }

        private void onLayoutButtonClick() {
            listener.redirectToDetailScreen(binding.itemProfileImg, binding.itemTitle, binding.itemImgLanguage, binding.itemLikes, getItem(getLayoutPosition()));
        }

    }
}
