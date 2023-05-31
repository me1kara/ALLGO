package test.testspring.DTO;

public class SearchDTO {
    private String keyword;
    private String searchType;
    private Long categoryId;
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getKeyword() {
        return keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    @Override
    public String toString() {
        return "SearchDTO{" +
                "keyword='" + keyword + '\'' +
                ", searchType='" + searchType + '\'' +
                '}';
    }
}
