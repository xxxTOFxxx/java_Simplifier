
package simplifier;

import java.util.Scanner;


/**
 *
 * @author Tiago
 */
// Abstract class for user
abstract class User {
    private int id;
    private String fullName;
    private String email;
    private String password;
    private String type;

    public User(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.type = type;
    }

    public String getType() {
        return type;
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

    public abstract void showUserMenu(Scanner scanner);
}
