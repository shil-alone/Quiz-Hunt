package com.codershil.quizhunt.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codershil.quizhunt.R;
import com.codershil.quizhunt.models.User;
import com.codershil.quizhunt.databinding.RowLeaderboardsBinding;

import java.util.ArrayList;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.LeaderBoardViewHolder> {


    Context context ;
    ArrayList<User> users;
    public LeaderBoardAdapter(Context context, ArrayList<User> users){
        this.context = context;
        this.users = users;
    }


    @NonNull
    @Override
    public LeaderBoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_leaderboards,parent,false);
        return new LeaderBoardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderBoardViewHolder holder, int position) {
        User user = users.get(position);
        holder.binding.txtLeaderName.setText(user.getName());
        holder.binding.txtLeaderPoints.setText(String.valueOf(user.getCoins()));
        holder.binding.txtLeaderRank.setText(String.format("#%d",position+1));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class LeaderBoardViewHolder extends RecyclerView.ViewHolder{
        RowLeaderboardsBinding binding;
        public LeaderBoardViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowLeaderboardsBinding.bind(itemView);

        }
    }
}
