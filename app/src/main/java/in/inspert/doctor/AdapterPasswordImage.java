package in.inspert.doctor;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class AdapterPasswordImage extends RecyclerView.Adapter<AdapterPasswordImage.ImageViewHolder> {
    Context context;
    ArrayList<Integer> images;

    public AdapterPasswordImage(Context context, ArrayList<Integer> images) {
        this.context = context;
        this.images = images;
    }

    class ImageViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout relativeLayout;
        ImageView imageView;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.rrAdapterImage);
            imageView = itemView.findViewById(R.id.adapterImagePassword);
        }
    }

    interface OnItemClick{
        void onClick(int position);
    }

    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_image_password, viewGroup, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder imageViewHolder, int i) {
        imageViewHolder.relativeLayout.setBackgroundColor(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageViewHolder.imageView.setImageDrawable(context.getDrawable(images.get(i)));
        }

        imageViewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageViewHolder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorSelected));
                onItemClick.onClick(imageViewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return 9;
    }



}
