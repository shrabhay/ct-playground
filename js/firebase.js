// ─── FIREBASE CONFIG ─────────────────────────────────────────────────────────
// CT Playground — shared Firebase project (all verticals)
// API key is safe to commit — security enforced via Firestore Rules

const firebaseConfig = {
  apiKey:            "AIzaSyAtYscvwuRLa_7Z_ErPBRPkbmi-vzX7XIc",
  authDomain:        "ct-playground-0323.firebaseapp.com",
  projectId:         "ct-playground-0323",
  storageBucket:     "ct-playground-0323.firebasestorage.app",
  messagingSenderId: "636004903254",
  appId:             "1:636004903254:web:40d8d628b40ac5595c4ef7",
  measurementId:     "G-983RTYYSGW"
};

// ─── INITIALIZE ───────────────────────────────────────────────────────────────
firebase.initializeApp(firebaseConfig);

const fbAuth = firebase.auth();
const fbDb   = firebase.firestore();

// ─── SHARED HELPERS ───────────────────────────────────────────────────────────

function fbCurrentUser() {
  return fbAuth.currentUser;
}

// User profile — universal across all verticals
async function fbGetProfile(uid) {
  const doc = await fbDb.collection('users').doc(uid).get();
  return doc.exists ? doc.data() : null;
}

async function fbSetProfile(uid, data) {
  await fbDb.collection('users').doc(uid).set(data, { merge: true });
}
