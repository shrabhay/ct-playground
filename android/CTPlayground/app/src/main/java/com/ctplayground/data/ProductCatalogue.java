package com.ctplayground.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProductCatalogue {

    private static final List<Product> ALL = new ArrayList<>(Arrays.asList(

            // ── ELECTRONICS ──────────────────────────────────────────────────
            new Product("E001","Samsung Galaxy M14 5G (4GB RAM, 128GB)","Samsung","Electronics",10999,14999,27,4.2,231104,"📱","Samsung Galaxy M14 5G with Exynos 1330, 50MP triple camera and 6000mAh battery.",new String[]{"bestseller"}),
            new Product("E002","Apple iPhone 13 (128GB) — Midnight","Apple","Electronics",49999,69900,28,4.6,189432,"📱","iPhone 13 with A15 Bionic chip, 12MP dual camera, 5G capable and Face ID.",new String[]{"assured"}),
            new Product("E003","OnePlus Nord CE3 Lite 5G (8GB RAM, 128GB)","OnePlus","Electronics",17999,25999,31,4.1,94210,"📱","Snapdragon 695, 108MP camera, 67W SUPERVOOC charging and 5000mAh battery.",new String[]{"assured"}),
            new Product("E004","boAt Airdopes 141 TWS Earbuds — 42H Playback","boAt","Electronics",1299,4499,71,4.1,124832,"🎧","42H playback, ENx tech, IPX4 water resistant.",new String[]{"bestseller"}),
            new Product("E005","Noise ColorFit Pro 4 Smartwatch — 1.72\" Display","Noise","Electronics",2499,6999,64,4.3,86421,"⌚","Built-in GPS, 100+ sports modes, SpO2 monitoring.",new String[]{"bestseller"}),
            new Product("E006","Logitech MK215 Wireless Keyboard & Mouse Combo","Logitech","Electronics",1795,2795,36,4.4,52341,"⌨️","2.4GHz wireless, 24-month battery life, plug & play.",new String[]{"assured"}),
            new Product("E007","Sony Bravia 43\" 4K Ultra HD Android TV","Sony","Electronics",44990,64990,31,4.5,38921,"📺","Google TV, Dolby Audio, HDMI x3, 4K HDR processor.",new String[]{"assured"}),
            new Product("E008","HP 15s Core i5 12th Gen Laptop (8GB, 512GB SSD)","HP","Electronics",49990,67990,26,4.3,21043,"💻","Intel Core i5-1235U, 8GB DDR4, 512GB SSD, Windows 11.",new String[]{"new"}),
            new Product("E009","Redmi Note 12 Pro 5G (8GB RAM, 256GB)","Xiaomi","Electronics",19999,27999,29,4.3,112043,"📱","50MP OIS camera, Dimensity 1080, 67W turbo charging.",new String[]{"bestseller"}),
            new Product("E010","Sony WH-1000XM5 Wireless Noise Cancelling Headphones","Sony","Electronics",24990,34990,29,4.7,43210,"🎧","Industry-leading ANC, 30-hour battery, Hi-Res Audio.",new String[]{"assured"}),
            new Product("E011","Canon EOS 1500D 24.1MP DSLR Camera","Canon","Electronics",32995,44995,27,4.5,28432,"📷","24.1MP APS-C sensor, 18-55mm lens, Full HD, Wi-Fi + NFC.",new String[]{"assured"}),
            new Product("E012","Lenovo IdeaPad Slim 3 Ryzen 5 (8GB, 512GB)","Lenovo","Electronics",42990,57990,26,4.2,18932,"💻","AMD Ryzen 5 7520U, 8GB LPDDR5, 512GB SSD, Windows 11.",new String[]{"new"}),
            new Product("E013","realme Buds Air 5 Pro ANC TWS Earbuds","realme","Electronics",3499,5999,42,4.2,34821,"🎧","50dB ANC, 38H playback, LDAC Hi-Res Audio, IP55.",new String[]{"new"}),
            new Product("E014","Samsung 32\" Full HD Smart Monitor — M5","Samsung","Electronics",17990,24990,28,4.4,12043,"🖥️","Smart TV apps built-in, USB-C 65W charging, speakers.",new String[]{"bestseller"}),
            new Product("E015","Zebronics Zeb-County2 Wireless Bluetooth Speaker","Zebronics","Electronics",1299,2499,48,4.1,29841,"🔊","10W output, 8-hour playback, TWS mode, FM + USB.",new String[]{"bestseller"}),
            new Product("E016","Apple iPad 10th Gen (64GB, Wi-Fi) — Blue","Apple","Electronics",34900,44900,22,4.6,41032,"📱","A14 Bionic, 10.9\" Liquid Retina, 12MP front, USB-C.",new String[]{"assured"}),
            new Product("E017","Mi Smart Band 8 Activity Tracker","Xiaomi","Electronics",2999,3999,25,4.3,67821,"⌚","1.62\" AMOLED, 16-day battery, 150+ workout modes.",new String[]{"bestseller"}),
            new Product("E018","TP-Link Archer AX23 AX1800 Wi-Fi 6 Router","TP-Link","Electronics",3499,5499,36,4.4,18432,"📡","Wi-Fi 6 AX1800, dual band, OFDMA technology.",new String[]{"new"}),
            new Product("E019","Realme Narzo 60x 5G (4GB RAM, 64GB)","realme","Electronics",9999,13999,29,4.0,43210,"📱","Dimensity 6100+, 50MP AI camera, AMOLED display.",new String[]{"new"}),
            new Product("E020","JBL Flip 6 Portable Bluetooth Speaker","JBL","Electronics",8999,13999,36,4.5,52043,"🔊","IP67 waterproof, 12-hour battery, PartyBoost, USB-C.",new String[]{"bestseller"}),

            // ── FASHION ──────────────────────────────────────────────────────
            new Product("F001","Levi's 511 Slim Fit Men's Jeans — Dark Blue","Levi's","Fashion",2399,3999,40,4.3,9142,"👖","Slim fit, 99% cotton, machine washable.",new String[]{"bestseller"}),
            new Product("F002","H&M Women Floral Wrap Dress — Multicolour","H&M","Fashion",1299,2499,48,4.1,6832,"👗","V-neck wrap style, woven fabric, belt included.",new String[]{"new"}),
            new Product("F003","Nike Air Max 270 Running Shoes for Men","Nike","Fashion",6995,11995,42,4.5,18743,"👟","Max Air heel unit, foam midsole, mesh upper.",new String[]{"assured"}),
            new Product("F004","Allen Solly Men Slim Fit Formal Shirt — White","Allen Solly","Fashion",949,1799,47,4.2,14231,"👔","Slim fit, 100% cotton, full sleeves.",new String[]{"bestseller"}),
            new Product("F005","Fabindia Women Kurta Set — Ethnic Print","Fabindia","Fashion",1999,3499,43,4.4,7621,"👘","Cotton fabric, A-line kurta, dupatta included.",new String[]{"new"}),
            new Product("F006","Puma Men Hoodie Sweatshirt — Navy Blue","Puma","Fashion",1599,3499,54,4.3,11082,"🧥","Fleece lining, kangaroo pocket, drawstring hood.",new String[]{"assured"}),
            new Product("F007","Adidas Originals Women Track Pants — Black","Adidas","Fashion",2099,3499,40,4.3,8921,"👗","3-Stripe detail, elastic waistband, side pockets.",new String[]{"bestseller"}),
            new Product("F008","W Women Flared Kurti — Multiprint","W","Fashion",799,1499,47,4.2,13421,"👗","Flared hem, rayon fabric, printed design, 3/4 sleeves.",new String[]{"bestseller"}),
            new Product("F009","Peter England Men Slim Chinos — Khaki","Peter England","Fashion",1349,2299,41,4.1,9832,"👖","Slim fit, stretch fabric, flat front.",new String[]{"assured"}),
            new Product("F010","Woodland Men Leather Casual Shoes — Brown","Woodland","Fashion",3299,5499,40,4.4,21043,"👞","Full grain leather, rubber sole, lace-up closure.",new String[]{"bestseller"}),
            new Product("F011","Zara Men Slim Fit Blazer — Navy","Zara","Fashion",3990,6490,39,4.3,5432,"🧥","Slim fit, notched lapel, two-button closure.",new String[]{"new"}),
            new Product("F012","Bata Women Ballet Flats — Nude","Bata","Fashion",999,1799,44,4.2,16821,"👡","Synthetic upper, cushioned insole, slip-on.",new String[]{"bestseller"}),
            new Product("F013","US Polo Assn Men Polo T-Shirt — Red","US Polo Assn","Fashion",699,1299,46,4.1,22043,"👕","100% cotton, polo collar, short sleeves.",new String[]{"bestseller"}),
            new Product("F014","Tommy Hilfiger Men Analog Watch — Silver","Tommy Hilfiger","Fashion",5995,9995,40,4.5,8321,"⌚","Stainless steel, leather strap, quartz, 50m water resistant.",new String[]{"assured"}),
            new Product("F015","Jockey Men Cotton Brief — Pack of 3","Jockey","Fashion",549,849,35,4.5,43210,"🧦","100% cotton, elastic waistband, pack of 3.",new String[]{"bestseller"}),
            new Product("F016","Mango Women High-Waist Straight Jeans — White","Mango","Fashion",2799,4499,38,4.2,4321,"👖","High-waist, straight leg, stretch denim.",new String[]{"new"}),
            new Product("F017","Wildcraft Unisex Daypack Backpack 33L — Grey","Wildcraft","Fashion",1799,2999,40,4.3,12043,"🎒","33L capacity, laptop sleeve, rain cover.",new String[]{"bestseller"}),
            new Product("F018","Van Heusen Men Slim Fit Formal Trousers — Black","Van Heusen","Fashion",1499,2499,40,4.2,8932,"👖","Slim fit, wrinkle resistant, stretch fabric.",new String[]{"assured"}),
            new Product("F019","Crocs Classic Clog — Navy","Crocs","Fashion",2799,3995,30,4.4,31042,"👟","Croslite foam, lightweight, pivoting heel strap.",new String[]{"bestseller"}),
            new Product("F020","HRX by Hrithik Roshan Men Training T-Shirt — Black","HRX","Fashion",699,1299,46,4.1,18432,"👕","Dry-fit fabric, slim fit, reflective detail.",new String[]{"new"}),

            // ── GROCERY ──────────────────────────────────────────────────────
            new Product("G001","Tata Salt Iodised — 1kg Pack of 6","Tata","Grocery",162,180,10,4.6,43210,"🧂","Vacuum evaporated iodised salt, BIS certified.",new String[]{"bestseller"}),
            new Product("G002","Aashirvaad Atta Whole Wheat — 10kg","Aashirvaad","Grocery",379,430,12,4.5,89321,"🌾","100% whole wheat, soft rotis, no maida.",new String[]{"bestseller"}),
            new Product("G003","Amul Butter Pasteurised — 500g","Amul","Grocery",265,285,7,4.7,32104,"🧈","Rich and creamy pasteurised table butter.",new String[]{"assured"}),
            new Product("G004","Nescafé Classic Instant Coffee — 200g","Nescafé","Grocery",499,620,20,4.4,21843,"☕","100% pure coffee, rich aroma, resealable pack.",new String[]{"bestseller"}),
            new Product("G005","Fortune Sunlite Sunflower Oil — 5L","Fortune","Grocery",689,780,12,4.3,18420,"🫙","Light, digestible, high smoke point, Vitamin E enriched.",new String[]{"assured"}),
            new Product("G006","Tata Tea Premium — 500g","Tata Tea","Grocery",249,289,14,4.5,54321,"🍵","Bold Assam blend, aromatic, best with milk.",new String[]{"bestseller"}),
            new Product("G007","Maggi 2-Minute Noodles Masala — Pack of 12","Maggi","Grocery",168,204,18,4.6,98432,"🍜","Iconic masala taste, ready in 2 minutes.",new String[]{"bestseller"}),
            new Product("G008","Dabur Honey — 1kg NMR Tested","Dabur","Grocery",349,425,18,4.5,43210,"🍯","NMR tested purity, no added sugar, natural antioxidants.",new String[]{"assured"}),
            new Product("G009","Quaker Oats Original — 2kg","Quaker","Grocery",349,449,22,4.4,28432,"🌾","100% whole grain, high fibre, no added sugar.",new String[]{"new"}),
            new Product("G010","Surf Excel Easy Wash Detergent Powder — 4kg","Surf Excel","Grocery",469,560,16,4.5,61043,"🧺","Removes tough stains, colour safe, jasmine fragrance.",new String[]{"bestseller"}),
            new Product("G011","Britannia Good Day Butter Cookies — 600g","Britannia","Grocery",149,175,15,4.4,38210,"🍪","Real butter taste, crunchy, pack of 4.",new String[]{"bestseller"}),
            new Product("G012","Haldiram Aloo Bhujia — 1kg","Haldiram's","Grocery",299,360,17,4.6,52043,"🥜","Crispy, spicy, authentic recipe, airtight packaging.",new String[]{"bestseller"}),

            // ── HOME & FURNITURE ─────────────────────────────────────────────
            new Product("H001","Wakefit Orthopaedic Memory Foam Mattress — Queen","Wakefit","Home & Furniture",12999,19999,35,4.5,48321,"🛏️","7-zone support, 100 night trial, CertiPUR-US certified.",new String[]{"bestseller"}),
            new Product("H002","Solimo Engineered Wood Study Table — Walnut","Solimo","Home & Furniture",4499,7999,44,4.2,12043,"🪑","120x60cm tabletop, storage shelf, easy assembly.",new String[]{"assured"}),
            new Product("H003","Story@Home 300 TC Cotton Double Bedsheet Set","Story@Home","Home & Furniture",799,1799,56,4.3,34210,"🛌","300TC pure cotton, 2 pillow covers, machine washable.",new String[]{"bestseller"}),
            new Product("H004","IKEA KALLAX Shelf Unit — White","IKEA","Home & Furniture",8999,11999,25,4.4,21043,"🪑","4-cube storage, wall mountable, versatile use.",new String[]{"assured"}),
            new Product("H005","Pigeon Induction Base Non-Stick Tawa — 28cm","Pigeon","Home & Furniture",499,899,44,4.3,43210,"🍳","28cm, induction compatible, PFOA free.",new String[]{"bestseller"}),
            new Product("H006","Amazon Basics Microfibre Comforter — Double","Amazon Basics","Home & Furniture",1299,2499,48,4.3,28432,"🛌","Microfibre fill, box stitch, machine washable.",new String[]{"assured"}),
            new Product("H007","Cello Maxfresh Airtight Container Set — 12 Pcs","Cello","Home & Furniture",999,1799,44,4.4,38210,"🫙","12-piece, airtight lid, microwave safe, BPA free.",new String[]{"bestseller"}),
            new Product("H008","Philips LED Bulb 9W — Pack of 6 (Cool White)","Philips","Home & Furniture",449,699,36,4.5,54321,"💡","9W = 70W equivalent, 15,000 hour life.",new String[]{"bestseller"}),
            new Product("H009","Godrej Interio Office Chair — Black","Godrej","Home & Furniture",8999,13999,36,4.3,12043,"🪑","Lumbar support, height adjustable, mesh back.",new String[]{"new"}),
            new Product("H010","Milton Thermosteel Flip Lid Flask 1000ml","Milton","Home & Furniture",699,1199,42,4.5,43210,"🫙","24 hours hot/cold, stainless steel, leak-proof.",new String[]{"bestseller"}),
            new Product("H011","Nilkamal Plastic Foldable Table — White","Nilkamal","Home & Furniture",1999,3499,43,4.1,8932,"🪑","Foldable, high-density plastic, 80kg capacity.",new String[]{"new"}),
            new Product("H012","Borosil Vision Glass Set — 6 Pieces 350ml","Borosil","Home & Furniture",549,899,39,4.6,32104,"🥃","Borosilicate glass, microwave safe, dishwasher safe.",new String[]{"bestseller"}),

            // ── APPLIANCES ───────────────────────────────────────────────────
            new Product("A001","LG 8kg 5 Star Inverter Fully-Automatic Washing Machine","LG","Appliances",34990,49990,30,4.5,28432,"🫧","Inverter Direct Drive, steam wash, ThinQ AI.",new String[]{"assured"}),
            new Product("A002","Prestige Iris 750W Mixer Grinder with 3 Jars","Prestige","Appliances",1795,3495,49,4.4,42219,"🍹","750W motor, 3 SS jars, 3-speed control.",new String[]{"bestseller"}),
            new Product("A003","Voltas 1.5 Ton 5 Star Inverter Split AC","Voltas","Appliances",38990,55000,29,4.3,19832,"❄️","5-star energy rating, 4-in-1 mode, copper condenser.",new String[]{"assured"}),
            new Product("A004","Atomberg Renesa 1200mm BLDC Ceiling Fan + Remote","Atomberg","Appliances",2849,4500,37,4.4,22310,"💨","65% energy saving, remote control, anti-dust coating.",new String[]{"new"}),
            new Product("A005","Bajaj Majesty RCX 21 Rice Cooker — 1.8L","Bajaj","Appliances",1599,2499,36,4.3,31042,"🍚","1.8L, auto cook & warm, non-stick inner pot.",new String[]{"bestseller"}),
            new Product("A006","Philips HL7756 600W Juicer Mixer Grinder","Philips","Appliances",2499,3999,38,4.4,28432,"🍹","600W, 3 jars, turbo boost, 5-year motor warranty.",new String[]{"assured"}),
            new Product("A007","Orient Electric 25L Convection Microwave Oven","Orient","Appliances",9499,14999,37,4.2,14321,"🔌","25L, convection + grill, auto-cook menus, child lock.",new String[]{"new"}),
            new Product("A008","V-Guard Inverter Battery 150Ah Tall Tubular","V-Guard","Appliances",12999,16999,24,4.3,9832,"🔋","150Ah, tall tubular, 3-year warranty, low maintenance.",new String[]{"assured"}),
            new Product("A009","Havells Instanio 3-Litre Instant Water Geyser","Havells","Appliances",3499,5499,36,4.4,21043,"🔌","Instant heating, 4-star rating, rust-free tank.",new String[]{"bestseller"}),
            new Product("A010","Crompton Aura 48 BLDC Ceiling Fan — Pearl White","Crompton","Appliances",3299,4999,34,4.3,18432,"💨","BLDC motor, 48W, 5 speed settings, remote included.",new String[]{"new"}),

            // ── BEAUTY ───────────────────────────────────────────────────────
            new Product("B001","Himalaya Purifying Neem Face Wash — Pack of 3","Himalaya","Beauty",349,620,44,4.5,61003,"🧴","Neem + turmeric, oil-free, no parabens.",new String[]{"bestseller"}),
            new Product("B002","L'Oreal Paris Revitalift 1.5% Hyaluronic Acid Serum","L'Oreal","Beauty",799,1399,43,4.3,23841,"💆","Replumps in 1 week, dermatologist tested.",new String[]{"assured"}),
            new Product("B003","Maybelline Fit Me Matte+Poreless Foundation","Maybelline","Beauty",449,699,36,4.2,41032,"💄","16HR wear, SPF 22, 40 shades, oil-free.",new String[]{"bestseller"}),
            new Product("B004","Biotique Bio Papaya Exfoliating Face Wash — 150ml","Biotique","Beauty",149,249,40,4.3,34210,"🧴","Papaya enzymes, gentle exfoliation, brightening.",new String[]{"bestseller"}),
            new Product("B005","Lakme 9 to 5 Weightless Mousse Lipstick","Lakme","Beauty",349,499,30,4.2,28432,"💋","Mousse texture, velvet finish, 8-hour wear.",new String[]{"bestseller"}),
            new Product("B006","Dove Body Lotion Intense Moisture — 400ml","Dove","Beauty",299,425,30,4.5,54321,"🧴","48hr moisturisation, non-greasy, dermatologist approved.",new String[]{"bestseller"}),
            new Product("B007","Mamaearth Vitamin C Face Serum — 30ml","Mamaearth","Beauty",549,849,35,4.3,43210,"💆","15% Vitamin C, brightening, toxin-free.",new String[]{"new"}),
            new Product("B008","Gillette Mach3 Razor + 4 Blades Combo","Gillette","Beauty",449,699,36,4.5,32104,"🪒","3-blade, lubrication strip, flexible head.",new String[]{"bestseller"}),
            new Product("B009","WOW Apple Cider Vinegar Shampoo — 300ml","WOW","Beauty",349,599,42,4.3,38210,"🧴","Balances scalp pH, sulphate-free, DHT blocker.",new String[]{"new"}),
            new Product("B010","Forest Essentials Soundarya Radiance Cream — 50g","Forest Essentials","Beauty",2995,3995,25,4.5,8432,"🌿","24K gold flakes, Ayurvedic, anti-ageing.",new String[]{"assured"}),

            // ── SPORTS ───────────────────────────────────────────────────────
            new Product("SP001","Cosco Champion Cricket Kit — Full Set with Bag","Cosco","Sports",1899,3200,41,4.2,4328,"🏏","English willow bat, complete kit, helmet included.",new String[]{"bestseller"}),
            new Product("SP002","Yonex Voltric 1DG Badminton Racquet","Yonex","Sports",2899,4500,36,4.5,8932,"🏸","TRI-VOLTAGE system, carbon graphite, pre-strung 24lbs.",new String[]{"assured"}),
            new Product("SP003","Nivia Storm Football — Size 5","Nivia","Sports",649,999,35,4.3,6210,"⚽","Size 5, 32 panel, machine stitched, PU casing.",new String[]{"new"}),
            new Product("SP004","Vector X VXS-1500 Football Shoes — Blue/White","Vector X","Sports",1299,2499,48,4.1,8432,"👟","Rubber studs, PU upper, ankle support.",new String[]{"bestseller"}),
            new Product("SP005","Burnlab Pro Resistance Bands Set — 5 Levels","Burnlab","Sports",799,1499,47,4.4,14321,"💪","5 levels, natural latex, carry bag included.",new String[]{"bestseller"}),
            new Product("SP006","SG RSD Xtreme Cricket Batting Gloves","SG","Sports",899,1499,40,4.3,5432,"🏏","Premium leather palm, full finger protection.",new String[]{"assured"}),
            new Product("SP007","Decathlon Kalenji Run Dry Men Running T-Shirt","Decathlon","Sports",499,799,38,4.4,32104,"👕","Dry-fit, UPF 30+, lightweight, reflective detail.",new String[]{"bestseller"}),
            new Product("SP008","Nivia Iron Dumbbell Pair — 5kg × 2","Nivia","Sports",1499,2299,35,4.4,21043,"🏋️","Cast iron, rubber coated, hex design, anti-roll.",new String[]{"bestseller"}),
            new Product("SP009","Cosco Rapid Badminton Set with Net","Cosco","Sports",1299,2199,41,4.2,8932,"🏸","2 racquets, net, steel shaft, 3 shuttlecocks.",new String[]{"new"}),
            new Product("SP010","Reebok Lite 3.0 Running Shoes — White","Reebok","Sports",2999,5499,45,4.3,18432,"👟","FLEXWEAVE upper, lightweight foam sole, breathable.",new String[]{"assured"}),

            // ── BOOKS ────────────────────────────────────────────────────────
            new Product("BK001","Atomic Habits — James Clear","Penguin","Books",399,799,50,4.7,184320,"📗","#1 NYT Bestseller. Practical strategies to form good habits.",new String[]{"bestseller"}),
            new Product("BK002","The Psychology of Money — Morgan Housel","Jaico","Books",349,599,42,4.6,92104,"📘","19 short stories on financial wisdom.",new String[]{"bestseller"}),
            new Product("BK003","Rich Dad Poor Dad — Robert T. Kiyosaki","Manjul","Books",299,495,40,4.5,143210,"📙","#1 personal finance book of all time.",new String[]{"bestseller"}),
            new Product("BK004","The Alchemist — Paulo Coelho","HarperCollins","Books",299,499,40,4.6,121043,"📕","One of the most beloved books in history.",new String[]{"bestseller"}),
            new Product("BK005","Zero to One — Peter Thiel","Currency","Books",349,599,42,4.4,54321,"📗","Essential guide to building startups.",new String[]{"bestseller"}),
            new Product("BK006","Sapiens — Yuval Noah Harari","Vintage","Books",499,799,38,4.6,87432,"📘","History of humankind, global bestseller.",new String[]{"bestseller"}),
            new Product("BK007","Deep Work — Cal Newport","Piatkus","Books",399,699,43,4.5,43210,"📙","Focus strategies for distraction-free work.",new String[]{"new"}),
            new Product("BK008","The Lean Startup — Eric Ries","Crown Business","Books",449,699,36,4.4,38210,"📗","Build-measure-learn loop, MVP concept.",new String[]{"assured"}),
            new Product("BK009","Think and Grow Rich — Napoleon Hill","Fingerprint","Books",199,350,43,4.4,98432,"📕","Classic self-help, 100M copies sold.",new String[]{"bestseller"}),
            new Product("BK010","Ikigai — Héctor García & Francesc Miralles","Penguin","Books",299,499,40,4.5,67821,"📘","Japanese philosophy of purpose.",new String[]{"bestseller"})
    ));

    public static List<Product> getAll() { return ALL; }

    public static List<Product> getByCategory(String category) {
        return ALL.stream().filter(p -> p.category.equals(category)).collect(Collectors.toList());
    }

    public static List<Product> getDeals() {
        return ALL.stream()
                .filter(p -> p.discount >= 30)
                .sorted((a, b) -> b.discount - a.discount)
                .collect(Collectors.toList());
    }

    public static List<Product> search(String query) {
        String q = query.toLowerCase();
        return ALL.stream()
                .filter(p -> p.name.toLowerCase().contains(q)
                        || p.brand.toLowerCase().contains(q)
                        || p.category.toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    public static Product getById(String id) {
        return ALL.stream().filter(p -> p.id.equals(id)).findFirst().orElse(null);
    }
}
