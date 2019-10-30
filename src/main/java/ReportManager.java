import java.io.File;
import java.io.IOException;

public class ReportManager {
    private String reportNumber, result = null;
    private File reportDirectory = null;

    public ReportManager()  {
        ConsoleHelper.writeMessage(ConsoleHelper.GREETING);
    }

    public void runProgram() throws IOException {
        ConsoleHelper.writeMessage(ConsoleHelper.MAKE_CHOICE_TOP);
        result = ConsoleHelper.readMessage();
        switch (result) {
            case ("1"):
                reportNumber = result;
                reportBuild();
                break;
            case ("0"):
                ConsoleHelper.writeMessage(ConsoleHelper.GOODBYE);
                System.exit(1);
            default:
                ConsoleHelper.writeMessage(ConsoleHelper.IO_ERROR);
                ConsoleHelper.writeMessage(ConsoleHelper.MAKE_CHOICE_TOP);
                break;
        }
    }

    private void reportBuild() throws IOException {
        result = null;
        ConsoleHelper.writeMessage(ConsoleHelper.CHOICE_DISLOCATION);

        while (result == null) result = ConsoleHelper.readMessage();

        reportDirectory = new File(result);
        if(!reportDirectory.exists()) reportDirectory.mkdirs();

        Block block = new Block(reportNumber, reportDirectory);

        try {
            block.build();
        } catch (Exception e) {}
    }
}
