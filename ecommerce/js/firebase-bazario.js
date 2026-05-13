// ─── BAZARIO — FIRESTORE HELPERS ─────────────────────────────────────────────
// Depends on fbDb and fbAuth from js/firebase.js (must load after)

// ── Cart ──────────────────────────────────────────────────────────────────────
// ── Cart item sanitizer — strips nested arrays/objects not needed for display ──
function sanitizeCartItem(item) {
  return {
    id:            item.id            || '',
    name:          item.name          || '',
    brand:         item.brand         || '',
    category:      item.category      || '',
    price:         item.price         || 0,
    originalPrice: item.originalPrice || item.price || 0,
    discount:      item.discount      || 0,
    rating:        item.rating        || 0,
    emoji:         item.emoji         || '📦',
    qty:           item.qty           || 1,
    variant:       item.variant       || ''
  };
}

async function fbGetCart(uid) {
  const doc = await fbDb.collection('carts').doc(uid).get();
  return doc.exists ? (doc.data().items || []) : [];
}

async function fbSetCart(uid, items) {
  await fbDb.collection('carts').doc(uid).set({ items: items.map(sanitizeCartItem) });
}

// ── Wishlist ──────────────────────────────────────────────────────────────────
async function fbGetWishlist(uid) {
  const doc = await fbDb.collection('wishlists').doc(uid).get();
  return doc.exists ? (doc.data().items || []) : [];
}

async function fbSetWishlist(uid, items) {
  await fbDb.collection('wishlists').doc(uid).set({ items });
}

// ── Orders ────────────────────────────────────────────────────────────────────
async function fbAddOrder(uid, order) {
  await fbDb.collection('orders').doc(uid)
    .collection('items').doc(order.orderId).set(order);
}

async function fbGetOrders(uid) {
  const snap = await fbDb.collection('orders').doc(uid)
    .collection('items').orderBy('date', 'desc').get();
  return snap.docs.map(d => d.data());
}

// ── Addresses ─────────────────────────────────────────────────────────────────
async function fbGetAddresses(uid) {
  const doc = await fbDb.collection('addresses').doc(uid).get();
  return doc.exists ? (doc.data().items || []) : [];
}

async function fbSetAddresses(uid, items) {
  await fbDb.collection('addresses').doc(uid).set({ items });
}

// ─── GUEST DATA MERGE ─────────────────────────────────────────────────────────
// Migrates guest localStorage data → Firestore on login

async function fbMergeGuestData(uid, email) {
  const guestCart     = JSON.parse(localStorage.getItem('bazario_cart_guest') || '[]');
  const guestWishlist = JSON.parse(localStorage.getItem('bazario_wishlist_guest') || '[]');

  // Merge cart
  if (guestCart.length > 0) {
    const existingCart = await fbGetCart(uid);
    const merged = [...existingCart];
    guestCart.forEach(guestItem => {
      const idx = merged.findIndex(i => i.id === guestItem.id);
      if (idx > -1) {
        merged[idx].qty = (merged[idx].qty || 1) + (guestItem.qty || 1);
      } else {
        merged.push(guestItem);
      }
    });
    await fbSetCart(uid, merged);
    localStorage.removeItem('bazario_cart_guest');
  }

  // Merge wishlist
  if (guestWishlist.length > 0) {
    const existingWishlist = await fbGetWishlist(uid);
    const mergedIds = new Set(existingWishlist.map(i => i.id));
    guestWishlist.forEach(item => {
      if (!mergedIds.has(item.id)) existingWishlist.push(item);
    });
    await fbSetWishlist(uid, existingWishlist);
    localStorage.removeItem('bazario_wishlist_guest');
  }

  // Legacy localStorage cleanup
  localStorage.removeItem(`bazario_cart_${email}`);
  localStorage.removeItem(`bazario_wishlist_${email}`);
}
