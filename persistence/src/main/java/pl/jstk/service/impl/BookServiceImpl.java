package pl.jstk.service.impl;



import java.util.List;
import java.util.stream.Collectors;

import pl.jstk.entity.BookEntity;
import pl.jstk.mapper.BookMapper;
import pl.jstk.repository.BookRepository;
import pl.jstk.service.BookService;
import pl.jstk.to.BookTo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<BookTo> findAllBooks() {
        return BookMapper.map2To(bookRepository.findAll());   
    }
    
    
    public List<BookTo> getBooksByParameters(String title, String author){
    	
    	
    	List <BookTo> books = findAllBooks().stream()
    			.filter(t -> t.getTitle().toUpperCase().equals(title.toUpperCase()) || title.isEmpty() )
    			.filter(a -> a.getAuthors().toUpperCase().equals(author.toUpperCase()) || author.isEmpty())
    			.collect(Collectors.toList());
    
    	
    	return books;
    		
    }

    @Override
    public List<BookTo> findBooksByTitle(String title) {
        return BookMapper.map2To(bookRepository.findBookByTitle(title));
    }

    @Override
    public List<BookTo> findBooksByAuthor(String author) {
        return BookMapper.map2To(bookRepository.findBookByAuthor(author));
    }
    
    public BookTo findBookById(String id){
    	return BookMapper.map(bookRepository.findBookById(id));
    }

    @Override
    @Transactional
    public BookTo saveBook(BookTo book) {
        BookEntity entity = BookMapper.map(book);
        entity = bookRepository.save(entity);
        return BookMapper.map(entity);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);

    }
}
