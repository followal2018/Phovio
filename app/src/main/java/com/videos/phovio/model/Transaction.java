package com.videos.phovio.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Transaction {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("label")
        @Expose
        private String label;
        @SerializedName("points")
        @Expose
        private String points;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("created")
        @Expose
        private String created;
        @SerializedName("enabled")
        @Expose
        private Boolean enabled;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public void setPoints(String points) {
            this.points = points;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getLabel() {
            return label;
        }

        public String getPoints() {
            return points;
        }

        public String getAmount() {
            return amount;
        }

        public String getCreated() {
                return created;
            }

        public void setCreated(String created) {
            this.created = created;
        }

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }
}
