import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.*;

// Full application test (Database Connectivity)
@SpringBootTest
public class ApplicationTest extends AbstractTestNGSpringContextTests {

    @Test
    public void testApplicationStarts() {
        assertTrue(true, "Application started successfully");
    }
}

// Repository Test (Database Layer)
@DataJpaTest
class MyRepositoryTest {

    @Autowired
    private MyRepository myRepository;

    @Test
    public void testFindData() {
        MyEntity entity = new MyEntity();
        entity.setValue("Test Data");
        myRepository.save(entity);

        MyEntity result = myRepository.findByValue("Test Data");
        assertNotNull(result);
        assertEquals(result.getValue(), "Test Data");
    }
}

// Service Test (Business Logic)
class MyServiceTest {

    @InjectMocks
    private MyService myService;

    @Mock
    private MyRepository myRepository;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetData() {
        when(myRepository.findByValue("Test Data")).thenReturn(new MyEntity());

        String result = myService.getData();
        assertEquals(result, "Test Data");

        verify(myRepository, times(1)).findByValue("Test Data");
    }
}

// Controller Test (API Layer)
@WebMvcTest(MyController.class)
class MyControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MyService myService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetData() throws Exception {
        when(myService.getData()).thenReturn("Mocked Data");

        mockMvc.perform(get("/api/data"))
                .andExpect(status().isOk())
                .andExpect(content().string("Mocked Data"));

        verify(myService, times(1)).getData();
    }
}
