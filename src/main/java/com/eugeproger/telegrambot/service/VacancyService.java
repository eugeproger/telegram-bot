package com.eugeproger.telegrambot.service;

import com.eugeproger.telegrambot.dto.VacancyDto;
import jakarta.annotation.PostConstruct;
import org.jvnet.hk2.annotations.Service;

import java.util.HashMap;
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

        VacancyDto middle = new VacancyDto();
        middle.setId("1");
        middle.setTitle("Middle Java dev");
        middle.setShortDescription("Join our awesome company!");
        vacancies.put("2", middle);

    }
}
