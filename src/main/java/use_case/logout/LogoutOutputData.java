package use_case.logout;

/**
 * Output Data for the Logout Use Case.
 */
public class LogoutOutputData {

    private String name;
    private boolean useCaseFailed;

    public LogoutOutputData(String name, boolean useCaseFailed) {
        this.name = name;
        this.useCaseFailed = useCaseFailed;
    }

    public String getUsername() {
        return name;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}
