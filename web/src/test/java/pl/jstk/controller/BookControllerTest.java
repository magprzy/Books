package pl.jstk.controller;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import pl.jstk.enumerations.BookStatus;
import pl.jstk.service.impl.BookServiceImpl;
import pl.jstk.to.BookTo;




@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

	@Autowired     
	private WebApplicationContext webApplicationContext;     
	 
	@Mock  
	private BookServiceImpl bookService;     
	
	@Autowired
	private BookController bookController;
	
	
	private MockMvc mockMvc;
	
	@Before  
	public void setup() 
	{    
		this.mockMvc = MockMvcBuilders.standaloneSetup(new BookController()).build();
		MockitoAnnotations.initMocks(bookService);
		
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build(); 
		
		ReflectionTestUtils.setField(bookController, "bookService", bookService);
		}
	
	@Test
	public void shouldReturnAllBooks() throws Exception {
		//given
		List<BookTo> books = new ArrayList<>();
		BookTo book1 = new BookTo(1L, "Book1", "Jan Kowalski", BookStatus.FREE);
		BookTo book2 = new BookTo(2L, "Book2", "Adam Nowak", BookStatus.FREE);
		
		books.add(book1);
		books.add(book2);
		
		
		//when
		when(bookService.findAllBooks()).thenReturn(books);
		
		//then
		
		mockMvc.perform(get("/books")).andExpect(status().isOk())
		.andExpect(model().attribute("bookList", hasSize(2)));
		
		
	}
	
	@Test
	public void shouldReturnDetailsBook() throws Exception{
		
		
		//given
		BookTo book1 = new BookTo(1L, "Book1", "Jan Kowalski", BookStatus.FREE);
		
		//when
		when(bookService.findBookById(1L)).thenReturn(book1);
		
		//then
		mockMvc.perform(get("/books/book?id=1")).andExpect(status().isOk())
		.andExpect(model().attribute("book",hasProperty("id", is(1L))))
		.andExpect(model().attribute("book",hasProperty("title", is("Book1"))))
		.andExpect(model().attribute("book",hasProperty("authors", is("Jan Kowalski"))));
	}

	@Test
	public void shouldReturnHTTPFoundWhenAnnonymousTryRemoveBook() throws Exception{
		
		//given
		List<BookTo> books = new ArrayList<>();
		BookTo book1 = new BookTo(1L, "Book1", "Jan Kowalski", BookStatus.FREE);
		BookTo book2 = new BookTo(2L, "Book2", "Adam Nowak", BookStatus.FREE);
		
		books.add(book1); books.add(book2);
		
		//when
		ResultActions result = mockMvc.perform(get("/books/remove?id=1"));
				
		//then
		result.andExpect(status().isFound());
		
	}

	@Test
	@WithMockUser(roles = { "USER" })
	public void shouldReturnHTTPForbiddenWhenUserTryRemoveBook() throws Exception{
		
		//given
		List<BookTo> books = new ArrayList<>();
		BookTo book1 = new BookTo(1L, "Book1", "Jan Kowalski", BookStatus.FREE);
		BookTo book2 = new BookTo(2L, "Book2", "Adam Nowak", BookStatus.FREE);
		
		books.add(book1); books.add(book2);
		
		//when
		ResultActions result = mockMvc.perform(get("/books/remove?id=1"));
		
		//then
		result.andExpect(status().isForbidden());
		
	}
	@Test
	@WithMockUser(roles = { "ADMIN" })
	public void shouldRemoveBook() throws Exception{
		
		//given
		List<BookTo> books = new ArrayList<>();
		BookTo book1 = new BookTo(1L, "Book1", "Jan Kowalski", BookStatus.FREE);
		BookTo book2 = new BookTo(2L, "Book2", "Adam Nowak", BookStatus.FREE);
		
		books.add(book1); books.add(book2);
		
		//when
		ResultActions result = mockMvc.perform(get("/books/remove?id=1"));
		
		//then
		result.andExpect(status().isOk());
	}
	

	}
