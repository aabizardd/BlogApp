package id.ac.id.telkomuniversity.tass.blogapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;

import id.ac.id.telkomuniversity.tass.blogapp.Activities.PostDetailActivity;
import id.ac.id.telkomuniversity.tass.blogapp.Models.Post;
import id.ac.id.telkomuniversity.tass.blogapp.R;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {


    Context mContext;
    List<Post> mData;

    public PostAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View row = LayoutInflater.from(mContext).inflate(R.layout.row_post_item, parent,false);


        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String uri = mData.get(position).getPicture();

        if(uri.contains("video")){

            holder.play.setVisibility(View.VISIBLE);

        }else{
            holder.play.setVisibility(View.INVISIBLE);
        }



        holder.tvTitle.setText(mData.get(position).getTitle());

        Glide.with(mContext).load(mData.get(position).getPicture()).into(holder.imgPost);
        Glide.with(mContext).load(mData.get(position).getUserPhoto()).into(holder.imgPostProfile);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{


        TextView tvTitle;
        ImageView imgPost, imgPostProfile, play;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.row_post_title);
            imgPost = itemView.findViewById(R.id.row_post_img);
            imgPostProfile = itemView.findViewById(R.id.row_post_profile_img);
            play = itemView.findViewById(R.id.play_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent postDetailActivity = new Intent(mContext, PostDetailActivity.class);
                    int position = getAdapterPosition();

                    postDetailActivity.putExtra("userId", mData.get(position).getUserId());

                    postDetailActivity.putExtra("postKey", mData.get(position).getPostKey());

                    postDetailActivity.putExtra("title", mData.get(position).getTitle());
                    postDetailActivity.putExtra("postImage", mData.get(position).getPicture());
                    postDetailActivity.putExtra("description", mData.get(position).getDescription());
                    postDetailActivity.putExtra("postKey", mData.get(position).getPostKey());
                    postDetailActivity.putExtra("userPhoto", mData.get(position).getUserPhoto());
                    postDetailActivity.putExtra("username", mData.get(position).getUsername());

                    postDetailActivity.putExtra("postDate", mData.get(position).getTimeStamp());



                    mContext.startActivity(postDetailActivity);


                }
            });
        }
    }
}


