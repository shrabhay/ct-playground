// =============================================================================
// catalogue.js — Bazario Shared Product Catalogue
// Single source of truth for all product data across the site.
// Loaded by: listing.html, product.html
// Usage: const products = catalogue['Electronics'];
// =============================================================================

const catalogue = {

  // ─── ELECTRONICS ──────────────────────────────────────────────────────────
  'Electronics': [
    {
      id: 'E001', name: 'Samsung Galaxy M14 5G (4GB RAM, 128GB)', brand: 'Samsung',
      category: 'Electronics', price: 10999, originalPrice: 14999, discount: 27,
      rating: 4.2, reviews: 231104, emoji: '📱', tags: ['bestseller'],
      highlights: ['5G ready', '50MP triple camera', '6000mAh battery', 'Android 13'],
      variants: { label: 'Storage', options: ['4GB+64GB', '4GB+128GB', '6GB+128GB'] },
      desc: 'The Samsung Galaxy M14 5G is powered by the Exynos 1330 processor and comes with a 50MP triple camera setup. The 6000mAh battery keeps you going all day with 5G connectivity.',
      specs: [['Brand','Samsung'],['Model','Galaxy M14 5G'],['RAM','4 GB'],['Storage','128 GB'],['Display','6.6" FHD+ TFT'],['Battery','6000 mAh'],['Processor','Exynos 1330'],['OS','Android 13'],['Camera','50MP + 5MP + 2MP']]
    },
    {
      id: 'E002', name: 'Apple iPhone 13 (128GB) — Midnight', brand: 'Apple',
      category: 'Electronics', price: 49999, originalPrice: 69900, discount: 28,
      rating: 4.6, reviews: 189432, emoji: '📱', tags: ['assured'],
      highlights: ['A15 Bionic chip', '12MP dual camera', '5G capable', 'Face ID'],
      variants: { label: 'Storage', options: ['128GB', '256GB', '512GB'] },
      desc: 'iPhone 13 features the A15 Bionic chip, the most powerful chip ever in a smartphone. Advanced dual-camera system with Night mode, Cinematic mode, and Photographic Styles.',
      specs: [['Brand','Apple'],['Model','iPhone 13'],['RAM','4 GB'],['Storage','128 GB'],['Display','6.1" Super Retina XDR'],['Battery','3227 mAh'],['Processor','A15 Bionic'],['OS','iOS 17'],['Camera','12MP + 12MP']]
    },
    {
      id: 'E003', name: 'OnePlus Nord CE3 Lite 5G (8GB RAM, 128GB)', brand: 'OnePlus',
      category: 'Electronics', price: 17999, originalPrice: 25999, discount: 31,
      rating: 4.1, reviews: 94210, emoji: '📱', tags: ['assured'],
      highlights: ['Snapdragon 695', '108MP camera', '67W SUPERVOOC', '5000mAh'],
      variants: { label: 'Storage', options: ['8GB+128GB', '8GB+256GB'] },
      desc: 'OnePlus Nord CE3 Lite 5G is equipped with Snapdragon 695 5G processor and a 108MP primary camera. The 67W SUPERVOOC charging fills the 5000mAh battery in just 44 minutes.',
      specs: [['Brand','OnePlus'],['Model','Nord CE3 Lite'],['RAM','8 GB'],['Storage','128 GB'],['Display','6.72" FHD+ LCD'],['Battery','5000 mAh'],['Processor','Snapdragon 695'],['OS','OxygenOS 13.1'],['Camera','108MP + 2MP + 2MP']]
    },
    {
      id: 'E004', name: 'boAt Airdopes 141 TWS Earbuds — 42H Playback', brand: 'boAt',
      category: 'Electronics', price: 1299, originalPrice: 4499, discount: 71,
      rating: 4.1, reviews: 124832, emoji: '🎧', tags: ['bestseller'],
      highlights: ['42H total playback', 'ENx tech', 'IPX4 water resistant', 'Instant connect'],
      variants: { label: 'Colour', options: ['Active Black', 'Bold Blue', 'Mint Green', 'Cherry Red'] },
      desc: 'boAt Airdopes 141 TWS earbuds deliver 42 hours of total playback with ENx technology for clear calls. IPX4 water resistance makes them perfect for workouts.',
      specs: [['Brand','boAt'],['Model','Airdopes 141'],['Driver Size','6mm'],['Frequency','20Hz–20KHz'],['Battery','30mAh + 300mAh case'],['Connectivity','Bluetooth 5.3'],['Water resistance','IPX4'],['Mic','ENx tech']]
    },
    {
      id: 'E005', name: 'Noise ColorFit Pro 4 Smartwatch — 1.72" Display', brand: 'Noise',
      category: 'Electronics', price: 2499, originalPrice: 6999, discount: 64,
      rating: 4.3, reviews: 86421, emoji: '⌚', tags: ['bestseller'],
      highlights: ['1.72" TFT display', 'GPS built-in', '100+ sports modes', 'SpO2 monitoring'],
      variants: { label: 'Colour', options: ['Jet Black', 'Silver Grey', 'Rose Gold', 'Teal'] },
      desc: 'The Noise ColorFit Pro 4 features a large 1.72" TFT display with built-in GPS for accurate tracking. Monitor SpO2, heart rate and stress levels across 100+ sports modes.',
      specs: [['Brand','Noise'],['Model','ColorFit Pro 4'],['Display','1.72" TFT'],['Battery','300 mAh'],['GPS','Built-in'],['Water resistance','IP68'],['Sensors','Heart rate, SpO2, Stress'],['Sports modes','100+']]
    },
    {
      id: 'E006', name: 'Logitech MK215 Wireless Keyboard & Mouse Combo', brand: 'Logitech',
      category: 'Electronics', price: 1795, originalPrice: 2795, discount: 36,
      rating: 4.4, reviews: 52341, emoji: '⌨️', tags: ['assured'],
      highlights: ['2.4GHz wireless', '24-month battery life', 'Compact design', 'Plug & play'],
      variants: { label: 'Version', options: ['Standard', 'Hindi keys'] },
      desc: 'The Logitech MK215 Wireless Combo brings together a comfortable keyboard and precise mouse via a single nano USB receiver. Battery life up to 24 months on keyboard.',
      specs: [['Brand','Logitech'],['Model','MK215'],['Connectivity','2.4GHz wireless'],['Range','10 metres'],['Keyboard battery','24 months'],['Mouse battery','12 months'],['DPI','1000 DPI'],['OS','Windows/Mac/Chrome']]
    },
    {
      id: 'E007', name: 'Sony Bravia 43" 4K Ultra HD Android TV', brand: 'Sony',
      category: 'Electronics', price: 44990, originalPrice: 64990, discount: 31,
      rating: 4.5, reviews: 38921, emoji: '📺', tags: ['assured'],
      highlights: ['4K HDR processor', 'Google TV', 'Dolby Audio', 'HDMI x3'],
      variants: { label: 'Size', options: ['43"', '50"', '55"', '65"'] },
      desc: 'Sony Bravia 43" 4K TV powered by Google TV gives you a world of entertainment in stunning 4K HDR. Dolby Audio and X1 processor deliver exceptional picture and sound quality.',
      specs: [['Brand','Sony'],['Model','KD-43X74L'],['Resolution','4K Ultra HD'],['Display type','LED'],['HDR','HDR10, HLG'],['Smart','Google TV'],['HDMI','3 ports'],['USB','2 ports']]
    },
    {
      id: 'E008', name: 'HP 15s Core i5 12th Gen Laptop (8GB, 512GB SSD)', brand: 'HP',
      category: 'Electronics', price: 49990, originalPrice: 67990, discount: 26,
      rating: 4.3, reviews: 21043, emoji: '💻', tags: ['new'],
      highlights: ['Intel Core i5-1235U', '8GB DDR4 RAM', '512GB SSD', 'Windows 11'],
      variants: { label: 'RAM', options: ['8GB + 512GB SSD', '16GB + 512GB SSD', '16GB + 1TB SSD'] },
      desc: 'HP 15s laptop powered by Intel Core i5-1235U 12th Gen delivers fast, fluid performance. With 8GB RAM and 512GB SSD, multitasking and boot times are significantly faster.',
      specs: [['Brand','HP'],['Processor','Intel Core i5-1235U'],['RAM','8 GB DDR4'],['Storage','512 GB SSD'],['Display','15.6" FHD IPS'],['Graphics','Intel Iris Xe'],['OS','Windows 11 Home'],['Weight','1.69 kg']]
    },
    {
      id: 'E009', name: 'Redmi Note 12 Pro 5G (8GB RAM, 256GB)', brand: 'Xiaomi',
      category: 'Electronics', price: 19999, originalPrice: 27999, discount: 29,
      rating: 4.3, reviews: 112043, emoji: '📱', tags: ['bestseller'],
      highlights: ['50MP OIS camera', 'Dimensity 1080', '67W turbo charging', 'AMOLED display'],
      variants: { label: 'Storage', options: ['6GB+128GB', '8GB+256GB'] },
      desc: 'Redmi Note 12 Pro 5G features a stunning 6.67" AMOLED display, a 50MP OIS camera for sharp photos, and 67W turbo charging to power up in minutes.',
      specs: [['Brand','Xiaomi'],['Model','Note 12 Pro 5G'],['RAM','8 GB'],['Storage','256 GB'],['Display','6.67" AMOLED'],['Battery','5000 mAh'],['Processor','Dimensity 1080'],['Camera','50MP OIS']]
    },
    {
      id: 'E010', name: 'Sony WH-1000XM5 Wireless Noise Cancelling Headphones', brand: 'Sony',
      category: 'Electronics', price: 24990, originalPrice: 34990, discount: 29,
      rating: 4.7, reviews: 43210, emoji: '🎧', tags: ['assured'],
      highlights: ['Industry-leading ANC', '30-hour battery', 'Multipoint connection', 'Hi-Res Audio'],
      variants: { label: 'Colour', options: ['Black', 'Silver'] },
      desc: 'Sony WH-1000XM5 headphones deliver industry-leading noise cancellation with eight microphones and two processors for the best call quality and ANC performance.',
      specs: [['Brand','Sony'],['Model','WH-1000XM5'],['Driver','30mm'],['Battery','30 hours'],['Charging','USB-C, 3min = 3hrs'],['ANC','Yes'],['Multipoint','Yes'],['Weight','250g']]
    },
    {
      id: 'E011', name: 'Canon EOS 1500D 24.1MP DSLR Camera with 18-55mm Lens', brand: 'Canon',
      category: 'Electronics', price: 32995, originalPrice: 44995, discount: 27,
      rating: 4.5, reviews: 28432, emoji: '📷', tags: ['assured'],
      highlights: ['24.1MP APS-C sensor', '18-55mm kit lens', 'Full HD video', 'Wi-Fi + NFC'],
      variants: { label: 'Kit', options: ['Body only', 'With 18-55mm lens', 'With 18-55mm + 55-250mm'] },
      desc: 'Canon EOS 1500D is a beginner-friendly DSLR with a 24.1MP sensor and the DIGIC 4+ image processor. Built-in Wi-Fi and NFC allow easy sharing to your smartphone.',
      specs: [['Brand','Canon'],['Model','EOS 1500D'],['Resolution','24.1 MP'],['Sensor','APS-C CMOS'],['ISO','100-6400'],['Video','Full HD 1080p'],['Screen','3" fixed LCD'],['Weight','475g']]
    },
    {
      id: 'E012', name: 'Lenovo IdeaPad Slim 3 Ryzen 5 Laptop (8GB, 512GB)', brand: 'Lenovo',
      category: 'Electronics', price: 42990, originalPrice: 57990, discount: 26,
      rating: 4.2, reviews: 18932, emoji: '💻', tags: ['new'],
      highlights: ['AMD Ryzen 5 7520U', '8GB LPDDR5 RAM', '512GB SSD', 'Windows 11'],
      variants: { label: 'RAM', options: ['8GB + 512GB', '16GB + 512GB'] },
      desc: 'Lenovo IdeaPad Slim 3 with AMD Ryzen 5 7520U delivers smooth everyday computing. The 15.6" FHD display and slim form factor make it a great everyday companion.',
      specs: [['Brand','Lenovo'],['Processor','AMD Ryzen 5 7520U'],['RAM','8 GB LPDDR5'],['Storage','512 GB SSD'],['Display','15.6" FHD IPS'],['OS','Windows 11 Home'],['Battery','45 Whr'],['Weight','1.62 kg']]
    },
    {
      id: 'E013', name: 'realme Buds Air 5 Pro ANC TWS Earbuds', brand: 'realme',
      category: 'Electronics', price: 3499, originalPrice: 5999, discount: 42,
      rating: 4.2, reviews: 34821, emoji: '🎧', tags: ['new'],
      highlights: ['50dB ANC', '38H total playback', 'LDAC Hi-Res Audio', 'IP55 rated'],
      variants: { label: 'Colour', options: ['Black', 'White'] },
      desc: 'realme Buds Air 5 Pro offers industry-leading 50dB ANC for an immersive audio experience. LDAC support brings Hi-Res Audio wirelessly with up to 38 hours total playback.',
      specs: [['Brand','realme'],['ANC','50dB'],['Battery','50mAh + 460mAh'],['Playback','38 hours total'],['Audio','LDAC Hi-Res'],['Water resistance','IP55'],['Connectivity','Bluetooth 5.3']]
    },
    {
      id: 'E014', name: 'Samsung 32" Full HD Smart Monitor — M5', brand: 'Samsung',
      category: 'Electronics', price: 17990, originalPrice: 24990, discount: 28,
      rating: 4.4, reviews: 12043, emoji: '🖥️', tags: ['bestseller'],
      highlights: ['32" Full HD VA panel', 'Smart TV apps built-in', 'USB-C 65W charging', 'Speakers included'],
      variants: { label: 'Size', options: ['27"', '32"', '43"'] },
      desc: 'Samsung Smart Monitor M5 works as a monitor, smart TV, and wireless hub — all in one. Stream Netflix, YouTube and more without a PC, or use it as a productivity display.',
      specs: [['Brand','Samsung'],['Size','32"'],['Resolution','1920×1080 FHD'],['Panel','VA'],['Refresh rate','60Hz'],['Smart','Tizen OS'],['USB-C','65W charging'],['HDMI','1 port']]
    },
    {
      id: 'E015', name: 'Zebronics Zeb-County2 Wireless Bluetooth Speaker', brand: 'Zebronics',
      category: 'Electronics', price: 1299, originalPrice: 2499, discount: 48,
      rating: 4.1, reviews: 29841, emoji: '🔊', tags: ['bestseller'],
      highlights: ['10W output', '8-hour playback', 'TWS mode', 'FM Radio + USB'],
      variants: { label: 'Colour', options: ['Black', 'Blue', 'Red'] },
      desc: 'Zebronics Zeb-County2 wireless speaker packs 10W output into a compact design. With 8 hours of playback, TWS pairing, FM radio, and USB playback, it covers all your audio needs.',
      specs: [['Brand','Zebronics'],['Output','10W'],['Battery','8 hours'],['Connectivity','Bluetooth 5.0'],['Extra','FM, USB, AUX, TWS'],['Water resistance','IPX5']]
    },
    {
      id: 'E016', name: 'Apple iPad 10th Gen (64GB, Wi-Fi) — Blue', brand: 'Apple',
      category: 'Electronics', price: 34900, originalPrice: 44900, discount: 22,
      rating: 4.6, reviews: 41032, emoji: '📱', tags: ['assured'],
      highlights: ['A14 Bionic chip', '10.9" Liquid Retina', '12MP front camera', 'USB-C'],
      variants: { label: 'Storage', options: ['64GB', '256GB'] },
      desc: 'The iPad 10th generation features an all-new design with a 10.9" Liquid Retina display and A14 Bionic chip. The landscape 12MP Ultra Wide front camera is perfect for video calls.',
      specs: [['Brand','Apple'],['Model','iPad 10th Gen'],['Chip','A14 Bionic'],['Display','10.9" Liquid Retina'],['Storage','64 GB'],['Front camera','12MP Ultra Wide'],['Connector','USB-C'],['Weight','477g']]
    },
    {
      id: 'E017', name: 'Mi Smart Band 8 Activity Tracker', brand: 'Xiaomi',
      category: 'Electronics', price: 2999, originalPrice: 3999, discount: 25,
      rating: 4.3, reviews: 67821, emoji: '⌚', tags: ['bestseller'],
      highlights: ['1.62" AMOLED display', '16-day battery', '150+ workout modes', 'Blood oxygen'],
      variants: { label: 'Strap', options: ['Black', 'Orange', 'Pink', 'Blue'] },
      desc: 'Mi Smart Band 8 features a vivid 1.62" AMOLED display with 16-day battery life. Track 150+ workout modes, monitor blood oxygen, heart rate, and sleep quality.',
      specs: [['Brand','Xiaomi'],['Display','1.62" AMOLED'],['Battery','16 days'],['Workout modes','150+'],['Water resistance','5ATM'],['Sensors','Heart rate, SpO2, Sleep'],['GPS','Connected GPS']]
    },
    {
      id: 'E018', name: 'TP-Link Archer AX23 AX1800 Wi-Fi 6 Router', brand: 'TP-Link',
      category: 'Electronics', price: 3499, originalPrice: 5499, discount: 36,
      rating: 4.4, reviews: 18432, emoji: '📡', tags: ['new'],
      highlights: ['Wi-Fi 6 AX1800', 'Dual band', 'OFDMA technology', '4 antennas'],
      variants: { label: 'Version', options: ['Standard', 'With modem'] },
      desc: 'TP-Link Archer AX23 brings Wi-Fi 6 technology to your home with AX1800 speeds. OFDMA and MU-MIMO technology supports more devices simultaneously with less congestion.',
      specs: [['Brand','TP-Link'],['Standard','Wi-Fi 6 (802.11ax)'],['Speed','AX1800'],['Band','Dual band'],['Ports','1 WAN + 4 LAN'],['Antennas','4 external'],['Security','WPA3']]
    },
    {
      id: 'E019', name: 'Realme Narzo 60x 5G (4GB RAM, 64GB)', brand: 'realme',
      category: 'Electronics', price: 9999, originalPrice: 13999, discount: 29,
      rating: 4.0, reviews: 43210, emoji: '📱', tags: ['new'],
      highlights: ['Dimensity 6100+', '50MP AI camera', '33W SUPERVOOC', 'AMOLED display'],
      variants: { label: 'Storage', options: ['4GB+64GB', '6GB+128GB'] },
      desc: 'Realme Narzo 60x 5G brings 5G connectivity at an accessible price with Dimensity 6100+ chipset, a 50MP AI camera, and a vibrant AMOLED display.',
      specs: [['Brand','realme'],['Processor','Dimensity 6100+'],['RAM','4 GB'],['Storage','64 GB'],['Display','6.72" AMOLED'],['Battery','5000 mAh'],['Camera','50MP + 2MP'],['Charging','33W SUPERVOOC']]
    },
    {
      id: 'E020', name: 'JBL Flip 6 Portable Bluetooth Speaker', brand: 'JBL',
      category: 'Electronics', price: 8999, originalPrice: 13999, discount: 36,
      rating: 4.5, reviews: 52043, emoji: '🔊', tags: ['bestseller'],
      highlights: ['IP67 waterproof', '12-hour battery', 'PartyBoost feature', 'USB-C charging'],
      variants: { label: 'Colour', options: ['Black', 'Blue', 'Red', 'Teal', 'Squad'] },
      desc: 'JBL Flip 6 is a portable Bluetooth speaker with bold JBL Original Pro Sound. IP67 waterproof and dustproof, with 12 hours of playtime and JBL PartyBoost to connect multiple speakers.',
      specs: [['Brand','JBL'],['Output','30W'],['Battery','12 hours'],['Water resistance','IP67'],['Charging','USB-C'],['PartyBoost','Yes'],['Dimensions','178×68×72mm'],['Weight','550g']]
    },
  ],

  // ─── FASHION ──────────────────────────────────────────────────────────────
  'Fashion': [
    {
      id: 'F001', name: "Levi's 511 Slim Fit Men's Jeans — Dark Blue", brand: "Levi's",
      category: 'Fashion', price: 2399, originalPrice: 3999, discount: 40,
      rating: 4.3, reviews: 9142, emoji: '👖', tags: ['bestseller'],
      highlights: ['Slim fit', '99% cotton', 'Machine washable', 'Available in 28–38 waist'],
      variants: { label: 'Size (Waist)', options: ['28','30','32','34','36','38'] },
      desc: "Levi's 511 Slim Fit jeans sit below the waist and are slim through the thigh with a narrow leg opening. Made from stretch denim for all-day comfort in a classic dark blue wash.",
      specs: [['Brand',"Levi's"],['Fit','Slim'],['Fabric','99% cotton, 1% elastane'],['Wash','Dark Blue'],['Closure','Zip fly'],['Care','Machine wash cold']]
    },
    {
      id: 'F002', name: 'H&M Women Floral Wrap Dress — Multicolour', brand: 'H&M',
      category: 'Fashion', price: 1299, originalPrice: 2499, discount: 48,
      rating: 4.1, reviews: 6832, emoji: '👗', tags: ['new'],
      highlights: ['Regular fit', 'V-neck wrap style', 'Woven fabric', 'Belt included'],
      variants: { label: 'Size', options: ['XS','S','M','L','XL'] },
      desc: 'Elegant H&M floral wrap dress with a V-neck wrap style and attached belt. Perfect for brunch, office, or evening out.',
      specs: [['Brand','H&M'],['Fit','Regular'],['Neckline','V-neck'],['Fabric','Viscose woven'],['Length','Midi'],['Care','Hand wash cold']]
    },
    {
      id: 'F003', name: 'Nike Air Max 270 Running Shoes for Men', brand: 'Nike',
      category: 'Fashion', price: 6995, originalPrice: 11995, discount: 42,
      rating: 4.5, reviews: 18743, emoji: '👟', tags: ['assured'],
      highlights: ['Max Air heel unit', 'Foam midsole', 'Mesh upper', 'Available UK 6–12'],
      variants: { label: 'Size (UK)', options: ['6','7','8','9','10','11','12'] },
      desc: "Nike Air Max 270 features Nike's first lifestyle Air unit. The mesh upper ensures breathability while the foam midsole delivers lightweight cushioning.",
      specs: [['Brand','Nike'],['Model','Air Max 270'],['Upper','Mesh + synthetic'],['Sole','Rubber'],['Closure','Lace-up'],['Cushioning','Max Air 270']]
    },
    {
      id: 'F004', name: 'Allen Solly Men Slim Fit Formal Shirt — White', brand: 'Allen Solly',
      category: 'Fashion', price: 949, originalPrice: 1799, discount: 47,
      rating: 4.2, reviews: 14231, emoji: '👔', tags: ['bestseller'],
      highlights: ['Slim fit collar', '100% cotton', 'Full sleeves', 'Machine wash safe'],
      variants: { label: 'Size', options: ['S','M','L','XL','XXL'] },
      desc: 'Allen Solly slim fit formal shirt in crisp white, made from 100% cotton for breathability and comfort. Ideal for office or formal occasions.',
      specs: [['Brand','Allen Solly'],['Fit','Slim'],['Fabric','100% Cotton'],['Sleeve','Full sleeve'],['Collar','Classic formal'],['Care','Machine wash safe']]
    },
    {
      id: 'F005', name: 'Fabindia Women Kurta Set — Ethnic Print', brand: 'Fabindia',
      category: 'Fashion', price: 1999, originalPrice: 3499, discount: 43,
      rating: 4.4, reviews: 7621, emoji: '👘', tags: ['new'],
      highlights: ['Cotton fabric', 'A-line kurta', 'Dupatta included', 'Straight palazzo'],
      variants: { label: 'Size', options: ['XS','S','M','L','XL','XXL'] },
      desc: 'Fabindia ethnic print kurta set in breathable cotton. Includes A-line kurta, straight palazzo, and matching dupatta — perfect for festive occasions.',
      specs: [['Brand','Fabindia'],['Fabric','Cotton'],['Set','Kurta + Palazzo + Dupatta'],['Fit','A-line'],['Care','Dry clean recommended']]
    },
    {
      id: 'F006', name: 'Puma Men Hoodie Sweatshirt — Navy Blue', brand: 'Puma',
      category: 'Fashion', price: 1599, originalPrice: 3499, discount: 54,
      rating: 4.3, reviews: 11082, emoji: '🧥', tags: ['assured'],
      highlights: ['Fleece inner lining', 'Kangaroo pocket', 'Drawstring hood', 'Regular fit'],
      variants: { label: 'Size', options: ['S','M','L','XL','XXL'] },
      desc: 'Puma hoodie sweatshirt with a soft fleece lining, kangaroo pocket, and adjustable drawstring hood. Ideal for casual days and outdoor activities.',
      specs: [['Brand','Puma'],['Fit','Regular'],['Fabric','Cotton fleece'],['Hood','Yes, drawstring'],['Pocket','Kangaroo'],['Care','Machine washable']]
    },
    {
      id: 'F007', name: 'Adidas Originals Women Track Pants — Black', brand: 'Adidas',
      category: 'Fashion', price: 2099, originalPrice: 3499, discount: 40,
      rating: 4.3, reviews: 8921, emoji: '👗', tags: ['bestseller'],
      highlights: ['3-Stripe detail', 'Elastic waistband', 'Side pockets', 'Regular fit'],
      variants: { label: 'Size', options: ['XS','S','M','L','XL'] },
      desc: 'Adidas Originals track pants with the iconic 3-Stripe detail. Comfortable elastic waistband and side pockets for everyday casual wear.',
      specs: [['Brand','Adidas'],['Fit','Regular'],['Fabric','Polyester'],['Waist','Elastic + drawstring'],['Pockets','2 side'],['Care','Machine wash']]
    },
    {
      id: 'F008', name: 'W Women Flared Kurti — Multiprint', brand: 'W',
      category: 'Fashion', price: 799, originalPrice: 1499, discount: 47,
      rating: 4.2, reviews: 13421, emoji: '👗', tags: ['bestseller'],
      highlights: ['Flared hem', 'Rayon fabric', 'Printed design', '3/4 sleeves'],
      variants: { label: 'Size', options: ['XS','S','M','L','XL','XXL'] },
      desc: 'W Women flared kurti in soft rayon fabric with a vibrant multiprint design. The flared hem and 3/4 sleeves give it a modern ethnic look.',
      specs: [['Brand','W'],['Fabric','Rayon'],['Fit','Flared'],['Sleeve','3/4 sleeves'],['Neck','Round neck'],['Care','Machine wash gentle']]
    },
    {
      id: 'F009', name: 'Peter England Men Slim Chinos — Khaki', brand: 'Peter England',
      category: 'Fashion', price: 1349, originalPrice: 2299, discount: 41,
      rating: 4.1, reviews: 9832, emoji: '👖', tags: ['assured'],
      highlights: ['Slim fit', 'Stretch fabric', 'Flat front', 'Machine washable'],
      variants: { label: 'Size (Waist)', options: ['28','30','32','34','36','38'] },
      desc: 'Peter England slim chinos in khaki with stretch fabric for comfort. Flat front design and slim fit make these versatile for casual and semi-formal occasions.',
      specs: [['Brand','Peter England'],['Fit','Slim'],['Fabric','98% cotton, 2% elastane'],['Front','Flat'],['Closure','Zip + button'],['Care','Machine wash cold']]
    },
    {
      id: 'F010', name: 'Woodland Men Leather Casual Shoes — Brown', brand: 'Woodland',
      category: 'Fashion', price: 3299, originalPrice: 5499, discount: 40,
      rating: 4.4, reviews: 21043, emoji: '👞', tags: ['bestseller'],
      highlights: ['Full grain leather', 'Rubber sole', 'Lace-up closure', 'UK 6–12'],
      variants: { label: 'Size (UK)', options: ['6','7','8','9','10','11','12'] },
      desc: 'Woodland casual shoes crafted from full grain leather for durability and style. Thick rubber sole provides excellent grip on all terrain.',
      specs: [['Brand','Woodland'],['Material','Full grain leather'],['Sole','Rubber'],['Closure','Lace-up'],['Care','Leather conditioner recommended']]
    },
    {
      id: 'F011', name: 'Zara Men Slim Fit Blazer — Navy', brand: 'Zara',
      category: 'Fashion', price: 3990, originalPrice: 6490, discount: 39,
      rating: 4.3, reviews: 5432, emoji: '🧥', tags: ['new'],
      highlights: ['Slim fit', 'Notched lapel', 'Two-button closure', 'Lined interior'],
      variants: { label: 'Size', options: ['S','M','L','XL'] },
      desc: 'Zara slim fit blazer in classic navy with a modern cut. Notched lapel and two-button closure — perfect for business casual or smart occasions.',
      specs: [['Brand','Zara'],['Fit','Slim'],['Fabric','Polyester blend'],['Lapel','Notched'],['Buttons','2'],['Lining','Full lining']]
    },
    {
      id: 'F012', name: "Bata Women Ballet Flats — Nude", brand: 'Bata',
      category: 'Fashion', price: 999, originalPrice: 1799, discount: 44,
      rating: 4.2, reviews: 16821, emoji: '👡', tags: ['bestseller'],
      highlights: ['Synthetic upper', 'Cushioned insole', 'Slip-on', 'UK 3–8'],
      variants: { label: 'Size (UK)', options: ['3','4','5','6','7','8'] },
      desc: 'Bata ballet flats in nude with a cushioned insole for all-day comfort. Classic slip-on design that pairs effortlessly with any outfit.',
      specs: [['Brand','Bata'],['Upper','Synthetic'],['Sole','TPR'],['Closure','Slip-on'],['Insole','Cushioned'],['Heel height','Flat']]
    },
    {
      id: 'F013', name: 'US Polo Assn Men Polo T-Shirt — Red', brand: 'US Polo Assn',
      category: 'Fashion', price: 699, originalPrice: 1299, discount: 46,
      rating: 4.1, reviews: 22043, emoji: '👕', tags: ['bestseller'],
      highlights: ['100% cotton', 'Polo collar', 'Short sleeves', 'Regular fit'],
      variants: { label: 'Size', options: ['S','M','L','XL','XXL'] },
      desc: 'US Polo Assn polo t-shirt in vibrant red made from 100% cotton for breathability. Classic polo collar and embroidered logo give it a smart casual look.',
      specs: [['Brand','US Polo Assn'],['Fabric','100% Cotton'],['Fit','Regular'],['Collar','Polo'],['Sleeve','Short'],['Care','Machine wash']]
    },
    {
      id: 'F014', name: 'Tommy Hilfiger Men Analog Watch — Silver', brand: 'Tommy Hilfiger',
      category: 'Fashion', price: 5995, originalPrice: 9995, discount: 40,
      rating: 4.5, reviews: 8321, emoji: '⌚', tags: ['assured'],
      highlights: ['Stainless steel case', 'Leather strap', 'Quartz movement', 'Water resistant 50m'],
      variants: { label: 'Strap', options: ['Leather', 'Metal bracelet'] },
      desc: 'Tommy Hilfiger analog watch with a clean silver stainless steel case and genuine leather strap. Quartz movement and 50m water resistance.',
      specs: [['Brand','Tommy Hilfiger'],['Case','Stainless steel 40mm'],['Strap','Genuine leather'],['Movement','Quartz'],['Water resistance','50m'],['Glass','Mineral crystal']]
    },
    {
      id: 'F015', name: 'Jockey Men Cotton Brief — Pack of 3', brand: 'Jockey',
      category: 'Fashion', price: 549, originalPrice: 849, discount: 35,
      rating: 4.5, reviews: 43210, emoji: '🧦', tags: ['bestseller'],
      highlights: ['100% cotton', 'Elastic waistband', 'Pack of 3', 'Available S–XXL'],
      variants: { label: 'Size', options: ['S','M','L','XL','XXL'] },
      desc: 'Jockey cotton briefs in a pack of 3 — made from 100% cotton for comfort and breathability. Wide elastic waistband and full coverage seat.',
      specs: [['Brand','Jockey'],['Fabric','100% Cotton'],['Pack','3 briefs'],['Waistband','Wide elastic'],['Care','Machine wash']]
    },
    {
      id: 'F016', name: 'Mango Women High-Waist Straight Jeans — White', brand: 'Mango',
      category: 'Fashion', price: 2799, originalPrice: 4499, discount: 38,
      rating: 4.2, reviews: 4321, emoji: '👖', tags: ['new'],
      highlights: ['High-waist', 'Straight leg', 'Stretch denim', 'Five pockets'],
      variants: { label: 'Size (Waist)', options: ['26','28','30','32','34'] },
      desc: 'Mango high-waist straight jeans in clean white stretch denim. The straight leg silhouette and high waist give a modern, elongating look.',
      specs: [['Brand','Mango'],['Fit','Straight'],['Rise','High waist'],['Fabric','Stretch denim'],['Pockets','5'],['Care','Machine wash inside out']]
    },
    {
      id: 'F017', name: 'Wildcraft Unisex Daypack Backpack 33L — Grey', brand: 'Wildcraft',
      category: 'Fashion', price: 1799, originalPrice: 2999, discount: 40,
      rating: 4.3, reviews: 12043, emoji: '🎒', tags: ['bestseller'],
      highlights: ['33L capacity', 'Laptop sleeve', 'Rain cover', 'Ergonomic straps'],
      variants: { label: 'Colour', options: ['Grey', 'Black', 'Blue', 'Olive'] },
      desc: 'Wildcraft Daypack 33L backpack with a dedicated laptop sleeve, rain cover, and ergonomic padded straps. Perfect for daily commutes, college, and weekend trips.',
      specs: [['Brand','Wildcraft'],['Capacity','33 litres'],['Laptop sleeve','Yes (up to 15.6")'],['Rain cover','Yes'],['Material','Polyester 600D'],['Weight','750g']]
    },
    {
      id: 'F018', name: 'Van Heusen Men Slim Fit Formal Trousers — Black', brand: 'Van Heusen',
      category: 'Fashion', price: 1499, originalPrice: 2499, discount: 40,
      rating: 4.2, reviews: 8932, emoji: '👖', tags: ['assured'],
      highlights: ['Slim fit', 'Wrinkle resistant', 'Stretch fabric', 'Machine washable'],
      variants: { label: 'Size (Waist)', options: ['28','30','32','34','36','38'] },
      desc: 'Van Heusen formal trousers with wrinkle-resistant stretch fabric — always looking sharp from morning to evening. Slim fit for a clean, professional silhouette.',
      specs: [['Brand','Van Heusen'],['Fit','Slim'],['Fabric','Polyester-Viscose blend'],['Waist','Flat front'],['Care','Machine wash cold']]
    },
    {
      id: 'F019', name: 'Crocs Classic Clog — Navy', brand: 'Crocs',
      category: 'Fashion', price: 2799, originalPrice: 3995, discount: 30,
      rating: 4.4, reviews: 31042, emoji: '👟', tags: ['bestseller'],
      highlights: ['Croslite foam', 'Lightweight', 'Pivoting heel strap', 'Ventilation ports'],
      variants: { label: 'Size (UK)', options: ['4','5','6','7','8','9','10','11','12'] },
      desc: 'The Crocs Classic Clog is lightweight, fun, and buoyant — with Croslite foam cushioning that makes it incredibly comfortable. Pivoting heel strap for a secure fit.',
      specs: [['Brand','Crocs'],['Material','Croslite foam'],['Closure','Slip-on + heel strap'],['Weight','~170g per shoe'],['Care','Rinse with water']]
    },
    {
      id: 'F020', name: 'HRX by Hrithik Roshan Men Training T-Shirt — Black', brand: 'HRX',
      category: 'Fashion', price: 699, originalPrice: 1299, discount: 46,
      rating: 4.1, reviews: 18432, emoji: '👕', tags: ['new'],
      highlights: ['Dry-fit fabric', 'Round neck', 'Slim fit', 'Reflective detail'],
      variants: { label: 'Size', options: ['S','M','L','XL','XXL'] },
      desc: 'HRX training t-shirt in dry-fit fabric that wicks sweat away during intense workouts. Slim fit with reflective detail for low-light visibility.',
      specs: [['Brand','HRX'],['Fabric','Polyester dry-fit'],['Fit','Slim'],['Neck','Round'],['Care','Machine wash cold'],['Reflective','Yes']]
    },
  ],

  // ─── GROCERY ──────────────────────────────────────────────────────────────
  'Grocery': [
    {
      id: 'G001', name: 'Tata Salt Iodised — 1kg Pack of 6', brand: 'Tata',
      category: 'Grocery', price: 162, originalPrice: 180, discount: 10,
      rating: 4.6, reviews: 43210, emoji: '🧂', tags: ['bestseller'],
      highlights: ['Iodised salt', 'Fine grain', 'Free flowing', 'BIS certified'],
      variants: { label: 'Pack Size', options: ['1kg', 'Pack of 2', 'Pack of 6'] },
      desc: "Tata Salt is India's most trusted salt brand. Vacuum evaporated iodised salt with consistent quality.",
      specs: [['Brand','Tata'],['Type','Iodised salt'],['Grain','Fine'],['Weight','1 kg'],['Iodine','Min 15 ppm'],['Certification','BIS']]
    },
    {
      id: 'G002', name: 'Aashirvaad Atta Whole Wheat — 10kg', brand: 'Aashirvaad',
      category: 'Grocery', price: 379, originalPrice: 430, discount: 12,
      rating: 4.5, reviews: 89321, emoji: '🌾', tags: ['bestseller'],
      highlights: ['100% whole wheat', 'Soft rotis', 'No maida', 'Sampoorna atta'],
      variants: { label: 'Weight', options: ['5kg', '10kg', '20kg'] },
      desc: 'Aashirvaad Sampoorna Atta is made from 100% whole wheat. Carefully selected wheat gives you soft, nutritious rotis every time.',
      specs: [['Brand','Aashirvaad'],['Type','Whole wheat atta'],['Weight','10 kg'],['Additives','None']]
    },
    {
      id: 'G003', name: 'Amul Butter Pasteurised — 500g', brand: 'Amul',
      category: 'Grocery', price: 265, originalPrice: 285, discount: 7,
      rating: 4.7, reviews: 32104, emoji: '🧈', tags: ['assured'],
      highlights: ['Pasteurised', 'Table butter', 'Rich & creamy', 'Refrigerate on delivery'],
      variants: { label: 'Weight', options: ['100g', '200g', '500g'] },
      desc: "Amul Pasteurised Butter is India's favourite table butter — rich, creamy, and perfect for cooking, baking, or spreading.",
      specs: [['Brand','Amul'],['Type','Pasteurised butter'],['Weight','500 g'],['Storage','Refrigerate below 4°C']]
    },
    {
      id: 'G004', name: 'Nescafé Classic Instant Coffee — 200g', brand: 'Nescafé',
      category: 'Grocery', price: 499, originalPrice: 620, discount: 20,
      rating: 4.4, reviews: 21843, emoji: '☕', tags: ['bestseller'],
      highlights: ['100% pure coffee', 'Rich aroma', 'Easy to brew', 'Resealable pack'],
      variants: { label: 'Weight', options: ['50g', '100g', '200g', '400g'] },
      desc: 'Nescafé Classic brings you the rich, full-bodied flavour of roasted coffee beans. Easy to brew in seconds — just add hot water or milk.',
      specs: [['Brand','Nescafé'],['Type','Instant coffee'],['Weight','200 g'],['Caffeine','~60mg per cup']]
    },
    {
      id: 'G005', name: 'Fortune Sunlite Sunflower Oil — 5L', brand: 'Fortune',
      category: 'Grocery', price: 689, originalPrice: 780, discount: 12,
      rating: 4.3, reviews: 18420, emoji: '🫙', tags: ['assured'],
      highlights: ['Light & digestible', 'High smoke point', 'Vitamin E enriched', 'Less absorption'],
      variants: { label: 'Volume', options: ['1L', '2L', '5L', '15L tin'] },
      desc: 'Fortune Sunlite sunflower oil is light, digestible, and enriched with Vitamin E. Its high smoke point makes it ideal for all types of Indian cooking.',
      specs: [['Brand','Fortune'],['Type','Sunflower oil'],['Volume','5 litres'],['Vitamin E','Yes'],['Cholesterol','0mg per serving']]
    },
    {
      id: 'G006', name: 'Tata Tea Premium — 500g', brand: 'Tata Tea',
      category: 'Grocery', price: 249, originalPrice: 289, discount: 14,
      rating: 4.5, reviews: 54321, emoji: '🍵', tags: ['bestseller'],
      highlights: ['Bold flavour', 'Aromatic blend', 'Dust tea', 'Best with milk'],
      variants: { label: 'Weight', options: ['250g', '500g', '1kg'] },
      desc: "Tata Tea Premium is a blend of the best Assam teas — bold, aromatic, and perfect for a strong cup of Indian masala chai.",
      specs: [['Brand','Tata Tea'],['Type','CTC dust tea'],['Weight','500 g'],['Origin','Assam']]
    },
    {
      id: 'G007', name: 'Maggi 2-Minute Noodles Masala — Pack of 12', brand: 'Maggi',
      category: 'Grocery', price: 168, originalPrice: 204, discount: 18,
      rating: 4.6, reviews: 98432, emoji: '🍜', tags: ['bestseller'],
      highlights: ['Ready in 2 minutes', 'Iconic masala taste', 'Whole wheat noodles option', 'Pack of 12'],
      variants: { label: 'Pack', options: ['Pack of 6', 'Pack of 12', 'Pack of 24'] },
      desc: "Maggi 2-Minute Noodles — India's favourite instant noodle with the iconic masala taste. Ready in just 2 minutes, anytime hunger strikes.",
      specs: [['Brand','Maggi'],['Flavour','Masala'],['Pack','12 × 70g'],['Cooking time','2 minutes']]
    },
    {
      id: 'G008', name: 'Dabur Honey — 1kg NMR Tested', brand: 'Dabur',
      category: 'Grocery', price: 349, originalPrice: 425, discount: 18,
      rating: 4.5, reviews: 43210, emoji: '🍯', tags: ['assured'],
      highlights: ['NMR tested purity', 'No added sugar', 'Natural antioxidants', 'Immunity boosting'],
      variants: { label: 'Weight', options: ['250g', '500g', '1kg'] },
      desc: 'Dabur Honey is 100% pure and NMR tested — the highest international standard for honey purity. Rich in natural antioxidants and natural sugars.',
      specs: [['Brand','Dabur'],['Type','Natural honey'],['Weight','1 kg'],['Testing','NMR tested'],['Additives','None']]
    },
    {
      id: 'G009', name: "Quaker Oats Original — 2kg", brand: 'Quaker',
      category: 'Grocery', price: 349, originalPrice: 449, discount: 22,
      rating: 4.4, reviews: 28432, emoji: '🌾', tags: ['new'],
      highlights: ['100% whole grain', 'High fibre', 'No added sugar', 'Ready in 2 minutes'],
      variants: { label: 'Weight', options: ['500g', '1kg', '2kg'] },
      desc: 'Quaker Oats are made from 100% whole grain oats — a nutritious start to your day that keeps you full longer. High in fibre and naturally cholesterol-free.',
      specs: [['Brand','Quaker'],['Type','Rolled oats'],['Weight','2 kg'],['Fibre','4g per serving'],['Sugar','0g added']]
    },
    {
      id: 'G010', name: 'Surf Excel Easy Wash Detergent Powder — 4kg', brand: 'Surf Excel',
      category: 'Grocery', price: 469, originalPrice: 560, discount: 16,
      rating: 4.5, reviews: 61043, emoji: '🧺', tags: ['bestseller'],
      highlights: ['Removes tough stains', 'Colour safe', 'For hand wash', 'Jasmine fragrance'],
      variants: { label: 'Weight', options: ['1kg', '2kg', '4kg', '8kg'] },
      desc: 'Surf Excel Easy Wash detergent powder removes tough stains with less effort. Colour-safe formula with a fresh jasmine fragrance.',
      specs: [['Brand','Surf Excel'],['Type','Detergent powder'],['Weight','4 kg'],['Use','Hand wash'],['Fragrance','Jasmine']]
    },
    {
      id: 'G011', name: 'Britannia Good Day Butter Cookies — 600g', brand: 'Britannia',
      category: 'Grocery', price: 149, originalPrice: 175, discount: 15,
      rating: 4.4, reviews: 38210, emoji: '🍪', tags: ['bestseller'],
      highlights: ['Real butter taste', 'Crunchy texture', 'Pack of 4 × 150g', 'Shelf life 6 months'],
      variants: { label: 'Flavour', options: ['Butter', 'Cashew', 'Pista Badam'] },
      desc: "Britannia Good Day butter cookies with the rich taste of real butter. Crunchy, indulgent, and perfect with a cup of tea.",
      specs: [['Brand','Britannia'],['Type','Butter cookies'],['Weight','600g (4 × 150g)'],['Shelf life','6 months']]
    },
    {
      id: 'G012', name: 'Haldiram Aloo Bhujia — 1kg', brand: "Haldiram's",
      category: 'Grocery', price: 299, originalPrice: 360, discount: 17,
      rating: 4.6, reviews: 52043, emoji: '🥜', tags: ['bestseller'],
      highlights: ['Crispy texture', 'Authentic recipe', 'Airtight packaging', 'No artificial flavours'],
      variants: { label: 'Weight', options: ['200g', '500g', '1kg'] },
      desc: "Haldiram's Aloo Bhujia — crispy, spicy, and utterly addictive. Made with the authentic Haldiram recipe and packed in airtight packaging for freshness.",
      specs: [['Brand',"Haldiram's"],['Type','Namkeen / Snack'],['Weight','1 kg'],['Artificial flavours','None'],['Shelf life','6 months']]
    },
  ],

  // ─── HOME & FURNITURE ─────────────────────────────────────────────────────
  'Home & Furniture': [
    {
      id: 'H001', name: 'Wakefit Orthopaedic Memory Foam Mattress — Queen', brand: 'Wakefit',
      category: 'Home & Furniture', price: 12999, originalPrice: 19999, discount: 35,
      rating: 4.5, reviews: 48321, emoji: '🛏️', tags: ['bestseller'],
      highlights: ['7-zone orthopaedic support', '20cm height', '100 night trial', 'CertiPUR-US certified'],
      variants: { label: 'Size', options: ['Single', 'Double', 'Queen', 'King'] },
      desc: 'Wakefit Orthopaedic Memory Foam Mattress provides zoned support for your entire body. The memory foam conforms to your shape for pressure relief and spinal alignment.',
      specs: [['Brand','Wakefit'],['Type','Memory foam'],['Height','20 cm'],['Trial','100 nights'],['Certification','CertiPUR-US'],['Warranty','10 years']]
    },
    {
      id: 'H002', name: 'Solimo Engineered Wood Study Table — Walnut', brand: 'Solimo',
      category: 'Home & Furniture', price: 4499, originalPrice: 7999, discount: 44,
      rating: 4.2, reviews: 12043, emoji: '🪑', tags: ['assured'],
      highlights: ['Engineered wood', 'Easy assembly', 'Storage shelf', '120x60cm tabletop'],
      variants: { label: 'Colour', options: ['Walnut', 'Wenge', 'White'] },
      desc: 'Solimo study table in engineered wood with a spacious 120×60cm tabletop and lower storage shelf. Easy to assemble with all tools and instructions included.',
      specs: [['Brand','Solimo'],['Material','Engineered wood'],['Dimensions','120 × 60 × 75 cm'],['Weight capacity','50 kg'],['Assembly','Self-assembly']]
    },
    {
      id: 'H003', name: 'Story@Home 300 TC Cotton Double Bedsheet Set', brand: 'Story@Home',
      category: 'Home & Furniture', price: 799, originalPrice: 1799, discount: 56,
      rating: 4.3, reviews: 34210, emoji: '🛌', tags: ['bestseller'],
      highlights: ['300 TC pure cotton', '2 pillow covers', 'Machine washable', 'Reactive print'],
      variants: { label: 'Design', options: ['Floral Blue', 'Geometric Grey', 'Paisley Red', 'Abstract Green'] },
      desc: 'Story@Home 300 TC pure cotton double bedsheet with reactive print for vibrant, fade-resistant colours. Includes 2 matching pillow covers.',
      specs: [['Brand','Story@Home'],['Thread count','300 TC'],['Fabric','100% cotton'],['Set includes','1 bedsheet + 2 pillow covers'],['Care','Machine wash cold']]
    },
    {
      id: 'H004', name: 'IKEA KALLAX Shelf Unit — White', brand: 'IKEA',
      category: 'Home & Furniture', price: 8999, originalPrice: 11999, discount: 25,
      rating: 4.4, reviews: 21043, emoji: '🪑', tags: ['assured'],
      highlights: ['4-cube storage', 'Wall mountable', 'Easy assembly', 'Can be used vertically or horizontally'],
      variants: { label: 'Size', options: ['1×2', '2×2', '2×4', '4×4'] },
      desc: 'IKEA KALLAX shelf unit with 4 cubes for versatile storage. Can be mounted on wall or used as a room divider. Works both vertically and horizontally.',
      specs: [['Brand','IKEA'],['Material','Particleboard'],['Dimensions','77 × 77 cm (2×2)'],['Max load per cube','13 kg'],['Assembly','Self-assembly']]
    },
    {
      id: 'H005', name: 'Pigeon Induction Base Non-Stick Tawa — 28cm', brand: 'Pigeon',
      category: 'Home & Furniture', price: 499, originalPrice: 899, discount: 44,
      rating: 4.3, reviews: 43210, emoji: '🍳', tags: ['bestseller'],
      highlights: ['28cm diameter', 'Induction compatible', 'PFOA free', 'Riveted handle'],
      variants: { label: 'Size', options: ['24cm', '28cm', '30cm'] },
      desc: 'Pigeon non-stick tawa with induction-compatible base. PFOA-free coating ensures safe cooking. Sturdy riveted handle for easy handling.',
      specs: [['Brand','Pigeon'],['Diameter','28 cm'],['Compatible','Gas + Induction'],['Coating','PFOA-free non-stick'],['Handle','Riveted bakelite']]
    },
    {
      id: 'H006', name: 'Amazon Basics Microfibre Comforter — Double (Grey)', brand: 'Amazon Basics',
      category: 'Home & Furniture', price: 1299, originalPrice: 2499, discount: 48,
      rating: 4.3, reviews: 28432, emoji: '🛌', tags: ['assured'],
      highlights: ['Microfibre fill', 'Box stitch', 'Machine washable', 'Double size'],
      variants: { label: 'Size', options: ['Single', 'Double', 'King'] },
      desc: 'Amazon Basics microfibre comforter with box stitch design to keep fill evenly distributed. Soft, lightweight, and machine washable.',
      specs: [['Brand','Amazon Basics'],['Fill','Microfibre'],['Size','Double (200×200cm)'],['Stitch','Box stitch'],['Care','Machine washable']]
    },
    {
      id: 'H007', name: 'Cello Maxfresh Airtight Container Set — 12 Pieces', brand: 'Cello',
      category: 'Home & Furniture', price: 999, originalPrice: 1799, discount: 44,
      rating: 4.4, reviews: 38210, emoji: '🫙', tags: ['bestseller'],
      highlights: ['12-piece set', 'Airtight lid', 'Microwave safe', 'BPA free'],
      variants: { label: 'Set', options: ['6-piece', '12-piece', '18-piece'] },
      desc: 'Cello Maxfresh airtight container set keeps food fresh for longer. Microwave-safe, BPA-free, and dishwasher-safe for easy cleaning.',
      specs: [['Brand','Cello'],['Pieces','12'],['Material','Polypropylene'],['Microwave safe','Yes'],['Dishwasher safe','Yes'],['BPA','Free']]
    },
    {
      id: 'H008', name: 'Philips LED Bulb 9W — Pack of 6 (Cool White)', brand: 'Philips',
      category: 'Home & Furniture', price: 449, originalPrice: 699, discount: 36,
      rating: 4.5, reviews: 54321, emoji: '💡', tags: ['bestseller'],
      highlights: ['9W = 70W equivalent', '6500K cool white', '15,000 hour life', 'Pack of 6'],
      variants: { label: 'Colour', options: ['Cool White 6500K', 'Warm White 3000K', 'Neutral White 4000K'] },
      desc: 'Philips LED bulbs — energy-efficient, long-lasting, and bright. 9W replaces a 70W incandescent bulb with 15,000 hours of rated life.',
      specs: [['Brand','Philips'],['Wattage','9W'],['Equivalent','70W incandescent'],['Colour temp','6500K'],['Life','15,000 hours'],['Base','B22 bayonet']]
    },
    {
      id: 'H009', name: 'Godrej Interio Office Chair — Black', brand: 'Godrej',
      category: 'Home & Furniture', price: 8999, originalPrice: 13999, discount: 36,
      rating: 4.3, reviews: 12043, emoji: '🪑', tags: ['new'],
      highlights: ['Lumbar support', 'Height adjustable', 'Mesh back', '5-point base'],
      variants: { label: 'Colour', options: ['Black', 'Grey', 'Blue'] },
      desc: 'Godrej Interio office chair with mesh back for breathability, adjustable lumbar support, and height-adjustable seat. Sturdy 5-point base with smooth-rolling casters.',
      specs: [['Brand','Godrej'],['Back','Mesh'],['Seat height','Adjustable 43–53cm'],['Weight capacity','100 kg'],['Base','5-point chrome']]
    },
    {
      id: 'H010', name: 'Milton Thermosteel Flip Lid Flask 1000ml', brand: 'Milton',
      category: 'Home & Furniture', price: 699, originalPrice: 1199, discount: 42,
      rating: 4.5, reviews: 43210, emoji: '🫙', tags: ['bestseller'],
      highlights: ['24 hours hot/cold', 'Stainless steel', 'Leak-proof', '1000ml capacity'],
      variants: { label: 'Size', options: ['500ml', '750ml', '1000ml'] },
      desc: 'Milton Thermosteel flask keeps beverages hot for 24 hours and cold for 24 hours. 304 food-grade stainless steel with a leak-proof flip lid.',
      specs: [['Brand','Milton'],['Capacity','1000 ml'],['Material','304 stainless steel'],['Hot/Cold','24 hours'],['Lid','Flip lid, leak-proof']]
    },
    {
      id: 'H011', name: 'Nilkamal Plastic Foldable Table — White', brand: 'Nilkamal',
      category: 'Home & Furniture', price: 1999, originalPrice: 3499, discount: 43,
      rating: 4.1, reviews: 8932, emoji: '🪑', tags: ['new'],
      highlights: ['Foldable design', 'High-density plastic', '60×90cm tabletop', 'Weight capacity 80kg'],
      variants: { label: 'Colour', options: ['White', 'Blue', 'Green'] },
      desc: 'Nilkamal foldable plastic table — lightweight, durable, and easy to store. Perfect for outdoor use, parties, and extra workspace at home.',
      specs: [['Brand','Nilkamal'],['Material','High-density polyethylene'],['Dimensions','90 × 60 × 74 cm'],['Weight capacity','80 kg'],['Foldable','Yes']]
    },
    {
      id: 'H012', name: 'Borosil Vision Glass Set — 6 Pieces 350ml', brand: 'Borosil',
      category: 'Home & Furniture', price: 549, originalPrice: 899, discount: 39,
      rating: 4.6, reviews: 32104, emoji: '🥃', tags: ['bestseller'],
      highlights: ['Borosilicate glass', 'Microwave safe', 'Dishwasher safe', 'Set of 6'],
      variants: { label: 'Size', options: ['250ml', '350ml', '500ml'] },
      desc: 'Borosil Vision Glasses are made from thermal shock-resistant borosilicate glass. Microwave and dishwasher safe — perfect for hot and cold beverages.',
      specs: [['Brand','Borosil'],['Material','Borosilicate glass'],['Capacity','350 ml'],['Set','6 glasses'],['Microwave safe','Yes'],['Dishwasher safe','Yes']]
    },
  ],

  // ─── APPLIANCES ───────────────────────────────────────────────────────────
  'Appliances': [
    {
      id: 'A001', name: 'LG 8kg 5 Star Inverter Fully-Automatic Washing Machine', brand: 'LG',
      category: 'Appliances', price: 34990, originalPrice: 49990, discount: 30,
      rating: 4.5, reviews: 28432, emoji: '🫧', tags: ['assured'],
      highlights: ['Inverter Direct Drive', 'Steam wash', 'ThinQ AI', '10yr motor warranty'],
      variants: { label: 'Capacity', options: ['6kg', '7kg', '8kg', '10kg'] },
      desc: 'LG fully-automatic washing machine with Inverter Direct Drive motor and 6 Motion DD technology. Steam wash removes 99.9% of allergens.',
      specs: [['Brand','LG'],['Capacity','8 kg'],['Energy rating','5 star'],['Motor warranty','10 years'],['Smart','ThinQ AI'],['Programs','14']]
    },
    {
      id: 'A002', name: 'Prestige Iris 750W Mixer Grinder with 3 Jars', brand: 'Prestige',
      category: 'Appliances', price: 1795, originalPrice: 3495, discount: 49,
      rating: 4.4, reviews: 42219, emoji: '🍹', tags: ['bestseller'],
      highlights: ['750W motor', '3 stainless steel jars', 'Whipper jar', '3-speed control'],
      variants: { label: 'Colour', options: ['White', 'Red', 'Black'] },
      desc: 'Prestige Iris 750W mixer grinder with 3 stainless steel jars for grinding, chutney, and blending. 3-speed control with pulse function.',
      specs: [['Brand','Prestige'],['Wattage','750W'],['Jars','3 (Wet, Dry, Chutney)'],['Speeds','3 + Pulse'],['Warranty','2 years']]
    },
    {
      id: 'A003', name: 'Voltas 1.5 Ton 5 Star Inverter Split AC', brand: 'Voltas',
      category: 'Appliances', price: 38990, originalPrice: 55000, discount: 29,
      rating: 4.3, reviews: 19832, emoji: '❄️', tags: ['assured'],
      highlights: ['5-star energy rating', '4-in-1 adjustable mode', 'Anti-bacterial filter', 'Copper condenser'],
      variants: { label: 'Capacity', options: ['1 Ton', '1.5 Ton', '2 Ton'] },
      desc: 'Voltas inverter split AC with 5-star energy rating saves up to 45% on electricity bills. 4-in-1 adjustable mode and copper condenser for long-lasting performance.',
      specs: [['Brand','Voltas'],['Capacity','1.5 Ton'],['Energy rating','5 Star'],['Condenser','Copper'],['Filter','Anti-bacterial'],['Inverter','Yes']]
    },
    {
      id: 'A004', name: 'Atomberg Renesa 1200mm BLDC Ceiling Fan + Remote', brand: 'Atomberg',
      category: 'Appliances', price: 2849, originalPrice: 4500, discount: 37,
      rating: 4.4, reviews: 22310, emoji: '💨', tags: ['new'],
      highlights: ['BLDC motor', '65% energy saving', 'Remote control', 'Anti-dust coating'],
      variants: { label: 'Colour', options: ['White', 'Midnight Black', 'Ivory'] },
      desc: 'Atomberg Renesa BLDC ceiling fan with remote consumes only 28W at top speed — saving 65% energy versus conventional fans. Anti-dust coating for easy cleaning.',
      specs: [['Brand','Atomberg'],['Sweep','1200mm'],['Motor','BLDC'],['Wattage','28W (top speed)'],['Remote','Yes'],['Warranty','2 years']]
    },
    {
      id: 'A005', name: 'Bajaj Majesty RCX 21 Rice Cooker — 1.8L', brand: 'Bajaj',
      category: 'Appliances', price: 1599, originalPrice: 2499, discount: 36,
      rating: 4.3, reviews: 31042, emoji: '🍚', tags: ['bestseller'],
      highlights: ['1.8L capacity', 'Auto cook & warm', 'Non-stick inner pot', 'Cool touch handles'],
      variants: { label: 'Capacity', options: ['1L', '1.8L', '2.8L'] },
      desc: 'Bajaj Majesty rice cooker with 1.8L capacity cooks rice perfectly every time with auto-cook and auto-warm functions. Non-stick inner pot for easy cleaning.',
      specs: [['Brand','Bajaj'],['Capacity','1.8 litres'],['Functions','Cook + Warm'],['Inner pot','Non-stick'],['Power','500W'],['Warranty','2 years']]
    },
    {
      id: 'A006', name: 'Philips HL7756 600W Juicer Mixer Grinder', brand: 'Philips',
      category: 'Appliances', price: 2499, originalPrice: 3999, discount: 38,
      rating: 4.4, reviews: 28432, emoji: '🍹', tags: ['assured'],
      highlights: ['600W motor', '3 jars', 'Turbo boost', '5-year motor warranty'],
      variants: { label: 'Colour', options: ['Blue', 'Red'] },
      desc: 'Philips HL7756 juicer mixer grinder with 600W motor and Turbo Boost function for extra power when needed. 5-year warranty on motor.',
      specs: [['Brand','Philips'],['Wattage','600W'],['Jars','3'],['Turbo boost','Yes'],['Motor warranty','5 years'],['Speeds','3 + Pulse']]
    },
    {
      id: 'A007', name: 'Orient Electric 25L Convection Microwave Oven', brand: 'Orient',
      category: 'Appliances', price: 9499, originalPrice: 14999, discount: 37,
      rating: 4.2, reviews: 14321, emoji: '🔌', tags: ['new'],
      highlights: ['25L capacity', 'Convection + grill', 'Auto-cook menus', 'Child lock'],
      variants: { label: 'Capacity', options: ['20L', '25L', '30L'] },
      desc: 'Orient convection microwave oven with 25L capacity. Convection mode bakes and grills, auto-cook menus make cooking effortless, and child lock ensures safety.',
      specs: [['Brand','Orient'],['Capacity','25 litres'],['Type','Convection + Grill'],['Auto menus','Yes'],['Child lock','Yes'],['Warranty','1 year']]
    },
    {
      id: 'A008', name: 'V-Guard Inverter Battery 150Ah Tall Tubular', brand: 'V-Guard',
      category: 'Appliances', price: 12999, originalPrice: 16999, discount: 24,
      rating: 4.3, reviews: 9832, emoji: '🔋', tags: ['assured'],
      highlights: ['150Ah capacity', 'Tall tubular technology', '3-year warranty', 'Low maintenance'],
      variants: { label: 'Capacity', options: ['100Ah', '150Ah', '180Ah', '200Ah'] },
      desc: 'V-Guard tall tubular battery with 150Ah capacity for reliable backup power. Low maintenance design with 3-year warranty.',
      specs: [['Brand','V-Guard'],['Capacity','150 Ah'],['Type','Tall tubular'],['Warranty','3 years (36+24 months)'],['Maintenance','Low']]
    },
    {
      id: 'A009', name: 'Havells Instanio 3-Litre Instant Water Geyser', brand: 'Havells',
      category: 'Appliances', price: 3499, originalPrice: 5499, discount: 36,
      rating: 4.4, reviews: 21043, emoji: '🔌', tags: ['bestseller'],
      highlights: ['3-litre capacity', 'Instant heating', '4-star rating', 'Rust-free tank'],
      variants: { label: 'Capacity', options: ['1L instant', '3L instant', '6L storage', '10L storage'] },
      desc: 'Havells Instanio instant water geyser heats water instantly with a rust-free polymer tank. 4-star energy rating and multi-functional safety valve.',
      specs: [['Brand','Havells'],['Capacity','3 litres'],['Type','Instant'],['Energy rating','4 star'],['Tank','Rust-free polymer'],['Warranty','2 years']]
    },
    {
      id: 'A010', name: 'Crompton Aura 48 BLDC Ceiling Fan — Pearl White', brand: 'Crompton',
      category: 'Appliances', price: 3299, originalPrice: 4999, discount: 34,
      rating: 4.3, reviews: 18432, emoji: '💨', tags: ['new'],
      highlights: ['BLDC motor', '48W low power', '5 speed settings', 'Remote included'],
      variants: { label: 'Colour', options: ['Pearl White', 'Midnight Black', 'Opal Brown'] },
      desc: 'Crompton Aura BLDC ceiling fan with 48-inch sweep and 48W power consumption. 5-speed remote control and LED indicator for speed display.',
      specs: [['Brand','Crompton'],['Sweep','1200mm'],['Motor','BLDC'],['Wattage','48W'],['Speeds','5 (with remote)'],['Warranty','2 years']]
    },
  ],

  // ─── BEAUTY ───────────────────────────────────────────────────────────────
  'Beauty': [
    {
      id: 'B001', name: 'Himalaya Purifying Neem Face Wash — Pack of 3', brand: 'Himalaya',
      category: 'Beauty', price: 349, originalPrice: 620, discount: 44,
      rating: 4.5, reviews: 61003, emoji: '🧴', tags: ['bestseller'],
      highlights: ['Neem + turmeric', 'Oil-free formula', 'Suitable all skin types', 'No parabens'],
      variants: { label: 'Pack', options: ['Single 150ml', 'Pack of 2', 'Pack of 3'] },
      desc: 'Himalaya Purifying Neem Face Wash with neem and turmeric removes excess oil and impurities without over-drying. Dermatologically tested and paraben-free.',
      specs: [['Brand','Himalaya'],['Volume','150ml × 3'],['Skin type','All, especially oily'],['Key ingredients','Neem, Turmeric'],['Paraben free','Yes']]
    },
    {
      id: 'B002', name: "L'Oreal Paris Revitalift 1.5% Hyaluronic Acid Serum", brand: "L'Oreal",
      category: 'Beauty', price: 799, originalPrice: 1399, discount: 43,
      rating: 4.3, reviews: 23841, emoji: '💆', tags: ['assured'],
      highlights: ['1.5% hyaluronic acid', 'Replumps in 1 week', 'Dermatologist tested', 'All skin types'],
      variants: { label: 'Size', options: ['15ml', '30ml'] },
      desc: "L'Oreal Paris Revitalift 1.5% Pure Hyaluronic Acid serum intensely hydrates skin and reduces wrinkles in 1 week. Dermatologist tested for all skin types.",
      specs: [['Brand',"L'Oreal"],['Key ingredient','1.5% Hyaluronic Acid'],['Volume','30ml'],['Skin type','All'],['Dermatologist tested','Yes']]
    },
    {
      id: 'B003', name: 'Maybelline Fit Me Matte+Poreless Foundation', brand: 'Maybelline',
      category: 'Beauty', price: 449, originalPrice: 699, discount: 36,
      rating: 4.2, reviews: 41032, emoji: '💄', tags: ['bestseller'],
      highlights: ['16HR wear', 'SPF 22', '40 shades', 'Oil-free formula'],
      variants: { label: 'Shade', options: ['110 Porcelain', '120 Classic Ivory', '220 Natural Beige', '310 Sun Beige'] },
      desc: 'Maybelline Fit Me Matte+Poreless Foundation blurs pores and controls shine for 16 hours. Available in 40 shades with SPF 22 protection.',
      specs: [['Brand','Maybelline'],['Coverage','Medium to full'],['Finish','Matte'],['SPF','22'],['Wear time','16 hours'],['Volume','30ml']]
    },
    {
      id: 'B004', name: 'Biotique Bio Papaya Exfoliating Face Wash — 150ml', brand: 'Biotique',
      category: 'Beauty', price: 149, originalPrice: 249, discount: 40,
      rating: 4.3, reviews: 34210, emoji: '🧴', tags: ['bestseller'],
      highlights: ['Papaya enzymes', 'Gentle exfoliation', 'Ayurvedic formula', 'Brightening'],
      variants: { label: 'Size', options: ['150ml', '300ml'] },
      desc: "Biotique Bio Papaya face wash with papaya enzymes gently exfoliates dead skin cells, revealing brighter, smoother skin. Ayurvedic formula safe for daily use.",
      specs: [['Brand','Biotique'],['Key ingredient','Papaya extract'],['Volume','150 ml'],['Skin type','All'],['Formula','Ayurvedic']]
    },
    {
      id: 'B005', name: 'Lakme 9 to 5 Weightless Mousse Lipstick', brand: 'Lakme',
      category: 'Beauty', price: 349, originalPrice: 499, discount: 30,
      rating: 4.2, reviews: 28432, emoji: '💋', tags: ['bestseller'],
      highlights: ['Mousse texture', 'Velvet finish', '8-hour wear', 'Weightless feel'],
      variants: { label: 'Shade', options: ['Red Stop', 'Pink Pause', 'Nude Mesh', 'Berry Base'] },
      desc: 'Lakme 9 to 5 Weightless Mousse lipstick gives you a velvet matte finish that feels like nothing on your lips. Long-lasting 8-hour wear.',
      specs: [['Brand','Lakme'],['Finish','Velvet matte'],['Wear','8 hours'],['Weight','1.8g'],['Hydrating','Yes']]
    },
    {
      id: 'B006', name: 'Dove Body Lotion Intense Moisture — 400ml', brand: 'Dove',
      category: 'Beauty', price: 299, originalPrice: 425, discount: 30,
      rating: 4.5, reviews: 54321, emoji: '🧴', tags: ['bestseller'],
      highlights: ['Deep moisture', '48hr moisturisation', 'Non-greasy', 'Dermatologist approved'],
      variants: { label: 'Size', options: ['250ml', '400ml', '800ml'] },
      desc: 'Dove Intense Moisture body lotion with a unique moisture serum that gives you 48 hours of moisturisation in one application. Non-greasy, fast-absorbing formula.',
      specs: [['Brand','Dove'],['Volume','400 ml'],['Moisturisation','48 hours'],['Texture','Non-greasy'],['Dermatologist approved','Yes']]
    },
    {
      id: 'B007', name: 'Mamaearth Vitamin C Face Serum — 30ml', brand: 'Mamaearth',
      category: 'Beauty', price: 549, originalPrice: 849, discount: 35,
      rating: 4.3, reviews: 43210, emoji: '💆', tags: ['new'],
      highlights: ['15% Vitamin C', 'Brightening', 'Anti-ageing', 'Toxin-free'],
      variants: { label: 'Size', options: ['15ml', '30ml'] },
      desc: 'Mamaearth Vitamin C Face Serum with 15% Vitamin C and niacinamide brightens skin and reduces dark spots. Toxin-free, cruelty-free formula.',
      specs: [['Brand','Mamaearth'],['Key ingredient','15% Vitamin C + Niacinamide'],['Volume','30 ml'],['Toxin free','Yes'],['Cruelty free','Yes']]
    },
    {
      id: 'B008', name: 'Gillette Mach3 Razor + 4 Blades Combo', brand: 'Gillette',
      category: 'Beauty', price: 449, originalPrice: 699, discount: 36,
      rating: 4.5, reviews: 32104, emoji: '🪒', tags: ['bestseller'],
      highlights: ['3-blade razor', 'Lubrication strip', 'Flexible head', 'Includes 4 cartridges'],
      variants: { label: 'Pack', options: ['Razor + 2 blades', 'Razor + 4 blades', 'Refill pack of 8'] },
      desc: 'Gillette Mach3 razor with 3 blades provides a close, comfortable shave. Lubrication strip and flexible head adapt to facial contours.',
      specs: [['Brand','Gillette'],['Blades','3 per cartridge'],['Lubrication','Strip'],['Head','Flexible pivot'],['Includes','1 razor + 4 cartridges']]
    },
    {
      id: 'B009', name: 'WOW Skin Science Apple Cider Vinegar Shampoo — 300ml', brand: 'WOW',
      category: 'Beauty', price: 349, originalPrice: 599, discount: 42,
      rating: 4.3, reviews: 38210, emoji: '🧴', tags: ['new'],
      highlights: ['Apple cider vinegar', 'Balances scalp pH', 'Sulphate-free', 'DHT blocker'],
      variants: { label: 'Size', options: ['300ml', '500ml'] },
      desc: 'WOW Apple Cider Vinegar shampoo balances scalp pH, removes buildup, and strengthens hair from root to tip. Sulphate and paraben free.',
      specs: [['Brand','WOW'],['Key ingredient','Apple Cider Vinegar'],['Volume','300 ml'],['Sulphate free','Yes'],['Paraben free','Yes']]
    },
    {
      id: 'B010', name: 'Forest Essentials Soundarya Radiance Cream — 50g', brand: 'Forest Essentials',
      category: 'Beauty', price: 2995, originalPrice: 3995, discount: 25,
      rating: 4.5, reviews: 8432, emoji: '🌿', tags: ['assured'],
      highlights: ['24K gold flakes', 'Ayurvedic formula', 'Anti-ageing', 'Brightening'],
      variants: { label: 'Size', options: ['25g', '50g'] },
      desc: 'Forest Essentials Soundarya cream with 24K gold flakes and Ayurvedic actives reduces signs of ageing, brightens skin, and restores radiance.',
      specs: [['Brand','Forest Essentials'],['Key ingredient','24K gold, Saffron, Aloe vera'],['Weight','50 g'],['Skin type','All'],['Formula','Ayurvedic']]
    },
  ],

  // ─── SPORTS ───────────────────────────────────────────────────────────────
  'Sports': [
    {
      id: 'SP001', name: 'Cosco Champion Cricket Kit — Full Set with Bag', brand: 'Cosco',
      category: 'Sports', price: 1899, originalPrice: 3200, discount: 41,
      rating: 4.2, reviews: 4328, emoji: '🏏', tags: ['bestseller'],
      highlights: ['English willow bat', 'Complete kit', 'Helmet included', 'Kit bag with wheels'],
      variants: { label: 'Size', options: ['Boys (Aged 8–12)', 'Junior (Aged 12–15)', 'Senior'] },
      desc: 'Cosco Champion Cricket Kit — complete with an English willow bat, gloves, pads, helmet, and a wheeled kit bag. Ideal for junior and amateur players.',
      specs: [['Brand','Cosco'],['Bat','English willow'],['Kit includes','Bat, Gloves, Pads, Helmet, Bag'],['Level','Amateur/Junior']]
    },
    {
      id: 'SP002', name: 'Yonex Voltric 1DG Badminton Racquet', brand: 'Yonex',
      category: 'Sports', price: 2899, originalPrice: 4500, discount: 36,
      rating: 4.5, reviews: 8932, emoji: '🏸', tags: ['assured'],
      highlights: ['TRI-VOLTAGE system', 'Carbon graphite', 'G4 grip size', 'Pre-strung 24lbs'],
      variants: { label: 'Grip', options: ['G4 (Small)', 'G5 (Medium)'] },
      desc: 'Yonex Voltric 1DG badminton racquet with TRI-VOLTAGE system for powerful smashes. Carbon graphite frame and pre-strung at 24lbs.',
      specs: [['Brand','Yonex'],['Weight','85g'],['Flex','Medium'],['Frame','Carbon graphite'],['String tension','24 lbs'],['Balance','Head heavy']]
    },
    {
      id: 'SP003', name: 'Nivia Storm Football — Size 5', brand: 'Nivia',
      category: 'Sports', price: 649, originalPrice: 999, discount: 35,
      rating: 4.3, reviews: 6210, emoji: '⚽', tags: ['new'],
      highlights: ['Size 5', '32 panel', 'Machine stitched', 'PU outer casing'],
      variants: { label: 'Size', options: ['Size 3', 'Size 4', 'Size 5'] },
      desc: 'Nivia Storm football in size 5 with a 32-panel PU casing and machine-stitched construction for consistent performance on grass and turf.',
      specs: [['Brand','Nivia'],['Size','5'],['Panels','32'],['Casing','PU'],['Construction','Machine stitched']]
    },
    {
      id: 'SP004', name: 'Vector X VXS-1500 Football Shoes — Blue/White', brand: 'Vector X',
      category: 'Sports', price: 1299, originalPrice: 2499, discount: 48,
      rating: 4.1, reviews: 8432, emoji: '👟', tags: ['bestseller'],
      highlights: ['Rubber studs', 'PU upper', 'Ankle support', 'Available UK 6–11'],
      variants: { label: 'Size (UK)', options: ['6','7','8','9','10','11'] },
      desc: 'Vector X football shoes with rubber studs for firm ground. PU upper for durability and added ankle support to reduce injury risk.',
      specs: [['Brand','Vector X'],['Upper','PU'],['Sole','Rubber studs'],['Ground type','Firm ground'],['Ankle','High collar support']]
    },
    {
      id: 'SP005', name: 'Burnlab Pro Resistance Bands Set — 5 Levels', brand: 'Burnlab',
      category: 'Sports', price: 799, originalPrice: 1499, discount: 47,
      rating: 4.4, reviews: 14321, emoji: '💪', tags: ['bestseller'],
      highlights: ['5 resistance levels', 'Natural latex', 'Carry bag included', 'For all fitness levels'],
      variants: { label: 'Set', options: ['5-band set', '3-band set'] },
      desc: 'Burnlab Pro resistance bands set with 5 levels from light to extra heavy. Made from natural latex with a carry bag — perfect for home workouts, yoga, and rehab.',
      specs: [['Brand','Burnlab'],['Bands','5'],['Material','Natural latex'],['Resistance levels','Light, Medium, Heavy, X-Heavy, XX-Heavy'],['Carry bag','Yes']]
    },
    {
      id: 'SP006', name: 'SG RSD Xtreme Cricket Batting Gloves', brand: 'SG',
      category: 'Sports', price: 899, originalPrice: 1499, discount: 40,
      rating: 4.3, reviews: 5432, emoji: '🏏', tags: ['assured'],
      highlights: ['Premium leather palm', 'Full finger protection', 'Velcro wrist strap', 'Both hands available'],
      variants: { label: 'Hand', options: ['Right hand', 'Left hand'] },
      desc: 'SG RSD Xtreme batting gloves with premium leather palm and full finger protection. Velcro wrist strap for secure fit.',
      specs: [['Brand','SG'],['Palm','Premium leather'],['Fingers','Full protection'],['Wrist','Velcro strap'],['Size','Senior']]
    },
    {
      id: 'SP007', name: 'Decathlon Kalenji Run Dry Men Running T-Shirt', brand: 'Decathlon',
      category: 'Sports', price: 499, originalPrice: 799, discount: 38,
      rating: 4.4, reviews: 32104, emoji: '👕', tags: ['bestseller'],
      highlights: ['Dry-fit fabric', 'UPF 30+', 'Lightweight', 'Reflective detail'],
      variants: { label: 'Size', options: ['S','M','L','XL','XXL'] },
      desc: "Decathlon Kalenji running t-shirt in dry-fit fabric that wicks sweat during your run. UPF 30+ sun protection and reflective detail for low-light visibility.",
      specs: [['Brand','Decathlon'],['Fabric','Polyester dry-fit'],['UPF','30+'],['Reflective','Yes'],['Weight','120g']]
    },
    {
      id: 'SP008', name: 'Nivia Iron Dumbbell Pair — 5kg × 2', brand: 'Nivia',
      category: 'Sports', price: 1499, originalPrice: 2299, discount: 35,
      rating: 4.4, reviews: 21043, emoji: '🏋️', tags: ['bestseller'],
      highlights: ['Cast iron', 'Rubber coating', 'Hex design', 'Anti-roll'],
      variants: { label: 'Weight', options: ['2kg pair', '5kg pair', '8kg pair', '10kg pair'] },
      desc: 'Nivia hex rubber-coated iron dumbbells — the hex shape prevents rolling and the rubber coating protects floors. Ideal for home gym workouts.',
      specs: [['Brand','Nivia'],['Weight','5 kg each'],['Material','Cast iron'],['Coating','Rubber'],['Shape','Hexagonal']]
    },
    {
      id: 'SP009', name: 'Cosco Rapid Badminton Set with Net', brand: 'Cosco',
      category: 'Sports', price: 1299, originalPrice: 2199, discount: 41,
      rating: 4.2, reviews: 8932, emoji: '🏸', tags: ['new'],
      highlights: ['2 racquets + net', 'Steel shaft', 'Full court net', '3 shuttlecocks included'],
      variants: { label: 'Set', options: ['2-player set', '4-player set'] },
      desc: 'Cosco Rapid badminton set with 2 steel shaft racquets, a full-length court net, and 3 shuttlecocks — everything you need to start playing.',
      specs: [['Brand','Cosco'],['Racquets','2'],['Shaft','Steel'],['Net','Full court length'],['Shuttles','3 included']]
    },
    {
      id: 'SP010', name: 'Reebok Lite 3.0 Running Shoes for Men — White', brand: 'Reebok',
      category: 'Sports', price: 2999, originalPrice: 5499, discount: 45,
      rating: 4.3, reviews: 18432, emoji: '👟', tags: ['assured'],
      highlights: ['FLEXWEAVE upper', 'Lightweight foam sole', 'Breathable mesh', 'UK 6–11'],
      variants: { label: 'Size (UK)', options: ['6','7','8','9','10','11'] },
      desc: 'Reebok Lite 3.0 running shoes with FLEXWEAVE upper and lightweight foam sole for fast, comfortable runs. Breathable mesh keeps feet cool.',
      specs: [['Brand','Reebok'],['Upper','FLEXWEAVE'],['Midsole','Lightweight foam'],['Weight','~180g per shoe'],['Drop','8mm']]
    },
  ],

  // ─── BOOKS ────────────────────────────────────────────────────────────────
  'Books': [
    {
      id: 'BK001', name: 'Atomic Habits — James Clear', brand: 'Penguin',
      category: 'Books', price: 399, originalPrice: 799, discount: 50,
      rating: 4.7, reviews: 184320, emoji: '📗', tags: ['bestseller'],
      highlights: ['#1 NYT Bestseller', 'Paperback edition', 'English', '320 pages'],
      variants: { label: 'Format', options: ['Paperback', 'Hardcover', 'Kindle'] },
      desc: 'Atomic Habits reveals practical strategies to form good habits, break bad ones, and master the tiny behaviours that lead to remarkable results.',
      specs: [['Author','James Clear'],['Publisher','Penguin'],['Pages','320'],['Language','English'],['Format','Paperback']]
    },
    {
      id: 'BK002', name: 'The Psychology of Money — Morgan Housel', brand: 'Jaico',
      category: 'Books', price: 349, originalPrice: 599, discount: 42,
      rating: 4.6, reviews: 92104, emoji: '📘', tags: ['bestseller'],
      highlights: ['19 short stories', 'Financial wisdom', 'Paperback', '256 pages'],
      variants: { label: 'Format', options: ['Paperback', 'Kindle'] },
      desc: 'The Psychology of Money explores how people think about money through 19 compelling short stories — a must-read for anyone wanting to make smarter financial decisions.',
      specs: [['Author','Morgan Housel'],['Publisher','Jaico'],['Pages','256'],['Language','English'],['Format','Paperback']]
    },
    {
      id: 'BK003', name: 'Rich Dad Poor Dad — Robert T. Kiyosaki', brand: 'Manjul',
      category: 'Books', price: 299, originalPrice: 495, discount: 40,
      rating: 4.5, reviews: 143210, emoji: '📙', tags: ['bestseller'],
      highlights: ['25yr anniversary ed.', 'Financial literacy', 'Paperback', '336 pages'],
      variants: { label: 'Format', options: ['Paperback', 'Hardcover', 'Kindle'] },
      desc: 'Rich Dad Poor Dad is the #1 personal finance book of all time — challenging the myth that you need to earn a high income to be rich.',
      specs: [['Author','Robert T. Kiyosaki'],['Publisher','Manjul'],['Pages','336'],['Language','English'],['Edition','25th Anniversary']]
    },
    {
      id: 'BK004', name: 'The Alchemist — Paulo Coelho', brand: 'HarperCollins',
      category: 'Books', price: 299, originalPrice: 499, discount: 40,
      rating: 4.6, reviews: 121043, emoji: '📕', tags: ['bestseller'],
      highlights: ['182 languages', 'Inspirational fiction', 'Paperback', '197 pages'],
      variants: { label: 'Format', options: ['Paperback', 'Hardcover', 'Kindle'] },
      desc: "The Alchemist tells the story of Santiago, a shepherd boy who dreams of travelling the world in search of treasure. One of the most beloved and sold books in history.",
      specs: [['Author','Paulo Coelho'],['Publisher','HarperCollins'],['Pages','197'],['Language','English'],['Genre','Inspirational fiction']]
    },
    {
      id: 'BK005', name: 'Zero to One — Peter Thiel', brand: 'Currency',
      category: 'Books', price: 349, originalPrice: 599, discount: 42,
      rating: 4.4, reviews: 54321, emoji: '📗', tags: ['bestseller'],
      highlights: ['Startup bible', 'Business insights', 'Paperback', '224 pages'],
      variants: { label: 'Format', options: ['Paperback', 'Kindle'] },
      desc: 'Zero to One is Peter Thiel\'s essential guide to building the future — packed with contrarian thinking about startups, innovation, and what it means to create something truly new.',
      specs: [['Author','Peter Thiel'],['Publisher','Currency'],['Pages','224'],['Language','English'],['Genre','Business / Startups']]
    },
    {
      id: 'BK006', name: 'Sapiens — Yuval Noah Harari', brand: 'Vintage',
      category: 'Books', price: 499, originalPrice: 799, discount: 38,
      rating: 4.6, reviews: 87432, emoji: '📘', tags: ['bestseller'],
      highlights: ['Global bestseller', 'History of humankind', 'Paperback', '498 pages'],
      variants: { label: 'Format', options: ['Paperback', 'Hardcover', 'Kindle'] },
      desc: 'Sapiens traces the history of humankind from the Stone Age to the twenty-first century — exploring how biology and history have defined us and shaped human society.',
      specs: [['Author','Yuval Noah Harari'],['Publisher','Vintage'],['Pages','498'],['Language','English'],['Genre','History / Non-fiction']]
    },
    {
      id: 'BK007', name: 'Deep Work — Cal Newport', brand: 'Piatkus',
      category: 'Books', price: 399, originalPrice: 699, discount: 43,
      rating: 4.5, reviews: 43210, emoji: '📙', tags: ['new'],
      highlights: ['Productivity classic', 'Focus strategies', 'Paperback', '296 pages'],
      variants: { label: 'Format', options: ['Paperback', 'Kindle'] },
      desc: 'Deep Work argues that the ability to focus without distraction is becoming increasingly rare and increasingly valuable — and provides a rigorous training regimen to cultivate this skill.',
      specs: [['Author','Cal Newport'],['Publisher','Piatkus'],['Pages','296'],['Language','English'],['Genre','Productivity / Self-help']]
    },
    {
      id: 'BK008', name: 'The Lean Startup — Eric Ries', brand: 'Crown Business',
      category: 'Books', price: 449, originalPrice: 699, discount: 36,
      rating: 4.4, reviews: 38210, emoji: '📗', tags: ['assured'],
      highlights: ['Startup methodology', 'MVP concept', 'Paperback', '336 pages'],
      variants: { label: 'Format', options: ['Paperback', 'Kindle'] },
      desc: 'The Lean Startup introduces the concept of validated learning, the build-measure-learn loop, and minimum viable products — redefining how companies are built and new products are launched.',
      specs: [['Author','Eric Ries'],['Publisher','Crown Business'],['Pages','336'],['Language','English'],['Genre','Business / Startups']]
    },
    {
      id: 'BK009', name: 'Think and Grow Rich — Napoleon Hill', brand: 'Fingerprint',
      category: 'Books', price: 199, originalPrice: 350, discount: 43,
      rating: 4.4, reviews: 98432, emoji: '📕', tags: ['bestseller'],
      highlights: ['Classic self-help', '100M copies sold', 'Paperback', '272 pages'],
      variants: { label: 'Format', options: ['Paperback', 'Hardcover', 'Kindle'] },
      desc: "Think and Grow Rich has been called the Granddaddy of all motivational books. Hill's philosophy of success has helped millions achieve financial independence.",
      specs: [['Author','Napoleon Hill'],['Publisher','Fingerprint'],['Pages','272'],['Language','English'],['Genre','Self-help / Personal finance']]
    },
    {
      id: 'BK010', name: 'Ikigai — Héctor García & Francesc Miralles', brand: 'Penguin',
      category: 'Books', price: 299, originalPrice: 499, discount: 40,
      rating: 4.5, reviews: 67821, emoji: '📘', tags: ['bestseller'],
      highlights: ['Japanese philosophy', 'Find your purpose', 'Paperback', '208 pages'],
      variants: { label: 'Format', options: ['Paperback', 'Kindle'] },
      desc: "Ikigai explores the Japanese concept of 'reason for being' — the intersection of what you love, what you're good at, what the world needs, and what you can be paid for.",
      specs: [['Author','García & Miralles'],['Publisher','Penguin'],['Pages','208'],['Language','English'],['Genre','Philosophy / Self-help']]
    },
  ],

};

// Make catalogue available globally
window.catalogue = catalogue;

// ─── HELPER: Get flat list of all products ───
window.getAllProducts = function() {
  return Object.values(catalogue).flat();
};

// ─── HELPER: Find product by ID ───
window.getProductById = function(id) {
  return window.getAllProducts().find(p => p.id === id) || null;
};

// ─── HELPER: Get products by category ───
window.getProductsByCategory = function(category) {
  return catalogue[category] || [];
};

// ─── HELPER: Get all category names ───
window.getCategoryNames = function() {
  return Object.keys(catalogue);
};