package cita;

import java.util.ArrayList;
import java.time.*;
import java.util.Iterator;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        String RFC = "";
        int nuconsultorio = 1;
        String hora1 = "", hora2 = "", hora3 = "";
        ArrayList<ArrayList<Object>> cita = new ArrayList<>();
        ArrayList<ArrayList<LocalTime>> consultorio = new ArrayList<>();
        Scanner scan = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("Elija una opcion:\n1.- Agendar cita\n2.-Ver horarios ocupados del consultorio\n3.-FIN");
            opcion = scan.nextInt();
            scan.nextLine();
            switch (opcion) {
                case 1:
                    System.out.println("Ingrese la hora a la que desea registrar la cita (Unicamente hora)");
                    hora1 = scan.nextLine();
                    System.out.println(("Ingrese los minutos de la hora que desea registrar la cita"));
                    hora2 = scan.nextLine();
                    hora3 = hora1 + ":" + hora2;
                    LocalTime horacomprobar = LocalTime.parse(hora3);
                    int err = 0;
                    ArrayList<LocalTime> horasocupadas = new ArrayList<>();
                    horasocupadas.add(horacomprobar);
                    for (int j = 0; j < 30; j++) {
                        Iterator<ArrayList<LocalTime>> itrArrayListNumeros = consultorio.iterator();
                        while (itrArrayListNumeros.hasNext()) {
                            ArrayList<LocalTime> numeros = itrArrayListNumeros.next();
                            Iterator<LocalTime> itrNumeros = numeros.iterator();
                            while (itrNumeros.hasNext()) {
                                LocalTime numero = itrNumeros.next();
                                if (horasocupadas.get(j).compareTo(numero) == 0) {
                                    System.out.println("Horario ya asignado");
                                    err = 1;
                                    break;
                                }
                            }
                            if (err == 1) {
                                break;
                            }
                        }
                        LocalTime nuevo = (horasocupadas.get(j)).plusMinutes(1);
                        horasocupadas.add(nuevo);
                        if (err == 1) {
                            break;
                        }
                    }
                    if (err != 1) {
                        consultorio.add(horasocupadas);
                        System.out.println("Ingrese su RFC:");
                        RFC = scan.nextLine();
                        ArrayList<Object> datos = new ArrayList<>();
                        datos.add(RFC);
                        datos.add(nuconsultorio);
                        datos.add(horasocupadas.get(0));
                        datos.add(horasocupadas.get((horasocupadas.size()) - 1));
                        cita.add((datos));
                    }
                    break;
                case 2:
                    Iterator<ArrayList<Object>> itrArrayListNumeros = cita.iterator();
                    while (itrArrayListNumeros.hasNext()) {
                        int i = 0;
                        ArrayList<Object> numeros = itrArrayListNumeros.next();
                        Iterator<Object> itrNumeros = numeros.iterator();
                        while (itrNumeros.hasNext()) {
                            if (i == 0) {
                                String rfc = String.valueOf(itrNumeros.next());
                                System.out.println("RFC: " + rfc + " ");
                            } else {
                                if (i == 1) {
                                    int numero = ((Integer) itrNumeros.next()).intValue();
                                    System.out.println("Numero de consultorio: " + numero + " ");
                                } else {
                                    if (i == 2) {
                                        LocalTime horaentrada = LocalTime.parse(String.valueOf(itrNumeros.next()));
                                        System.out.println("Hora de entrada: " + horaentrada + " ");
                                    } else {
                                        if (i == 3) {
                                            LocalTime horasalida = LocalTime.parse(String.valueOf(itrNumeros.next()));
                                            System.out.println("Hora de salida: " + horasalida);
                                        }
                                    }
                                }
                            }
                            i++;
                        }
                        System.out.println();
                    }
                    break;
                case 3:
                    System.out.println("Adios!");
                    break;
                default:
                    System.out.println("Ingrese una opción valida");
                    break;
            }
        } while (opcion != 3);
    }
}
