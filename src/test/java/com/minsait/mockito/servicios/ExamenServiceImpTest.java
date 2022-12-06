package com.minsait.mockito.servicios;
// BARBARA LEYVA MUÑOZ
import com.minsait.mockito.models.Examen;
import com.minsait.mockito.repositories.ExamenRepository;
import com.minsait.mockito.repositories.PreguntaRepository;
import lombok.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImpTest {

    @Mock
    ExamenRepository examenRepository;

    @Mock
    PreguntaRepository preguntaRepository;

    @Mock
    ExamenRepository saveRepository;

    @InjectMocks
    ExamenServiceImp service;

    @Captor
    ArgumentCaptor<Long> captor;

    @Test
    void testArgumentCaptor(){
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        service.findExamenPorNombreConPreguntas("Historia");
        verify(preguntaRepository).findPreguntasByExamenId(captor.capture());
        assertEquals(3L,captor.getValue());



    }

@Test
void testSaveExamen(){

    when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
    Examen examenNull =service.findExamenPorNombreConPreguntas("");
    assertNull(examenNull);
    //assertNotNull(examenNull.getPreguntas());
}
@Test
void testNoSavePreguntas(){
        /*
    when(saveRepository.findAll()).thenReturn(Datos.EXAMENES);
    List<Examen> preguntas2 = new ArrayList<>();
    preguntas2.add(new Examen(null,"pregunta1"));

   Examen PreguntasNoSave= service.save(preguntas);
   assertNull(PreguntasNoSave);
*/
//Examen preguntas=service.save(Datos.EXAMEN);
    Examen preguntas=Datos.EXAMEN;
    when(examenRepository.save(preguntas)).thenReturn(preguntas);
   // Examen preguntas=Datos.EXAMEN;
    Examen examen = service.save(preguntas);
//when(examenRepository.save(preguntas)).thenReturn(preguntas);
    assertNotNull(preguntas);
    assertEquals(Examen.class,service.save(examen).getClass());
    assertEquals("Calculo",examen.getNombre());
    assertEquals(5L,examen.getId());

    Examen examen2 =service.findExamenPorNombreConPreguntas("");
    assertNull(examen2);

    //assertTrue(preguntas.getPreguntas().contains("Pregunta1"));
    //assertNull(preguntas.getPreguntas().isEmpty());


    //Examen examen =service.findExamenPorNombreConPreguntas("");
  //  examen.getPreguntas().isEmpty();
    //assertNotNull(examen);
    //assertTrue(examen.getPreguntas().remove("Historia"));
    //assertNull(examen.getPreguntas().remove("Historia"));
   // assertNull(examen.getPreguntas());


}

    @Test
    void testfindExamenPorNombre() {
        //Datos necesarios similados
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);

        //Llamada al metodo
        Optional<Examen> examen =service.findExamenPorNombre("Historia");

        //Prueba unitaria o la afirmacación de que nuestro examenen se ha obtenido correctamente
        assertTrue(examen.isPresent());

    }

    @Test
    void testPreguntaExamen (){
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen =service.findExamenPorNombreConPreguntas("Historia");

        assertNotNull(examen);

        assertTrue(examen.getPreguntas().contains("Aritmetica"));

        verify(examenRepository,atLeast(2)).findAll();
        verify(preguntaRepository,times(1)).findPreguntasByExamenId(anyLong());



    }
    @Test
    void testException() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenThrow(IllegalArgumentException.class);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.findExamenPorNombreConPreguntas("Matematicas");
        });

        assertTrue(IllegalArgumentException.class.equals(exception.getClass()));
    }

    @Test
    void testDoThrow(){
        doThrow(IllegalArgumentException.class).when(preguntaRepository).savePreguntas(anyList());
        Examen examen =Datos.EXAMEN;
        examen.setPreguntas(Datos.PREGUNTAS);

        assertThrows(IllegalArgumentException.class,()-> service.save(examen));
    }

    @Test
    void testDoAnswer(){
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        doAnswer(invocationOnMock -> {
            Long id=invocationOnMock.getArgument(0);
            return id==1?Datos.PREGUNTAS: Collections.EMPTY_LIST;
        }).when(preguntaRepository).findPreguntasByExamenId(anyLong());
        //when(preguntaRepository.findPreguntasByExamenId(1L)).thenReturn(Datos.PREGUNTAS);
        //when(preguntaRepository.findPreguntasByExamenId(1L)).thenReturn(Datos.PREGUNTAS);

        Examen examen=service.findExamenPorNombreConPreguntas("Español");

        assertNotNull(examen);
        assertAll(
                ()-> assertNotNull(examen),
                ()-> assertTrue(examen.getPreguntas().isEmpty()),
                ()-> assertEquals(0,examen.getPreguntas().size())

        );
    }


}