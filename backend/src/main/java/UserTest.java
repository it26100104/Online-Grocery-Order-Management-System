import model.User;
import service.UserService;

public class UserTest {

    public static void main(String[] args) {

        UserService service = new UserService();

        User user = new User(
                "U002",
                "Nimal",
                "nimal@gmail.com",
                "1234",
                "0771234567",
                "Galle",
                "CUSTOMER",
                "ACTIVE"
        );

        // REGISTER
        boolean registered = service.registerUser(user);
        System.out.println(
                registered ? "Registration successful!"
                        : "Registration failed!"
        );

        // LOGIN
        User loggedIn = service.login(
                "nimal@gmail.com", "1234"
        );
        System.out.println(
                loggedIn != null ? "Login successful!"
                        : "Login failed!"
        );

        // UPDATE PROFILE
        boolean updated = service.updateUserProfile(
                "nimal@gmail.com",
                "0711111111",
                "Colombo"
        );
        System.out.println(
                updated ? "Profile update successful!"
                        : "Profile update failed!"
        );

        // DEACTIVATE ACCOUNT
        boolean deactivated = service.deactivateUser(
                "nimal@gmail.com"
        );
        System.out.println(
                deactivated ? "Account deactivated successfully!"
                        : "Account deactivation failed!"
        );

        // LOGIN AFTER DEACTIVATION
        User loginAgain = service.login(
                "nimal@gmail.com", "1234"
        );
        System.out.println(
                loginAgain == null
                        ? "Login blocked: Account is inactive."
                        : "Login successful!"
        );
    }
}