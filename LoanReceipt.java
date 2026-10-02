public class LoanReceipt {

    private final String memberId;
    private final String[] bookIds;


    // Static branch-level state
    private static String branchCode;

    static {
        branchCode = "PT-CHENNAI";
    }


    public LoanReceipt(
            String memberId,
            String[] bookIds) {

        this.memberId = memberId;

        // Defensive copy
        this.bookIds = bookIds.clone();
    }


    public String[] getBookIds() {

        // Defensive copy
        return bookIds.clone();
    }


    public LoanReceipt withCorrectedBookId(
            int index,
            String newId) {

        String[] corrected =
                bookIds.clone();

        corrected[index] = newId;

        // Return a brand-new object
        return new LoanReceipt(
            memberId,
            corrected
        );
    }


    public static String processNightlyCirculation(
            LoanReceipt[] receipts) {

        int processed = 0;
        int nullSkipped = 0;
        int referenceOnly = 0;
        int regular = 0;


        for (LoanReceipt receipt : receipts) {

            if (receipt == null) {

                nullSkipped++;

            } else {

                processed++;

                if (receipt instanceof ReferenceOnlyLoanReceipt) {
                    referenceOnly++;
                } else {
                    regular++;
                }
            }
        }


        return processed
                + " processed | "
                + nullSkipped
                + " null skipped | "
                + referenceOnly
                + " reference-only | "
                + regular
                + " regular";
    }


    public static void main(String[] args) {

        // Create receipt
        LoanReceipt r =
                new LoanReceipt(
                    "LIB-8841",
                    new String[]{
                        "BK-100",
                        "BK-101"
                    }
                );


        // Test defensive copy from getter
        String[] ids =
                r.getBookIds();

        ids[0] = "HACKED";


        System.out.println(
            "Original first book ID: "
            + r.getBookIds()[0]
        );


        // Test with-style correction
        LoanReceipt corrected =
                r.withCorrectedBookId(
                    1,
                    "BK-102"
                );


        System.out.println(
            "Original IDs: "
            + String.join(
                ", ",
                r.getBookIds()
            )
        );


        System.out.println(
            "Corrected IDs: "
            + String.join(
                ", ",
                corrected.getBookIds()
            )
        );


        // Test nightly processing
        LoanReceipt[] receipts = {

            new ReferenceOnlyLoanReceipt(
                "LIB-001",
                new String[]{"BK-200"},
                "Reading Room 3"
            ),

            null,

            new LoanReceipt(
                "LIB-002",
                new String[]{"BK-201"}
            )
        };


        System.out.println(
            processNightlyCirculation(
                receipts
            )
        );
    }
}


/*
 * Reference-only receipt
 */
class ReferenceOnlyLoanReceipt
        extends LoanReceipt {

    private final String roomNumber;


    public ReferenceOnlyLoanReceipt(
            String memberId,
            String[] bookIds,
            String roomNumber) {

        super(memberId, bookIds);

        this.roomNumber = roomNumber;
    }
}