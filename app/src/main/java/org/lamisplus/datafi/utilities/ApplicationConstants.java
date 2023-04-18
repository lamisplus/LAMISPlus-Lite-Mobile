package org.lamisplus.datafi.utilities;

public class ApplicationConstants {

    public abstract static class LAMISPlusSharedPreferenceNames {
        public static final String SHARED_PREFERENCES_NAME = "shared_preferences";
    }

    public abstract static class API {
        public static final String REST_ENDPOINT = "/api/v1/";
    }

    public static final String EMPTY_STRING = "";
    public static final Integer RST_AGE = 15;
    public static final String SERVER_URL = "server_url";
    public static final String SYSTEM_ID = "system_id";
    public static final String SESSION_TOKEN = "session_id";
    public static final String AUTHORIZATION_TOKEN = "authorisation";
    public static final String LAST_LOGIN_SERVER_URL = "last_login_server_url";
    public static final String DEFAULT_LAMIS_PLUS_URL = "http://lamisplus.org:7090";
    public static String LAMIS_TAG = "LAMISPlus Mobile";
    public static final String LAST_SESSION_TOKEN = "last_session_id";
    public static final String DB_PASSWORD_LITERAL_PEPPER = "Lamis Sesame";
    public static final String DB_PASSWORD_BCRYPT_PEPPER = "$2a$08$iUp3M1VapYpjcAXQBNX6uu";
    public static final int DEFAULT_BCRYPT_ROUND = 8;

    public abstract static class UserKeys {
        public static final String USER_NAME = "username";
        public static final String PASSWORD = "password";
        public static final String HASHED_PASSWORD = "hashedPassword";
        public static final String USER_PERSON_NAME = "userDisplay";
        public static final String USER_UUID = "userUUID";
        public static final String LOGIN = "login";
    }

    public abstract static class BundleKeys {
        public static final String PATIENT_ID_BUNDLE = "patientID";
        public static final String PATIENT_QUERY_BUNDLE = "patientQuery";
    }

    public abstract static class Forms{
        public static final String RISK_STRATIFICATION_FORM = "Risk Stratification";
        public static final String CLIENT_INTAKE_FORM = "Client Intake Form";
        public static final String PRE_TEST_COUNSELING_FORM = "Pre Test Counseling Form";
        public static final String REQUEST_RESULT_FORM = "Request Result Form";
        public static final String POST_TEST_COUNSELING_FORM = "Post Test Counseling Form";
        public static final String HIV_RECENCY_FORM = "HIV Recency Form";
        public static final String ELICITATION = "Index Notification Services - Elicitation";
        public static final String ANC_FORM = "ANC Form";
        public static final String PMTCT_ENROLLMENT_FORM = "PMTCT Enrollment Form";
        public static final String LABOUR_DELIVERY_FORM = "Labour & Delivery Form";
        public static final String INFANT_INFORMATION_FORM = "Infant Information Form";
    }

}
