# 📱 Telco-Verified Consent Receipt for Secure Onboarding

## 🚀 Overview
Digital platforms like UPI apps, wallets, and microloan services rely heavily on mobile numbers and OTPs for user verification. However, mobile numbers are frequently recycled or reassigned, especially in prepaid-heavy markets. This creates a blind spot where fraudsters can hijack dormant accounts or impersonate users.

This project implements a **Telco-Verified Consent Receipt** system using **GSMA Open Gateway APIs via Nokia Network-as-Code**. It ensures that user consent is tied to the rightful SIM holder, reducing fraud and improving trust in digital financial services.

---

## 🌍 What This Project Actually Is
This project is a **trust layer for digital onboarding**. Instead of relying only on OTPs or basic KYC checks, it introduces a **telco-verified consent receipt**—a digital proof that the person giving consent is the rightful owner of the SIM and phone number at that moment.

Think of it as a **network-signed certificate of identity**: when someone signs up for a loan, wallet, or UPI app, the telecom operator confirms that the number is valid, hasn’t been recycled recently, and belongs to the current SIM holder. That confirmation is then bound into a receipt that can be audited later.

---

## 🎯 Why We Are Using It
- **Close a Blind Spot in Identity Verification**  
  OTP-based verification assumes that whoever receives the OTP owns the number. But in prepaid-heavy markets, numbers are recycled frequently. Fraudsters exploit this gap by hijacking dormant accounts or impersonating users.

- **Strengthen Consent Capture**  
  Regulators (like RBI in India) require platforms to maintain consent logs. By tying consent to a telco-verified identity, platforms can prove that the user was genuine at the time of onboarding.

- **Reduce Fraud in Financial Inclusion**  
  Microfinance, rural banking, and UPI apps often deal with vulnerable populations. A telco-backed verification layer reduces fraud risk without adding friction for genuine users.

---

## 🌟 Impact It Might Have
- **For Users**: Safer onboarding, fewer cases of account hijack, and more trust in digital financial services.  
- **For Platforms**: Stronger compliance with regulatory mandates, reduced fraud losses, and a competitive edge by offering “network-verified” trust.  
- **For Regulators**: A reproducible audit trail that shows consent was captured with telecom-level verification, aligning with emerging data protection and cybersecurity rules.  
- **For the Ecosystem**: Builds a foundation for telco-backed digital identity, which can extend beyond finance into education, healthcare, and e-commerce.

---

## 🏗️ Architecture

### Frontend
- **Android App (Jetpack Compose)**
  - Captures mobile number from device
  - Displays consent receipt and audit status

### Backend
- **Node.js + TypeScript Service**
  - Routes: `/verify`, `/sim-swap`, `/number-verification`
  - Integrates with Nokia Network-as-Code APIs
  - Generates signed consent receipts (JWT)
  - Stores evidence bundle with API response IDs and timestamps

### APIs Used
- **Number Verification API** → Confirms SIM holder owns the number
- **Number Recycling API** → Checks if the number was reassigned recently
- **SIM Swap Detection API (optional)** → Flags recent SIM changes

---

## 🔑 Authentication Flow
1. **Get Client Credentials** → Retrieve `client_id` and `client_secret`
2. **Get Endpoints** → Authorization + Token endpoints from metadata
3. **Get Authorization Code** → Redirect via RequestCatcher to capture `code`
4. **Exchange Code for Access Token** → OAuth2 token endpoint
5. **Verify Number** → Call Number Verification API with `Bearer <ACCESS_TOKEN>`

---

## 📂 Project Structure
```
/src
  /routes
    verifyRouter.ts
    simSwapRouter.ts
    numberVerificationRouter.ts
  /services
    numberVerificationService.ts
    receiptService.ts
  index.ts
```

---

## ⚙️ Technical Stack
- **Frontend**: Android (Jetpack Compose, Kotlin)
- **Backend**: Node.js, TypeScript, Express.js
- **Database (optional)**: MongoDB / PostgreSQL for audit logs
- **APIs**: Nokia Network-as-Code (GSMA Open Gateway)
- **Security**: OAuth2, JWT, TLS

---

## 🧑‍💻 Running Locally
1. Clone the repo:

```bash
git clone https://github.com/signalbind.git
cd telco-consent-receipt
```

2. Install dependencies:

```bash
npm install
```

3. Set environment variables in `.env`:

```bash
RAPIDAPI_KEY=<your-key>
REDIRECT_URI=https://<your-url>.requestcatcher.com/
```

4. Start the backend:

```bash
npm run dev
```

5. Test with curl:

```bash
curl -X POST http://localhost:3000/verify \
  -H "Content-Type: application/json" \
  -d '{"phoneNumber": "+99999991001"}'
```

---

## 📊 Demo Strategy
- Live demo on Android device with real SIM context
- UI shows real-time decision flow and audit metadata

---

## 🔮 Future Scope
- Extend to UPI onboarding, wallet login, microloan disbursal
- Align with RBI consent log mandates & India’s DPDPA
- Scalable across fintech, edtech, and e-commerce platforms
- Telco-backed trust infrastructure for digital identity


