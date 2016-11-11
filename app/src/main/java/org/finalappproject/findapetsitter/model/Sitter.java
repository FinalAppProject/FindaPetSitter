package org.finalappproject.findapetsitter.model;

/**
 * Created by mkhade on 11/9/2016.
 */

public class Sitter {

    private String firstName;
    private String lastName;
    private String tagLine;
    private int review_count;
    private String ratings;
    private String profilepic_url;

    public String getAmount_charged() {
        return amount_charged;
    }

    public void setAmount_charged(String amount_charged) {
        this.amount_charged = amount_charged;
    }

    private String amount_charged;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTagLine() {
        return tagLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }

    public int getReview_count() {
        return review_count;
    }

    public void setReview_count(int review_count) {
        this.review_count = review_count;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getProfilepic_url() {
        return profilepic_url;
    }

    public void setProfilepic_url(String profilepic_url) {
        this.profilepic_url = profilepic_url;
    }
}
