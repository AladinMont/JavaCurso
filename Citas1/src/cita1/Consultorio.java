package cita1;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;

public class Consultorio {
    public ArrayList<LocalTime> entrada = new ArrayList<>();
    public ArrayList<LocalTime> salida = new ArrayList<>();

    public int buscar(LocalTime busca){
        ArrayList<LocalTime> horasocupadas = new ArrayList<>();
        horasocupadas.add(busca);
        for (int i = 0; i < 30; i++) {
            Iterator<LocalTime> buscentrada = entrada.iterator();
            while(buscentrada.hasNext()){
                LocalTime elemento = buscentrada.next();
                if (horasocupadas.get(i).compareTo(elemento)==0){

                    return 2;
                }
            }

            this.entrada.add(horasocupadas.get(i));

            LocalTime nuevo = (horasocupadas.get(i)).plusMinutes(1);
            horasocupadas.add(nuevo);
        }


        ArrayList<LocalTime> horasocupadas2 = new ArrayList<>();
        horasocupadas2.add(busca);
        for (int i = 0; i < 30; i++) {
            Iterator<LocalTime> buscsalida = salida.iterator();
            while(buscsalida.hasNext()){
                LocalTime elemento = buscsalida.next();
                if (horasocupadas2.get(i).compareTo(elemento)==0){
                    return 2;
                }
            }

            this.salida.add(horasocupadas2.get(i));
            LocalTime nuevo2 = (horasocupadas.get(i)).plusMinutes(1);
            horasocupadas2.add(nuevo2);
        }

        return 1;
    }

}
