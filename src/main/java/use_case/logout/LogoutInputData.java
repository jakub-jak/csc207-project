package use_case.logout;

/**
 * The Input Data for the Logout Use Case.
 */
public class LogoutInputData {

    private final String name;

    public LogoutInputData(String name) {this.name = name;}

    public String getName() {
        return this.name;
    }
}
