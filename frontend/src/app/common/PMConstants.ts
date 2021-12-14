export class PMConstants {
    // Auth module URI
    public static AUTHENTICATION_MODULE_BASE_URI = 'auth';
    public static SIGNUP_URI = 'signup';
    public static LOGIN_URI = 'login';

    // Project module URI
    public static PROJECT_MODULE_BASE_URI = 'project';

    //ENUMERATIONS
    public static USER_STORY_STATUS_CLOSED = 'CLOSED';
    public static USER_STORY_STATUS_OPENED = 'OPENED';

    public static USER_STORY_STATUS_IMPORTANCE_HIGHT = 'HIGHT';
    public static USER_STORY_STATUS_IMPORTANCE_NORMAL = 'NORMAL';
    public static USER_STORY_STATUS_IMPORTANCE_LOW = 'LOW';

    public static TASK_STATUS_TODO = 'TODO';
    public static TASK_STATUS_DOING = 'DOING';
    public static TASK_STATUS_DONE = 'DONE';

    // session keys
    public static SESSION_USER_ID_KEY = "USER_ID";
    public static SESSION_PROJECT_ID_KEY = "PROJECT_ID";
    public static SESSION_TOKEN_ID_KEY = "TOKEN";
}