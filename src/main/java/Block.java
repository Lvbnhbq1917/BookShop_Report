import java.io.File;
import java.sql.SQLException;

public class Block {
    private Model model = null;
    private View view = null;
    private String reportNumber = null;
    private File reportDirectory = null;

    public Block(String reportNumber, File reportDirectory) {
        this.reportNumber = reportNumber;
        this.reportDirectory = reportDirectory;
    }

    public void build() throws SQLException, ClassNotFoundException {
        model = new Model(reportNumber);
        model.reportDataRequest();
        view = new View(model);
        view.display_report(reportDirectory);
    }
}
