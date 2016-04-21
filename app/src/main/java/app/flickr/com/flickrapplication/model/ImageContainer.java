package app.flickr.com.flickrapplication.model;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Guest1 on 4/20/2016.
 */
public class ImageContainer {

    @SerializedName("photos")
    Photos photos;

    public Photos getPhotos() {
        return photos;
    }

    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    public class Photos{
        @SerializedName("photo")
        private Photo[] photoList;

        public Photo[] getPhotoList() {
            return photoList;
        }

        public void setPhotoList(Photo[] photoList) {
            this.photoList = photoList;
        }

        public class Photo{
            @SerializedName("id")
            private String id;
            private int position;
            private String thumbURL;
            private Bitmap thumb;
            private String largeURL;

            @SerializedName("owner")
            private String owner;

            @SerializedName("secret")
            private String secret;

            @SerializedName("server")
            private String server;

            @SerializedName("farm")
            private String farm;

            @SerializedName("title")
            private String title;

            final static int PHOTO_THUMB=1;
            final static int PHOTO_LARGE=2;


            public String getThumbURL() {
                return thumbURL;
            }

            public void setThumbURL() {
                thumbURL=createPhotoURL(PHOTO_THUMB);
            }

            public String getLargeURL() {
                return largeURL;
            }

            public void setLargeURL() {
                largeURL=createPhotoURL(PHOTO_LARGE);
            }

            @Override
            public String toString() {
                return "ImageContener [id=" + id + ", thumbURL=" + thumbURL + ", largeURL=" + largeURL + ", owner=" + owner + ", secret=" + secret + ", server=" + server + ", farm="
                        + farm + "]";
            }

            private String createPhotoURL(int photoType) {
                String tmp = null;
                tmp = "http://farm" + farm + ".staticflickr.com/" + server + "/" + id + "_" + secret;// +".jpg";
                switch (photoType) {
                    case PHOTO_THUMB:
                        tmp += "_t";
                        break;
                    case PHOTO_LARGE:
                        tmp += "_z";
                        break;

                }
                tmp += ".jpg";
                return tmp;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getPosition() {
                return position;
            }

            public void setPosition(int position) {
                this.position = position;
            }

            public Bitmap getThumb() {
                return thumb;
            }

            public void setThumb(Bitmap thumb) {
                this.thumb = thumb;
            }

            public String getOwner() {
                return owner;
            }

            public void setOwner(String owner) {
                this.owner = owner;
            }

            public String getSecret() {
                return secret;
            }

            public void setSecret(String secret) {
                this.secret = secret;
            }

            public String getServer() {
                return server;
            }

            public void setServer(String server) {
                this.server = server;
            }

            public String getFarm() {
                return farm;
            }

            public void setFarm(String farm) {
                this.farm = farm;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

    }
}
