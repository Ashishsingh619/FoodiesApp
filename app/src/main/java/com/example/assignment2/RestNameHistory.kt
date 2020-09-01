package com.example.assignment2

import org.json.JSONArray

data class RestNameHistory(
    var RestName: String,
    var totalCost: String,
    var orderDateTime: String,
    var ResItems: JSONArray
)