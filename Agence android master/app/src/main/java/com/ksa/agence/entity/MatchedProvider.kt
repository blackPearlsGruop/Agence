package com.ksa.agence.entity

// Local mock model for the AI Matching flow (Steps 1→3 in AiMatchingFragment).
// Mirrors the reference design's candidate pool exactly (name/price/rating/
// delivery/score). Swap for a real API response once the matching endpoint
// exists on staging — same automatic-fallback pattern used elsewhere in Home.
data class MatchedProvider(
    val id: Int,
    val name: String,
    val price: String,
    val rating: Float,
    val delivery: String,
    val score: Int,
    val photoUrl: String
)
