package com.jubasbackend.utils;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class MailMessageUtils {

    public static class ClientCancellationExpiredTime {
        public static String messageClient(String clientName,
                                           String employeeName,
                                           LocalDateTime date,
                                           LocalTime time) {
            return String.format("""
                    Olá %s,
                    O atendimento agendado com %s para o dia %s às %sh não pôde ser realizado.
                    Estamos à disposição para reagendar.
                    """, clientName, employeeName, date, time);
        }

        public static String messageEmployee(String clientName,
                                             LocalDateTime date,
                                             LocalTime time) {
            return String.format("""
                    O cliente %s informou que não poderá comparecer ao atendimento marcado para o dia %s às %sh.
                    """, clientName, date, time);
        }

        public static String messageAdmin(String clientName,
                                          String employeeName,
                                          LocalDateTime date,
                                          LocalTime time) {
            return String.format("""
                    O cliente %s informou que não poderá comparecer ao atendimento com %s, marcado para o dia %s às %sh.
                    """, clientName, employeeName, date, time);
        }
    }

    public static class EmployeeCancellationExpiredTime {
        public static String clientMessage(String clientName,
                                           String employeeName,
                                           LocalDateTime date,
                                           LocalTime time) {
            return String.format("""
                    Olá %s,
                    O atendimento com %s para o dia %s às %sh não pôde ser realizado.
                    Estamos à disposição para reagendar.
                    """, clientName, employeeName, date, time);
        }

        public static String employeeMessage(String clientName,
                                             LocalDateTime date,
                                             LocalTime time) {
            return String.format("""
                    O atendimento com o cliente %s, agendado para o dia %s às %sh, foi marcado como cancelado.
                    """, clientName, date, time);
        }

        public static String adminMessage(String clientName,
                                          String employeeName,
                                          LocalDateTime date,
                                          LocalTime time) {
            return String.format("""
                    O funcionário %s informou que não pôde realizar o atendimento de %s, marcado para o dia %s às %sh
                    porque o cliente não compareceu.
                    """, employeeName, clientName, date, time);
        }

    }

    public static class ClientCancellation {
        public static String clientMessage(String clientName,
                                           String employeeName,
                                           LocalDateTime date,
                                           LocalTime time) {
            return String.format("""
                    Olá %s,
                    O atendimento agendado com %s para o dia %s às %sh foi cancelado.
                    """, clientName, date, time, employeeName);
        }

        public static String employeeMessage(String clientName,
                                             LocalDateTime date,
                                             LocalTime time) {
            return String.format("""
                    O cliente %s cancelou o atendimento marcado para o dia %s às %sh.
                    """, clientName, date, time);
        }

        public static String adminMessage(String clientName,
                                          String employeeName,
                                          LocalDateTime date,
                                          LocalTime time) {
            return String.format("""
                    O cliente %s cancelou o atendimento com %s, marcado para o dia %s às %sh.
                    """, clientName, employeeName, date, time);
        }

    }

    public static class EmployeeCancellation {
        public static String clientMessage(String clientName,
                                           String employeeName,
                                           LocalDateTime date,
                                           LocalTime time) {
            return String.format(""" 
                    Olá %s,
                    Infelizmente, precisamos cancelar o seu atendimento com %s para o dia %s às %sh.
                    Pedimos desculpas pelo inconveniente e estamos à disposição para remarcar.
                    """, clientName, employeeName, date, time);
        }

        public static String employeeMessage(String clientName,
                                             LocalDateTime date,
                                             LocalTime time) {
            return String.format("""
                    O atendimento com o cliente %s, agendado para o dia %s às %sh, foi cancelado.
                    """, clientName, date, time);
        }

        public static String adminMessage(String clientName,
                                          String employeeName,
                                          LocalDateTime date,
                                          LocalTime time) {
            return String.format("""
                    O funcionário %s cancelou o atendimento de %s, marcado para o dia %s às %sh.
                    """, employeeName, clientName, date, time);
        }

    }
}
