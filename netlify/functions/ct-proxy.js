// =============================================================================
// ct-proxy.js — CleverTap API Proxy
// Netlify Serverless Function
//
// Keeps CT passcode server-side — never exposed to the browser.
// Handles two operations:
//   POST /ct-proxy/upload  → CT Upload API (write events + profiles)
//   GET  /ct-proxy/profile → CT Profile API (read any user's profile)
//
// Environment variables required (set in Netlify dashboard):
//   CT_ACCOUNT_ID  → your CleverTap Account ID
//   CT_PASSCODE    → your CleverTap Passcode (never in code/GitHub)
// =============================================================================

const CT_BASE_URL = 'https://api.clevertap.com';

// ─── CORS headers ─────────────────────────────────────────────────────────────
// Allow requests from your Netlify domain and localhost for development
const CORS_HEADERS = {
  'Access-Control-Allow-Origin':  '*',
  'Access-Control-Allow-Methods': 'GET, POST, OPTIONS',
  'Access-Control-Allow-Headers': 'Content-Type',
  'Content-Type':                 'application/json'
};

// ─── Main handler ──────────────────────────────────────────────────────────────
exports.handler = async (event) => {

  // Handle preflight CORS request
  if (event.httpMethod === 'OPTIONS') {
    return { statusCode: 204, headers: CORS_HEADERS, body: '' };
  }

  // Read CT credentials from environment variables
  const ACCOUNT_ID = process.env.CT_ACCOUNT_ID;
  const PASSCODE   = process.env.CT_PASSCODE;

  // Safety check — fail clearly if credentials are missing
  if (!ACCOUNT_ID || !PASSCODE) {
    return {
      statusCode: 500,
      headers: CORS_HEADERS,
      body: JSON.stringify({
        status:  'error',
        message: 'CT credentials not configured. Set CT_ACCOUNT_ID and CT_PASSCODE in Netlify environment variables.'
      })
    };
  }

  // ─── Standard CT auth headers ───────────────────────────────────────────────
  const CT_HEADERS = {
    'X-CleverTap-Account-Id': ACCOUNT_ID,
    'X-CleverTap-Passcode':   PASSCODE,
    'Content-Type':           'application/json'
  };

  // ─── Determine operation from path ──────────────────────────────────────────
  // Netlify Functions receive the full path — we check the last segment
  const path = event.path || '';

  try {

    // ═══════════════════════════════════════════════════════════════════════════
    // WRITE: POST /ct-proxy/upload
    // Forwards to CT Upload API — handles both events and profiles
    // Body: { d: [...records] }
    // ═══════════════════════════════════════════════════════════════════════════
    if (event.httpMethod === 'POST' && path.endsWith('/upload')) {

      // Parse request body
      let body;
      try {
        body = JSON.parse(event.body || '{}');
      } catch {
        return {
          statusCode: 400,
          headers: CORS_HEADERS,
          body: JSON.stringify({ status: 'error', message: 'Invalid JSON in request body' })
        };
      }

      // Validate — must have a 'd' array
      if (!body.d || !Array.isArray(body.d) || body.d.length === 0) {
        return {
          statusCode: 400,
          headers: CORS_HEADERS,
          body: JSON.stringify({ status: 'error', message: 'Request body must contain a non-empty "d" array' })
        };
      }

      // Forward to CT Upload API
      const ctResponse = await fetch(`${CT_BASE_URL}/1/upload`, {
        method:  'POST',
        headers: CT_HEADERS,
        body:    JSON.stringify(body)
      });

      const ctData = await ctResponse.json();

      return {
        statusCode: ctResponse.status,
        headers:    CORS_HEADERS,
        body:       JSON.stringify({
          status:     ctData.status || 'success',
          ct_response: ctData,
          records_sent: body.d.length,
          request_payload: body
        })
      };
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // READ: GET /ct-proxy/profile?email=user@example.com
    // Forwards to CT Profile API — returns full profile for any user
    // Query param: email (required)
    // ═══════════════════════════════════════════════════════════════════════════
    if (event.httpMethod === 'GET' && path.endsWith('/profile')) {

      const email = event.queryStringParameters?.email;

      if (!email) {
        return {
          statusCode: 400,
          headers: CORS_HEADERS,
          body: JSON.stringify({ status: 'error', message: 'email query parameter is required' })
        };
      }

      // Forward to CT Profile API
      const ctResponse = await fetch(
        `${CT_BASE_URL}/1/profile.json?email=${encodeURIComponent(email)}`,
        { method: 'GET', headers: CT_HEADERS }
      );

      const ctData = await ctResponse.json();

      // CT returns status: "success" or "fail"
      if (ctData.status === 'fail') {
        return {
          statusCode: 404,
          headers: CORS_HEADERS,
          body: JSON.stringify({
            status:  'not_found',
            message: `No profile found for ${email}`,
            email
          })
        };
      }

      return {
        statusCode: 200,
        headers:    CORS_HEADERS,
        body:       JSON.stringify({
            status:  'success',
            email,
            profile: ctData.profile || ctData,
            raw:     ctData  // full CT response for debugging
        })
     };
    }

    // ─── Unknown path ───────────────────────────────────────────────────────
    return {
      statusCode: 404,
      headers: CORS_HEADERS,
      body: JSON.stringify({
        status:  'error',
        message: `Unknown endpoint: ${path}. Available: POST /upload, GET /profile`
      })
    };

  } catch (err) {
    // Catch any unexpected errors — network failures, CT downtime etc.
    console.error('CT Proxy error:', err);
    return {
      statusCode: 502,
      headers: CORS_HEADERS,
      body: JSON.stringify({
        status:  'error',
        message: 'Proxy request failed — see Netlify function logs for details',
        error:   err.message
      })
    };
  }
};