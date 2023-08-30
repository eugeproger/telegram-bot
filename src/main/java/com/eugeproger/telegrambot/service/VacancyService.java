package com.eugeproger.telegrambot.service;

import com.eugeproger.telegrambot.VacanciesBot;
import com.eugeproger.telegrambot.dto.VacancyDto;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VacancyService {
    private final Map<String, VacancyDto> vacancies = new HashMap<>();

    @PostConstruct
    public void init() {
        VacancyDto juniorMaDeveloper = new VacancyDto();
        juniorMaDeveloper.setId("1");
        juniorMaDeveloper.setTitle("Junior Dav at MA");
        juniorMaDeveloper.setShortDescription("Java Core is required!");
        vacancies.put("1", juniorMaDeveloper);

        VacancyDto googleDev = new VacancyDto();
        googleDev.setId("3");
        googleDev.setTitle("Junior Dev at Google");
        googleDev.setShortDescription("Welcome to Google!");
        vacancies.put("3", googleDev);

        VacancyDto middle = new VacancyDto();
        middle.setId("1");
        middle.setTitle("Middle Java dev");
        middle.setShortDescription("Join our awesome company!");
        vacancies.put("2", middle);

    }

    public List<VacancyDto> getJuniorVacancies() {
        return vacancies.values().stream()
                .filter(v -> v.getTitle().toLowerCase().contains("junior"))
                .toList();
    }

    public VacancyDto get(String id) {
        return vacancies.get(id);
    }
}
