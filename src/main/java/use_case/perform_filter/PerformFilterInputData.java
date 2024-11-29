package use_case.perform_filter;

import java.util.List;

public class PerformFilterInputData {
    private final List<String> categoriesFilterList;

    public PerformFilterInputData(List<String> categoriesFilterList) {
        this.categoriesFilterList = categoriesFilterList;
    }

    public List<String> getCategoriesFilterList() {
        return categoriesFilterList;
    }
}
