    package com.entity;

    import com.googlecode.objectify.annotation.Entity;
    import com.googlecode.objectify.annotation.Id;
    import com.googlecode.objectify.annotation.Index;
    import com.googlecode.objectify.annotation.Unindex;

    import java.util.UUID;

    import static com.googlecode.objectify.ObjectifyService.ofy;


    @Entity
    public class AdminCredential {
        @Index
        private String tokenKey;
        @Unindex
        private String secretToken;
        @Id
        private String userId;
        @Index
        private long createdTime;
        @Index
        private long expiredTime;
        @Index
        private int status; //1-active, 2-dead.

        public AdminCredential() {
        }

        public AdminCredential(String userId) {
            this.tokenKey = UUID.randomUUID().toString();
            this.secretToken = this.tokenKey;
            this.userId = userId;
            this.createdTime = System.currentTimeMillis();
            this.expiredTime = this.createdTime+86400*1000;
            this.status = 1;
        }

        public String getTokenKey() {
            return tokenKey;
        }

        public void setTokenKey(String tokenKey) {
            this.tokenKey = tokenKey;
            this.secretToken = tokenKey;
        }

        public String getSecretToken() {
            return secretToken;
        }

        public void setSecretToken(String secretToken) {
            this.secretToken = secretToken;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public long getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(long createdTime) {
            this.createdTime = createdTime;
        }

        public long getExpiredTime() {
            return expiredTime;
        }

        public void setExpiredTime(long expiredTime) {
            this.expiredTime = expiredTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }


        public static AdminCredential loadCredential(String secretToken){
            if(secretToken==null){
                return null;
            }
            AdminCredential adminCredential = ofy().load().type(AdminCredential.class).filter("tokenKey",secretToken).first().now();
            if (adminCredential ==null){
                return null;
            }
            if (adminCredential.getExpiredTime()<System.currentTimeMillis()){
                adminCredential.setStatus(0);
                ofy().save().entity(adminCredential).now();
                return null;
            }
            return adminCredential;
        }
    }
