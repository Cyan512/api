package com.api.service;

import com.api.dto.TipoProgramaRequest;
import com.api.model.TipoPrograma;

public interface TipoProgramaService {
    TipoPrograma createTipoPrograma(TipoProgramaRequest request);
}
