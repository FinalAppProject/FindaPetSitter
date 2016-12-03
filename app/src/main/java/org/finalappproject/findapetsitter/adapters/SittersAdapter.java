package org.finalappproject.findapetsitter.adapters;

public class SittersAdapter { /*extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    /*

    private static List<User> petSitters;
    private static Context mContext;

    private Context getContext() {
        return mContext;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvItemName)
        TextView tvItemFirstName;
        @BindView(R.id.ivItemProfileImage)
        ImageView ivItemProfilePic;
        @BindView(R.id.tvItemTagline)
        TextView tvTagline;

        //@BindView(R.id.tvNumReviews)
        //TextView tvNumReviews;

        @BindView(R.id.tvDistance)
        TextView tvDistance;
        @BindView(R.id.RlSitterItem)
        RelativeLayout RlSitterItem;

        public UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_sitter, parent, false);
        RecyclerView.ViewHolder viewHolder = new UserViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        UserViewHolder vh = (UserViewHolder) holder;
        final User sitter = mAvailableSittersWithDistance.get(position).user;
        vh.tvItemFirstName.setText(sitter.getFullName());
        vh.tvTagline.setText(sitter.getDescription());

        ImageHelper.loadImage(mContext, sitter.getProfileImage(), R.drawable.cat, vh.ivItemProfilePic);
        //vh.tvNumReviews.setText("45");
        double distanceVal = Math.round( mAvailableSittersWithDistance.get(position).distance * 100.0 ) / 100.0;
        if(distanceVal != 0) {
            vh.tvDistance.setText(distanceVal + " miles");
        } else {
            vh.tvDistance.setVisibility(View.INVISIBLE);
        }

        vh.RlSitterItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userProfileIntent = new Intent(getContext(), UserProfileActivity.class);
                userProfileIntent.putExtra(EXTRA_USER_OBJECT_ID, sitter.getObjectId());
                getContext().startActivity(userProfileIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mAvailableSittersWithDistance.size();
    }

    public SittersAdapter(Context context, List<User> petSitters) {
        this.mAvailableSittersWithDistance = availablSittersWithDistance;
        mContext = context;
    }
    */
}