import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Model {
    private List<String> book_names_list, authors_list;
    private List<Integer> year_of_publishing;
    private String name;
    private List<Book> books;
    private List<Author> authors;
    private String reportNumber;
    private ResultSet rs;

    public Model(String reportNumber) {
        this.reportNumber = reportNumber;
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void reportDataRequest() {
        try(Connection con = ConnectionToBD.getConnectionToBD();
            Statement stmt = con.createStatement())
            {
            books = fillBooksList(stmt);
            authors = fillAuthorsList(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<Book> fillBooksList(Statement stmt) throws SQLException {
        book_names_list = new LinkedList<>();
        books = new LinkedList<>();

        //производим выборку всех книг из базы - дубликаты убираем
        rs = stmt.executeQuery("SELECT DISTINCT goods_name FROM sb_goods ORDER BY goods_name");
        while (rs.next()) {
            book_names_list.add(rs.getString("goods_name"));
        }

        //производим последовательную выборку всех данных для вкладки "книги" путем создания колекции объектов Книги
        Iterator<String> iterator = book_names_list.iterator();
        while(iterator.hasNext()) {
            authors_list = new LinkedList<>();
            year_of_publishing = new LinkedList<>();

            name = iterator.next();
            //test
            //System.out.println(name);
            Book book = new Book(name);

            rs = stmt.executeQuery("SELECT DISTINCT author_name FROM sb_authors, sb_goods, sb_authorbooks \n" +
                    "WHERE sb_goods.goods_id = sb_authorbooks.booksname_id \n" +
                    "AND sb_authors.author_id = sb_authorbooks.authorsname_id \n" +
                    "AND sb_goods.goods_name = '" + name + "'\n" +
                    "ORDER BY author_name");
            while(rs.next()) {
                authors_list.add(rs.getString("author_name"));
            }

            //test
            //for(String string: authors_list) System.out.println(string);
            //System.out.println("\n");

            rs = stmt.executeQuery("SELECT DISTINCT goods_yearofpublishing FROM sb_goods\n" +
                    "WHERE goods_name = '" + name +"'\n" +
                    "ORDER BY goods_yearofpublishing");
            while(rs.next()) {
                year_of_publishing.add((int) rs.getLong("goods_yearofpublishing"));
            }

            //test
            //for(int number: year_of_publishing) System.out.println(number);
            //System.out.println("\n");

            book.setAuthors_list(authors_list);
            book.setYear_of_publishing(year_of_publishing);
            books.add(book);
        }

        return books;
    }

    private List<Author> fillAuthorsList(Statement stmt) throws SQLException {
        authors = new LinkedList<>();
        authors_list = new LinkedList<>();

        //аналогично, делаю выборку всех авторов из таблицы и количество публикаций
        rs = stmt.executeQuery("SELECT DISTINCT author_name, COUNT(booksname_id) AS number " +
                                    "FROM sb_authorbooks, sb_authors \n" +
                                    "WHERE sb_authorbooks.authorsname_id = sb_authors.author_id\n" +
                                    "GROUP BY author_name");
        while (rs.next()) {
            Author author = new Author();
            author.setName(rs.getString("author_name"));
            author.setQuantity_of_publishing((int) rs.getLong("number"));
            authors.add(author);
        }

        return authors;
    }
}
