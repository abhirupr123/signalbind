import axios from 'axios';
import { generateReceipt } from '../utils/receiptGenerator.js';
import { runFullNumberVerificationFlow } from './numberVerification.js';


export async function retrieveSimSwapDate(phoneNumber: string) {
  const options = {
    method: 'POST',
    url: 'https://network-as-code.p-eu.rapidapi.com/passthrough/camara/v1/sim-swap/sim-swap/v0/retrieve-date',
    headers: {
      'x-rapidapi-key': process.env.RAPIDAPI_KEY!,
      'x-rapidapi-host': 'network-as-code.nokia.rapidapi.com',
      'Content-Type': 'application/json'
    },
    data: { phoneNumber }
  };

  try {
    const response = await axios.request(options);
    return response.data.latestSimChange;
  } catch (error: any) {
    console.error('SIM Swap API error:', error.message);
    throw new Error('SIM swap retrieval failed');
  }
}


export async function matchKycData(kycData: any) {
  const options = {
    method: 'POST',
    url: 'https://network-as-code.p-eu.rapidapi.com/passthrough/camara/v1/passthrough/kyc-match/v0.3/match',
    headers: {
      'x-rapidapi-key': process.env.RAPIDAPI_KEY!,
      'x-rapidapi-host': 'network-as-code.nokia.rapidapi.com',
      'Content-Type': 'application/json',
      'x-correlator': 'b4333c46-49c0-4f62-80d7-f0ef930f1c46'
    },
    data: kycData
  };

  try {
    const response = await axios.request(options);
    return {
      match: response.data.match,
      confidence: response.data.confidenceScore || 0
    };
  } catch (error: any) {
    console.error('KYC Match API error:', error.message);
    throw new Error('KYC match failed');
  }
}



export async function verifyPhoneNumber(phoneNumber: string) {

  try {
    const response = await runFullNumberVerificationFlow(phoneNumber);
    return response;
  } catch (error: any) {
    console.error('Number verification failed:', error.message);
    throw new Error('Telco verification error');
  }
}

export async function verifyNumberAndGenerateReceipt(phoneNumber: string, mockMode: boolean) {
  if (mockMode) {
    return generateReceipt({
      verified: false,
      recycledSince: '2025-09-20',
      reasonCode: 'SIM_RECYCLED',
    });
  }

  const headers = {
    Authorization: `Bearer ${process.env.NOKIA_API_KEY}`,
    'Content-Type': 'application/json',
  };

  const verifyRes = await axios.post('https://api.nokia.network/verify', { phoneNumber }, { headers });
  const recycleRes = await axios.post('https://api.nokia.network/recycle', { phoneNumber }, { headers });

  return generateReceipt({
    verified: verifyRes.data.verified,
    recycledSince: recycleRes.data.recycledSince,
    reasonCode: verifyRes.data.verified ? 'VERIFIED' : 'UNVERIFIED',
  });
}
