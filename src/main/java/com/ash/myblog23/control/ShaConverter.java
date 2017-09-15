package com.ash.myblog23.control;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stlevkov
 */
public class ShaConverter {

    private static final Logger LOG = Logger.getLogger(ShaConverter.class.getName());

    private ShaConverter() {
        // Hide
    }

    public static String generateHash(String input) {
        StringBuilder hash = new StringBuilder();
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = sha.digest(input.getBytes());
            for (byte b : hashedBytes) {
                hash.append(String.format("%02x", b));
            }

        } catch (NoSuchAlgorithmException nsae) {
            LOG.log(Level.SEVERE, "Error on generate Hash", nsae);
        }

        return hash.toString();
    }

    public static boolean comparePass(String input, String storedHash) {
        StringBuilder hash = new StringBuilder();
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = sha.digest(input.getBytes());
            for (byte b : hashedBytes) {
                hash.append(String.format("%02x", b));
            }
            return hash.toString().compareTo(storedHash) == 0;
        } catch (NoSuchAlgorithmException e) {
            LOG.log(Level.SEVERE, "Error on generate Hash", e);
            return false;
        }
    }
}
