package ru.klimkin.demo.project2boot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.klimkin.demo.project2boot.models.Book;
import ru.klimkin.demo.project2boot.models.Person;
import ru.klimkin.demo.project2boot.repositories.BooksRepository;


import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll(boolean sortByYear) {
        if (sortByYear)
            return booksRepository.findAll(Sort.by("year"));
        else
            return booksRepository.findAll();
    }

    public List<Book> findAllWithPagination(Boolean boo, int page, int booksPerPage) {
        if (boo) {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
        } else {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
        }
    }

    public Book findOne(int id) {
        Optional<Book> foundBook = booksRepository.findById(id);
        return foundBook.orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook){
        updatedBook.setId(id);
        updatedBook.setOwner(updatedBook.getOwner());
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

     public Person getBookOwner(int id){
        return booksRepository.findById(id).map(Book::getOwner).orElse(null);
    }

    @Transactional
    public void release(int id) {

/*      Book bookToRelease = booksRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());

        bookToRelease.setOwner(null);
        bookToRelease.setTakenAt(null);

        booksRepository.save(bookToRelease);*/

        booksRepository.findById(id).ifPresent(
                book -> {
                    book.setOwner(null);
                    book.setTakenAt(null);
                }
        );

       // jdbcTemplate.update("UPDATE Book SET person_id=NULL WHERE id=?", id);
    }

    @Transactional
    public void assign(int id, Person selectedPerson) {

/*      Book bookToAssign = booksRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        bookToAssign.setOwner(selectedPerson);
        bookToAssign.setTakenAt(new Date());

        booksRepository.save(bookToAssign);*/

        booksRepository.findById(id).ifPresent(
                book -> {
                    book.setOwner(selectedPerson);
                    book.setTakenAt(new Date());
                }
        );
    }

    public List<Book> findBookByTitleStartingWith(String startingWith) {

        return booksRepository.findBookByTitleStartingWith(startingWith);
    }
}
