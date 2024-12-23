package it.fast4x.rimusic

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext
import it.fast4x.rimusic.ui.styling.LocalAppearance
import it.fast4x.rimusic.utils.rememberPreference
import it.fast4x.rimusic.utils.showSearchTabKey
import it.fast4x.rimusic.utils.showStatsInNavbarKey

@Composable
fun typography() = LocalAppearance.current.typography

@Composable
@ReadOnlyComposable
fun colorPalette() = LocalAppearance.current.colorPalette

@Composable
fun thumbnailShape() = LocalAppearance.current.thumbnailShape

@Composable
fun showSearchIconInNav() = rememberPreference( showSearchTabKey, false ).value

@Composable
fun showStatsIconInNav() = rememberPreference( showStatsInNavbarKey, false ).value

@Composable
fun binder() = LocalPlayerServiceBinder.current?.service

fun appContext(): Context = Dependencies.application.applicationContext
fun context(): Context = Dependencies.application