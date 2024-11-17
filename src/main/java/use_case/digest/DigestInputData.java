package use_case.digest;

public class DigestInputData {

    private final String keyword;
    private final String fromDate;
    private final String toDate;
    private final String language;
    private final String sortBy;
    private final int page;
    private final int pageSize;

    public DigestInputData(String keyword, String fromDate, String toDate, String language, String sortBy, int page, int pageSize) {
        this.keyword = keyword;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.language = language;
        this.sortBy = sortBy;
        this.page = page;
        this.pageSize = pageSize;
    }

    String getKeyword() {
        return keyword;
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

    int getPage() {
        return page;
    }
    int getPageSize() {
        return pageSize;
    }
}
