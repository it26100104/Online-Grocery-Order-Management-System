package model;

    public class User {

        private String userId;
        private String name;
        private String email;
        private String password;
        private String phone;
        private String address;
        private String role;
        private String status;

        public User(String userId, String name, String email,
                    String password, String phone,
                    String address, String role, String status) {

            this.userId = userId;
            this.name = name;
            this.email = email;
            this.password = password;
            this.phone = phone;
            this.address = address;
            this.role = role;
            this.status = status;
        }

        public String getUserId() {
            return userId;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public String getPhone() {
            return phone;
        }

        public String getAddress() {
            return address;
        }

        public String getRole() {
            return role;
        }

        public String getStatus() {
            return status;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

