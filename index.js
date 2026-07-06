// 📦 Import required modules
const nodemailer = require('nodemailer');
const express = require('express');
const fs = require('fs');
const cors = require('cors');

// 🚀 Create express app
const app = express();
const port = 3000;

// 🔧 Middleware
app.use(cors());
app.use(express.json()); // Important for parsing JSON

// 🚨 POST Endpoint: Receive SOS data
app.post('/api/sos', (req, res) => {
    const { latitude, longitude, timestamp } = req.body;

    // ✅ Validation
    if (!latitude || !longitude || !timestamp) {
        return res.status(400).send({ error: 'Missing data in request body' });
    }

    // 📝 Log entry
    const log = `SOS Alert: ${timestamp} - [${latitude}, ${longitude}]\n`;
    fs.appendFileSync('sos-log.txt', log);

    // ✅ Response
    res.status(200).send({ message: 'SOS received successfully' });
});

// 📥 GET Endpoint: Send SOS logs to frontend
app.get('/api/sos', (req, res) => {
    fs.readFile('sos-log.txt', 'utf8', (err, data) => {
        if (err) {
            return res.status(500).send({ error: 'Failed to read SOS logs' });
        }

        // 🔍 Parse lines
        const entries = data.split('\n').filter(line => line.trim() !== '');
        res.send({ entries });
    });
});

// ▶️ Start the server
app.listen(port, () => {
    console.log(`✅ SOS API is running at http://localhost:${port}`);
});
