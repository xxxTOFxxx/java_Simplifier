
package simplifier;

import java.util.Scanner;


/**
 *
 * @author Tiago
 */
// Abstract class for user
abstract class User {
    private String fullName;
    private String email;
    private String password;

    public User(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void showUserMenu(Scanner scanner1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
