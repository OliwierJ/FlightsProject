# ✈️ Flights System Project 
## Made by Oliwier Jakubiec and Brandon Jaroszczak

#### As part of the Systems Analysis, Design & Testing and Object Oriented Programming modules for the SETU Carlow software development course

---

## To build:

- Java 21 or higher required
- MySQL Database required
- Simply double click the JAR to run
- Or rebuild with own database credentials using the database dumps provided

---

## Features

### Complete Booking System
- Simulated booking process for flights across multiple destinations.
- Selection of departure and return flights.
- Passenger count selection with detailed input for each passenger.
- Dynamic seat selection for all passengers on both departure and return flights.
- Selection of optional extras:
    - Priority Boarding (+20 currency units)
    - 20kg Luggage (+39 currency units)

---

### Booking Management
- View existing bookings with all details.
- Update booking information:
    - Passenger Details
    - Booking email
    - Seat assignment (per passenger per flight)
- Cancel a booking directly from the system.

---

### Additional Functionalities
- Weather checking feature for departure and arrival cities of the flight using external weather API.
- User-friendly GUI built with Java Swing for an intuitive booking experience.

---

## Technical Stack
- Java 21.0.5
- Java Swing (GUI)
- MySQL Database Integration
- JDBC for database connectivity
- Object-Oriented Programming principles
- Event-driven programming (Listeners & Handlers)

---

## Known Limitations
- No payment gateway integration (simulated booking only).
- No user authentication (admin or customer accounts).

---

## Final Note
This version delivers a full simulation of the flight booking experience, from flight selection to booking management, all integrated with a MySQL database for persistent data storage. It lays the groundwork for a more advanced, feature-rich booking system in future releases.

---

_Compiled and built using Java 21.0.5_  
_Backend powered by MySQL Database_

_All names and emails used are for demo purposes only and do not actually exist_