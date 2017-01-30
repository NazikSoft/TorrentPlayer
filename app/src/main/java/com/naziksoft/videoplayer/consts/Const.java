package com.naziksoft.videoplayer.consts;

public class Const {


    // MainActivity
    public static final int REQUEST_GET_FILE_PATH = 1;
    public static final int REQUEST_GET_USER_EMAIL = 3;

    // FileChooser
    public static final String EXTRA_FILE_PATH = "extra file path";
    public static final int REQUEST_FILE_SELECT = 2;

    // AuthActivity
    public static final int RC_SIGN_IN = 9001;
    public static final String EXTRA_USER_EMAIL = "extra user email";

    // DB
    public static final String DATABASE_NAME = "VideoDB.db";
    public static final String TABLE_NAME = "VideoTable";
    public static int DATABASE_VERSION = 1;


    // Global
    public static final String TAG = "My LOG";
    public static final String EXTRA_PLAY_PATH = "extra play path";
    public static final String SHARED_PREF_USER = "shared user";
}
