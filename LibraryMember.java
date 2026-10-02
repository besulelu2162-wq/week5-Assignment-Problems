public class LibraryMember {

    private String membershipId;
    private String name;
    private boolean premiumMember;

    private String securityAnswer;


    // Public no-argument constructor
    public LibraryMember() {
    }


    // Write-once membershipId
    public String getMembershipId() {
        return membershipId;
    }


    public void setMembershipId(String id) {

        // Only first call is accepted
        if (this.membershipId == null) {
            this.membershipId = id;
        }
    }


    // Normal JavaBean property
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    // Boolean JavaBean property
    public boolean isPremiumMember() {
        return premiumMember;
    }


    public void setPremiumMember(
            boolean premium) {

        this.premiumMember = premium;
    }


    // Write-only security answer
    public void setSecurityAnswer(
            String answer) {

        if (answer != null) {

            // Deterministic one-way transformation
            this.securityAnswer =
                    Integer.toHexString(
                        answer.hashCode()
                    );
        }
    }


    public static void main(String[] args) {

        LibraryMember m =
                new LibraryMember();

        m.setMembershipId("LIB-8841");

        m.setName("Priya Nair");

        m.setPremiumMember(true);

        System.out.println(
            m.getMembershipId()
        );

        // Second call should be ignored
        m.setMembershipId("FAKE-0000");

        System.out.println(
            m.getMembershipId()
        );

        System.out.println(
            m.isPremiumMember()
        );

        // No output because securityAnswer
        // has no getter
        m.setSecurityAnswer("BlueMountain");

        System.out.println(
            "Security answer stored"
        );
    }
}