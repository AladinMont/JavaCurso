package cita1;

import java.time.LocalTime;

public class Cita {
    public String rfc="";
    public int consultorio=0;
    public LocalTime horaentrada;
    public LocalTime horaSalida;

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public void setConsultorio(int consultorio) {
        this.consultorio = consultorio;
    }

    public void setHoraentrada(LocalTime horaentrada) {
        this.horaentrada = horaentrada;
    }

    public void setHoraSalida(LocalTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public String getRfc() {
        return rfc;
    }

    public int getConsultorio() {
        return consultorio;
    }

    public LocalTime getHoraentrada() {
        return horaentrada;
    }

    public LocalTime getHoraSalida() {
        return horaSalida;
    }
    public int comprobarHora(LocalTime hora, Consultorio con){
        int res = con.buscar(hora);
        if (res==1){
            setHoraentrada(hora);
            LocalTime hora2 = hora.plusMinutes(30);
            setHoraSalida(hora2);
            return 1;
        } else if (res == 2) {
            System.out.println("Horario ya asignado");
            return 2;
        }
        return 1;
    }
}
