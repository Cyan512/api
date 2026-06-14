package com.api.service;

import java.util.List;

import com.api.dto.TipoProgramaRequest;
import com.api.model.TipoPrograma;

public interface TipoProgramaService {
    List<TipoPrograma> getAllTipoProgramas();
    TipoPrograma getTipoProgramaBySlug(String slug);
    TipoPrograma createTipoPrograma(TipoProgramaRequest request);
    TipoPrograma updateTipoPrograma(String slug, TipoProgramaRequest request);
    void deleteTipoPrograma(String slug);
}
