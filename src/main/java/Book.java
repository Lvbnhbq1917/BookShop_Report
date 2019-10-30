import java.util.List;

public class Book {
    private String book_name;
    private List<String> authors_list;
    private List<Integer> year_of_publishing;

    public Book(String book_name) {
        this.book_name = book_name;
    }

    public String getBook_name() {
        return book_name;
    }

    public List<String> getAuthors_list() {
        return authors_list;
    }

    public void setAuthors_list(List<String> authors_list) {
        this.authors_list = authors_list;
    }

    public List<Integer> getYear_of_publishing() {
        return year_of_publishing;
    }

    public void setYear_of_publishing(List<Integer> year_of_publishing) {
        this.year_of_publishing = year_of_publishing;
    }
}
