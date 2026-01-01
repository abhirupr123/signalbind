import express from 'express';
import { verifyNumberAndGenerateReceipt, verifyPhoneNumber, retrieveSimSwapDate, matchKycData } from '../services/telcoApi.js';
import { generateReceipt } from '../utils/receiptGenerator.js';

const router = express.Router();


router.post('/', async (req, res) => {
  const { phoneNumber, mockMode = false, kycData } = req.body;

  try {
    // SIM Swap
    const simSwapDate = mockMode
      ? new Date(Date.now() - 5 * 24 * 60 * 60 * 1000).toISOString()
      : await retrieveSimSwapDate(phoneNumber);

    const daysSinceSwap = Math.floor((Date.now() - new Date(simSwapDate).getTime()) / (1000 * 60 * 60 * 24));
    const simSwapRisk = daysSinceSwap < 30;

    // Number Verification
    const verificationResult = mockMode
      ? { verified: true }
      : await verifyPhoneNumber(phoneNumber);

    // KYC Match
    const kycMatchResult = mockMode
      ? { match: true, confidence: 0.95 }
      : await matchKycData(kycData);

    // Generate Consent Receipt
    const receipt = generateReceipt({
      phoneNumber,
      verified: verificationResult.verified,
      simSwapDate,
      daysSinceSwap,
      simSwapRisk,
      kycMatch: kycMatchResult.match,
      kycConfidence: kycMatchResult.confidence,
      timestamp: new Date().toISOString()
    });

    res.json(receipt);
  } catch (error) {
    console.error('Verification error:', error);
    res.status(500).json({ error: 'Verification failed' });
  }
});

router.post('/number-verification', async (req, res) => {
  const { phoneNumber, mockMode = false } = req.body;
  try {
    const telcoData = mockMode
      ? { verified: false, recycledSince: '2025-09-20', reasonCode: 'SIM_RECYCLED' }
      : await verifyPhoneNumber(phoneNumber);

    const receipt = generateReceipt(telcoData);
    res.json(receipt);
  } catch (error) {
    res.status(500).json({ error: 'Verification failed' });
  }
});

router.post('/sim-swap', async (req, res) => {
  const { phoneNumber, mockMode = false } = req.body;

  try {
    const simSwapDate = mockMode
      ? new Date(Date.now() - 5 * 24 * 60 * 60 * 1000).toISOString() // 5 days ago
      : await retrieveSimSwapDate(phoneNumber);

    const daysSinceSwap = Math.floor((Date.now() - new Date(simSwapDate).getTime()) / (1000 * 60 * 60 * 24));
    const reasonCode = daysSinceSwap < 30 ? 'RECENT_SIM_SWAP' : 'SIM_STABLE';

    const receipt = {
      receiptId: `CR-${Date.now()}`,
      phoneNumber,
      simSwapDate,
      daysSinceSwap,
      reasonCode,
      timestamp: new Date().toISOString()
    };

    res.json(receipt);
  } catch (error) {
    res.status(500).json({ error: 'Verification failed' });
  }
});


export default router;
