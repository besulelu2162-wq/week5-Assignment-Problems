public class AccessChecker {

    // Checks Java access rules
    static String classifyAccess(
            String fieldModifier,
            String accessorContext) {

        boolean allowed = false;

        // private
        if (fieldModifier.equals("private")) {

            if (accessorContext.equals("SAME_CLASS")) {
                allowed = true;
            }

        }

        // default
        else if (fieldModifier.equals("default")) {

            if (accessorContext.equals("SAME_CLASS")
                    || accessorContext.equals("SAME_PACKAGE")) {
                allowed = true;
            }

        }

        // protected
        else if (fieldModifier.equals("protected")) {

            if (accessorContext.equals("SAME_CLASS")
                    || accessorContext.equals("SAME_PACKAGE")) {
                allowed = true;
            }

        }

        // public
        else if (fieldModifier.equals("public")) {

            allowed = true;
        }

        // M2: protected subclass rules
        if (fieldModifier.equals("protected")) {

            if (accessorContext.equals(
                    "SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE")) {

                allowed = true;
            }

            if (accessorContext.equals(
                    "SUBCLASS_DIFFERENT_PACKAGE_PARENT_TYPE")) {

                allowed = false;
            }
        }

        // private/default denied in cross-package subclass
        if (fieldModifier.equals("private")
                || fieldModifier.equals("default")) {

            if (accessorContext.equals(
                    "SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE")
                    || accessorContext.equals(
                    "SUBCLASS_DIFFERENT_PACKAGE_PARENT_TYPE")) {

                allowed = false;
            }
        }

        return allowed ? "ALLOWED" : "DENIED";
    }


    // M1: Group results by modifier
    static String summarizeByModifier(
            String[][] attempts) {

        int privateAllowed = 0;
        int privateDenied = 0;

        int defaultAllowed = 0;
        int defaultDenied = 0;

        int protectedAllowed = 0;
        int protectedDenied = 0;

        int publicAllowed = 0;
        int publicDenied = 0;

        for (String[] attempt : attempts) {

            String modifier = attempt[0];
            String context = attempt[1];

            String result =
                    classifyAccess(modifier, context);

            if (modifier.equals("private")) {

                if (result.equals("ALLOWED")) {
                    privateAllowed++;
                } else {
                    privateDenied++;
                }

            } else if (modifier.equals("default")) {

                if (result.equals("ALLOWED")) {
                    defaultAllowed++;
                } else {
                    defaultDenied++;
                }

            } else if (modifier.equals("protected")) {

                if (result.equals("ALLOWED")) {
                    protectedAllowed++;
                } else {
                    protectedDenied++;
                }

            } else if (modifier.equals("public")) {

                if (result.equals("ALLOWED")) {
                    publicAllowed++;
                } else {
                    publicDenied++;
                }
            }
        }

        return "private: "
                + privateAllowed + " allowed / "
                + privateDenied + " denied"
                + " | default: "
                + defaultAllowed + " allowed / "
                + defaultDenied + " denied"
                + " | protected: "
                + protectedAllowed + " allowed / "
                + protectedDenied + " denied"
                + " | public: "
                + publicAllowed + " allowed / "
                + publicDenied + " denied";
    }


    // M2: Find first denied attempt
    static String firstDeniedAttempt(
            String[][] attempts) {

        for (int i = 0; i < attempts.length; i++) {

            String modifier = attempts[i][0];
            String context = attempts[i][1];

            String result =
                    classifyAccess(modifier, context);

            if (result.equals("DENIED")) {

                return modifier
                        + " via "
                        + context
                        + " (attempt #"
                        + (i + 1)
                        + ")";
            }
        }

        return "None Denied";
    }


    public static void main(String[] args) {

        // M1
        System.out.println(
            classifyAccess(
                "private",
                "SAME_CLASS"
            )
        );

        System.out.println(
            classifyAccess(
                "protected",
                "DIFFERENT_PACKAGE"
            )
        );


        String[][] attempts = {

            {"private", "SAME_CLASS"},
            {"private", "SAME_PACKAGE"},

            {"default", "SAME_PACKAGE"},
            {"default", "DIFFERENT_PACKAGE"},

            {"protected", "SAME_PACKAGE"},
            {"protected", "SAME_CLASS"},

            {"public", "DIFFERENT_PACKAGE"}
        };

        System.out.println(
            summarizeByModifier(attempts)
        );


        // M2
        String[][] orderedAttempts = {

            {
                "public",
                "SUBCLASS_DIFFERENT_PACKAGE_PARENT_TYPE"
            },

            {
                "protected",
                "SUBCLASS_DIFFERENT_PACKAGE_PARENT_TYPE"
            },

            {
                "protected",
                "SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE"
            }
        };

        System.out.println(
            firstDeniedAttempt(orderedAttempts)
        );


        String[][] allowedAttempts = {

            {
                "public",
                "SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE"
            },

            {
                "protected",
                "SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE"
            }
        };

        System.out.println(
            firstDeniedAttempt(allowedAttempts)
        );
    }
}