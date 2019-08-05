package ru.javawebinar.topjava.mockito;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.web.RootController;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.mockito.TestData.ADMIN;
import static ru.javawebinar.topjava.mockito.TestData.USER;

@ExtendWith(MockitoExtension.class)
public class RootControllerTest {

    private MockMvc mockMvc;

    @Mock
    protected UserService userService;

    @InjectMocks
    private RootController rootController;

    @BeforeEach
    private void createMockMvc() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/view/");
        resolver.setSuffix(".jsp");
        mockMvc = MockMvcBuilders.standaloneSetup(rootController).setViewResolvers(resolver).build();
    }

    @Test
    public void getUsers_should_return_http_200() throws Exception {
        given(userService.getAll()).willReturn(List.of(USER, ADMIN));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());

    }
}
