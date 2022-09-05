package ru.klimkin.demo.project2boot.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.klimkin.demo.project2boot.models.Person;
import ru.klimkin.demo.project2boot.services.PeopleService;

@Component
public class PersonValidator implements Validator {

    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

    if (peopleService.findPersonByFullName(person.getFullName()).isPresent())
            errors.rejectValue("fullName", "", "Такой человек уже существует");
    }
}

