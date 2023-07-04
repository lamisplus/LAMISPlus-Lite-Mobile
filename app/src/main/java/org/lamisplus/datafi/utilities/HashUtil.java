package org.lamisplus.datafi.utilities;

import android.util.Base64;

import org.mindrot.jbcrypt.BCrypt;

public class HashUtil {
    public static String bcryptHash(byte[] template) {
        String encoded = Base64.encodeToString(template, Base64.NO_WRAP);
        return BCrypt.hashpw(encoded, "$2a$12$MklNDNgs4Agd50cSasj91O");
    }

}
