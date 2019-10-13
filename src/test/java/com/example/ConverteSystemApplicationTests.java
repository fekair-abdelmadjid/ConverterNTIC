package com.example;

import com.example.dao.UserRepository;
import com.example.entities.User;
import com.example.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConverteSystemApplicationTests {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Before
    public void initDB() {
        User user1 = new User("fekair.mohammed@gmail.com", "Fekair Mohammed", "1234");
        userService.createUser(user1);

        User user2 = new User("fekair.abdelmadjid@gmail.com", "Fekair Abdelmadjid", "1234");
        userService.createAdmin(user2);
    }

    @Test
    public void TestUser() {
        User userUser = userRepository.findByEmail("fekair.mohammed@gmail.com");
        Assert.assertNotNull(userUser);

        User userAdmin = userRepository.findByEmail("fekair.abdelmadjid@gmail.com");
        Assert.assertEquals(userAdmin.getEmail(), "fekair.abdelmadjid@gmail.com");
    }


}

