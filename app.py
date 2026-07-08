from flask import Flask, request, jsonify
from flask_cors import CORS
from pymongo import MongoClient
import certifi  # Needed for SSL certificate verification

app = Flask(__name__)
CORS(app) # Allows your HTML to talk to this Python server

# 1. DEFINE THE CONNECTION STRING FIRST
MONGO_URI = "mongodb+srv://admin:Cherry%403652@cluster0.wlmctkv.mongodb.net/?appName=Cluster0"

# 2. THEN CONNECT TO MONGODB (Using the certifi fix)
# We use tlsCAFile=certifi.where() to fix the SSL Handshake error
try:
    client = MongoClient(MONGO_URI, tlsCAFile=certifi.where())
    db = client['TravelMateDB'] # Creates a database named TravelMateDB
    users_collection = db['users'] # Creates a collection for users
    print("✅ Successfully connected to MongoDB!")
except Exception as e:
    print(f"❌ Error connecting to MongoDB: {e}")

# --- ROUTES ---

@app.route('/signup', methods=['POST'])
def signup():
    data = request.json
    email = data.get('email')
    password = data.get('password') # In a real app, hash this!
    
    # Check if user already exists
    if users_collection.find_one({'email': email}):
        return jsonify({"message": "User already exists!", "success": False}), 400
    
    # Save to MongoDB
    users_collection.insert_one({'email': email, 'password': password})
    return jsonify({"message": "Account created successfully!", "success": True}), 201

@app.route('/login', methods=['POST'])
def login():
    data = request.json
    email = data.get('email')
    password = data.get('password')
    
    # Find user in MongoDB
    user = users_collection.find_one({'email': email, 'password': password})
    
    if user:
        return jsonify({"message": "Login successful!", "success": True}), 200
    else:
        return jsonify({"message": "Invalid credentials", "success": False}), 401

if __name__ == '__main__':
    app.run(debug=True, port=5000)