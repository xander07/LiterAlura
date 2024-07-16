package com.aluracursos.literalura.service;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvertirDatos implements IConvertirDatos {

    private ObjectMapper objectMapper;

    public ConvertirDatos() {}

    public <T> T gettingData(String jsonData, Class<T> clase) {
        if (jsonData == null || jsonData.isEmpty()) {
            return null;
        }

        try {
            T data = objectMapper.readValue(jsonData, clase);

            if (data == null) {
                System.out.println("No hay datos en el JSON");
            } else {
                System.out.println("Datos en el JSON: " + data);
            }
            return data;
        } catch (Exception e) {
            System.out.println("Ocurrio un error al convertir el JSON: " + jsonData);
            return null;
        }
    }
}
