## 🍃 Deploy da Aplicação

O [deploy da aplicação](https://jubas-backend.onrender.com/swagger-ui.html) do Ecossistema Jubas já está disponível. Por favor, note que pode haver uma demora inicial no carregamento do container devido ao serviço gratuito utilizado. Agradeço pela sua compreensão.

![Swagger UI](https://github.com/marcelo-de-santana/imagioteca/blob/master/jubas-backend/swagger-ui-from-jubas-backend-v1.png?raw=true)

---

<h3 align="center">
  Backend do Ecossistema Jubas
</h3>

<p align="center">
  <img alt="Language: Java" src="https://img.shields.io/badge/language-java-green">
  <img alt="Docker" src="https://img.shields.io/badge/Docker-🐳-blue">
  <img alt="Swagger 3.0" src="https://img.shields.io/badge/Swagger-3.0-orange">
  <img alt="Version: 1.0" src="https://img.shields.io/badge/version-2.0-yellowgreen">
</p>

## :straight_ruler: Diagrama de Classes

```mermaid
classDiagram
    direction LR
    PermissionType "1" -- "1" User : has
    
    Profile "n" -- "1" User : associated 
    
    Profile "1" --* "1" Employee : assigned

    Employee "1" -- "1" WorkingHour : assigned

    Employee "n" -- "n" Specialty : associated
    
    Specialty "n" -- "1" Category : belongs

    Employee "1" -- "n" Appointment : contains

    Profile "1" -- "n" Appointment : contains
    
    Specialty "1" -- "1" Appointment : contains
    
    Appointment "1" -- "1"  AppointmentStatus: associated

    Appointment "n" -- "1"  Payment: associated

    class PermissionType {
        <<Enumeration>>
        ADMIN
        BARBEIRO
        CLIENTE
    }

    class User {
        UUID id
        String email
        String password
        PermissionType permission
        List~Profile~ profiles
    }

    class Profile {
        UUID id
        String name
        String cpf
        boolean status
        User user
        List~Appointment~ appointments
    }

    class Employee {
        Profile profile
        WorkingHour workingHour
        List~Specialty~ specialties
        List~Appointment~ attendances
    }

    class WorkingHour {
        UUID id
        Time startTime
        Time endTime
        Time intervalStart
        Time intervalEnd
    }

    class Specialty {
        UUID id
        String name
        Time duration
        BigDecimal price
        Category category
        List~Employee~ employees
    }

    class Category {
        Short id
        String name
    }

    class Appointment {
        UUID id
        LocalDateTime date
        Instant createdAt
        Instant updatedAt
        Employee employee
        Profile client
        Specialty specialty
        AppointmentStatus appointmentStatus
    }

    class AppointmentStatus {
        <<Enumeration>>
        MARCADO
        EM_ATENDIMENTO
        FINALIZADO
        CANCELADO
    }
    
    class NonServiceDay {
        LocalDate date
    }

    class DayAvailable {
        int quantity
    }

    class Payment {
        UUID id
        String type
        BigDecimal value
    }
```

## :wrench: Tecnologias utilizadas

* Docker
* Java 17
* JUnit
* Mockito
* MySQL
* Spring Boot
* Swagger

Desenvolvido por Marcelo Santana
