package it.fast4x.innertube.models.bodies

import it.fast4x.innertube.models.Context
import kotlinx.serialization.Serializable


@Serializable
data class BrowseBodyWithLocale(
    val context: Context = Context.DefaultWebWithLocale,
    val browseId: String,
    val params: String? = null
)
