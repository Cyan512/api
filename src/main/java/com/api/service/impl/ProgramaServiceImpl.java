package com.api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.model.Programa;
import com.api.repository.ProgramaRepository;
import com.api.service.ProgramaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProgramaServiceImpl implements ProgramaService {

    private final ProgramaRepository programaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Programa> getAllProgramas() {
        return programaRepository.findAll();
    }
}
