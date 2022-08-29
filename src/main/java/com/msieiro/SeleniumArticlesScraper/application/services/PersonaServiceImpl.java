package com.msieiro.SeleniumArticlesScraper.application.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.msieiro.SeleniumArticlesScraper.application.PersonaService;
import com.msieiro.SeleniumArticlesScraper.domain.Person;
import com.msieiro.SeleniumArticlesScraper.domain.repository.PersonRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class PersonaServiceImpl implements PersonaService {

    private final PersonRepository personRepository;

    public Person getPersonByName(final String name) {
        return personRepository.findByName(name);
    }

    @Transactional(readOnly = false)
    public Person savePerson(final Person person) {
        return personRepository.save(person);
    }

    @Transactional(readOnly = false)
    public List<Person> savePersons(final List<Person> persons) {
        return personRepository.saveAll(persons);
    }

}
