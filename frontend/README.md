# SignalBind Android App

A Kotlin Android application built with Jetpack Compose that allows users to verify mobile number ownership and freshness using GSMA Open Gateway APIs via Nokia Network-as-Code.

## Features

### Mobile Number Verification
- **Mobile Number Verification**: Users can manually enter their mobile number for verification
- **Mock Mode**: Toggle switch to simulate risky scenarios during testing
- **Consent Receipt Display**: Shows detailed verification results including:
  - Receipt ID
  - Verification status (true/false)
  - RecycledSince date
  - Reason code
  - Timestamp

### KYC Identity Verification
- **Comprehensive KYC Form**: Collects all required identity data for GSMA Open Gateway's KYC Match v0.3 API
- **Required Fields**: Phone number, ID document, name, given name, family name, birthdate, gender, email
- **Optional Fields**: Middle names, family name at birth, kana names, complete address information
- **Form Validation**: Real-time validation with error messages for required fields
- **Date Picker**: User-friendly date selection for birthdate
- **Gender Dropdown**: Predefined options (Male, Female, Other)
- **Match Results**: Detailed verification results with confidence scores and match details
- **Loading States**: Visual feedback during form submission

### General Features
- **Material3 Design**: Modern UI with Material3 styling
- **Loading Indicators**: Visual feedback during API calls
- **Error Handling**: User-friendly error messages
- **Navigation**: Seamless navigation between screens

## Architecture

- **UI**: Jetpack Compose with Material3
- **State Management**: ViewModel with StateFlow
- **Navigation**: Compose Navigation
- **Networking**: Retrofit with OkHttp
- **Data Models**: Gson for JSON serialization

## API Integration

The app integrates with GSMA Open Gateway APIs through Nokia Network-as-Code for two main services:

1. **Mobile Number Verification**: Verifies mobile number ownership and freshness
2. **KYC Match v0.3**: Performs identity verification using comprehensive user data

The backend service endpoints should be configured in `NetworkModule.kt`.

## Setup

1. Update the `BASE_URL` in `NetworkModule.kt` with your actual API endpoint
2. Build and run the app on an Android device or emulator
3. Choose from two verification options:
   - **Mobile Number Verification**: Enter a mobile number and tap "Verify & Consent"
   - **KYC Identity Verification**: Tap "KYC Identity Verification" to access the comprehensive form
4. View the verification results on the respective result screens

## Requirements

- Android API 24+ (Android 7.0)
- Internet permission for API calls
- Kotlin 2.0.0+
- Jetpack Compose BOM 2024.04.01
