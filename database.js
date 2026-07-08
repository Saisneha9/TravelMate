require('dotenv').config();
const express = require('express');
const mongoose = require('mongoose');
const bcrypt = require('bcryptjs');
const cors = require('cors');
const path = require('path');

const app = express();

// Middleware
app.use(cors());
app.use(express.json());

// MongoDB Atlas Connection
const connectToDB = async () => {
    try {
        const atlasUri = process.env.MONGODB_URI || 
            "mongodb+srv://login:12345@cluster-1.p03opc1.mongodb.net/data?retryWrites=true&w=majority&appName=Cluster-1";
        // Added database name "data" ^^^^
        
        await mongoose.connect(atlasUri, {
            useNewUrlParser: true,
            useUnifiedTopology: true,
            ssl: true,
            serverSelectionTimeoutMS: 5000
        });
        
        console.log('✅ Connected to MongoDB Atlas');
    } catch (err) {
        console.error('❌ MongoDB connection error:', err.message);
        process.exit(1);
    }
};

// User Schema
const userSchema = new mongoose.Schema({
    firstname: { type: String, required: true },
    lastname: { type: String, required: true },
    email: { 
        type: String, 
        required: true,
        unique: true,
        lowercase: true,
        match: [/\S+@\S+\.\S+/, 'is invalid']
    },
    password: { type: String, required: true }
}, { collection: 'login' }); // Explicit collection name

// Hash password before saving
userSchema.pre('save', async function(next) {
    if (this.isModified('password')) {
        this.password = await bcrypt.hash(this.password, 10);
    }
    next();
});

const User = mongoose.model('User', userSchema);

// Registration Route
app.post('/api/register', async (req, res) => {
    try {
        const { firstname, lastname, email, password } = req.body;
        
        // Basic validation
        if (!email || !password) {
            return res.status(400).json({ message: 'Email and password are required' });
        }

        const existingUser = await User.findOne({ email });
        if (existingUser) {
            return res.status(400).json({ message: 'Email already exists' });
        }
        
        const user = new User({ firstname, lastname, email, password });
        await user.save();
        
        res.status(201).json({ 
            message: 'Registration successful',
            user: {
                firstname: user.firstname,
                lastname: user.lastname,
                email: user.email,
                id: user._id
            }
        });
    } catch (err) {
        console.error('Registration error:', err);
        res.status(500).json({ message: 'Registration failed', error: err.message });
    }
});

// Serve signup-page.html from project root
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'signup-page.html'));
});

// Start Server
const PORT = process.env.PORT || 5000;

connectToDB().then(() => {
    app.listen(PORT, () => {
        console.log(`🚀 Server running on http://localhost:${PORT}`);
        console.log(`📄 Signup page: http://localhost:${PORT}/`);
    });
});
// Add this after your registration route (before server startup)

// Login Route
app.post('/api/login', async (req, res) => {
    try {
        const { email, password } = req.body;
        
        // Validation
        if (!email || !password) {
            return res.status(400).json({ success: false, message: 'Email and password are required' });
        }

        const user = await User.findOne({ email });
        if (!user) {
            return res.status(401).json({ success: false, message: 'Invalid credentials' });
        }

        const isMatch = await bcrypt.compare(password, user.password);
        if (!isMatch) {
            return res.status(401).json({ success: false, message: 'Invalid credentials' });
        }

        res.json({ 
            success: true,
            message: 'Login successful',
            user: {
                id: user._id,
                firstname: user.firstname,
                email: user.email
            }
        });
    } catch (err) {
        console.error('Login error:', err);
        res.status(500).json({ success: false, message: 'Login failed' });
    }
});

// Serve login page
app.get('/loginpage.html', (req, res) => {
    res.sendFile(path.join(__dirname, 'loginpage.html'));
});
// Add this with your other routes
app.get('/originalpage.html', (req, res) => {
    res.sendFile(path.join(__dirname, 'originalpage.html'));
});
// Login Route
app.post('/api/login', async (req, res) => {
    try {
        const { email, password } = req.body;
        
        // Validation
        if (!email || !password) {
            return res.status(400).json({ 
                success: false, 
                message: 'Email and password are required' 
            });
        }

        const user = await User.findOne({ email });
        if (!user) {
            return res.status(404).json({ 
                success: false, 
                message: 'User not found' 
            });
        }

        const isMatch = await bcrypt.compare(password, user.password);
        if (!isMatch) {
            return res.status(401).json({ 
                success: false, 
                message: 'Invalid password' 
            });
        }

        res.json({ 
            success: true,
            message: 'Login successful',
            user: {
                id: user._id,
                firstname: user.firstname,
                email: user.email
            }
        });
    } catch (err) {
        console.error('Login error:', err);
        res.status(500).json({ 
            success: false, 
            message: 'Login failed' 
        });
    }
});