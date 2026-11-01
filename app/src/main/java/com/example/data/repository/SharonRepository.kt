package com.example.data.repository

import com.example.R
import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar

class SharonRepository {

  // Paint Products Catalog
  // Initial Paint Products Catalog with official factory rates and strict packaging sizes
  private val initialProductsList: List<PaintProduct> = listOf(
    PaintProduct(
      id = "emulsion",
      name = "Sharon Emulsion Paint",
      uppercaseTitle = "EMULSION PAINT",
      category = PaintCategory.DECORATIVE,
      shortDescription = "Premium high-opacity water-based paint for interior and exterior walls.",
      fullDescription = "Sharon Emulsion Paint is manufactured with top-grade acrylic copolymer binders and light-fast pigments. Formulated to resist fading, chalking, and fungal growth while delivering a smooth, velvety finish.",
      availableSizes = listOf(PaintSize.SIZE_20L, PaintSize.SIZE_4L),
      prices = mapOf(
        PaintSize.SIZE_20L to 19780L,
        PaintSize.SIZE_4L to 4900L
      ),
      coverageSqmPerLiter = 10.0,
      dryingTimeHours = 1.0,
      recoatTimeHours = 2.5,
      recommendedCoats = 2,
      finishType = "Smooth Velvet Matt",
      suitableSurfaces = listOf("Plastered Walls", "Ceilings", "Concrete", "Asbestos Sheets"),
      applicationTools = listOf("Roller", "Brush", "Airless Spray"),
      imageResId = R.drawable.prod_sharon_emulsion,
      isPopular = true
    ),
    PaintProduct(
      id = "vineyard_matt",
      name = "Vineyard Matt Luxury Paint",
      uppercaseTitle = "VINEYARD MATT",
      category = PaintCategory.PREMIUM_MATT,
      shortDescription = "Ultra-rich designer matt paint with deep color saturation and washability.",
      fullDescription = "Part of our flagship Vineyard collection. Formulated for executive residences, corporate suites, and discerning interiors that demand total glare reduction and flawless elegance.",
      availableSizes = listOf(PaintSize.SIZE_20L),
      prices = mapOf(
        PaintSize.SIZE_20L to 47725L
      ),
      coverageSqmPerLiter = 12.0,
      dryingTimeHours = 1.5,
      recoatTimeHours = 3.0,
      recommendedCoats = 2,
      finishType = "Ultra-Low Sheen Luxury Matt (20L Drum)",
      suitableSurfaces = listOf("POP Screeded Walls", "Smooth Plaster", "Drywall"),
      applicationTools = listOf("Microfiber Roller", "Fine Bristle Brush"),
      imageResId = R.drawable.prod_sharon_vineyard_matt,
      isPopular = true
    ),
    PaintProduct(
      id = "vineyard_silk",
      name = "Vineyard Silk Soft Sheen",
      uppercaseTitle = "VINEYARD SILK",
      category = PaintCategory.PREMIUM_MATT,
      shortDescription = "Satin luster finish with 100% washability and stain resistance.",
      fullDescription = "Sharon Vineyard Silk gives walls a heavenly soft glow. Specially engineered for high-traffic corridors, master bedrooms, and dining areas. Stains wipe off effortlessly with a damp cloth.",
      availableSizes = listOf(PaintSize.SIZE_20L, PaintSize.SIZE_10L, PaintSize.SIZE_4L),
      prices = mapOf(
        PaintSize.SIZE_20L to 87400L,
        PaintSize.SIZE_10L to 44565L,
        PaintSize.SIZE_4L to 20070L
      ),
      coverageSqmPerLiter = 18.0,
      dryingTimeHours = 1.5,
      recoatTimeHours = 3.0,
      recommendedCoats = 2,
      finishType = "Satin Silk Soft Sheen (100% Washable)",
      suitableSurfaces = listOf("Screeded Walls", "Living Rooms", "Kitchens", "Offices"),
      applicationTools = listOf("High Density Foam Roller", "Synthetic Brush"),
      imageResId = R.drawable.prod_sharon_vineyard_silk,
      isPopular = true
    ),
    PaintProduct(
      id = "texcoat",
      name = "Sharon Texcoat Paint",
      uppercaseTitle = "TEXCOAT PAINT",
      category = PaintCategory.TEXTURED,
      shortDescription = "Heavy-bodied textured masonry coating with reinforced weather defense.",
      fullDescription = "High-build textured finish containing graded marble aggregates and elastomeric resins. Hides minor wall cracks, withstands torrential tropical downpours, and protects exterior facades.",
      availableSizes = listOf(PaintSize.SIZE_20L),
      prices = mapOf(
        PaintSize.SIZE_20L to 23460L
      ),
      coverageSqmPerLiter = 3.8,
      dryingTimeHours = 3.0,
      recoatTimeHours = 6.0,
      recommendedCoats = 1,
      finishType = "Fine to Medium Textured Relief",
      suitableSurfaces = listOf("Exterior Masonry", "Perimeter Fences", "Highways & Pillars"),
      applicationTools = listOf("Texture Roller", "Honeycomb Roller", "Trowel"),
      imageResId = R.drawable.prod_sharon_emulsion
    ),
    PaintProduct(
      id = "lylitex",
      name = "Lylitex Textured Coating",
      uppercaseTitle = "LYLITEX PAINT",
      category = PaintCategory.TEXTURED,
      shortDescription = "Architectural patterned texture paint for distinctive perimeter elegance.",
      fullDescription = "Sharon Lylitex delivers distinctive swirl, comb, or stipple finishes. Resistant to UV discoloration and aggressive tropical weather conditions.",
      availableSizes = listOf(PaintSize.SIZE_20L),
      prices = mapOf(
        PaintSize.SIZE_20L to 41170L
      ),
      coverageSqmPerLiter = 4.2,
      dryingTimeHours = 3.0,
      recoatTimeHours = 6.0,
      recommendedCoats = 1,
      finishType = "Relief Architectural Stipple",
      suitableSurfaces = listOf("Exterior Facades", "Commercial Buildings", "Balconies"),
      applicationTools = listOf("Pattern Trowel", "Stipple Brush"),
      imageResId = R.drawable.prod_sharon_vineyard_matt
    ),
    PaintProduct(
      id = "gold_matt",
      name = "Sharon Gold Matt Paint",
      uppercaseTitle = "GOLD MATT PAINT",
      category = PaintCategory.PREMIUM_MATT,
      shortDescription = "Premium high-solid matt paint with royal pigment richness.",
      fullDescription = "Sharon Gold Matt represents the pinnacle of our interior wall paints. Offers superior hiding power, scrub resistance, and a warm rich tactile feel.",
      availableSizes = listOf(PaintSize.SIZE_20L),
      prices = mapOf(
        PaintSize.SIZE_20L to 28600L
      ),
      coverageSqmPerLiter = 12.5,
      dryingTimeHours = 1.0,
      recoatTimeHours = 2.5,
      recommendedCoats = 2,
      finishType = "Velvety Gold Matt",
      suitableSurfaces = listOf("Screeded Living Rooms", "Luxury Hotels", "Chambers"),
      applicationTools = listOf("Roller", "Brush"),
      imageResId = R.drawable.prod_sharon_gold_matt,
      isPopular = true
    ),
    PaintProduct(
      id = "super_flex",
      name = "Sharon Super Flex Paint",
      uppercaseTitle = "SUPER FLEX PAINT",
      category = PaintCategory.SPECIALIZED,
      shortDescription = "Elastomeric bridge-coating paint that flexes with structural movement.",
      fullDescription = "Super Flex is formulated with 100% elastomeric polymers that stretch and bridge hairline cracks up to 1.5mm. Ideal for areas subject to vibration or settlement.",
      availableSizes = listOf(PaintSize.SIZE_20L),
      prices = mapOf(
        PaintSize.SIZE_20L to 87400L
      ),
      coverageSqmPerLiter = 8.0,
      dryingTimeHours = 2.0,
      recoatTimeHours = 4.0,
      recommendedCoats = 2,
      finishType = "Flexible Crack-Bridging Elastomeric",
      suitableSurfaces = listOf("Exterior Masonry", "Vibration Prone Walls", "Parapets"),
      applicationTools = listOf("Roller", "Airless Spray"),
      imageResId = R.drawable.prod_sharon_pop
    ),
    PaintProduct(
      id = "weather_proof",
      name = "Sharon Weather Proof Paint",
      uppercaseTitle = "WEATHER PROOF PAINT",
      category = PaintCategory.SPECIALIZED,
      shortDescription = "Extreme rain, UV, and salt-air shield for long-lasting exterior protection.",
      fullDescription = "Engineered with anti-algae and biocide chemistry to resist the harsh Nigerian rainy season, mold streaks, and intense sun exposure.",
      availableSizes = listOf(PaintSize.SIZE_20L),
      prices = mapOf(
        PaintSize.SIZE_20L to 50025L
      ),
      coverageSqmPerLiter = 10.0,
      dryingTimeHours = 1.5,
      recoatTimeHours = 3.5,
      recommendedCoats = 2,
      finishType = "Weather-Shield Satin UV Defense",
      suitableSurfaces = listOf("External Walls", "Coastal Buildings", "Rooftops & Parapets"),
      applicationTools = listOf("Roller", "Heavy Duty Brush"),
      imageResId = R.drawable.prod_sharon_vineyard_matt,
      isPopular = true
    ),
    PaintProduct(
      id = "oil_gloss",
      name = "Sharon Oil Gloss Paint",
      uppercaseTitle = "OIL GLOSS PAINT",
      category = PaintCategory.DECORATIVE,
      shortDescription = "Brilliant mirror-like synthetic alkyd enamel for wood and metal (4 Litres Drums Only).",
      fullDescription = "Sharon Gloss Finish delivers a tough, scratch-resistant enamel film with outstanding gloss retention. Protects metal gates, burglar proofs, railings, and wooden joinery.",
      availableSizes = listOf(PaintSize.SIZE_4L),
      prices = mapOf(
        PaintSize.SIZE_4L to 17390L
      ),
      coverageSqmPerLiter = 14.0,
      dryingTimeHours = 4.0,
      recoatTimeHours = 12.0,
      recommendedCoats = 2,
      finishType = "High Mirror Gloss Enamel (4L Drums Only)",
      suitableSurfaces = listOf("Metal Gates", "Steel Beams", "Doors", "Window Frames"),
      applicationTools = listOf("Natural Bristle Brush", "Roller", "Conventional Spray"),
      imageResId = R.drawable.prod_sharon_gloss,
      isPopular = true
    ),
    PaintProduct(
      id = "auto_paints",
      name = "Sharon Auto Paints",
      uppercaseTitle = "AUTO PAINT",
      category = PaintCategory.AUTOMOBILE,
      shortDescription = "Fast-drying high-gloss automotive enamel based on premium alkyd resin (4L Cans Only).",
      fullDescription = "Sharon Grape Yard Autoflex is an elastomeric high-performance automotive coating. Provides brilliant optical depth, exceptional color retention, and chemical resistance.",
      availableSizes = listOf(PaintSize.SIZE_4L),
      prices = mapOf(
        PaintSize.SIZE_4L to 14515L
      ),
      coverageSqmPerLiter = 15.0,
      dryingTimeHours = 0.5,
      recoatTimeHours = 1.5,
      recommendedCoats = 3,
      finishType = "Showroom Automotive Alkyd Enamel (4L Cans)",
      suitableSurfaces = listOf("Vehicle Bodies", "Tricycles/Keke", "Industrial Machinery"),
      applicationTools = listOf("HVLP Spray Gun", "Pneumatic Spray System"),
      imageResId = R.drawable.prod_sharon_auto_grapeyard,
      isPopular = true
    ),
    PaintProduct(
      id = "stucco_bonds",
      name = "Sharon Stucco Paints and Bonds",
      uppercaseTitle = "STUCCO PAINT & BOND",
      category = PaintCategory.TEXTURED,
      shortDescription = "Venetian marble-effect plaster and heavy duty masonry bonding agents.",
      fullDescription = "Achieve authentic Italian polished marble and stone textures. Contains modified acrylic resins and micro-fine calcium carbonate for burnished elegance.",
      availableSizes = listOf(PaintSize.SIZE_20L),
      prices = mapOf(
        PaintSize.SIZE_20L to 50140L
      ),
      coverageSqmPerLiter = 2.5,
      dryingTimeHours = 4.0,
      recoatTimeHours = 8.0,
      recommendedCoats = 2,
      finishType = "Venetian Burnished Stone / Stucco Plaster",
      suitableSurfaces = listOf("Feature Walls", "Pillars", "Luxury Ceilings", "Arches"),
      applicationTools = listOf("Stainless Steel Trowel", "Burnishing Spatula"),
      imageResId = R.drawable.prod_sharon_pop
    ),
    PaintProduct(
      id = "wall_putty",
      name = "Sharon Wall Putty (Screeding)",
      uppercaseTitle = "WALL PUTTY PAINT",
      category = PaintCategory.SURFACE_PREP,
      shortDescription = "Ultra-fine white cementitious screeding putty for glass-smooth wall prep.",
      fullDescription = "Formulated with German redispersible polymers. Fills micro-pores, eliminates surface waviness, and provides the ultimate mirror-flat base before applying Vineyard Silk or Matt.",
      availableSizes = listOf(PaintSize.SIZE_20L),
      prices = mapOf(
        PaintSize.SIZE_20L to 31165L
      ),
      coverageSqmPerLiter = 2.0,
      dryingTimeHours = 2.0,
      recoatTimeHours = 4.0,
      recommendedCoats = 2,
      finishType = "Glass-Smooth White Base (20L Drum)",
      suitableSurfaces = listOf("Raw Cement Plaster", "Brickwork", "Rough Concrete"),
      applicationTools = listOf("Screeding Blade", "Trowel", "Sanding Block"),
      imageResId = R.drawable.prod_sharon_pop
    ),
    PaintProduct(
      id = "onyx_paints",
      name = "Sharon Onyx Paints",
      uppercaseTitle = "ONYX PAINT",
      category = PaintCategory.SPECIALIZED,
      shortDescription = "Cost-effective high-durability emulsion paint with clean opacity.",
      fullDescription = "Sharon Onyx provides excellent coverage at an economical price point. Ideal for rental apartments, warehouses, and extensive commercial spaces.",
      availableSizes = listOf(PaintSize.SIZE_20L, PaintSize.SIZE_4L),
      prices = mapOf(
        PaintSize.SIZE_20L to 14835L,
        PaintSize.SIZE_4L to 4025L
      ),
      coverageSqmPerLiter = 8.0,
      dryingTimeHours = 1.5,
      recoatTimeHours = 3.0,
      recommendedCoats = 2,
      finishType = "Onyx Economy & Mineral Sheen",
      suitableSurfaces = listOf("Interior Plaster", "Warehouses", "Apartments"),
      applicationTools = listOf("Roller", "Brush"),
      imageResId = R.drawable.prod_sharon_silver
    ),
    PaintProduct(
      id = "pop_paints",
      name = "Sharon POP Paints",
      uppercaseTitle = "POP PAINT",
      category = PaintCategory.SURFACE_PREP,
      shortDescription = "Specialized ultra-white non-reflective paint for POP ceiling boards.",
      fullDescription = "High-porosity breathability designed specifically for Plaster of Paris (POP) cornices, drop ceilings, and gypsum boards without blistering or peel risk.",
      availableSizes = listOf(PaintSize.SIZE_20L),
      prices = mapOf(
        PaintSize.SIZE_20L to 22500L
      ),
      coverageSqmPerLiter = 11.0,
      dryingTimeHours = 1.0,
      recoatTimeHours = 2.0,
      recommendedCoats = 2,
      finishType = "Non-Reflective Pure White (20L Drum)",
      suitableSurfaces = listOf("POP Ceilings", "Cornices", "Gypsum Boards", "Acoustic Tiles"),
      applicationTools = listOf("Ceiling Roller", "Angle Brush"),
      imageResId = R.drawable.prod_sharon_pop,
      isPopular = true
    ),
    PaintProduct(
      id = "gravitex",
      name = "Sharon Gravitex Paints",
      uppercaseTitle = "GRAVITEX PAINT",
      category = PaintCategory.TEXTURED,
      shortDescription = "Aggressive granite-grain textured protective armor for exterior barriers.",
      fullDescription = "Granular aggregate heavy-duty exterior paint. Creates an impenetrable shield against dust, wind-driven rain, and vehicular abrasion.",
      availableSizes = listOf(PaintSize.SIZE_20L),
      prices = mapOf(
        PaintSize.SIZE_20L to 38500L
      ),
      coverageSqmPerLiter = 3.5,
      dryingTimeHours = 3.5,
      recoatTimeHours = 6.0,
      recommendedCoats = 1,
      finishType = "Granite Grain Protective Armor (20L Drum)",
      suitableSurfaces = listOf("Security Fences", "Industrial Facades", "Foundations"),
      applicationTools = listOf("Gravitex Hopper Gun", "Textured Trowel"),
      imageResId = R.drawable.prod_sharon_emulsion
    ),
    PaintProduct(
      id = "grape_yard",
      name = "Grape Yard Industrial Paints",
      uppercaseTitle = "GRAPEYARD PAINT",
      category = PaintCategory.AUTOMOBILE,
      shortDescription = "Heavy duty anti-corrosion protective coatings for structural steel (4L Cans Only).",
      fullDescription = "High chemical and marine environment resistance. Formulated for oil & gas installations, fuel tanks, bridges, and commercial vehicle chassis.",
      availableSizes = listOf(PaintSize.SIZE_4L),
      prices = mapOf(
        PaintSize.SIZE_4L to 27430L
      ),
      coverageSqmPerLiter = 18.0,
      dryingTimeHours = 1.0,
      recoatTimeHours = 3.0,
      recommendedCoats = 2,
      finishType = "Grape Yard High-Solid Protective Enamel (4L Cans)",
      suitableSurfaces = listOf("Steel Structures", "Chassis", "Storage Tanks"),
      applicationTools = listOf("Airless Spray Gun", "Heavy Duty Roller"),
      imageResId = R.drawable.prod_sharon_auto_grapeyard
    )
  )

  // Reactive Products State for Dynamic Price List Management by Admin
  private val _productsState = MutableStateFlow(initialProductsList)
  val productsFlow: StateFlow<List<PaintProduct>> = _productsState.asStateFlow()
  val products: List<PaintProduct> get() = _productsState.value

  private val _priceListRevisionNotes = MutableStateFlow("Factory Price Schedule 24th March 2026 - Official Factory Direct Rates")
  val priceListRevisionNotes: StateFlow<String> = _priceListRevisionNotes.asStateFlow()

  fun updateProductPrice(productId: String, size: PaintSize, newPrice: Long) {
    _productsState.value = _productsState.value.map { prod ->
      if (prod.id == productId) {
        val updatedMap = prod.prices.toMutableMap()
        updatedMap[size] = newPrice
        prod.copy(prices = updatedMap)
      } else {
        prod
      }
    }
  }

  fun uploadAndSyncPriceList(revisionTitle: String) {
    _priceListRevisionNotes.value = revisionTitle
  }

  // Color Charts with Sharon Official Codes & Swatches
  val colorCharts: List<ColorItem> = listOf(
    // Greys
    ColorItem("9090", "Cool Grey", "Greys", "#D3D7DB", true, "Crisp modern neutral grey for executive living rooms."),
    ColorItem("9091", "Topaz", "Greys", "#EAE6DF", true, "Soft warm off-grey with pearl undertone."),
    ColorItem("9092", "Silk Grey", "Greys", "#C8CFD4", true, "Cool refined silver silk tone."),
    ColorItem("9093", "253 Silver Grey", "Greys", "#B9C2CA", true, "Sharon classic architectural silver grey."),
    ColorItem("9094", "Light Ash", "Greys", "#CAD3D8", true, "Bright daylight ash grey for exterior trims."),
    ColorItem("9095", "225 Dove Grey", "Greys", "#9EA6AF", true, "Balanced mid-tone grey with muted warmth."),
    ColorItem("9097", "256 Gun Metal", "Greys", "#5C6771", true, "Deep charcoal graphite tone for accents."),

    // Beiges
    ColorItem("3029", "Light Husk", "Beiges", "#F2EBE1", true, "Delicate warm linen beige."),
    ColorItem("3030", "Polished Bread", "Beiges", "#EADECF", true, "Subtle toasted wheat tone."),
    ColorItem("3031", "Almond", "Beiges", "#DECDBB", true, "Rich buttery almond tone."),
    ColorItem("3032", "Ivory Silk", "Beiges", "#DDD5CE", true, "Opulent ivory with cashmere softness."),
    ColorItem("3033", "Baked Nut", "Beiges", "#CFC2B7", true, "Nutty clay neutral for cozy bedrooms."),
    ColorItem("3034", "Beige", "Beiges", "#BFB7A7", true, "Traditional balanced beige tone."),
    ColorItem("3035", "Calabash", "Beiges", "#9E998B", true, "Earthy clay beige with olive nuance."),

    // Browns
    ColorItem("3043", "Abuja Brown", "Browns", "#B3A59A", true, "Iconic capital city limestone brown."),
    ColorItem("3042", "Sky Honey", "Browns", "#D8C3A9", true, "Golden nectar honey brown."),
    ColorItem("1703", "Sand", "Browns", "#BA9E8E", true, "Sahara dune sand brown."),
    ColorItem("3044", "Rich Brown", "Browns", "#9E7154", true, "Warm timber and cedar brown."),
    ColorItem("1710", "Laterite Red", "Browns", "#7B554F", true, "Traditional Nigerian laterite soil red-brown."),
    ColorItem("3046", "Chocolate", "Browns", "#584B46", true, "Deep cocoa roast brown for exterior plinths."),

    // Yellows & Creams
    ColorItem("SP50", "Candle Light", "Yellows", "#FAF0D7", true, "Soft incandescent glow cream."),
    ColorItem("7010", "Vivid Yellow", "Yellows", "#F9E998", true, "Bright cheerful sunbeam yellow."),
    ColorItem("4046", "503 Bone White", "Yellows", "#F6EEDF", true, "Subtle cream white for all-over interior."),
    ColorItem("7012", "Pastel Yellow", "Yellows", "#F8E48F", true, "Gentle morning citrus pastel."),
    ColorItem("3040", "505 Cream", "Yellows", "#E9D29E", true, "Sharon classic warm rich cream."),
    ColorItem("7014", "Yellow Maize", "Yellows", "#F6CB61", true, "Golden harvest corn yellow."),
    ColorItem("7015", "Mustard", "Yellows", "#D1A955", true, "Deep historic spice mustard."),

    // Oranges
    ColorItem("SP51", "Beryl Cream", "Oranges", "#F5E9DC", true, "Delicate peach undertone cream."),
    ColorItem("SP53", "Magnolia", "Oranges", "#F6EBDD", true, "Classic British magnolia floral tone."),
    ColorItem("SP54", "Sardina Yellow", "Oranges", "#F6BFA1", true, "Mediterranean apricot orange."),
    ColorItem("3041", "Rich Cream", "Oranges", "#EFCCA5", true, "Velvety orange cream."),
    ColorItem("SP05", "Cameo", "Oranges", "#D7A27D", true, "Warm terracotta cameo peach."),
    ColorItem("SP06", "Dark Orange", "Oranges", "#BA6935", true, "Rich sunset bronze orange."),
    ColorItem("0004", "249 Orange", "Oranges", "#CB6826", true, "Sharon bold high-visibility orange."),

    // Reds & Pinks
    ColorItem("8080", "Eclair", "Reds", "#F8ECE7", true, "Sweet blush powder white."),
    ColorItem("8081", "Bubble Gum", "Reds", "#F6D2DC", true, "Playful vibrant pink for kids rooms."),
    ColorItem("1020", "Pink", "Reds", "#EDB5B7", true, "Elegant rose pink."),
    ColorItem("8090", "Minty Rose", "Reds", "#EAD8D6", true, "Cool desaturated dusty rose."),
    ColorItem("1021", "243 Coral", "Reds", "#CE8B88", true, "Living coral reef tone."),
    ColorItem("1023", "562 Chinese Red", "Reds", "#A2483E", true, "Historic brick red."),
    ColorItem("1024", "Venetian Red", "Reds", "#7C2C27", true, "Deep Renaissance wine red."),

    // Blues
    ColorItem("7090", "Paradise Water", "Blues", "#CCDDE8", true, "Calm tropical shallow lagoon blue."),
    ColorItem("8087", "527 Ice Grey", "Blues", "#D7E4EE", true, "Chilly glacial pale sky blue."),
    ColorItem("7082", "Powder Blue", "Blues", "#9EC8CE", true, "Vintage soft aqua powder blue."),
    ColorItem("SS 5110", "520 Light Blue", "Blues", "#76B1C9", true, "Clear mid-day azure sky blue."),
    ColorItem("7092", "Sky Blue", "Blues", "#85B6D4", true, "Serene atmospheric sky blue."),
    ColorItem("7083", "Calabar Blue", "Blues", "#5FA8D2", true, "Coastal maritime Calabar deep sky."),
    ColorItem("7084", "528 Lagoon", "Blues", "#338CBF", true, "Rich vibrant oceanic lagoon blue."),

    // Greens
    ColorItem("6060", "Ice Green", "Greens", "#E7EFE6", true, "Crisp frosty mint white."),
    ColorItem("6070", "532 Pastel Green", "Greens", "#BDDEBE", true, "Gentle herbal pastel green."),
    ColorItem("SP20", "Lemon", "Greens", "#BED79A", true, "Zesty lime-lemon tint."),
    ColorItem("6071", "536 Leaf Green", "Greens", "#7DB478", true, "Fresh spring foliage green."),
    ColorItem("5063", "239 Bay Leaf", "Greens", "#8B9565", true, "Muted organic olive leaf."),
    ColorItem("0010", "538 Emerald", "Greens", "#2D8C65", true, "Prestigious deep emerald jewel tone."),
    ColorItem("6075", "Green Pasture", "Greens", "#1C824C", true, "Vibrant lush meadow grass green."),

    // Violets
    ColorItem("7101", "Sweet Lilac", "Violets", "#BDBFE0", true, "Delicate French lavender."),
    ColorItem("7102", "Nursery Rhymes", "Violets", "#D2C5DF", true, "Whimsical pastel heather."),
    ColorItem("7103", "Lilac", "Violets", "#CBB8D4", true, "Soothing twilight orchid."),
    ColorItem("7104", "Sky Purple", "Violets", "#9596C9", true, "Royal horizon sky purple."),
    ColorItem("7105", "Purple", "Violets", "#7C639C", true, "Regal imperial velvet purple."),
    ColorItem("7106", "Bishopric", "Violets", "#745E8D", true, "Ceremonial deep bishop violet."),
    ColorItem("0012", "Pilot Blue", "Violets", "#2A6E9C", true, "Deep twilight aeronautical blue-violet."),

    // Automobile & Metallic
    ColorItem("DB 737", "Metallic Silver", "Auto & Metallic", "#D1D5DB", true, "High flake metallic auto silver."),
    ColorItem("DB 419", "Sharon Gold", "Auto & Metallic", "#B49544", true, "Signature metallic champagne gold."),
    ColorItem("RAL 5010", "Gentian Blue", "Auto & Metallic", "#1A3B6B", true, "European specification fleet blue."),
    ColorItem("P 1752", "Gunmetal Flake", "Auto & Metallic", "#4B5563", true, "Stealth metallic graphite."),
    ColorItem("VWL 7014", "Deep Onyx Black", "Auto & Metallic", "#1F2124", true, "High-depth mirror piano black."),

    // Whites & Specials
    ColorItem("1-024", "Brilliant White", "Whites & Specials", "#FFFFFF", true, "Sharon ultra-bright 99% light reflectance white."),
    ColorItem("9-102", "Off White", "Whites & Specials", "#FAF8F5", true, "Warm architectural anti-glare white."),
    ColorItem("9-103", "Signal Red", "Whites & Specials", "#B91C1C", true, "Emergency and safety bold signal red.")
  )

  // 8 Official Depots with real Nigerian contacts & coordinates
  val depots: List<Depot> = listOf(
    Depot(
      id = "depot_9th_mile",
      name = "9th Mile Central Factory Line & Headquarters",
      townState = "9th Mile Corner, Ngwo, Enugu State",
      address = "12-20 Sharon Paints Avenue, 9th Mile Corner, Ameke Ngwo, Enugu",
      managerName = "Mr. Ojuola",
      managerRole = "Central Factory Production & Plant Manager",
      phone = "+2348037789738",
      whatsappNumber = "+2348037789738",
      latitude = 6.4361,
      longitude = 7.3983,
      isHeadquarters = true
    ),
    Depot(
      id = "depot_onitsha",
      name = "Onitsha Commercial Depot",
      townState = "Onitsha, Anambra State",
      address = "32 Anyaeji Omenwa Street, Off Limca Old Road, Onitsha",
      managerName = "Mrs. Uwa",
      managerRole = "Commercial Distribution Lead",
      phone = "+2348033656828",
      whatsappNumber = "+2348033656828",
      latitude = 6.1362,
      longitude = 6.7933
    ),
    Depot(
      id = "depot_agbani",
      name = "Agbani Regional Depot",
      townState = "Agbani, Nkanu West, Enugu State",
      address = "241 Agbani Road, P.O. Box 14102, Enugu",
      managerName = "Mrs. Chiagugua",
      managerRole = "Depot Superintendent",
      phone = "+2348035055408",
      whatsappNumber = "+2348035055408",
      latitude = 6.3025,
      longitude = 7.5512
    ),
    Depot(
      id = "depot_abakpa",
      name = "Abakpa Strategic Commercial Depot",
      townState = "Abakpa Nike, Enugu State",
      address = "Nike Road / Abakpa Market Commercial Junction, Enugu",
      managerName = "Mrs. Grace",
      managerRole = "Abakpa Operations Supervisor",
      phone = "+2347069135897",
      whatsappNumber = "+2348037108731",
      latitude = 6.4678,
      longitude = 7.5342
    ),
    Depot(
      id = "depot_ugwuaji",
      name = "Ugwuaji Express Depot",
      townState = "Ugwuaji, Enugu South, Enugu State",
      address = "Plot 18 Ugwuaji Express Interchange, Enugu South",
      managerName = "Mr. Obinna Nnamani",
      managerRole = "Logistics Coordinator",
      phone = "+2348068824571",
      whatsappNumber = "+2348068824571",
      latitude = 6.3980,
      longitude = 7.5180
    ),
    Depot(
      id = "depot_sokoto",
      name = "Sokoto Northern Hub",
      townState = "Sokoto, Sokoto State",
      address = "Western Bypass Commercial Layout, Opp. Central Terminal, Sokoto",
      managerName = "Mr. Philimon",
      managerRole = "Northern Regional Depot Manager",
      phone = "+2348037949529",
      whatsappNumber = "+2348037949529",
      latitude = 13.0609,
      longitude = 5.2476
    ),
    Depot(
      id = "depot_abuja",
      name = "Abuja Federal Capital Depot",
      townState = "Dei-Dei, Abuja FCT",
      address = "Plot 142 Dei-Dei Building Materials Market, Abuja FCT",
      managerName = "Mr. Sunday Adebayo",
      managerRole = "FCT Territory Executive",
      phone = "+2348054428901",
      whatsappNumber = "+2348054428901",
      latitude = 9.0765,
      longitude = 7.3986
    ),
    Depot(
      id = "depot_okene",
      name = "Okene Transit Depot",
      townState = "Okene, Kogi State",
      address = "Total Roundabout Commercial Axis, Ajaokuta Road, Okene, Kogi",
      managerName = "Mallam Ibrahim Salihu",
      managerRole = "Central Belt Operations Lead",
      phone = "+2348136709934",
      whatsappNumber = "+2348136709934",
      latitude = 7.5508,
      longitude = 6.2361
    ),
    Depot(
      id = "depot_owerri",
      name = "Owerri Heartland Depot",
      townState = "Owerri, Imo State",
      address = "Relief Market Extension, Egbu Road Commercial Strip, Owerri",
      managerName = "Mr. Kelechi Anyanwu",
      managerRole = "Imo State Branch Manager",
      phone = "+2348029983341",
      whatsappNumber = "+2348029983341",
      latitude = 5.4836,
      longitude = 7.0333
    )
  )

  // Two Pre-configured Company Admins
  val companyAdmins: List<CompanyAdmin> = listOf(
    CompanyAdmin(
      id = "admin_1",
      name = "Mrs. Adaesze Chikaodiri Phillip",
      role = "First App Administrator & Senior Operations Director",
      confirmationCode = "SHP-ADA-0816",
      email = "adaesze.phillip@sharonpaint.com",
      phone = "+2348167043645"
    ),
    CompanyAdmin(
      id = "admin_2",
      name = "Mrs. Ani Nnenna",
      role = "Second App Administrator & Executive Finance Officer",
      confirmationCode = "SHP-ANI-0810",
      email = "ani.nnenna@sharonpaint.com",
      phone = "+2348100777371"
    )
  )

  // AI Voice Consultant - Painting Errors, Remedies, and Advice
  val paintingIssues: List<PaintingIssue> = listOf(
    PaintingIssue(
      id = "blistering",
      problemTitle = "Blistering & Bubbling (Hollow Pockets)",
      defectCategory = "Moisture & Heat",
      symptomDescription = "Bubbles or blister domes developing under the paint film, often filled with air or moisture.",
      rootCauses = listOf(
        "Painting on wet or uncured plaster surfaces.",
        "Moisture migrating through the exterior wall from plumbing leaks or rising dampness.",
        "Applying oil gloss directly under blistering afternoon tropical heat (vapors trapped)."
      ),
      audioExplanation = "Hello, this is your Sharon AI voice consultant. Blistering occurs when trapped moisture or solvent vapors expand underneath the paint film. Never paint over freshly plastered walls until they have cured for at least twenty-eight days. To fix this, scrape away all loose blistered bubbles, sand flat, apply Sharon Wall Putty, prime with Sharon Sealer, and recoat with Sharon Emulsion.",
      stepByStepSolution = listOf(
        "Scrape all blistered and hollow areas down to the sound substrate with a putty knife.",
        "Allow wall to dry completely (moisture content must be below 12%).",
        "Apply Sharon Wall Putty to level the surface, followed by fine grit sanding (220-grit).",
        "Apply one coat of Sharon Alkali-Resistant Primer before your finish coats."
      ),
      recommendedSharonProduct = "Sharon Wall Putty & Weather Proof Paint",
      preventionTips = "Always ensure wall surfaces are thoroughly dry before painting and avoid painting in direct noon scorching sunlight."
    ),
    PaintingIssue(
      id = "peeling",
      problemTitle = "Peeling & Flaking (Loss of Adhesion)",
      defectCategory = "Surface Preparation",
      symptomDescription = "Large sheets or flakes of paint separating cleanly from the underlying wall or previous coats.",
      rootCauses = listOf(
        "Painting over powdery, chalking old paint without washing or priming.",
        "Applying water-based emulsion over greasy or glossy oil paint without scuff sanding.",
        "Presence of efflorescence salt deposits on the cement."
      ),
      audioExplanation = "This is your Sharon AI voice tutor. Paint peeling happens due to poor surface preparation and weak adhesion. If you apply high-quality paint over chalky residue or un-sanded gloss paint, the new paint will peel like wallpaper. The remedy is to wire-brush and wash away all old flakes, sand thoroughly, apply Sharon Primer Bond, and then apply two coats of Sharon Vineyard Matt or Silk.",
      stepByStepSolution = listOf(
        "Scrape and wire-brush all loose, peeling edges back until you reach solid firm paint.",
        "Sand the transition edges smooth (feather-edge) to eliminate visible paint borders.",
        "Wash the entire wall with clean water and detergent to remove grease and dust.",
        "Apply Sharon Primer Bond, then recoat with Sharon Vineyard Silk."
      ),
      recommendedSharonProduct = "Sharon Vineyard Silk & Primer Bond",
      preventionTips = "Always prime previously painted surfaces and never apply emulsion directly over high-gloss enamel without de-glossing."
    ),
    PaintingIssue(
      id = "chalking",
      problemTitle = "Chalking & Powdering (White Dust on Hands)",
      defectCategory = "Pigment Weathering",
      symptomDescription = "A fine powdery dust forming on the paint surface that comes off onto clothes or fingertips.",
      rootCauses = listOf(
        "Excessive thinning of paint with too much water, breaking down the acrylic binder.",
        "Using cheap interior paints on exterior walls exposed to harsh tropical sunlight.",
        "Severe chemical erosion from extreme ultraviolet (UV) radiation."
      ),
      audioExplanation = "Greetings! Sharon AI consultant here. Chalking is when the paint resin binder breaks down, releasing loose pigment powder. This happens when painters over-dilute paint with water to stretch it, which weakens the glue holding the pigments. To rectify, wash down the chalky powder with a pressure washer or sponge, let it dry, and apply Sharon Weather Proof Paint which is engineered with UV stabilizers.",
      stepByStepSolution = listOf(
        "Wash the wall with water and stiff nylon brush to remove all powdery residue.",
        "Allow 24 hours of drying time under sunshine.",
        "Apply Sharon Acrylic Masonry Sealer to bind any remaining micro-particles.",
        "Apply two unadulterated coats of Sharon Weather Proof Paint."
      ),
      recommendedSharonProduct = "Sharon Weather Proof Paint (with UV Shield)",
      preventionTips = "Never dilute Sharon Emulsion with more than 10-15% clean water. Over-dilution destroys paint durability."
    ),
    PaintingIssue(
      id = "roller_marks",
      problemTitle = "Roller Spatter & Lap Marks (Uneven Bands)",
      defectCategory = "Application Technique",
      symptomDescription = "Visible darker stripes where roller paths overlap, and thick ridges or splatters across the wall.",
      rootCauses = listOf(
        "Allowing a painted section to dry before rolling into it (failing to maintain a wet edge).",
        "Using cheap low-quality rollers or applying too much heavy pressure on the roller handle.",
        "Painting in windy or drafty conditions that flash-dry the paint too fast."
      ),
      audioExplanation = "Hello painter! Sharon AI voice tutor advises: lap marks occur when you roll over an edge that has already started drying. Always maintain a wet edge by working in two-meter vertical sections from top to bottom. Use an overlapping W or M pattern, followed by gentle, vertical finishing strokes. Sharon Vineyard Matt has extended open-time to eliminate lap marks entirely.",
      stepByStepSolution = listOf(
        "Lightly scuff sand the ridges with 180-grit sandpaper.",
        "Wipe away sanding dust with a microfiber cloth.",
        "Reapply using Sharon Vineyard Matt: work swiftly to maintain a continuous wet edge.",
        "Roll vertically in one direction from top to bottom for the final pass."
      ),
      recommendedSharonProduct = "Sharon Vineyard Matt Luxury Paint",
      preventionTips = "Keep windows closed temporarily during application to prevent premature flash-drying, and use premium microfiber roller sleeves."
    ),
    PaintingIssue(
      id = "efflorescence",
      problemTitle = "Efflorescence (White Crusty Salt Deposits)",
      defectCategory = "Chemical & Masonry",
      symptomDescription = "White crusty crystalline salts blooming on the paint surface, pushing through cement and brick.",
      rootCauses = listOf(
        "Water leaching soluble mineral salts out of unsealed cement, sand, or mortar blocks.",
        "Rising dampness from lack of a proper Damp Proof Course (DPC) at the building foundation.",
        "Alkali attack breaking down the paint binder."
      ),
      audioExplanation = "Attention from Sharon AI consultant! Efflorescence is not mold; it is soluble mineral salts carried by water through the masonry. When the water evaporates, hard white crystals are left behind. Do not simply paint over it! Wire-brush off all crystals, treat with mild vinegar solution, seal the moisture source, apply Sharon Alkali-Resistant Primer, and finish with Sharon Super Flex.",
      stepByStepSolution = listOf(
        "Dry-brush the salt deposits away completely with a stiff wire brush (do not use water initially).",
        "Locate and fix the water infiltration or pipe leakage source.",
        "Neutralize the masonry alkali with a dilute 5% acetic acid rinse, then flush clean.",
        "Apply Sharon Alkali-Resistant Primer followed by two coats of Sharon Super Flex."
      ),
      recommendedSharonProduct = "Sharon Super Flex & Alkali-Resistant Primer",
      preventionTips = "Always ensure proper building foundation damp-proofing and cure blocks before applying decorative coats."
    ),
    PaintingIssue(
      id = "algae_mold",
      problemTitle = "Algae, Mildew & Green/Black Stains",
      defectCategory = "Biological Growth",
      symptomDescription = "Discolored green, dark brown, or black velvety patches, especially on shaded exterior damp walls.",
      rootCauses = listOf(
        "Persistent high humidity and dripping roof gutters without sufficient sunlight.",
        "Absence of anti-fungal and biocide additives in lower-quality paints.",
        "Painting over living spores without sterilizing the wall."
      ),
      audioExplanation = "Sharon AI voice tutor here. Tropical humidity creates the perfect breeding ground for mold and algae. Painting over live mildew simply feeds the fungus and it will burst through within weeks. Wash the surface with a solution of household bleach and water, rinse well, allow complete drying, and apply Sharon Weather Proof Paint which contains certified anti-fungal biocides.",
      stepByStepSolution = listOf(
        "Scrub infected walls with a solution of 1 part bleach to 3 parts water.",
        "Allow bleach solution to stand for 15 minutes to kill all root spores.",
        "Rinse thoroughly with clean water and let dry for 24-48 hours.",
        "Apply two protective coats of Sharon Weather Proof Paint."
      ),
      recommendedSharonProduct = "Sharon Weather Proof Paint (Fungicide Formula)",
      preventionTips = "Redirect rainwater gutters away from exterior walls and ensure adequate foliage trimming around building facades."
    )
  )

  // AI Voice Tutor Lessons
  val tutorLessons: List<VoiceLesson> = listOf(
    VoiceLesson(
      id = "lesson_1",
      lessonNumber = 1,
      title = "Choosing the Perfect Paint: Emulsion vs. Matt vs. Silk",
      duration = "3 mins",
      summary = "Understand optical sheens, light reflectance, and room-specific suitability across the Sharon catalog.",
      spokenScript = "Welcome to Lesson One of the Sharon Paints Masterclass. Many customers ask whether to choose Emulsion, Vineyard Matt, or Vineyard Silk. Here is the golden rule: Sharon Emulsion is the versatile workhorse—breathable, high-coverage, and economical for bedrooms, corridors, and exterior perimeter walls. Vineyard Matt provides zero-glare, camera-ready elegance, hiding plaster imperfections on living room walls and executive suites. Finally, Vineyard Silk offers an exquisite pearl satin luster with 100% washability. It is your best choice for kitchens, children's rooms, dining halls, and high-touch areas where stains must be wiped off effortlessly without stripping color.",
      keyTakeaways = listOf(
        "Sharon Emulsion: Best for general walls, high-breathability, and exterior budgets.",
        "Vineyard Matt: Best for hiding wall undulations and luxury master lounges.",
        "Vineyard Silk: 100% washable, satin luster, ideal for kitchens and busy hallways."
      )
    ),
    VoiceLesson(
      id = "lesson_2",
      lessonNumber = 2,
      title = "Surface Preparation & The Power of Sharon Wall Putty",
      duration = "4 mins",
      summary = "Why 80% of paint longevity depends on screeding and priming before color application.",
      spokenScript = "Lesson Two: The secret to a million-naira finish is not just the paint—it is the preparation underneath. No topcoat can rescue a rough, dusty, or crumbling wall. Before opening your paint bucket, inspect the plaster. Patch large voids with cement mortar, then apply two tight passes of Sharon Wall Putty. Sharon Putty is formulated with redispersible polymers that bond tenaciously to cement, creating a glass-smooth surface. Once dry, sand with fine 220-grit sandpaper, brush off all micro-dust, and apply an acrylic primer. Your topcoat of Sharon Vineyard Silk will glide on like glass and last for over a decade.",
      keyTakeaways = listOf(
        "Never paint over raw crumbling plaster without screeding.",
        "Sharon Wall Putty bridges micro-pores and prevents uneven paint absorption.",
        "Always dust down after sanding before applying finish paint."
      )
    ),
    VoiceLesson(
      id = "lesson_3",
      lessonNumber = 3,
      title = "Mastering Exterior Weatherproofing in the Nigerian Climate",
      duration = "4 mins",
      summary = "How Sharon Weather Proof and Texcoat defend against tropical storms, UV sun bleaching, and algae.",
      spokenScript = "Lesson Three focuses on exterior durability. Nigeria's tropical climate subjects buildings to relentless alternating cycles of scorching UV sunshine and torrential rains. Inferior paints fade, crack, and turn black with algae within six months. Sharon Weather Proof Paint is fortified with high-grade titanium dioxide and biocide inhibitors. For buildings requiring architectural impact, Sharon Texcoat and Gravitex incorporate graded marble aggregates that provide thick, protective armor against wind-driven rain and physical impacts. For maximum protection, apply on dry walls with our specialized textured rollers.",
      keyTakeaways = listOf(
        "Exterior paints require UV stabilizers and biocide fungicides.",
        "Sharon Texcoat and Gravitex provide structural armor and hide exterior masonry cracks.",
        "Never apply exterior paint when rain is forecast within 4 hours."
      )
    ),
    VoiceLesson(
      id = "lesson_4",
      lessonNumber = 4,
      title = "The Art of Painting POP Ceilings Without Peeling",
      duration = "3 mins",
      summary = "Correct techniques for painting Plaster of Paris (POP) cornices and ceiling boards.",
      spokenScript = "Lesson Four: Plaster of Paris, or POP, is delicate. Standard heavy paints can cause POP to suffocate and peel under thermal expansion. Sharon POP Paint is specifically engineered with ultra-high breathability and pure white light dispersion. When painting new POP ceilings, verify that the plaster has dried completely to a chalk-white state. Wipe away casting oils, apply Sharon POP Paint evenly with a low-nap roller, and cut in cornices with a soft sash brush. The result is a brilliant, non-reflective ceiling that makes rooms look higher and brighter.",
      keyTakeaways = listOf(
        "New POP must be 100% bone dry before painting.",
        "Sharon POP Paint provides non-reflective, pure white light diffusion.",
        "Avoid using glossy or heavy enamel paint on POP ceilings."
      )
    ),
    VoiceLesson(
      id = "lesson_5",
      lessonNumber = 5,
      title = "Auto Paints & Metal Enamels: Grape Yard Autoflex Techniques",
      duration = "4 mins",
      summary = "Achieving mirror-like reflections on vehicles, gates, and industrial steel.",
      spokenScript = "Lesson Five: Sharon Grape Yard Autoflex and Oil Gloss are designed for high-stress surfaces. When painting automobile panels or steel gates, rust removal is paramount. Use wire wheels or sandpaper to expose bare metal. Apply anti-corrosive red oxide or zinc chromate primer. Once primed, apply Sharon Grape Yard Autoflex in thin, uniform coats using a spray gun at 35 to 45 PSI, allowing 15 minutes flash-off between coats. The alkyd resin cures into a mirror-like finish that repels scratches, rain, and road debris.",
      keyTakeaways = listOf(
        "Always clean and de-rust metal before applying primer.",
        "Apply multiple thin coats rather than one heavy, sagging coat.",
        "Sharon Grape Yard Autoflex gives showroom high-gloss protection."
      )
    )
  )

  // AI Musics & Video Ads
  val advertisements: List<AdvertisementItem> = listOf(
    AdvertisementItem(
      id = "ad_jingle_1",
      title = "Sharon Paints Official Anthem: Beauty That Lasts",
      format = "Audio Jingle & Radio Ad",
      duration = "0:45",
      tagLine = "The World Pace-Setters in Paint Making",
      description = "Inspiring high-energy musical jingle celebrating Sharon Paints' 40-year legacy of quality, beauty, and durability.",
      scriptContent = "From the heart of Enugu at 9th Mile, across Onitsha, Abuja, and every home in Nigeria... When you want beauty that defies the rain, colors that never fade, and elegance that speaks of royalty, trust Sharon Paints! The world pace-setters in paint making! Sharon Emulsion, Vineyard Silk, Texcoat, and Auto Paints! Call today or visit your nearest Sharon Depot! Sharon Paints: Beauty That Lasts Forever!",
      audioTone = "Uplifting, Afro-orchestral rhythm, energetic vocal chorus with warm professional voiceover.",
      visualDescription = "Sweeping drone footage of modern mansions painted in Sharon Brilliant White and Dove Grey, transitioning to the automated 9th Mile factory and smiling professional painters."
    ),
    AdvertisementItem(
      id = "ad_video_1",
      title = "Rain-Shield Weather Proof TV Commercial",
      format = "Video Commercial Reel",
      duration = "0:30",
      tagLine = "Tougher Than the Toughest Nigerian Rains",
      description = "Cinematic slow-motion demonstration of water beading and sliding off a Sharon Weather Proof painted exterior during a heavy storm.",
      scriptContent = "Rain is a blessing, but to your building walls, it can be a test. Water, algae, peeling paint... Unless your walls wear the armor of Sharon Weather Proof! Watch water bounce off without soaking in. Certified by Standards Organisation of Nigeria, NIS, and MANCAP. Sharon Paints: Defend your investment.",
      audioTone = "Cinematic thunder sound effects, warm deep baritone voiceover, victorious orchestral swell.",
      visualDescription = "Close-up slow-motion raindrops beading on vibrant yellow and terracotta walls, clean paint surface with water sliding right off into lush green gardens."
    ),
    AdvertisementItem(
      id = "ad_jingle_2",
      title = "Vineyard Silk: The Touch of Royalty",
      format = "Audio Jingle & Radio Ad",
      duration = "0:35",
      tagLine = "Satin Glow, 100% Washable",
      description = "Smooth jazz and acoustic guitar ad highlighting the luxurious satin sheen and washability of Sharon Vineyard Silk.",
      scriptContent = "Imagine a home where coffee spills, children's crayon marks, and kitchen steam vanish with just a single wipe. That is the magic of Sharon Vineyard Silk! Soft sheen satin, rich velvety colors, and complete washability. Turn your house into a palace. Sharon Paints: The World Pace-Setters in Paint Making.",
      audioTone = "Smooth jazz piano, gentle acoustic guitar, luxurious conversational voice.",
      visualDescription = "A mother smiling as she effortlessly wipes a crayon smudge off a pristine ivory silk wall with a damp cloth, leaving the radiant satin finish untouched."
    ),
    AdvertisementItem(
      id = "ad_video_2",
      title = "Automotive Excellence: Grape Yard Autoflex Showcase",
      format = "Video Commercial Reel",
      duration = "0:40",
      tagLine = "Mirror High Gloss for Champions",
      description = "High-octane spray booth video showing precision auto paint application with dazzling specular reflections.",
      scriptContent = "On the highway, your car is your statement. Give it the showroom mirror reflection it deserves with Sharon Grape Yard Autoflex! Fast drying, scratch resistant, brilliant gloss that turns heads everywhere you drive. Available at all Sharon Depots nationwide.",
      audioTone = "Energetic synth-wave, spray gun sound effects, crisp automotive sports commentary style.",
      visualDescription = "A sleek silver sedan undergoing high-pressure spray painting, emerging into sunlight with glass-like specular highlights reflecting the sky."
    )
  )

  // Live Stock Inventory
  private val _stockItems = MutableStateFlow<List<StockItem>>(
    products.flatMap { prod ->
      listOf(
        StockItem(prod.id, prod.name, PaintSize.SIZE_20L, quantity = (80..350).random(), unitPrice = prod.getPrice(PaintSize.SIZE_20L)),
        StockItem(prod.id, prod.name, PaintSize.SIZE_10L, quantity = (40..220).random(), unitPrice = prod.getPrice(PaintSize.SIZE_10L)),
        StockItem(prod.id, prod.name, PaintSize.SIZE_4L, quantity = (60..300).random(), unitPrice = prod.getPrice(PaintSize.SIZE_4L))
      )
    }
  )
  val stockItems: StateFlow<List<StockItem>> = _stockItems.asStateFlow()

  // Orders State
  private val _orders = MutableStateFlow<List<Order>>(
    listOf(
      Order(
        id = "ORD-90214",
        trackingCode = "SHP-TRK-78421",
        customer = CustomerProfile(
          businessName = "Apex Mega Construction Nig Ltd",
          contactPerson = "Arc. Jude Eze",
          email = "jude.eze@apexbuild.ng",
          phone = "08037129940",
          shopAddress = "14 Garden Avenue, GRA, Enugu",
          isOtpVerified = true,
          isBiometricEnabled = true,
          securityRegistrationCode = "SEC-7749"
        ),
        items = listOf(
          CartItem(products[0], PaintSize.SIZE_20L, colorCharts[0], 25), // 25 buckets of 20L Emulsion
          CartItem(products[2], PaintSize.SIZE_20L, colorCharts[51], 15) // 15 buckets of 20L Vineyard Silk Brilliant White
        ),
        targetDepotId = "depot_9th_mile",
        scheduledPickupDate = "Tuesday, Nov 03, 2026",
        scheduledPickupTime = "10:30 AM",
        isWithinWorkingHours = true,
        totalAmount = (25 * 28500L) + (15 * 56000L), // 712,500 + 840,000 = 1,552,500
        paymentReference = "ACC-REF-20261027-010439",
        status = OrderStatus.APPROVED_FOR_PICKUP,
        approvedByAdmin = "Mrs. Adaesze Chikaodiri Phillip (SHP-ADA-0816)",
        createdAt = System.currentTimeMillis() - 86400000L,
        antiFraudVerificationHash = "SHA256-9A8B7C6D-SHARON-SECURE"
      ),
      Order(
        id = "ORD-90215",
        trackingCode = "SHP-TRK-88319",
        customer = CustomerProfile(
          businessName = "Onitsha Bridgehead Master Painters Hub",
          contactPerson = "Mr. Kenneth Okoli",
          email = "okoli.ken@gmail.com",
          phone = "08023341182",
          shopAddress = "Shop 4, Line 3, Building Materials Market, Onitsha",
          isOtpVerified = true,
          isBiometricEnabled = true,
          securityRegistrationCode = "SEC-9182"
        ),
        items = listOf(
          CartItem(products[3], PaintSize.SIZE_20L, colorCharts[18], 10), // 10 buckets of Texcoat
          CartItem(products[8], PaintSize.SIZE_4L, colorCharts[53], 20) // 20 cans of Oil Gloss Signal Red
        ),
        targetDepotId = "depot_onitsha",
        scheduledPickupDate = "Wednesday, Nov 04, 2026",
        scheduledPickupTime = "02:00 PM",
        isWithinWorkingHours = true,
        totalAmount = (10 * 36000L) + (20 * 13000L), // 360,000 + 260,000 = 620,000
        paymentReference = "ACC-REF-20261028-449102",
        status = OrderStatus.PAYMENT_SUBMITTED,
        approvedByAdmin = "",
        createdAt = System.currentTimeMillis() - 25000000L,
        antiFraudVerificationHash = "SHA256-4D3C2B1A-SHARON-SECURE"
      )
    )
  )
  val orders: StateFlow<List<Order>> = _orders.asStateFlow()

  // Shopping Cart State
  private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
  val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

  // Current Active Customer Profile
  private val _currentCustomer = MutableStateFlow(
    CustomerProfile(
      businessName = "Standard Decor & Paint Masters",
      contactPerson = "Alhaji / Engr. Customer",
      email = "customer@sharonpaint.com",
      phone = "08101615380",
      shopAddress = "Plot 12 Commercial Way, Enugu",
      isOtpVerified = true,
      isBiometricEnabled = true,
      securityRegistrationCode = "SEC-5501"
    )
  )
  val currentCustomer: StateFlow<CustomerProfile> = _currentCustomer.asStateFlow()

  // Logged-in Admin (null if viewing customer mode)
  private val _activeAdmin = MutableStateFlow<CompanyAdmin?>(null)
  val activeAdmin: StateFlow<CompanyAdmin?> = _activeAdmin.asStateFlow()

  // Audit Receipts
  private val _auditReceipts = MutableStateFlow<List<StockAuditReceipt>>(
    listOf(
      StockAuditReceipt(
        receiptId = "AUD-MTH-2026-10",
        period = AuditReportPeriod.MONTHLY,
        periodName = "October 2026 Monthly Audit",
        generatedDate = "28 Oct 2026",
        generatedByAdmin = "Mrs. Adaesze Chikaodiri Phillip (SHP-ADA-0816)",
        totalUnitsManufactured = 14200,
        totalUnitsSold = 11850,
        currentUnitsInStock = 2350,
        inventoryValueNaira = 98450000L,
        totalSalesRevenueNaira = 412850000L,
        accessBankApprovedTransfers = 412850000L
      )
    )
  )
  val auditReceipts: StateFlow<List<StockAuditReceipt>> = _auditReceipts.asStateFlow()

  // Cart operations
  fun addToCart(product: PaintProduct, size: PaintSize, color: ColorItem, quantity: Int) {
    val current = _cart.value.toMutableList()
    val index = current.indexOfFirst { it.product.id == product.id && it.size == size && it.color.code == color.code }
    if (index >= 0) {
      val existing = current[index]
      current[index] = existing.copy(quantity = existing.quantity + quantity)
    } else {
      current.add(CartItem(product, size, color, quantity))
    }
    _cart.value = current
  }

  fun updateCartQuantity(index: Int, newQuantity: Int) {
    val current = _cart.value.toMutableList()
    if (index in current.indices) {
      if (newQuantity <= 0) {
        current.removeAt(index)
      } else {
        current[index] = current[index].copy(quantity = newQuantity)
      }
      _cart.value = current
    }
  }

  fun clearCart() {
    _cart.value = emptyList()
  }

  // Customer Management
  fun updateCustomerProfile(profile: CustomerProfile) {
    _currentCustomer.value = profile
  }

  fun logoutCustomer() {
    _currentCustomer.value = _currentCustomer.value.copy(
      isOtpVerified = false,
      isBiometricEnabled = false
    )
  }

  fun verifyOtp(enteredCode: String): Boolean {
    // 6-digit OTP verification
    if (enteredCode.length == 6) {
      _currentCustomer.value = _currentCustomer.value.copy(isOtpVerified = true)
      return true
    }
    return false
  }

  // Admin Authentication
  fun loginAdmin(confirmationCode: String): CompanyAdmin? {
    val matched = companyAdmins.find { it.confirmationCode.equals(confirmationCode.trim(), ignoreCase = true) }
    if (matched != null) {
      _activeAdmin.value = matched
    }
    return matched
  }

  fun logoutAdmin() {
    _activeAdmin.value = null
  }

  // Working Hours Verification: Tuesday - Saturday, 9am - 5pm
  fun isWithinOperatingHours(dayOfWeek: Int, hourOfDay: Int): Boolean {
    // Calendar.SUNDAY = 1, MONDAY = 2, TUESDAY = 3, WEDNESDAY = 4, THURSDAY = 5, FRIDAY = 6, SATURDAY = 7
    val isWorkingDay = dayOfWeek in listOf(Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY)
    val isWorkingHour = hourOfDay in 9..16 // 9:00 AM to 4:59 PM (closes at 5:00 PM)
    return isWorkingDay && isWorkingHour
  }

  // Place Order
  fun placeOrder(
    depotId: String,
    pickupDate: String,
    pickupTime: String,
    paymentRef: String,
    isWithinHours: Boolean
  ): Order {
    val items = _cart.value.toList()
    val total = items.sumOf { it.subtotal }
    val newOrder = Order(
      id = "ORD-${(10000..99999).random()}",
      trackingCode = "SHP-TRK-${(10000..99999).random()}",
      customer = _currentCustomer.value,
      items = items,
      targetDepotId = depotId,
      scheduledPickupDate = pickupDate,
      scheduledPickupTime = pickupTime,
      isWithinWorkingHours = isWithinHours,
      totalAmount = total,
      paymentReference = paymentRef,
      bankName = "Access Bank PLC",
      accountNumber = "0104392154",
      accountName = "Sharon paints and chemical company",
      status = if (paymentRef.isNotBlank()) OrderStatus.PAYMENT_SUBMITTED else OrderStatus.AWAITING_PAYMENT,
      antiFraudVerificationHash = "SHA256-${(100000..999999).random()}-SHARON"
    )
    _orders.value = listOf(newOrder) + _orders.value
    clearCart()
    return newOrder
  }

  // Admin Approval of Access Bank Payments
  fun approveOrderPayment(orderId: String, adminName: String): Boolean {
    val list = _orders.value.toMutableList()
    val index = list.indexOfFirst { it.id == orderId }
    if (index >= 0) {
      val ord = list[index]
      list[index] = ord.copy(
        status = OrderStatus.APPROVED_FOR_PICKUP,
        approvedByAdmin = adminName
      )
      _orders.value = list
      return true
    }
    return false
  }

  // Admin Update Stock
  fun updateStockQuantity(productId: String, size: PaintSize, newQuantity: Int) {
    val list = _stockItems.value.toMutableList()
    val index = list.indexOfFirst { it.productId == productId && it.size == size }
    if (index >= 0) {
      list[index] = list[index].copy(quantity = newQuantity)
      _stockItems.value = list
    }
  }

  // Generate Periodic Stock Audit Receipt
  fun generateAuditReceipt(period: AuditReportPeriod, adminName: String): StockAuditReceipt {
    val totalStockVal = _stockItems.value.sumOf { it.totalValuation }
    val totalUnits = _stockItems.value.sumOf { it.quantity }
    val approvedRevenue = _orders.value
      .filter { it.status == OrderStatus.APPROVED_FOR_PICKUP || it.status == OrderStatus.COMPLETED }
      .sumOf { it.totalAmount }

    val receipt = StockAuditReceipt(
      receiptId = "AUD-${if (period == AuditReportPeriod.MONTHLY) "MTH" else "YRL"}-${System.currentTimeMillis() % 100000}",
      period = period,
      periodName = if (period == AuditReportPeriod.MONTHLY) "Current Month Sharon Audit Receipt" else "Annual Sharon Consolidated Ledger",
      generatedDate = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault()).format(java.util.Date()),
      generatedByAdmin = adminName,
      totalUnitsManufactured = totalUnits + 5000,
      totalUnitsSold = 4800,
      currentUnitsInStock = totalUnits,
      inventoryValueNaira = totalStockVal,
      totalSalesRevenueNaira = approvedRevenue + 12500000L,
      accessBankApprovedTransfers = approvedRevenue + 12500000L
    )

    _auditReceipts.value = listOf(receipt) + _auditReceipts.value
    return receipt
  }
}
