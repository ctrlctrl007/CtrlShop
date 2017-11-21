package com.ctrl.ctrlshopmall.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.ctrlshopmall.R;
import com.ctrl.ctrlshopmall.bean.Campaign;
import com.ctrl.ctrlshopmall.bean.HomeCampaign;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ctrlc on 2017/11/11.
 */

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {

    private List<HomeCampaign> homeCampaigns;
    private static int VIEW_TYPE_L = 0;
    private static int VIEW_TYPE_R = 1;

    private OnHomeCategoryOnClickListener mLinstener;

    private LayoutInflater mInflater;

    private Context mContext;

    public HomeCategoryAdapter(List<HomeCampaign> homeCampaigns,Context context) {
        this.homeCampaigns = homeCampaigns;
        mContext = context;
    }
    public void setHomeCampaignsListener(OnHomeCategoryOnClickListener listener){
        mLinstener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        if(viewType == VIEW_TYPE_L){
            return new ViewHolder(mInflater.inflate(R.layout.template_home_cardview,parent,false));
        }

        return new ViewHolder(mInflater.inflate(R.layout.template_home_cardview2,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HomeCampaign homeCampaign = homeCampaigns.get(position);
        holder.titleTxt.setText(homeCampaign.getTitle());
        Picasso.with(mContext).load(homeCampaign.getCpOne().getImgUrl()).into(holder.bigImageView);
        Picasso.with(mContext).load(homeCampaign.getCpTwo().getImgUrl()).into(holder.smallTopImageView);
        Picasso.with(mContext).load(homeCampaign.getCpThree().getImgUrl()).into(holder.smallBottomImageView);

    }


    @Override
    public int getItemViewType(int position) {
        if(position % 2 == 0){
            return VIEW_TYPE_L;
        }
        return VIEW_TYPE_R;
    }

    @Override
    public int getItemCount() {
        return homeCampaigns.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView titleTxt;
        private ImageView bigImageView;
        private ImageView smallTopImageView;
        private ImageView smallBottomImageView;


        public ViewHolder(View itemView) {
            super(itemView);
            titleTxt = (TextView) itemView.findViewById(R.id.text_title);
            bigImageView = (ImageView) itemView.findViewById(R.id.imgview_big);
            smallTopImageView = (ImageView) itemView.findViewById(R.id.imgview_small_top);
            smallBottomImageView = (ImageView) itemView.findViewById(R.id.imgview_small_bottom);
            bigImageView.setOnClickListener(this);
            smallTopImageView.setOnClickListener(this);
            smallBottomImageView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
          anim(view);

        }

        private void anim(final View v) {

            ObjectAnimator animator = ObjectAnimator.ofFloat(v, "rotationX", 0.0F, 360.0F)
                    .setDuration(200);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {

                    HomeCampaign campaign = homeCampaigns.get(getLayoutPosition());

                    switch (v.getId()) {

                        case R.id.imgview_big:
                            mLinstener.onClick(v, campaign.getCpOne());
                            break;

                        case R.id.imgview_small_top:
                            mLinstener.onClick(v, campaign.getCpTwo());
                            break;

                        case R.id.imgview_small_bottom:
                            mLinstener.onClick(v, campaign.getCpThree());
                            break;

                    }

                }
            });
            animator.start();
        }
    }


    public interface OnHomeCategoryOnClickListener{
        void  onClick(View view, Campaign campaign);
    }
}
