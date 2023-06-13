package cita1;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        ArrayList<Cita> cita = new ArrayList<>();
        Consultorio con = new Consultorio();
        int opcion, consultorio =1;
        do {
            System.out.println("Elija una opcion:\n1.- Agendar cita\n2.-Ver horarios ocupados del consultorio\n3.-FIN");
            opcion = scan.nextInt();
            scan.nextLine();
            switch (opcion){
                case 1:
                    System.out.println("Ingrese la hora a la que desea registrar la cita (Unicamente hora)");
                    String hora1 = scan.nextLine();
                    System.out.println(("Ingrese los minutos de la hora que desea registrar la cita"));
                    String hora2 = scan.nextLine();
                    String hora3 = hora1 + ":" + hora2;
                    LocalTime horacomprobar = LocalTime.parse(hora3);
                    Cita busqueda= new Cita();

                    int comp = busqueda.comprobarHora(horacomprobar, con);
                    if (comp==1){
                        System.out.println("Ingrese su RFC:");
                        String rfc = scan.nextLine();
                        busqueda.setRfc(rfc);
                        busqueda.setConsultorio(consultorio);
                        cita.add(busqueda);
                        System.out.println("Registro exitoso");
                    }
                    break;
                case 2:
                    Iterator<Cita> itrcita = cita.iterator();
                    while(itrcita.hasNext()){
                        Cita guardados = itrcita.next();
                        System.out.println("RFC: "+ guardados.getRfc()+"\tNumero de consultorio: "
                                +guardados.getConsultorio()+"\tHora de entrada: "
                        +guardados.getHoraentrada()+"\tHora de salida: "+guardados.getHoraSalida()+"\n");
                    }
                    break;
                case 3:
                    System.out.println("Adios!");
                    break;
                default:
                    System.out.println("Ingrese un valor valido");
            }

        } while (opcion!=3);
    }
}
