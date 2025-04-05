package net.engineeringdigest.CampusApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class UserProfileDTO {
        private String pfpurl;
        private String fullName;
        private String enrollmentno;
        private String email;
        private String github;
        private String linkedin;
    }

