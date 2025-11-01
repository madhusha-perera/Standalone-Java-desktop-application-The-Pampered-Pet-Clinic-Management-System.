# 🐾 The Pampered Pet Clinic Management System 🩺

![Java](https://img.shields.io/badge/Language-Java-orange?style=flat-square) ![MySQL](https://img.shields.io/badge/Database-MySQL-blue?style=flat-square) ![License](https://img.shields.io/badge/License-MIT-green?style=flat-square) ![Platform](https://img.shields.io/badge/Platform-Desktop-blue?style=flat-square)  

A standalone **Java desktop application** with **MySQL backend** designed to streamline operations at veterinary clinics, managing **appointments, pets, owners, and medical records** efficiently.  

---

## 🌟 Features

### 🩺 Veterinarian
- View appointments 📅  
- Check pet details 🐶🐱  
- Add medical records 📝  

**Fields**: ID, First Name, Last Name, Specialization, Phone, Username, Password, DOB, Address  

---

### 🐾 Pet
**Fields**: ID, Name, Species, Breed, DOB, Gender, Owner  

---

### 🧑‍💼 Owner
**Fields**: ID, First Name, Last Name, Email, Address, Phone, Password  

---

### 🏢 Receptionist
- Manage appointments 📆  
- Manage pets 🐕🐈  
- Manage owners 👤  
- Manage veterinarians 🩺  

**Fields**: ID, Username, Password, First Name, Last Name, Phone, Address, DOB  

---

### 👑 Head Receptionist
- Full management capabilities:  
  - Appointments, Pets, Owners, Veterinarians, Receptionists  

---

### 📅 Appointments
- ID, DateTime, Reason, Vet, Pet, Owner  

---

### 📝 Pet Medical Records
- Record ID, Pet ID, Visit Date, Diagnosis, Treatment, Medication  

---

## ⚙️ System Requirements
- **Java JDK 17+**  
- Desktop OS (Windows/Linux/Mac)  
- IDE (Eclipse, IntelliJ IDEA, NetBeans recommended)  
- **MySQL Server** (for backend database)  

---

## 🚀 Installation & Usage
1. Clone the repository  
2. Import the project into your preferred Java IDE.
3. Set up the MySQL database using the provided database.sql file (or schema).
4. Update database connection settings in DBContoller.java. 
5. Compile and run LoginFrame.java.
6.Login as Veterinarian, Receptionist, or Head Receptionist to manage the clinic data.

## Admin Credentials(Serverside hardcoded)
username:headrec
password:headrec123

