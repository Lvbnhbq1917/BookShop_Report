import java.awt.*;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import com.smartxls.*;


public class View {
    private WorkBook workBook;
    private Model model;
    private File reportDirectory;
    private Book book;
    private Author author;
    private RangeStyle rangeStyle;

    public View(Model model) {
        this.model = model;
        workBook = new WorkBook();
    }

    public File getReportDirectory() {
        return reportDirectory;
    }

    public void setReportDirectory(File reportDirectory) {
        this.reportDirectory = reportDirectory;
    }

    public void display_report(File reportDirectory) {
        table_building();
        file_creation(reportDirectory);
    }

    private void table_building() {
        List<Book> books = model.getBooks();
        List<Author> authors = model.getAuthors();

        //определяем параметры таблицы для построения
        int num_Col_authors = 0;
        int num_Col_years = 0;
        int num_rows = 2;
        int count = 0;
        int countCol = 0;

        Iterator<Book> iteration = books.iterator();
        while(iteration.hasNext()) {
            num_rows++;
            book = iteration.next();
            //test
            // System.out.println(count);
            if ((count = book.getAuthors_list().size()) > num_Col_authors) num_Col_authors = count;
            if ((count = book.getYear_of_publishing().size()) > num_Col_years) num_Col_years = count;
        }

        //создаю первый лист Книги
        try {
            workBook.setSheetName(0, "КНИГИ");

            //создаем поля таблицы
            rangeStyle = workBook.getRangeStyle(0, 0, 0, num_Col_authors - 1);
            rangeStyle.setMergeCells(true);
            workBook.setRangeStyle(rangeStyle, 0, 0, 0, num_Col_authors - 1);
            workBook.setText(0,0,"Авторы");
            for(int i = 0; i < num_Col_authors; i++) workBook.setText(1, i, "Автор №" + (i + 1));

            rangeStyle = workBook.getRangeStyle(0, num_Col_authors, 1, num_Col_authors );
            rangeStyle.setMergeCells(true);
            workBook.setRangeStyle(rangeStyle, 0, num_Col_authors, 1, num_Col_authors);
            workBook.setText(0, num_Col_authors,"Наименование книги");

            rangeStyle = workBook.getRangeStyle(0, num_Col_authors+1, 0, num_Col_authors + num_Col_years);
            rangeStyle.setMergeCells(true);
            workBook.setRangeStyle(rangeStyle,0, num_Col_authors+1, 0, num_Col_authors + num_Col_years);
            workBook.setText(0,num_Col_authors + 1,"Год публикации");

            for(int i = num_Col_authors + 1; i < num_Col_authors + num_Col_years + 1; i++) workBook.setText(1, i, "Год изд."  + (i - num_Col_authors));

            //создаем автовыравнивание ширины колонок
            for(int i = 0; i < num_Col_authors + num_Col_authors + 2; i++) workBook.setColWidthAutoSize(i, true);

            //заводим данные
            Iterator<Book> iterator = books.iterator();
            count = 2;
            while(iterator.hasNext()) {
                book = iterator.next();
                workBook.setText(count, num_Col_authors, book.getBook_name());
                for(String authorName: book.getAuthors_list()) workBook.setText(count, countCol++, authorName);
                countCol = num_Col_authors + 1;
                for(int yearNumber: book.getYear_of_publishing()) {
                    if (yearNumber > 2010) {
                        rangeStyle = workBook.getRangeStyle(count, countCol, count, countCol);
                        rangeStyle.setFontName("Arial");
                        rangeStyle.setFontSize(12*20);
                        rangeStyle.setFontItalic(true);
                        rangeStyle.setFontBold(true);
                        workBook.setRangeStyle(rangeStyle, count, countCol, count, countCol);
                        workBook.setNumber(count, countCol++, yearNumber);
                    }
                    else workBook.setNumber(count, countCol++, yearNumber);
                }
                countCol = 0;
                count++;
            }

            //создаем автовыравнивание текста в колонках
            rangeStyle = workBook.getRangeStyle(0, 0, num_rows - 1, num_Col_authors + num_Col_years);
            rangeStyle.setHorizontalAlignment(RangeStyle.HorizontalAlignmentCenter);
            rangeStyle.setVerticalAlignment(RangeStyle.VerticalAlignmentCenter);

            //создаем очертания таблицы
            rangeStyle.setLeftBorder(RangeStyle.BorderThick);
            rangeStyle.setRightBorder(RangeStyle.BorderThick);
            rangeStyle.setTopBorder(RangeStyle.BorderThick);
            rangeStyle.setBottomBorder(RangeStyle.BorderThick);
            rangeStyle.setHorizontalInsideBorder(RangeStyle.BorderHair);
            rangeStyle.setVerticalInsideBorder(RangeStyle.BorderThick);
            workBook.setRangeStyle(rangeStyle, 0, 0, num_rows - 1, num_Col_authors + num_Col_years);

            //создаем серый бэкграунд полей
            rangeStyle = workBook.getRangeStyle(0, 0, 1, num_Col_authors + num_Col_years);
            rangeStyle.setPattern(RangeStyle.PatternSolid);
            rangeStyle.setPatternFG(Color.LIGHT_GRAY.getRGB());
            rangeStyle.setHorizontalInsideBorder(RangeStyle.BorderThick);
            rangeStyle.setVerticalInsideBorder(RangeStyle.BorderThick);
            rangeStyle.setBottomBorder(RangeStyle.BorderThick);
            workBook.setRangeStyle(rangeStyle, 0, 0, 1, num_Col_authors + num_Col_years);

            //выравниваем ячейки полей
            for(int i = 0; i < num_Col_authors + num_Col_authors + 2; i++) workBook.setColWidthAutoSize(i, true);

            //создаю второй лист Авторы
            workBook.insertSheets(1, 2);
            workBook.setSheetName(1, "АВТОРЫ");

            //аналогично первому листу создаю заглавие под заданный шаблон
            rangeStyle = workBook.getRangeStyle(0, 0, 0,1);
            rangeStyle.setHorizontalAlignment(RangeStyle.HorizontalAlignmentCenter);
            rangeStyle.setVerticalAlignment(RangeStyle.VerticalAlignmentCenter);
            rangeStyle.setLeftBorder(RangeStyle.BorderThick);
            rangeStyle.setLeftBorderColor(Color.blue.getRGB());
            rangeStyle.setRightBorder(RangeStyle.BorderThick);
            rangeStyle.setRightBorderColor(Color.blue.getRGB());
            rangeStyle.setTopBorder(RangeStyle.BorderThick);
            rangeStyle.setTopBorderColor(Color.blue.getRGB());
            rangeStyle.setBottomBorder(RangeStyle.BorderThick);
            rangeStyle.setBottomBorderColor(Color.blue.getRGB());
            rangeStyle.setVerticalInsideBorder(RangeStyle.BorderThick);
            rangeStyle.setVerticalInsideBorderColor(Color.BLUE.getRGB());
            rangeStyle.setHorizontalInsideBorder(RangeStyle.BorderThick);
            rangeStyle.setHorizontalInsideBorderColor(Color.CYAN.getRGB());
            rangeStyle.setPattern(RangeStyle.PatternSolid);
            rangeStyle.setPatternFG(Color.CYAN.getRGB());
            workBook.setRangeStyle(rangeStyle, 0, 0,0,1);
            workBook.setText(0,0,"ФИО");
            workBook.setText(0,1,"Количество публикаций");

            //заполнение таблицы
            Iterator<Author> iterator2 = authors.iterator();
            count = 1;
            while(iterator2.hasNext()) {
                author = iterator2.next();
                workBook.setText(count,0, author.getName());
                workBook.setNumber(count,1, author.getQuantity_of_publishing());
                count++;
            }

            rangeStyle = workBook.getRangeStyle(1, 0, count - 1,1);
            rangeStyle.setHorizontalAlignment(RangeStyle.HorizontalAlignmentCenter);
            rangeStyle.setVerticalAlignment(RangeStyle.VerticalAlignmentCenter);
            rangeStyle.setLeftBorder(RangeStyle.BorderThick);
            rangeStyle.setLeftBorderColor(Color.blue.getRGB());
            rangeStyle.setRightBorder(RangeStyle.BorderThick);
            rangeStyle.setRightBorderColor(Color.blue.getRGB());
            rangeStyle.setTopBorder(RangeStyle.BorderThick);
            rangeStyle.setTopBorderColor(Color.blue.getRGB());
            rangeStyle.setBottomBorder(RangeStyle.BorderThick);
            rangeStyle.setBottomBorderColor(Color.blue.getRGB());
            rangeStyle.setVerticalInsideBorder(RangeStyle.BorderThick);
            rangeStyle.setVerticalInsideBorderColor(Color.BLUE.getRGB());
            rangeStyle.setHorizontalInsideBorder(RangeStyle.BorderDashDot);
            rangeStyle.setHorizontalInsideBorderColor(Color.CYAN.getRGB());
            workBook.setRangeStyle(rangeStyle, 1, 0,count - 1,1);

            //далаем автофильтр
            workBook.setSelection(0,0,count - 1,1);
            workBook.autoFilter();


            workBook.setColWidthAutoSize(0, true);
            workBook.setColWidthAutoSize(1, true);
            //workBook.setColWidth(1, 25);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void file_creation(File reportDirectory) {
        try {
            workBook.writeXLSX(reportDirectory.toString() + "\\report001.xlsx");
            ConsoleHelper.writeMessage(ConsoleHelper.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
