package com.msieiro.SeleniumArticlesScraper.application;

import java.util.List;

import com.msieiro.SeleniumArticlesScraper.domain.Person;

public interface PersonaService {

    Person getPersonByName(final String name);

    Person savePerson(final Person person);

    List<Person> savePersons(final List<Person> persons);
}
