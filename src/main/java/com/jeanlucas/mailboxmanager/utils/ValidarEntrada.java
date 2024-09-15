package com.jeanlucas.mailboxmanager.utils;

import java.util.regex.Pattern;

public class ValidarEntrada {

    public static boolean isValidEmail(String name) {
        Pattern pattern = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
        return pattern.matcher(name).matches();
    }

    public static boolean isValidFolderName(String name) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9_-]{1,100}");
        return pattern.matcher(name).matches();
    }
}
