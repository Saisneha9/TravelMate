require("dotenv").config();
const express = require("express");
const cors = require("cors");
const axios = require("axios");

const app = express();
app.use(express.json());
app.use(cors());

const GEMINI_API_KEY = process.env.GEMINI_API_KEY; // Load API key from .env file

app.post("/chatbot", async (req, res) => {
    const userMessage = req.body.text;

    try {
        const response = await axios.post(
            `https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=${GEMINI_API_KEY}`,
            {
                contents: [{ parts: [{ text: userMessage }] }]
            }
        );

        res.json({ response: response.data.candidates[0].content.parts[0].text });
    } catch (error) {
        console.error("Error fetching Gemini response:", error);
        res.status(500).json({ response: "Error connecting to AI." });
    }
});

app.listen(3000, () => console.log("✅ Server running on port 3000"));
