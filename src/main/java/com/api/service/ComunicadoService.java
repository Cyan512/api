package com.api.service;

import java.util.List;

import com.api.dto.ComunicadoRequest;
import com.api.model.Comunicado;

public interface ComunicadoService {
    List<Comunicado> getAllComunicados();
    Comunicado getComunicadoBySlug(String slug);
    Comunicado createComunicado(ComunicadoRequest request);
    Comunicado updateComunicado(String slug, ComunicadoRequest request);
    void deleteComunicado(String slug);
}
