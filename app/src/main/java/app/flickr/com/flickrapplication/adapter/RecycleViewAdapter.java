package app.flickr.com.flickrapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.Collections;
import java.util.List;

import app.flickr.com.flickrapplication.DetailActivity;
import app.flickr.com.flickrapplication.FlickrApplication;
import app.flickr.com.flickrapplication.R;
import app.flickr.com.flickrapplication.model.ImageContainer.Photos.Photo;

/**
 * Created by Guest1 on 4/20/2016.
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {
    List<Photo> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    private ImageLoader imageLoader;

    public RecycleViewAdapter(Context context, List<Photo> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        imageLoader= FlickrApplication.getMyApplication().getImageLoader();
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.image_list_row_item_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Photo photo = data.get(position);
        photo.setThumbURL();
        photo.setLargeURL();
        holder.title.setText(photo.getTitle());
        holder.imageView.setImageUrl(photo.getThumbURL(), imageLoader);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        NetworkImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = (NetworkImageView) itemView.findViewById(R.id.image_row_item_layout_image);
            title = (TextView) itemView.findViewById(R.id.image_row_item_layout_text);
        }

        @Override
        public void onClick(View view) {
            int position=getAdapterPosition();
            Photo photo = data.get(position);
            Intent intent=new Intent(context, DetailActivity.class);

            intent.putExtra(context.getString(R.string.str_item_name_extra), photo.getTitle());
            intent.putExtra(context.getString(R.string.str_item_image_url_extra),photo.getLargeURL());
            context.startActivity(intent);
        }
    }
}
