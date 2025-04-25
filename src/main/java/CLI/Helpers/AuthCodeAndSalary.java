package CLI.Helpers;

public class AuthCodeAndSalary {
    public String authCode;
    public Double salary;

    public AuthCodeAndSalary(String authCode, Double salary) {
        this.authCode = authCode;
        this.salary = salary;
    }

    public static AuthCodeAndSalary of(String authCode, Double salary) {
        return new AuthCodeAndSalary(authCode, salary);
    }
}
