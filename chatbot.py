from flask import Flask, request, jsonify
from flask_cors import CORS
from difflib import get_close_matches

app = Flask(__name__)
CORS(app) # Allows the frontend to talk to this specific server

# --- ZERO DATABASE KNOWLEDGE BASE ---
# Just a simple python dictionary. Uses almost 0 memory.
qa_pairs = {
    "hello": "Hello! Welcome to TravelMate. How can I help you plan your trip?",
    "hi": "Hi there! Ready to explore the world?",
    "book hotel": "You can book hotels by navigating to the 'Hotels' section in our menu.",
    "book flight": "Check out our 'Flights' page for the best deals on air travel.",
    "weather": "You can check the weather on our dashboard. Just enter your destination city!",
    "refund": "Refunds are processed within 5-7 business days according to our policy.",
    "contact": "You can reach our support team at support@travelmate.com.",
    "price": "Our packages start from $500. Check specific destinations for details.",
    "login": "Click the 'Login' button at the top right to access your account.",
    "signup": "You can create a new account for free using the 'Sign Up' page."
}

@app.route('/chat', methods=['POST'])
def chat():
    user_message = request.json.get('message', '').lower()
    
    # 1. Exact Match
    if user_message in qa_pairs:
        return jsonify({"reply": qa_pairs[user_message]})
    
    # 2. Fuzzy Match (Finds closest question if there's a typo)
    matches = get_close_matches(user_message, qa_pairs.keys(), n=1, cutoff=0.6)
    
    if matches:
        best_match = matches[0]
        return jsonify({"reply": qa_pairs[best_match]})
    else:
        return jsonify({"reply": "I'm not sure about that. Try asking about hotels, flights, or support!"})

if __name__ == '__main__':
    # IMPORTANT: We run this on PORT 5001 so it doesn't conflict with Login (5000)
    app.run(debug=True, port=5001)