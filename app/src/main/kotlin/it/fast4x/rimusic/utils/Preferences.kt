package it.fast4x.rimusic.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SnapshotMutationPolicy
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit

const val lastPlayerThumbnailSizeKey = "lastPlayerThumbnailSize"
const val lastPlayerPlayButtonTypeKey = "lastPlayerPlayButtonType"
const val lastPlayerTimelineTypeKey = "lastPlayerTimelineType"
const val lastPlayerVisualizerTypeKey = "lastPlayerVisualizerType"
const val playerPlayButtonTypeKey = "playerPlayButtonType"
const val playerTimelineTypeKey = "playerTimelineType"
const val playerVisualizerTypeKey = "playerVisualizerType"
const val thumbnailTapEnabledKey = "thumbnailTapEnabled"
const val wavedPlayerTimelineKey = "wavedPlayerTimeline"
const val languageAppKey = "languageApp"
const val indexNavigationTabKey = "indexNavigationTab"
const val effectRotationKey = "effectRotation"
const val playerThumbnailSizeKey = "playerThumbnailSize"
const val colorPaletteNameKey = "colorPaletteName"
const val colorPaletteModeKey = "colorPaletteMode"
const val thumbnailRoundnessKey = "thumbnailRoundness"
const val coilDiskCacheMaxSizeKey = "coilDiskCacheMaxSize"
const val exoPlayerDiskCacheMaxSizeKey = "exoPlayerDiskCacheMaxSize"
const val exoPlayerDiskDownloadCacheMaxSizeKey = "exoPlayerDiskDownloadCacheMaxSize"
const val exoPlayerMinTimeForEventKey = "exoPlayerMinTimeForEvent"
const val exoPlayerAlternateCacheLocationKey = "exoPlayerAlternateCacheLocation"
const val isInvincibilityEnabledKey = "isInvincibilityEnabled"
const val useSystemFontKey = "useSystemFont"
const val applyFontPaddingKey = "applyFontPadding"
const val songSortOrderKey = "songSortOrder"
const val songSortByKey = "songSortBy"
const val onDeviceSongSortByKey = "onDeviceSongSortBy"
const val onDeviceFolderSortByKey = "onDeviceFolderSortBy"
const val playlistSortOrderKey = "playlistSortOrder"
const val playlistSortByKey = "playlistSortBy"
const val albumSortOrderKey = "albumSortOrder"
const val albumSortByKey = "albumSortBy"
const val artistSortOrderKey = "artistSortOrder"
const val artistSortByKey = "artistSortBy"
const val trackLoopEnabledKey = "trackLoopEnabled"
const val queueLoopEnabledKey = "queueLoopEnabled"
const val reorderInQueueEnabledKey = "reorderInQueueEnabled"
const val skipSilenceKey = "skipSilence"
const val volumeNormalizationKey = "volumeNormalization"
const val resumePlaybackWhenDeviceConnectedKey = "resumePlaybackWhenDeviceConnected"
const val persistentQueueKey = "persistentQueue"
const val closebackgroundPlayerKey = "closebackgroundPlayer"
const val closeWithBackButtonKey = "closeWithBackButton"
const val isShowingSynchronizedLyricsKey = "isShowingSynchronizedLyrics"
const val isShowingThumbnailInLockscreenKey = "isShowingThumbnailInLockscreen"
const val homeScreenTabIndexKey = "homeScreenTabIndex"
const val searchResultScreenTabIndexKey = "searchResultScreenTabIndex"
const val artistScreenTabIndexKey = "artistScreenTabIndex"
const val pauseSearchHistoryKey = "pauseSearchHistory"
const val UiTypeKey = "UiType"
const val disablePlayerHorizontalSwipeKey = "disablePlayerHorizontalSwipe"
const val disableIconButtonOnTopKey = "disableIconButtonOnTop"
const val exoPlayerCustomCacheKey = "exoPlayerCustomCache"
const val disableScrollingTextKey = "disableScrollingText"
const val audioQualityFormatKey = "audioQualityFormat"
const val showLikeButtonBackgroundPlayerKey = "showLikeButtonBackgroundPlayer"
const val showDownloadButtonBackgroundPlayerKey = "showDownloadButtonBackgroundPlayer"
const val playEventsTypeKey = "playEventsType"
const val fontTypeKey = "fontType"
const val playlistSongSortByKey = "playlistSongSortBy"
const val showRelatedAlbumsKey = "showRelatedAlbums"
const val showSimilarArtistsKey = "showSimilarArtists"
const val showNewAlbumsArtistsKey = "showNewAlbumsArtists"
const val showNewAlbumsKey = "showNewAlbums"
const val showPlaylistMightLikeKey = "showPlaylistMightLike"
const val maxStatisticsItemsKey = "maxStatisticsItems"
const val showStatsListeningTimeKey = "showStatsListeningTime"
const val isProxyEnabledKey = "isProxyEnabled"
const val proxyHostnameKey = "proxyHostname"
const val proxyPortKey = "proxyPort"
const val proxyModeKey = "ProxyMode"
const val isRecommendationEnabledKey = "isRecommendationEnabled"
const val showButtonPlayerAddToPlaylistKey = "showButtonPlayerAddToPlaylist"
const val showButtonPlayerArrowKey = "showButtonPlayerArrow"
const val showButtonPlayerDownloadKey = "showButtonPlayerDownload"
const val showButtonPlayerLoopKey = "showButtonPlayerLoop"
const val showButtonPlayerLyricsKey = "showButtonPlayerLyrics"
const val showButtonPlayerShuffleKey = "showButtonPlayerShuffle"
const val isKeepScreenOnEnabledKey = "isKeepScreenOnEnabled"
const val isEnabledDiscoveryLangCodeKey = "isEnabledDiscoveryLangCode"
const val recommendationsNumberKey = "recommendationsNumber"
const val checkUpdateStateKey = "checkUpdateState"
const val showButtonPlayerSleepTimerKey = "showButtonPlayerSleepTimer"
const val keepPlayerMinimizedKey = "keepPlayerMinimized"
const val isSwipeToActionEnabledKey = "isSwipeToActionEnabled"
const val showButtonPlayerMenuKey = "showButtonPlayerMenu"
const val showButtonPlayerSystemEqualizerKey = "showButtonPlayerSystemEqualizer"
const val disableClosingPlayerSwipingDownKey = "disableClosingPlayerSwipingDown"
const val showSearchTabKey = "showSearchTab"
const val MaxTopPlaylistItemsKey = "MaxTopPlaylistItems"
const val contentWidthKey = "0.8f"
const val navigationBarPositionKey = "navigationBarPosition"
const val navigationBarTypeKey = "navigationBarType"
const val pauseBetweenSongsKey = "pauseBetweenSongs"
const val showFavoritesPlaylistKey = "showFavoritesPlaylist"
const val showCachedPlaylistKey = "showCachedPlaylist"
const val showMyTopPlaylistKey = "showMyTopPlaylist"
const val showDownloadedPlaylistKey = "showDownloadedPlaylist"
const val showOnDevicePlaylistKey = "showOnDevicePlaylist"
const val showPlaylistsKey = "showPlaylists"
const val isGradientBackgroundEnabledKey = "isGradientBackgroundEnabled"
const val playbackSpeedKey = "playbackSpeed"
const val playbackPitchKey = "playbackPitch"
const val showTotalTimeQueueKey = "showTotalTimeQueue"
const val backgroundProgressKey = "backgroundProgress"
const val maxSongsInQueueKey = "maxSongsInQueue"
const val showFoldersOnDeviceKey = "showFoldersOnDevice"
const val showNextSongsInPlayerKey = "showNextSongsInPlayer"
const val showRemainingSongTimeKey = "showRemainingSongTime"
const val lyricsFontSizeKey = "lyricsFontSize"
const val includeLocalSongsKey = "includeLocalSongs"
const val clickLyricsTextKey = "clickLyricsText"

inline fun <reified T : Enum<T>> SharedPreferences.getEnum(
    key: String,
    defaultValue: T
): T =
    getString(key, null)?.let {
        try {
            enumValueOf<T>(it)
        } catch (e: IllegalArgumentException) {
            null
        }
    } ?: defaultValue

inline fun <reified T : Enum<T>> SharedPreferences.Editor.putEnum(
    key: String,
    value: T
): SharedPreferences.Editor =
    putString(key, value.name)

val Context.preferences: SharedPreferences
    get() = getSharedPreferences("preferences", Context.MODE_PRIVATE)

@Composable
fun rememberPreference(key: String, defaultValue: Boolean): MutableState<Boolean> {
    val context = LocalContext.current
    return remember {
        mutableStatePreferenceOf(context.preferences.getBoolean(key, defaultValue)) {
            context.preferences.edit { putBoolean(key, it) }
        }
    }
}

@Composable
fun rememberPreference(key: String, defaultValue: Int): MutableState<Int> {
    val context = LocalContext.current
    return remember {
        mutableStatePreferenceOf(context.preferences.getInt(key, defaultValue)) {
            context.preferences.edit { putInt(key, it) }
        }
    }
}

@Composable
fun rememberPreference(key: String, defaultValue: Float): MutableState<Float> {
    val context = LocalContext.current
    return remember {
        mutableStatePreferenceOf(context.preferences.getFloat(key, defaultValue)) {
            context.preferences.edit { putFloat(key, it) }
        }
    }
}

@Composable
fun rememberPreference(key: String, defaultValue: String): MutableState<String> {
    val context = LocalContext.current
    return remember {
        mutableStatePreferenceOf(context.preferences.getString(key, null) ?: defaultValue) {
            context.preferences.edit { putString(key, it) }
        }
    }
}

@Composable
inline fun <reified T : Enum<T>> rememberPreference(key: String, defaultValue: T): MutableState<T> {
    val context = LocalContext.current
    return remember {
        mutableStatePreferenceOf(context.preferences.getEnum(key, defaultValue)) {
            context.preferences.edit { putEnum(key, it) }
        }
    }
}

inline fun <T> mutableStatePreferenceOf(
    value: T,
    crossinline onStructuralInequality: (newValue: T) -> Unit
) =
    mutableStateOf(
        value = value,
        policy = object : SnapshotMutationPolicy<T> {
            override fun equivalent(a: T, b: T): Boolean {
                val areEquals = a == b
                if (!areEquals) onStructuralInequality(b)
                return areEquals
            }
        })