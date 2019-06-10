import com.ufnowski.RestService;
import com.ufnowski.entity.User;
import com.ufnowski.entity.Book;
import com.ufnowski.service.UserService;
import com.ufnowski.service.BookService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.*;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestService.class)
@WebAppConfiguration
public class BookTest {
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;


    @Autowired
    private BookService bookService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserService userService;
    private Book bookToTests = new Book("Ufnow", "Death Knight");

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        HttpMessageConverter mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }
    @Test
    public void createBookWithService() {
        assertNotEquals(bookService.save(bookToTests).getId(), 0);
        bookService.delete(bookToTests.getId());
    }

    @Test
    public void deleteBookWithService() {
        long bookId = bookService.save(bookToTests).getId();
        bookService.delete(bookId);
        assertNull(bookService.findOne(bookId));
    }

    @Test
    public void countBooksTest() {
        assertNotEquals(bookService.count(), -1);
    }

    @Test
    public void postBookRestTest() throws Exception {
        long userId = userService.save(new User("test")).getId();
        String jsonBook = new JSONObject()
                .put("Name", "Wojtas")
                .put("className", "Testowa")
                .put("users", new JSONArray()
                        .put(new JSONObject().put("id",userId))).toString();


        String urlToAddedBook = this.mockMvc.perform(post("/api/books")
                .contentType(contentType)
                .content(jsonBook))
                .andExpect(status().isCreated()).andReturn().getResponse().getHeader("Location");
        long bookId = Long.parseLong(urlToAddedBook.replace("http://localhost/api/books/", ""));
        bookService.delete(bookId);
        userService.delete(userId);
        assertNull(userService.findOne(userId));

    }

    @Test
    public void getBooksListRestTest() throws Exception {
        this.mockMvc.perform(get("/api/books")).andExpect(status().isOk());
    }



    @Test
    public void deleteBookRestTest() throws Exception {
        long bookId = bookService.save(new Book("Ufnow", "Testowa")).getId();

        this.mockMvc.perform(delete("/api/books/delete/" + bookId)).andExpect(status().isNoContent());
    }

    @Test
    public void deleteBookFromUserRestTest() throws Exception {
        User bookUser = new User("test");
        long userId = userService.save(bookUser).getId();
        List<User> users = new LinkedList<>();
        users.add(bookUser);
        long bookId = bookService.save(new Book("Ufnow", "Death Knight", users)).getId();

        this.mockMvc.perform(delete("/api/books/" + bookId + "/remove/" + userId)).andExpect(status().isNoContent());
        this.mockMvc.perform(delete("/api/books/delete/" + bookId)).andExpect(status().isNoContent());
        this.mockMvc.perform(delete("/api/users/delete/" + userId)).andExpect(status().isNoContent());
    }

    @Test
    public void updateBookRestTest() throws Exception {
        long bookId = bookService.save(new Book("imie", "nazwisko")).getId();
        long userId = userService.save(new User("testowa")).getId();
        String jsonBook = new JSONObject()
                .put("id", bookId)
                .put("Name", "Ufnow")
                .put("className", "Death Knight")
                .put("users", new JSONArray()
                        .put(new JSONObject().put("id",userId))).toString();

        this.mockMvc.perform(put("/api/books/update/" + bookId)
                .contentType(contentType)
                .content(jsonBook))
                .andExpect(status().isOk());

        this.mockMvc.perform(delete("/api/books/delete/" + bookId)).andExpect(status().isNoContent());
        userService.delete(userId);
        assertNull(userService.findOne(userId));
    }






}
