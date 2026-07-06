# 🌍 TravelMate – Intelligent Full-Stack Travel Planning Platform

> An intelligent full-stack travel planning platform that streamlines trip planning through destination discovery, budget planning, hotel search, and an interactive travel assistant.

---

## 📖 Overview

TravelMate is a full-stack web application developed to simplify travel planning by integrating multiple travel services into a single platform. The application enables users to explore destinations, estimate travel budgets, discover hotels, and interact with an intelligent travel assistant through a clean and responsive interface.

The system follows a **client-server architecture**, where the frontend communicates with a Flask backend using RESTful APIs, while MongoDB is used for persistent data storage and user management.

---

## 🚀 Key Features

- 🌍 Explore travel destinations
- 💰 Estimate travel budgets
- 🏨 Search hotels
- 👤 User registration and login
- 🤖 Intelligent travel chatbot
- 📱 Responsive web interface
- 🗄️ MongoDB database integration

---

# 🛠️ Tech Stack

| Category | Technologies |
|-----------|--------------|
| **Frontend** | HTML5, CSS3, JavaScript |
| **Backend** | Python, Flask |
| **Database** | MongoDB |
| **API** | RESTful APIs |
| **Tools** | Git, GitHub, VS Code |

---

# 🏗️ System Architecture

```
                    User
                      │
                      ▼
      HTML • CSS • JavaScript Frontend
                      │
              REST API Requests
                      │
                      ▼
            Flask Backend (Python)
      ├── Authentication
      ├── Budget Planner
      ├── Hotel Search
      ├── Chatbot Module
      ├── Destination Services
      └── Business Logic
                      │
                      ▼
                 MongoDB Database
```

---

# ⚙️ How TravelMate Works

### 1️⃣ User Authentication

Users register and log in to access personalized travel planning features.

↓

### 2️⃣ Travel Planning

Users can

- Search destinations
- Plan budgets
- Explore hotels
- Ask travel-related questions

↓

### 3️⃣ Backend Processing

The frontend sends requests to the Flask backend using RESTful APIs.

The backend

- validates requests
- processes business logic
- retrieves data
- communicates with MongoDB
- generates responses

↓

### 4️⃣ Data Management

MongoDB stores

- User information
- Login credentials
- Travel-related data

↓

### 5️⃣ User Interface

Processed information is returned to the frontend and displayed dynamically for a seamless user experience.

---

# 🔧 REST APIs

The backend exposes RESTful APIs for communication between the frontend and server.

Major functionalities include:

- User Registration
- User Login
- Destination Search
- Hotel Search
- Budget Planning
- Chatbot Requests

These APIs exchange data in JSON format, enabling efficient communication between the client and server.

---

# 🤖 Intelligent Travel Assistant

TravelMate includes an intelligent chatbot developed using **Python** and **Fuzzy String Matching**.

The chatbot assists users by answering travel-related questions and identifying similar queries even when user input is not an exact match.

---

# 💾 Database Design

MongoDB is used to manage application data including:

- User Accounts
- Authentication Details
- User Preferences
- Travel Information

The database enables efficient storage and retrieval of user information while supporting scalable application development.

---

# 📸 Project Preview

## 🏠 Home Page

<img width="1895" height="1077" alt="Screenshot 2026-07-06 202538" src="https://github.com/user-attachments/assets/74ab28b1-d101-4550-87c2-80beaa1aa0cd" />
<img width="1900" height="1078" alt="Screenshot 2026-07-06 202526" src="https://github.com/user-attachments/assets/3b750b35-7624-47ae-855c-1a3ec6ddfd37" />


---

## 🌍 Destination Discovery

<img width="1445" height="937" alt="Screenshot 2026-07-06 203128" src="https://github.com/user-attachments/assets/c9da3b62-583d-4959-9741-6cd940dab61b" />
<img width="1538" height="1011" alt="Screenshot 2026-07-06 203121" src="https://github.com/user-attachments/assets/167975c6-37b6-454a-b339-3fb2c5899cfb" />
<img width="1482" height="605" alt="Screenshot 2026-07-06 203114" src="https://github.com/user-attachments/assets/cf1cfd50-8359-4bd5-abbb-29766fd4bf7d" />
<img width="1886" height="1071" alt="Screenshot 2026-07-06 203021" src="https://github.com/user-attachments/assets/569010cd-fc1d-4114-b23f-dd866b73f92d" />


---

## 💰 Budget Planner

<img width="1713" height="1078" alt="Screenshot 2026-07-06 200827" src="https://github.com/user-attachments/assets/774e00cd-e041-4d75-a6a0-b26e02505651" />


---


## 🤖 Travel Chatbot

<img width="612" height="708" alt="Screenshot 2026-07-06 202922" src="https://github.com/user-attachments/assets/bd5d9de6-d3f8-4e13-bf40-7ddbb3f113c0" />


---

# 📂 Project Structure

```
TravelMate
│
├── static/
│   ├── css/
│   ├── js/
│   └── images/
│
├── templates/
│
├── chatbot/
│
├── app.py
├── requirements.txt
└── README.md
```

---

# 🎯 Concepts Demonstrated

- Full-Stack Web Development
- REST API Development
- Client–Server Architecture
- Backend Development using Flask
- MongoDB Integration
- User Authentication
- Modular Software Design
- Frontend–Backend Communication
- Responsive Web Design
- Git Version Control

---

⭐ *If you found this project useful, consider giving it a star!*




