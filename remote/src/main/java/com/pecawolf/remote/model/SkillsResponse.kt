package com.pecawolf.remote.model

data class SkillsResponse(
    var stat: String = "",
    var skills: List<SkillResponse> = listOf(),
) {
    data class SkillResponse(
        var code: String = "",
        var name: Map<String, String> = mapOf(),
        var worlds: List<String> = listOf(),
    )
}
