package pl.jstk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.jstk.service.impl.BookServiceImpl;
import pl.jstk.to.BookTo;

@Controller
public class BookController {

	@Autowired
	BookServiceImpl bookService;

	@GetMapping(value = "/books")
	public String getAllBooks(Model model) {

		model.addAttribute("bookList", bookService.findAllBooks());

		return "books";

	}

	@GetMapping(value = "/books/book")
	public String getDetail(@RequestParam("id") String id, Model model) {

		model.addAttribute("book", bookService.findBookById(id));

		return "book";
	}

	@GetMapping(value = "/books/remove")
	public String removeBook(@RequestParam("id") Long id, Model model) {

		bookService.deleteBook(id);

		return "delete";
	}

	@GetMapping(value = "/books/add")
	public String addBook(Model model) {

		model.addAttribute("newBook", new BookTo());

		return "addBook";
	}

	@PostMapping(value = "/greeting")
	public String added(@ModelAttribute("newBook") BookTo book, Model model) {

		if ((book.getAuthors().isEmpty()) || (book.getTitle().isEmpty())) {
			return "needParameters";
		} else {
			model.addAttribute(bookService.saveBook(book));
		}

		return "greeting";
	}

	@GetMapping(value = "/books/search")
	public String searchBook(Model model) {
		model.addAttribute("searchBook", new BookTo());
		return "search";
	}

	@GetMapping(value = "/foundBooks")
	public String getSearchResult(@RequestParam("authors") String author, @RequestParam("title") String title,
			Model model) {

		if (bookService.getBooksByParameters(title, author).isEmpty()) {
			return "bookNotExists";
		} else {
			model.addAttribute("searchBookList", bookService.getBooksByParameters(title, author));

			return "searchResult";
		}
	}

}
