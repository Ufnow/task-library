import com.ufnowski.RestService;
import com.ufnowski.entity.Book;
import com.ufnowski.entity.User;
import com.ufnowski.service.UserService;
import com.ufnowski.service.BookService;
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

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestService.class)
@WebAppConfiguration
public class UserTest {
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

    private User userToTests = new User("testowaGrupa");

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
    public void createUserWithServiceTest() {
        User userTemp = userService.save(userToTests);
        assertNotNull(userTemp);
        userService.delete(userTemp.getId());
    }

    @Test
    public void deleteUserWithServiceTest() {
        long userId = userService.save(userToTests).getId();
        userService.delete(userId);
        assertNull(userService.findOne(userId));
    }

    @Test
    public void countUsersWithServiceTest() {
        assertNotEquals(userService.count(), -1);
    }

    @Test
    public void postUserRestTest() throws Exception {
        String userJson = new JSONObject()
                .put("userName", "test").toString();

        String urlToAddedUser = this.mockMvc.perform(post("/api/users")
                .contentType(contentType)
                .content(userJson))
                .andExpect(status().isCreated()).andReturn().getResponse().getHeader("Location");
        long userId = Long.parseLong(urlToAddedUser.replace("http://localhost/api/users/", ""));
        userService.delete(userId);
    }

    @Test
    public void getUsersList() throws Exception {
        this.mockMvc.perform(get("/api/users")).andExpect(status().isOk());
    }





    @Test
    public void deleteUserRestTest() throws Exception {
        long userId = userService.save(new User("test")).getId();

        this.mockMvc.perform(delete("/api/users/delete/" + userId)).andExpect(status().isNoContent());
    }

    @Test
    public void deleteUserFromBookRestTest() throws Exception {
        long userId = userService.save(userToTests).getId();
        List<User> users = new LinkedList<>();
        users.add(userToTests);
        long bookId = bookService.save(new Book("Ufnow", "Death Knight", users)).getId();

        this.mockMvc.perform(delete("/api/users/" + userId + "/remove/" + bookId)).andExpect(status().isNoContent());
        this.mockMvc.perform(delete("/api/books/delete/" + bookId)).andExpect(status().isNoContent());
        this.mockMvc.perform(delete("/api/users/delete/" + userId)).andExpect(status().isNoContent());
    }

    @Test
    public void updateUserRestTest() throws Exception {
        long userId = userService.save(userToTests).getId();
        String userJson = new JSONObject()
                .put("id", userId)
                .put("userName", "test2").toString();

        this.mockMvc.perform(put("/api/users/update/" + userId)
                .contentType(contentType)
                .content(userJson))
                .andExpect(status().isOk());

        this.mockMvc.perform(delete("/api/users/delete/" + userId)).andExpect(status().isNoContent());
    }
}
