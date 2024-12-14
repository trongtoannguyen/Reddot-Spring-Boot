package com.reddot.app.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class UserProfileDTO {
    private Integer id;
    private String username;
    private String email;
    private String avatar;
    @Size(min = 3, max = 100)
    private String displayName;
    private String aboutMe;
    private LocalDate dob;
    private String location;
    private String websiteUrl;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer id;
        private String username;
        private String email;
        private String avatar;
        private String displayName;
        private String aboutMe;
        private LocalDate dob;
        private String location;
        private String websiteUrl;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }


        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder avatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder aboutMe(String aboutMe) {
            this.aboutMe = aboutMe;
            return this;
        }

        public Builder dob(LocalDate dob) {
            this.dob = dob;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder websiteUrl(String websiteUrl) {
            this.websiteUrl = websiteUrl;
            return this;
        }

        public UserProfileDTO build() {
            UserProfileDTO dto = new UserProfileDTO();
            dto.id = this.id;
            dto.username = this.username;
            dto.email = this.email;
            dto.avatar = this.avatar;
            dto.displayName = this.displayName;
            dto.aboutMe = this.aboutMe;
            dto.dob = this.dob;
            dto.location = this.location;
            dto.websiteUrl = this.websiteUrl;
            return dto;
        }
    }

    public static class Builder{
        private Integer id;
        private String username;
        private String email;
        private String avatar;
        private String displayName;
        private String aboutMe;
        private LocalDate dob;
        private String location;
        private String websiteUrl;

        public Builder id(Integer id){
            this.id = id;
            return this;
        }

        public Builder username(String username){
            this.username = username;
            return this;
        }


        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder avatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder aboutMe(String aboutMe) {
            this.aboutMe = aboutMe;
            return this;
        }

        public Builder dob(LocalDate dob) {
            this.dob = dob;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder websiteUrl(String websiteUrl) {
            this.websiteUrl = websiteUrl;
            return this;
        }

        public UserProfileDTO build(){
            UserProfileDTO dto = new UserProfileDTO();
            dto.id = this.id;
            dto.username = this.username;
            dto.email = this.email;
            dto.avatar = this.avatar;
            dto.displayName = this.displayName;
            dto.aboutMe = this.aboutMe;
            dto.dob = this.dob;
            dto.location = this.location;
            dto.websiteUrl = this.websiteUrl;
            return dto;

        }
    }
}
