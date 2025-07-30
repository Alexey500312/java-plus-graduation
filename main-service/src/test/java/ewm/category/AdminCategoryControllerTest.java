package ewm.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import ewm.MainServiceApplication;
import jakarta.validation.ConstraintViolationException;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collection;
import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {MainServiceApplication.class})
@WebMvcTest(AdminCategoryController.class)
class AdminCategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @DisplayName("Запрос на создание категории без тела запроса")
    @SneakyThrows
    @Test
    public void should_ThrowHttpMessageNotReadableExceptionAndReturnBadRequest_WhenCreateCategoryRequestMadeWithoutBody() {
        mockMvc.perform(post("/admin/categories"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assertInstanceOf(HttpMessageNotReadableException.class, result.getResolvedException());
                    assertEquals("Required request body is missing: public ewm.category.CategoryDto ewm.category.AdminCategoryController.createCategory(ewm.category.CreateCategoryDto)", result.getResolvedException().getMessage());
                })
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.reason", is("Incorrectly made request.")))
                .andExpect(jsonPath("$.message", is("Error request body")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @DisplayName("Запрос на создание категории без названия")
    @SneakyThrows
    @Test
    public void should_ThrowMethodArgumentNotValidExceptionAndReturnBadRequest_WhenCreateCategoryRequestMadeWithNullName() {
        CreateCategoryDto createCategoryDto = CreateCategoryDto.builder().build();

        MockHttpServletRequestBuilder requestBuilder = post("/admin/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCategoryDto));

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException());

                    BindingResult bindingResult = ((MethodArgumentNotValidException) result.getResolvedException()).getBindingResult();

                    assertEquals(1, bindingResult.getErrorCount());
                    assertEquals(1, bindingResult.getFieldErrorCount("name"));

                    assertEquals("Название категории не может быть пустым", Objects.requireNonNull(bindingResult.getFieldError("name")).getDefaultMessage());
                })
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.reason", is("Incorrectly made request.")))
                .andExpect(jsonPath("$.message", is("Название категории не может быть пустым")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @DisplayName("Запрос на создание категории с пустым названием")
    @SneakyThrows
    @Test
    public void should_ThrowMethodArgumentNotValidExceptionAndReturnBadRequest_WhenCreateCategoryRequestMadeWithWhitespaceName() {
        CreateCategoryDto createCategoryDto = CreateCategoryDto.builder()
                .name(" ")
                .build();

        MockHttpServletRequestBuilder requestBuilder = post("/admin/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCategoryDto));

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException());

                    BindingResult bindingResult = ((MethodArgumentNotValidException) result.getResolvedException()).getBindingResult();

                    assertEquals(2, bindingResult.getErrorCount());
                    assertEquals(2, bindingResult.getFieldErrorCount("name"));

                    Collection<String> nameErrors = bindingResult.getFieldErrors("name").stream().map(FieldError::getDefaultMessage).toList();
                    assertTrue(nameErrors.contains("Название категории не может быть пустым"));
                    assertTrue(nameErrors.contains("Название категории не может быть меньше 2 символов"));
                })
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.message", containsString("Название категории не может быть пустым")))
                .andExpect(jsonPath("$.message", containsString("Название категории не может быть меньше 2 символов")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @DisplayName("Запрос на создание категории со слишком коротким названием")
    @SneakyThrows
    @Test
    public void should_ThrowMethodArgumentNotValidExceptionAndReturnBadRequest_WhenCreateCategoryRequestMadeWithTooShortName() {
        CreateCategoryDto createCategoryDto = CreateCategoryDto.builder()
                .name("А")
                .build();

        MockHttpServletRequestBuilder requestBuilder = post("/admin/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCategoryDto));

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException());

                    BindingResult bindingResult = ((MethodArgumentNotValidException) result.getResolvedException()).getBindingResult();

                    assertEquals(1, bindingResult.getErrorCount());
                    assertEquals(1, bindingResult.getFieldErrorCount("name"));

                    assertEquals("Название категории не может быть меньше 2 символов", Objects.requireNonNull(bindingResult.getFieldError("name")).getDefaultMessage());
                })
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.reason", is("Incorrectly made request.")))
                .andExpect(jsonPath("$.message", is("Название категории не может быть меньше 2 символов")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @DisplayName("Запрос на создание категории со слишком длинным названием")
    @SneakyThrows
    @Test
    public void should_ThrowMethodArgumentNotValidExceptionAndReturnBadRequest_WhenCreateCategoryRequestMadeWithTooLongName() {
        CreateCategoryDto createCategoryDto = CreateCategoryDto.builder()
                .name(StringUtils.repeat("А", 51))
                .build();

        MockHttpServletRequestBuilder requestBuilder = post("/admin/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCategoryDto));

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException());

                    BindingResult bindingResult = ((MethodArgumentNotValidException) result.getResolvedException()).getBindingResult();

                    assertEquals(1, bindingResult.getErrorCount());
                    assertEquals(1, bindingResult.getFieldErrorCount("name"));

                    assertEquals("Название категории не может быть больше 50 символов", Objects.requireNonNull(bindingResult.getFieldError("name")).getDefaultMessage());
                })
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.reason", is("Incorrectly made request.")))
                .andExpect(jsonPath("$.message", is("Название категории не может быть больше 50 символов")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @DisplayName("Запрос на создание категории")
    @SneakyThrows
    @Test
    public void should_CreateCategory_WhenCreateCategoryRequestIsValid() {
        Faker faker = new Faker();

        CreateCategoryDto createCategoryDto = CreateCategoryDto.builder()
                .name(faker.company().industry())
                .build();

        CategoryDto categoryDto = CategoryDto.builder()
                .id(1L)
                .name(createCategoryDto.getName())
                .build();

        when(categoryService.createCategory(createCategoryDto)).thenReturn(categoryDto);

        MockHttpServletRequestBuilder requestBuilder = post("/admin/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCategoryDto));

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is(createCategoryDto.getName())));

        verify(categoryService, times(1)).createCategory(createCategoryDto);
    }

    @DisplayName("Запрос на обновление категории без идентификатора в пути запроса")
    @SneakyThrows
    @Test
    public void should_ThrowHttpRequestMethodNotSupportedExceptionAndReturnBadRequest_WhenUpdateCategoryRequestMadeWithoutCategoryId() {
        mockMvc.perform(patch("/admin/categories"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assertInstanceOf(HttpRequestMethodNotSupportedException.class, result.getResolvedException());
                    assertEquals("Request method 'PATCH' is not supported", result.getResolvedException().getMessage());
                })
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.reason", is("Incorrectly made request.")))
                .andExpect(jsonPath("$.message", is("Request method 'PATCH' is not supported")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @DisplayName("Запрос на обновление категории с отрицательным идентификатором в пути запроса")
    @SneakyThrows
    @Test
    public void should_ThrowHttpMessageNotReadableExceptionAndReturnBadRequest_WhenUpdateCategoryRequestMadeWithNegativeCategoryId() {
        mockMvc.perform(patch("/admin/categories/-1"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assertInstanceOf(HttpMessageNotReadableException.class, result.getResolvedException());
                    assertEquals("Required request body is missing: public ewm.category.CategoryDto ewm.category.AdminCategoryController.updateCategory(java.lang.Long,ewm.category.UpdateCategoryDto)", result.getResolvedException().getMessage());
                })
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.reason", is("Incorrectly made request.")))
                .andExpect(jsonPath("$.message", is("Error request body")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @DisplayName("Запрос на обновление категории без названия")
    @SneakyThrows
    @Test
    public void should_ThrowMethodArgumentNotValidExceptionAndReturnBadRequest_WhenUpdateCategoryRequestMadeWithNullName() {
        UpdateCategoryDto createCategoryDto = UpdateCategoryDto.builder().build();

        MockHttpServletRequestBuilder requestBuilder = patch("/admin/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCategoryDto));

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException());

                    BindingResult bindingResult = ((MethodArgumentNotValidException) result.getResolvedException()).getBindingResult();

                    assertEquals(1, bindingResult.getErrorCount());
                    assertEquals(1, bindingResult.getFieldErrorCount("name"));

                    assertEquals("Название категории не может быть пустым", Objects.requireNonNull(bindingResult.getFieldError("name")).getDefaultMessage());
                })
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.reason", is("Incorrectly made request.")))
                .andExpect(jsonPath("$.message", is("Название категории не может быть пустым")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @DisplayName("Запрос на обновление категории с пустым названием")
    @SneakyThrows
    @Test
    public void should_ThrowMethodArgumentNotValidExceptionAndReturnBadRequest_WhenUpdateCategoryRequestMadeWithWhitespaceName() {
        UpdateCategoryDto createCategoryDto = UpdateCategoryDto.builder()
                .name(" ")
                .build();

        MockHttpServletRequestBuilder requestBuilder = patch("/admin/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCategoryDto));

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException());

                    BindingResult bindingResult = ((MethodArgumentNotValidException) result.getResolvedException()).getBindingResult();

                    assertEquals(2, bindingResult.getErrorCount());
                    assertEquals(2, bindingResult.getFieldErrorCount("name"));

                    Collection<String> nameErrors = bindingResult.getFieldErrors("name").stream().map(FieldError::getDefaultMessage).toList();
                    assertTrue(nameErrors.contains("Название категории не может быть пустым"));
                    assertTrue(nameErrors.contains("Название категории не может быть меньше 2 символов"));
                })
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.message", containsString("Название категории не может быть пустым")))
                .andExpect(jsonPath("$.message", containsString("Название категории не может быть меньше 2 символов")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @DisplayName("Запрос на обновление категории со слишком коротким названием")
    @SneakyThrows
    @Test
    public void should_ThrowMethodArgumentNotValidExceptionAndReturnBadRequest_WhenUpdateCategoryRequestMadeWithTooShortName() {
        UpdateCategoryDto createCategoryDto = UpdateCategoryDto.builder()
                .name("А")
                .build();

        MockHttpServletRequestBuilder requestBuilder = patch("/admin/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCategoryDto));

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException());

                    BindingResult bindingResult = ((MethodArgumentNotValidException) result.getResolvedException()).getBindingResult();

                    assertEquals(1, bindingResult.getErrorCount());
                    assertEquals(1, bindingResult.getFieldErrorCount("name"));

                    assertEquals("Название категории не может быть меньше 2 символов", Objects.requireNonNull(bindingResult.getFieldError("name")).getDefaultMessage());
                })
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.reason", is("Incorrectly made request.")))
                .andExpect(jsonPath("$.message", is("Название категории не может быть меньше 2 символов")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @DisplayName("Запрос на обновление категории со слишком длинным названием")
    @SneakyThrows
    @Test
    public void should_ThrowMethodArgumentNotValidExceptionAndReturnBadRequest_WhenUpdateCategoryRequestMadeWithTooLongName() {
        UpdateCategoryDto createCategoryDto = UpdateCategoryDto.builder()
                .name(StringUtils.repeat("А", 51))
                .build();

        MockHttpServletRequestBuilder requestBuilder = patch("/admin/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCategoryDto));

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException());

                    BindingResult bindingResult = ((MethodArgumentNotValidException) result.getResolvedException()).getBindingResult();

                    assertEquals(1, bindingResult.getErrorCount());
                    assertEquals(1, bindingResult.getFieldErrorCount("name"));

                    assertEquals("Название категории не может быть больше 50 символов", Objects.requireNonNull(bindingResult.getFieldError("name")).getDefaultMessage());
                })
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.reason", is("Incorrectly made request.")))
                .andExpect(jsonPath("$.message", is("Название категории не может быть больше 50 символов")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @DisplayName("Запрос на обновление категории")
    @SneakyThrows
    @Test
    public void should_UpdateCategory_WhenUpdateCategoryRequestIsValid() {
        Faker faker = new Faker();

        UpdateCategoryDto updateCategoryDto = UpdateCategoryDto.builder()
                .name(faker.company().industry())
                .build();

        CategoryDto categoryDto = CategoryDto.builder()
                .id(1L)
                .name(updateCategoryDto.getName())
                .build();

        when(categoryService.updateCategory(1L, updateCategoryDto)).thenReturn(categoryDto);

        MockHttpServletRequestBuilder requestBuilder = patch("/admin/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCategoryDto));

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is(updateCategoryDto.getName())));

        verify(categoryService, times(1)).updateCategory(1L, updateCategoryDto);
    }

    @DisplayName("Запрос на удаление категории без идентификатора в пути запроса")
    @SneakyThrows
    @Test
    public void should_ThrowHttpRequestMethodNotSupportedExceptionAndReturnBadRequest_WhenDeleteCategoryRequestMadeWithoutCategoryId() {
        mockMvc.perform(delete("/admin/categories"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assertInstanceOf(HttpRequestMethodNotSupportedException.class, result.getResolvedException());
                    assertEquals("Request method 'DELETE' is not supported", result.getResolvedException().getMessage());
                })
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.reason", is("Incorrectly made request.")))
                .andExpect(jsonPath("$.message", is("Request method 'DELETE' is not supported")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @DisplayName("Запрос на обновление категории с отрицательным идентификатором в пути запроса")
    @SneakyThrows
    @Test
    public void should_ThrowConstraintViolationExceptionAndReturnBadRequest_WhenDeleteCategoryRequestMadeWithNegativeCategoryId() {
        mockMvc.perform(delete("/admin/categories/-1"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assertInstanceOf(ConstraintViolationException.class, result.getResolvedException());
                    assertEquals("deleteCategory.categoryId: must be greater than 0", result.getResolvedException().getMessage());
                })
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.reason", is("Incorrectly made request.")))
                .andExpect(jsonPath("$.message", is("deleteCategory.categoryId: must be greater than 0")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @DisplayName("Запрос на создание категории")
    @SneakyThrows
    @Test
    public void should_DeleteCategory_WhenDeleteCategoryRequestIsValid() {
        doNothing().when(categoryService).deleteCategory(1L);

        MockHttpServletRequestBuilder requestBuilder = delete("/admin/categories/1");

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).deleteCategory(1L);
    }
}