package com.example

import com.example.data.model.AuditReportPeriod
import com.example.data.model.PaintCategory
import com.example.data.model.PaintSize
import com.example.data.repository.SharonRepository
import org.junit.Assert.*
import org.junit.Test
import java.util.Calendar

class ExampleUnitTest {

  private val repository = SharonRepository()

  @Test
  fun testProductCatalogIntegrity() {
    val products = repository.products
    // Verify all 16 paint types exist
    assertTrue("Should contain at least 16 products", products.size >= 16)
    
    val productIds = products.map { it.id }
    assertTrue(productIds.contains("emulsion"))
    assertTrue(productIds.contains("vineyard_matt"))
    assertTrue(productIds.contains("vineyard_silk"))
    assertTrue(productIds.contains("texcoat"))
    assertTrue(productIds.contains("lylitex"))
    assertTrue(productIds.contains("gold_matt"))
    assertTrue(productIds.contains("super_flex"))
    assertTrue(productIds.contains("weather_proof"))
    assertTrue(productIds.contains("oil_gloss"))
    assertTrue(productIds.contains("auto_paints"))
    assertTrue(productIds.contains("stucco_bonds"))
    assertTrue(productIds.contains("wall_putty"))
    assertTrue(productIds.contains("onyx_paints"))
    assertTrue(productIds.contains("pop_paints"))
    assertTrue(productIds.contains("gravitex"))
    assertTrue(productIds.contains("grape_yard"))

    // Verify Sharon Gloss has 4L drums only
    val gloss = products.first { it.id == "oil_gloss" }
    assertEquals(listOf(PaintSize.SIZE_4L), gloss.availableSizes)
    assertEquals(17390L, gloss.getPrice(PaintSize.SIZE_4L))

    // Verify Vineyard Matt has 20L only with factory rate 47,725
    val vineyardMatt = products.first { it.id == "vineyard_matt" }
    assertEquals(listOf(PaintSize.SIZE_20L), vineyardMatt.availableSizes)
    assertEquals(47725L, vineyardMatt.getPrice(PaintSize.SIZE_20L))

    // Verify Auto paints have 4L cans only
    val auto = products.first { it.id == "auto_paints" }
    assertEquals(listOf(PaintSize.SIZE_4L), auto.availableSizes)

    // Verify Vineyard Silk has 20L, 10L, 4L
    val silk = products.first { it.id == "vineyard_silk" }
    assertTrue(silk.availableSizes.contains(PaintSize.SIZE_10L))
    assertTrue(silk.availableSizes.contains(PaintSize.SIZE_20L))
    assertTrue(silk.availableSizes.contains(PaintSize.SIZE_4L))

    // Verify Emulsion has 20L and 4L
    val emulsion = products.first { it.id == "emulsion" }
    assertTrue(emulsion.availableSizes.contains(PaintSize.SIZE_20L))
    assertTrue(emulsion.availableSizes.contains(PaintSize.SIZE_4L))

    // Verify all products have valid pricing for their available sizes
    products.forEach { prod ->
      assertTrue(prod.availableSizes.isNotEmpty())
      prod.availableSizes.forEach { size ->
        assertTrue("Price for ${prod.name} ($size) must be > 0", prod.getPrice(size) > 0)
      }
      assertFalse(prod.uppercaseTitle.isBlank())
    }
  }

  @Test
  fun testDepotsIntegrity() {
    val depots = repository.depots
    assertEquals(9, depots.size)
    val depotIds = depots.map { it.id }
    assertTrue(depotIds.contains("depot_9th_mile"))
    assertTrue(depotIds.contains("depot_onitsha"))
    assertTrue(depotIds.contains("depot_agbani"))
    assertTrue(depotIds.contains("depot_abakpa"))
    assertTrue(depotIds.contains("depot_ugwuaji"))
    assertTrue(depotIds.contains("depot_sokoto"))
    assertTrue(depotIds.contains("depot_abuja"))
    assertTrue(depotIds.contains("depot_okene"))
    assertTrue(depotIds.contains("depot_owerri"))

    // Verify Abakpa depot details
    val abakpa = depots.first { it.id == "depot_abakpa" }
    assertEquals("Mrs. Grace", abakpa.managerName)
    assertTrue(abakpa.phone.contains("07069135897") || abakpa.phone.contains("7069135897"))

    // Verify 9th mile main factory manager
    val factory = depots.first { it.id == "depot_9th_mile" }
    assertEquals("Mr. Ojuola", factory.managerName)

    // Verify Sokoto manager
    val sokoto = depots.first { it.id == "depot_sokoto" }
    assertEquals("Mr. Philimon", sokoto.managerName)

    // Verify Agbani manager
    val agbani = depots.first { it.id == "depot_agbani" }
    assertEquals("Mrs. Chiagugua", agbani.managerName)

    // Verify Onitsha manager
    val onitsha = depots.first { it.id == "depot_onitsha" }
    assertEquals("Mrs. Uwa", onitsha.managerName)

    // Verify each depot has phone and manager
    depots.forEach { depot ->
      assertFalse(depot.managerName.isBlank())
      assertTrue(depot.cleanPhone.startsWith("+234") || depot.cleanPhone.startsWith("0"))
    }
  }

  @Test
  fun testOperatingHoursLogic() {
    // Tuesday to Saturday, 9am to 5pm (hour 9..16)
    assertTrue("Tuesday 10am should be open", repository.isWithinOperatingHours(Calendar.TUESDAY, 10))
    assertTrue("Saturday 3pm should be open", repository.isWithinOperatingHours(Calendar.SATURDAY, 15))
    assertFalse("Sunday 10am should be closed", repository.isWithinOperatingHours(Calendar.SUNDAY, 10))
    assertFalse("Monday 11am should be closed", repository.isWithinOperatingHours(Calendar.MONDAY, 11))
    assertFalse("Tuesday 8pm should be closed", repository.isWithinOperatingHours(Calendar.TUESDAY, 20))
  }

  @Test
  fun testAdminSecurityAndAccessBankPayments() {
    // Two Designated Admins: Mrs Adaesze Chikaodiri Phillip & Mrs Ani Nnenna
    val admin1 = repository.loginAdmin("SHP-ADA-0816")
    assertNotNull("Admin 1 should login with code SHP-ADA-0816", admin1)
    assertEquals("Mrs. Adaesze Chikaodiri Phillip", admin1?.name)
    assertEquals("+2348167043645", admin1?.phone)

    val admin2 = repository.loginAdmin("SHP-ANI-0810")
    assertNotNull("Admin 2 should login with code SHP-ANI-0810", admin2)
    assertEquals("Mrs. Ani Nnenna", admin2?.name)
    assertEquals("+2348100777371", admin2?.phone)

    val invalid = repository.loginAdmin("WRONG-CODE")
    assertNull("Invalid code should be rejected", invalid)

    // Access bank account verification
    val sampleOrder = repository.orders.value.first()
    assertEquals("Access Bank PLC", sampleOrder.bankName)
    assertEquals("0104392154", sampleOrder.accountNumber)
    assertEquals("Sharon paints and chemical company", sampleOrder.accountName)
  }

  @Test
  fun testStockAuditReceiptGeneration() {
    val receipt = repository.generateAuditReceipt(AuditReportPeriod.MONTHLY, "Mrs. Adaesze Chikaodiri Phillip (SHP-ADA-0816)")
    assertNotNull(receipt)
    assertTrue(receipt.receiptId.startsWith("AUD-MTH-"))
    assertTrue(receipt.inventoryValueNaira > 0)
    assertTrue(receipt.accessBankApprovedTransfers > 0)
  }
}
