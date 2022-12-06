package com.minsait.mockito.servicios;

import com.minsait.mockito.models.Examen;
import com.minsait.mockito.repositories.ExamenRepository;
import com.minsait.mockito.repositories.PreguntaRepository;
import java.util.Optional;

public class ExamenServiceImp  implements ExamenService{
   ExamenRepository examenRepository;
   PreguntaRepository preguntaRepository;
    @Override
    public Optional<Examen> findExamenPorNombre(String nombre) {

       examenRepository.findAll() ;
       return examenRepository.findAll().stream().
                filter(examen -> examen.getNombre().equals(nombre)).findFirst();
    }

    @Override
    public Examen findExamenPorNombreConPreguntas(String nombre) {
        Optional<Examen> examen=findExamenPorNombre(nombre);
        if(examen.isPresent()){
            examen.get().setPreguntas(preguntaRepository.findPreguntasByExamenId(examen.get().getId()));
            return  examen.get();
        }
        return null;
    }

    @Override
    public Examen save(Examen examen) {

//
        if(!examen.getPreguntas().isEmpty()){
            preguntaRepository.savePreguntas(examen.getPreguntas());
        }
        return examenRepository.save(examen);
    }
}
