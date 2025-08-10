package tvz.ntpr.api.config;

public class Urls {
    public static final String URL_COURSE = "/courses";
    public static final String URL_PROFESSOR_ID = "/professor/{professorId}";

    public static final String URL_GRADE = "/grades";
    public static final String URL_STUDENT_FILTER = "/student";
    public static final String URL_COURSE_FILTER = "/course";
    public static final String URL_STUDENT_ID = URL_STUDENT_FILTER + "/{studentId}";
    public static final String URL_COURSE_ID = URL_COURSE_FILTER + "/{courseId}";

    public static final String URL_PROFESSOR = "/professors";
    public static final String URL_UNAUTHORIZED = "/unauthorized";
    public static final String URL_AUTHORIZE = "/authorize";

    public static final String URL_REPORT = "/reports";


    public static final String URL_STUDENT = "/students";

    public static final String URL_USER = "/users";
    public static final String URL_USERNAME = "/username";
    public static final String URL_USERNAME_VALUE = URL_USERNAME + "/{username}";

    public static final String URL_AUTH = "/auth";
    public static final String URL_SIGNUP = "/signup";
    public static final String URL_REFRESH = "/refresh";

    public static final String URL_CRON = "/cron";

    public static final String URL_ID = "/{id}";
    public static final String URL_WILDCARD = "/**";

    public static class Headers {
        public static final String X_API_KEY = "X-API-KEY";
    }
}
