package be.bostoenapk.Utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by david on 9/05/2016.
 */
public class Validatie {
    private Pattern pattern;
    private Matcher matcher;
    /**
     * public boolean validate(final String hex) {
     matcher = pattern.matcher(hex);
     return matcher.matches();

     }
     * Regeliere expressie om een email adres te controleren.
     */
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public Validatie() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    /**
     *
     * @param email het emailadre die gecontroleerd moet worden
     * @param maxLength de maximum lengte van het adres, -1 als er geen limiet  is.
     * @return boolean true als het een geldig adres is.
     */
    public boolean valEmail(String email, int maxLength) {
        email = email.replace('"', ' ');
        email = email.trim();
        if(maxLength != -1 && email.length() > maxLength) {
            return false;
        }
        if(email.equals(null) || email.equals("")) {
            return false;
        }
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     *
     * @param s de string die je moet controleren.
     * @param maxLength de maximum lengte van de string <code>s</code>.
     * @param minLength de minimum lengte van de string <code>s</code>.
     * @return boolean true als de string klopt.
     */
    public boolean valString(String s, int maxLength, int minLength) {
        s = s.replace('"', ' ');
        if(s.equals(null) || s.equals("")) {
            return false;
        }
        if(minLength != -1 && s.length() < minLength) {
            return false;
        }
        if(maxLength != -1 && s.length() > maxLength) {
            return false;
        }

        return true;
    }

    /**
     * @param s de te controleren string.
     * @param length de lengte die de string moet zijn om correct te zijn.
     * @return boolean die true geeft als de lengte correct is.
     */
    public boolean valStringExactength(String s, int length) {
        s = s.replace('"', ' ');
        if(s.equals(null) || s.equals("")) {
            return false;
        }
        if(s.length() != length) {
            return false;
        }
        return true;
    }

    /**
     * methode die in een string kijkt of het een geldig nummer is.
     * @param number de te controleren string.
     * @param maxLength de maximum lengte van de string, geef -1 op als er geen maximum is.
     * @param minLength de minimum lengte van de string, geef -1 op als er geen minimum is.
     * @return
     */
    public boolean valNumber(String number, int maxLength, int minLength) {
        if(number.equals(null) || number.equals("")) {
            return false;
        }
        if(minLength != -1 && number.length() < minLength) {
            return false;
        }
        if(maxLength != -1 && number.length() > maxLength) {
            return false;
        }
        try {
            Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return false;
        }
        //het is vollledig in orde.
        return true;
    }

    public boolean valNumberExactLength(String number, int length) {
        if(number.equals(null) || number.equals("")) {
            return false;
        }
        if(number.length() != length) {
            return false;
        }
        try {
            Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return false;
        }
        //het is vollledig in orde.
        return true;
    }


}
