package org.finalappproject.findapetsitter.model;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;

import static android.R.attr.rating;

@ParseClassName("Review")
public class Review extends ParseObject {

    private static final String LOG_TAG = "Review";

    private static final String KEY_PET_TYPE = "pet_type";
    private static final String KEY_REVIEW = "review";
    private static final String KEY_REVIEWER = "reviewer";
    private static final String KEY_REVIEW_RECEIVER = "receiver";
    private static final String KEY_RATING = "rating";

    public PetType getType() {
        String type = getString(KEY_PET_TYPE);
        if (type != null) {
            return PetType.valueOf(type);
        }
        return null;
    }

    public void setType(PetType type) {
        if (type != null) {
            put(KEY_PET_TYPE, type.name());
        }
    }

    public String getReview() {
        return getString(KEY_REVIEW);
    }

    public void setReview(String review) {
        put(KEY_REVIEW, review);
    }

    public User getReviewer() {
        return (User) getParseUser(KEY_REVIEWER);
    }

    public void setReviewer(User sender) {
        put(KEY_REVIEWER, sender);
    }

    public User getReviewRecevier() {
        return (User) getParseUser(KEY_REVIEW_RECEIVER);
    }

    public void setReviewReceiver(User receiver) {
        put(KEY_REVIEW_RECEIVER, receiver);
    }

    public double getRating() {
        return getDouble(KEY_RATING);
    }

    public void setRating(double rating) {
        put(KEY_RATING, rating);
    }

    @Override
    public String toString() {
        return "REVIEW: PetType: " + this.getType() + "\nReview : " + this.getReview() + "\nRating : " + this.getRating();
    }

    public static void queryReviews(String requestId, GetCallback<org.finalappproject.findapetsitter.model.Review> findCallback) {
        // TODO verify/validate cache policy
        ParseQuery<org.finalappproject.findapetsitter.model.Review> reviewQuery = ParseQuery.getQuery(org.finalappproject.findapetsitter.model.Review.class);
        reviewQuery.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        reviewQuery.getInBackground(requestId, findCallback);
    }

    public static void queryByReviewReceiver(ParseUser user, FindCallback<org.finalappproject.findapetsitter.model.Review> findCallback) {
        ParseQuery<org.finalappproject.findapetsitter.model.Review> reviewQuery = ParseQuery.getQuery(org.finalappproject.findapetsitter.model.Review.class).whereEqualTo(KEY_REVIEW_RECEIVER, user);
        reviewQuery.findInBackground(findCallback);
    }

    public static void queryBySender(ParseUser user, FindCallback<org.finalappproject.findapetsitter.model.Review> findCallback) {
        ParseQuery<org.finalappproject.findapetsitter.model.Review> reviewQuery = ParseQuery.getQuery(org.finalappproject.findapetsitter.model.Review.class).whereEqualTo(KEY_REVIEWER, user);
        reviewQuery.findInBackground(findCallback);
    }
}