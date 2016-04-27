package com.group28.cs160.shared;

/**
 * Created by eviltwin on 4/20/16.
 */
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CenteredImageFragment extends Fragment {
    private OnClickListener listener;
    private String description;
    private int itemRes;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_centered_image, container, false);

        ImageView item = (ImageView) fragmentView.findViewById(R.id.item);
        item.setBackgroundResource(itemRes);

        TextView text = (TextView) fragmentView.findViewById(R.id.item_text);
        text.setText(description);

        if (listener != null) {
            fragmentView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View view) {
                        listener.onClick(view);
                }

            });
        }
        return fragmentView;
    }

    public void setImage(int i) {
        itemRes = i;
    }

    public void setDescription(String d) {
        description = d;
    }

    public void setOnClickListener(final OnClickListener listener) {
        this.listener = listener;
    }
}