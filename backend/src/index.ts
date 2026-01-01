import express from 'express';
import cors from 'cors';
import dotenv from 'dotenv';
import verifyRouter from './routes/verify.js';

dotenv.config();
const app = express();
app.use(cors());
app.use(express.json());

app.use('/verify', verifyRouter);

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`Server running on port ${PORT}`));
