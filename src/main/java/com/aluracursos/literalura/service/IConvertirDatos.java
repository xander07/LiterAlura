package com.aluracursos.literalura.service;

public interface IConvertirDatos {
    <T> T gettingData(String json, Class<T> clase);
}
