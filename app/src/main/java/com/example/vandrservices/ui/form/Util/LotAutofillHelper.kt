package com.example.vandrservices.ui.form.Util

data class LotAutofillResult(
    val exporter: String? = null,
    val inspectionPlace: String? = null,
    val label: String? = null,
    val grower: String? = null
)

private val autofillRules: Map<Pair<String, Int>, LotAutofillResult> = mapOf(
    // Ejemplos:
    "Banana" to 133 to LotAutofillResult(
        exporter = "Mamita Bananas",
        inspectionPlace = "Lineage",
        label = "Mamita Bananas"
    ),
    "Mangoes" to 199 to LotAutofillResult(
        exporter = "Dayka Hackett",
        inspectionPlace = "Fruturam",
        label = "Paisano",
        grower = "Grupo Paisano"
    ),
    "Berrie" to 199 to LotAutofillResult(
        exporter = "Gomez Berries",
        inspectionPlace = "Hi Line",
        label = "Ground Fresh Org"
    ),
    "Tomato" to 232 to LotAutofillResult(
        exporter = "Bova Fresh",
        inspectionPlace = "Frutura",
        label = "Acapulco",
        grower = "Frutas Acapulco"
    ),
    "Berrie" to 4 to LotAutofillResult(
        exporter = "Gomez Berries",
        inspectionPlace = "Hi Line",
        label = "Ground Fresh Org",
        grower = "Gomez Berries"
    ),
    "Raspberrie" to 4 to LotAutofillResult(
        exporter = "Healthy Harvest",
        inspectionPlace = "Hi Line",
        label = "All Season Org",
        grower = "Healthy Harvest"
    ),
    "Raspberrie" to 4 to LotAutofillResult(
        exporter = "Ganfer Fresh",
        inspectionPlace = "Hi Line",
        label = "Edible Harvest Org",
        grower = "Healthy Harvest"
    ),
    "Banana" to 3 to LotAutofillResult(
        exporter = "Coliman",
        inspectionPlace = "Bonafruit",
        label = "Whole Foods"
    ),
    "Aloe Vera" to 2 to LotAutofillResult(
        inspectionPlace = "BST"
    ),
    "Peppers" to 2 to LotAutofillResult(
        inspectionPlace = "BST"
    ),
    "Coco" to 2 to LotAutofillResult(
        inspectionPlace = "BST",
        label = "Generic"
    ),
    "Coco" to 265 to LotAutofillResult(
        exporter = "Tierra Bonita",
        inspectionPlace = "Produce Nation",
        label = "Generic"
    ),
    "Banana" to 70 to LotAutofillResult(
        exporter = "Chanitos",
        inspectionPlace = "LeBest Banana",
        label = "Marketside"
    ),
    "Raspberries" to 34 to LotAutofillResult(
        exporter = "Ganfer Fresh",
        inspectionPlace = "Travelers",
        label = "TGC"
    ),
    "Tomato" to 34 to LotAutofillResult(
        exporter = "Ganfer Fresh",
        label = "Generic"
    )
)

/**
 * Retorna valores autofill para los campos exporter, inspectionPlace y label según variety y companyId
 */
fun getLotAutofill(variety: String, companyId: Int?): LotAutofillResult {
    if (companyId == null) return LotAutofillResult()
    return autofillRules[variety to companyId] ?: LotAutofillResult()
}