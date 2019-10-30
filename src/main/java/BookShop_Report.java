import java.io.IOException;

public class BookShop_Report {
    public static ReportManager reportManager;

    public BookShop_Report() {reportManager = new ReportManager();}

    public static void main(String[] args) throws IOException {
        new BookShop_Report();
        while(true) reportManager.runProgram();
    }
}
