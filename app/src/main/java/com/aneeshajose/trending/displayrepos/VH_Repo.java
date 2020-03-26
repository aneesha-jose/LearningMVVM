package com.aneeshajose.trending.displayrepos;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import com.aneeshajose.genericadapters.model.AbstractBetterViewHolder;
import com.aneeshajose.trending.R;
import com.aneeshajose.trending.models.Repo;
import com.aneeshajose.trending.utility.ActivityUtilsKt;
import com.bumptech.glide.RequestManager;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Aneesha Jose on 2020-03-25.
 */
public class VH_Repo extends AbstractBetterViewHolder<Repo> {

    @BindView(R.id.ivProfile)
    ImageView ivProfile;

    @BindView(R.id.tvItemAuthor)
    TextView tvItemAuthor;

    @BindView(R.id.tvItemHeader)
    TextView tvItemHeader;

    @BindView(R.id.rlExpandedView)
    RelativeLayout rlExpandedView;

    @BindView(R.id.tvItemDesc)
    TextView tvItemDesc;

    @BindView(R.id.tvLanguage)
    TextView tvLanguage;

    @BindView(R.id.tvStars)
    TextView tvStars;

    @BindView(R.id.tvForks)
    TextView tvForks;

    private RequestManager glide;
    static final int LAYOUT = R.layout.item_repo;

    VH_Repo(View view, RequestManager glide) {
        super(view);
        this.glide = glide;
        ButterKnife.bind(this, view);
    }

    @Override
    public void bind(Repo element, int position) {
        if (!TextUtils.isEmpty(element.getAvatar())) {
            glide.load(element.getAvatar())
                    .fitCenter()
                    .into(ivProfile);
        }
        tvItemAuthor.setText(element.getAuthor());
        tvItemHeader.setText(element.getName());
        tvItemDesc.setText(element.getDescription());
        tvStars.setText(String.valueOf(element.getStars()));
        tvForks.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(element.getForks()));

        tvLanguage.setText(element.getLanguage());
        if (!TextUtils.isEmpty(element.getLanguageColor())) {
            Drawable drawable = ActivityUtilsKt.setColourForDrawable(itemView.getContext(), R.drawable.filled_circle, Color.parseColor(element.getLanguageColor()));
            if (drawable != null)
                tvLanguage.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }
    }

    void setSelectionIndicator(boolean isSelected) {
        ViewCompat.setElevation(itemView, isSelected ? 2 : 0);
        rlExpandedView.setVisibility(isSelected ? View.VISIBLE : View.GONE);
    }
}
