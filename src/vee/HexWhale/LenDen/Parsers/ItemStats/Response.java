
package vee.HexWhale.LenDen.Parsers.ItemStats;

public class Response {
    private Number favorite_items_count;
    private Number liked_items_count;
    private Number listed_items_count;

    public Number getFavorite_items_count() {
        return this.favorite_items_count;
    }

    public void setFavorite_items_count(Number favorite_items_count) {
        this.favorite_items_count = favorite_items_count;
    }

    public Number getLiked_items_count() {
        return this.liked_items_count;
    }

    public void setLiked_items_count(Number liked_items_count) {
        this.liked_items_count = liked_items_count;
    }

    public Number getListed_items_count() {
        return this.listed_items_count;
    }

    public void setListed_items_count(Number listed_items_count) {
        this.listed_items_count = listed_items_count;
    }
}
