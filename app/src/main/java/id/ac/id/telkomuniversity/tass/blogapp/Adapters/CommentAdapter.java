package id.ac.id.telkomuniversity.tass.blogapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Comment;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import id.ac.id.telkomuniversity.tass.blogapp.Activities.BottomSheet;
import id.ac.id.telkomuniversity.tass.blogapp.Activities.PostDetailActivity;
import id.ac.id.telkomuniversity.tass.blogapp.Models.CommentPost;
import id.ac.id.telkomuniversity.tass.blogapp.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    Context mContext;
    List<CommentPost> mData;

    public CommentAdapter(Context mContext, List<CommentPost> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_comment, parent,false);
        return new CommentViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentViewHolder holder, int position) {
        Glide.with(mContext).load(mData.get(position).getImageUser()).into(holder.civFotoKomen);
        holder.tvNama.setText(mData.get(position).getUserName());
        holder.tvIsi.setText(mData.get(position).getIsiComment());
        holder.tvTanggal.setText(timestampToString((Long) mData.get(position).getTimestamp()));

        holder.delete_btn.setImageDrawable(mContext.getDrawable(R.drawable.ic_baseline_delete_24));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{

        CircleImageView civFotoKomen;
        TextView tvNama,tvIsi,tvTanggal;

        ImageButton delete_btn;

        public CommentViewHolder(@NonNull final View itemView) {
            super(itemView);
            civFotoKomen = itemView.findViewById(R.id.foto);
            tvNama = itemView.findViewById(R.id.nama);
            tvIsi = itemView.findViewById(R.id.isi);
            tvTanggal = itemView.findViewById(R.id.tanggal);

            delete_btn = itemView.findViewById(R.id.delete_btn_comment);

            delete_btn.setVisibility(View.INVISIBLE);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {


                    itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blueOnLong));
//                    civFotoKomen.setVisibility(View.INVISIBLE);
                    delete_btn.setVisibility(View.VISIBLE);

                    delete_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String postKey = mData.get(getAdapterPosition()).getPostKey();
                            String commentKey = mData.get(getAdapterPosition()).getCommentKey();

                            CommentPost commentPost = new CommentPost();
                            commentPost.setPostKey(postKey);
                            commentPost.setCommentKey(commentKey);

                            deleteCmnt(commentPost);

                        }
                    });

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                            delete_btn.setVisibility(View.INVISIBLE);

                        }
                    });
                    return true;
                }
            });
        }
    }

    public String timestampToString(long timestamp){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(timestamp);
        String date = android.text.format.DateFormat.format("hh:mm",calendar).toString();
        return date;
    }

    private void deleteCmnt(CommentPost commentPost){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Comments").child(commentPost.getPostKey()).child(commentPost.getCommentKey());
        ref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(mContext, "maskdmaskmda", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
