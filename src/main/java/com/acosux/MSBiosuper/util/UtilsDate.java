package com.acosux.MSBiosuper.util;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class UtilsDate {

    public static int compareTo(Date fecha, Date fecha1) {
        return fechaFormatoDate(fecha).compareTo(fechaFormatoDate(fecha1));
    }

    public static Date date(Timestamp fecha) {
        return new Date(fecha.getTime());
    }

    public static Date dateCompleto(Date fecha) {
        return new Date(fecha.getTime() + 86399999);
    }

    public static String dateFin(Date fecha) {
        return fechaFormatoString(fecha) + " 23:59:59.999";
    }

    public static String dateInicio(Date fecha) {
        return fechaFormatoString(fecha) + " 00:00:00.000";
    }

    public static Date fechaFormatoDate(Date fecha) {
        return fechaFormatoDate(fechaFormatoString(fecha));
    }

    public static Date fechaFormatoDate(Date fecha, String formato) {
        try {
            return new SimpleDateFormat(formato).parse(fechaFormatoString(fecha, formato));
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date fechaFormatoDate(String fecha) {
        try {
            return formatoFecha.parse(fecha);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static Date fechaFormatoDate(String fecha, String formato) {
        try {
            return new SimpleDateFormat(formato).parse(fecha);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String fechaFormatoString(Date fecha) {
        return formatoFecha.format(fecha);
    }

    public static String fechaFormatoString(Date fecha, String formato) {
        try {
            return new SimpleDateFormat(formato).format(fecha);
        } catch (Exception e) {
            return null;
        }
    }

    public static String fechaFormatoString(String fecha) {
        return fechaFormatoString(fechaFormatoDate(fecha));
    }

    public static String fechaFormatoString(Timestamp fecha) {
        return formatoFecha.format(fecha);
    }

    public static Timestamp timestamp() {
        return new Timestamp(new Date().getTime());
    }

    public static Timestamp timestamp(Date fecha) {
        try {
            return new Timestamp(fecha.getTime());
        } catch (Exception e) {
            return null;
        }
    }

    public static Timestamp timestamp(String fecha) {
        return new Timestamp(fechaFormatoDate(fecha).getTime());
    }

    public static Timestamp timestampCompleto(Date fecha) {
        Calendar c = Calendar.getInstance();
        Calendar c1 = Calendar.getInstance();
        c.setTime(fecha);
        c.set(Calendar.HOUR_OF_DAY, c1.get(Calendar.HOUR_OF_DAY));
        c.set(Calendar.MINUTE, c1.get(Calendar.MINUTE));
        c.set(Calendar.SECOND, c1.get(Calendar.SECOND));
        return new Timestamp(c.getTime().getTime());
    }

    public static int getNumeroDia(String fecha, String formato) {
        Date date = fechaFormatoDate(fecha, formato);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static int getNumeroDiaMes(Date fecha) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getNumeroMes(Date fecha) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        System.out.println("1: " + calendar.get(Calendar.MONTH));
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        System.out.println("2: " + calendar.get(Calendar.MONTH));
        return calendar.get(Calendar.MONTH);
    }

    public static int getNumeroMes(String fecha, String formato) {
        Date date = fechaFormatoDate(fecha, formato);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.get(Calendar.MONTH);
    }

    public static int getNumeroAnio(Date fecha) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        System.out.println("1: " + calendar.get(Calendar.YEAR));
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        System.out.println("2: " + calendar.get(Calendar.YEAR));
        return calendar.get(Calendar.YEAR);
    }

    public static int getNumeroAnio(String fecha, String formato) {
        Date date = fechaFormatoDate(fecha, formato);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.get(Calendar.YEAR);
    }

    public static Date getPrimerDiaDelMes(Date fecha) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha);
        calendario.set(calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH),
                calendario.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendario.getTime();
    }

    public static Date getUltimoDiaDelMes(Date fecha) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha);
        calendario.set(calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH),
                calendario.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendario.getTime();
    }

    public static Date sumarDias(Date fechaInicio, int dias) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(fechaInicio);
        calendar.add(Calendar.DATE, dias);
        return calendar.getTime();
    }

    public static void diferenciaSegundos(String nombre, Timestamp fechaInicio, Timestamp fechaFin) {
        System.out.println("************** " + nombre + " ***************");
        System.out.println("Inicio: " + fechaInicio);
        System.out.println("Fin: " + fechaFin);
        System.out.println("Tiempo del metodo en ms: " + (fechaFin.getTime() - fechaInicio.getTime()) + " ; ss: "
                + (double) (fechaFin.getTime() - fechaInicio.getTime()) / 1000);
        System.out.println("*************************************");
    }

    public static Date fechaMayor(Date fecha1, Date fecha2) {
        if (fecha1.compareTo(fecha2) == 0) {
            return fecha1;
        } else if (fecha1.compareTo(fecha2) > 0) {
            return fecha1;
        } else {
            return fecha2;
        }
    }

    public static boolean fecha1EsMayorFecha2(Date fecha1, Date fecha2) {
        if (fecha1.compareTo(fecha2) == 0) {
            return false;
        } else if (fecha1.compareTo(fecha2) > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static Date DeStringADate(String fecha) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        String strFecha = fecha;
        Date fechaDate = null;
        try {
            fechaDate = formato.parse(strFecha);
            System.out.println(fechaDate.toString());
            return fechaDate;
        } catch (ParseException ex) {
            ex.printStackTrace();
            return fechaDate;
        }
    }

    public static String DeDateAString(Date fecha) {
        DateFormat fechaHora = new SimpleDateFormat("yyyy-MM-dd");
        return fechaHora.format(fecha);
    }
    
    public static String DeDateAStringDdMMyyyy(Date fecha) {
        DateFormat fechaHora = new SimpleDateFormat("dd-MM-yyyy");
        return fechaHora.format(fecha);
    }

    public static int diferenciaEntreFechas(Date a, Date b) {
        int tempDifference = 0;
        int difference = 0;
        Calendar earlier = Calendar.getInstance();
        Calendar later = Calendar.getInstance();

        if (a.compareTo(b) < 0) {
            earlier.setTime(a);
            later.setTime(b);
        } else {
            earlier.setTime(b);
            later.setTime(a);
        }

        while (earlier.get(Calendar.YEAR) != later.get(Calendar.YEAR)) {
            tempDifference = 365 * (later.get(Calendar.YEAR) - earlier.get(Calendar.YEAR));
            difference += tempDifference;

            earlier.add(Calendar.DAY_OF_YEAR, tempDifference);
        }

        if (earlier.get(Calendar.DAY_OF_YEAR) != later.get(Calendar.DAY_OF_YEAR)) {
            tempDifference = later.get(Calendar.DAY_OF_YEAR) - earlier.get(Calendar.DAY_OF_YEAR);
            difference += tempDifference;

            earlier.add(Calendar.DAY_OF_YEAR, tempDifference);
        }

        return difference;
    }

    public static String convetirDateConFormato(Date fechaSinFormato) {
        return new SimpleDateFormat("yyyy-MM-dd").format(fechaSinFormato);
    }

    public static String convetirTimeHHmm(Time tiempo) {
        return new SimpleDateFormat("HH:mm").format(tiempo);
    }

    private static final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

}
