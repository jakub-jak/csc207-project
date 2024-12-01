package use_case.digest;

/**
 * Input Data object for the digest use case.
 */
public class DigestInputData {

    private final String[] keywords;
    private final String fromDate;
    private final String toDate;
    private final String language;
    private final String sortBy;

    public DigestInputData(String[] keywords, String fromDate, String toDate, String language, String sortBy) {
        this.keywords = keywords;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.language = language;
        this.sortBy = sortBy;
    }

    String[] getKeywords() {
        return keywords;
    }

    String getFromDate() {
        return fromDate;
    }

    String getToDate() {
        return toDate;
    }

    String getLanguage() {
        return language;
    }

    String getSortBy() {
        return sortBy;
    }
}
