import axios from 'axios';
import querystring from 'querystring';

const NAC_CLIENT_CREDENTIALS_URL = 'https://network-as-code.p-eu.rapidapi.com/oauth2/v1/auth/clientcredentials';
const WELL_KNOWN_METADATA_URL = 'https://network-as-code.p-eu.rapidapi.com/.well-known/openid-configuration';
const NUMBER_VERIFICATION_URL = 'https://network-as-code.p-eu.rapidapi.com/passthrough/camara/v1/number-verification/number-verification/v0/verify';
const RAPIDAPIHOST = 'network-as-code.nokia.rapidapi.com';
const REDIRECT_URI = 'https://xyz.requestcatcher.com/'; // Replace with your catcher URL
const RAPIDAPI_KEY = process.env.RAPIDAPI_KEY!;

export async function runFullNumberVerificationFlow(phoneNumber: string) {
  let CLIENT_ID, CLIENT_SECRET, AUTH_ENDPOINT, TOKEN_ENDPOINT, AUTH_CODE, ACCESS_TOKEN;

  // Step 1: Get Client Credentials
  const credentialsRes = await axios.get(NAC_CLIENT_CREDENTIALS_URL, {
    headers: {
      'content-type': 'application/json',
      'X-RapidAPI-Key': RAPIDAPI_KEY,
      'X-RapidAPI-Host': RAPIDAPIHOST,
    },
  });
  CLIENT_ID = credentialsRes.data.client_id;
  CLIENT_SECRET = credentialsRes.data.client_secret;

  // Step 2: Get Endpoints
  const endpointsRes = await axios.get(WELL_KNOWN_METADATA_URL, {
    headers: {
      'content-type': 'application/json',
      'X-RapidAPI-Key': RAPIDAPI_KEY,
      'X-RapidAPI-Host': RAPIDAPIHOST,
    },
  });
  AUTH_ENDPOINT = endpointsRes.data.authorization_endpoint;
  TOKEN_ENDPOINT = endpointsRes.data.token_endpoint;

  // Step 3: Get Authorization Code
  const authCodeUrl = `${AUTH_ENDPOINT}?scope=number-verification:verify&response_type=code&client_id=${CLIENT_ID}&redirect_uri=${encodeURIComponent(
    REDIRECT_URI
  )}&state=App-state&login_hint=%2B${phoneNumber}`;
  const redirectRes = await axios.get(authCodeUrl, { maxRedirects: 10, validateStatus: () => true });
  const finalUrl = redirectRes.request?.res?.responseUrl || redirectRes.config.url;
  const parsedUrl = new URL(finalUrl);
  AUTH_CODE = parsedUrl.searchParams.get('code');
  if (!AUTH_CODE) throw new Error('Authorization code not received');

  // Step 4: Get Access Token
  const tokenRes = await axios.post(
    TOKEN_ENDPOINT,
    querystring.stringify({
      client_id: CLIENT_ID,
      client_secret: CLIENT_SECRET,
      grant_type: 'authorization_code',
      code: AUTH_CODE,
    }),
    { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } }
  );
  ACCESS_TOKEN = tokenRes.data.access_token;

  // Step 5: Verify Phone Number
  const verifyRes = await axios.post(
    NUMBER_VERIFICATION_URL,
    { phoneNumber: `+${phoneNumber}` },
    {
      headers: {
        'content-type': 'application/json',
        'X-RapidAPI-Key': RAPIDAPI_KEY,
        'X-RapidAPI-Host': RAPIDAPIHOST,
        Authorization: `Bearer ${ACCESS_TOKEN}`,
      },
    }
  );

  return verifyRes.data;
}
