package me.knighthat.component.tab.toolbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import it.fast4x.rimusic.LocalPlayerServiceBinder
import it.fast4x.rimusic.R
import it.fast4x.rimusic.enums.BuiltInPlaylist
import it.fast4x.rimusic.service.modern.PlayerServiceModern
import it.fast4x.rimusic.utils.builtInPlaylistKey
import it.fast4x.rimusic.utils.enqueue
import it.fast4x.rimusic.utils.rememberPreference
import me.knighthat.appContext

@UnstableApi
class EnqueueComponent private constructor(
    private val styleState: MutableState<BuiltInPlaylist>,
    private val items: () -> List<MediaItem>,
    private val binder: PlayerServiceModern.Binder?
): MenuIcon {

    companion object {
        @JvmStatic
        @Composable
        fun init(items: () -> List<MediaItem>) =
            EnqueueComponent(
                rememberPreference(builtInPlaylistKey, BuiltInPlaylist.Favorites),
                items,
                LocalPlayerServiceBinder.current
            )
    }

    var style: BuiltInPlaylist = styleState.value
        set(value) {
            styleState.value = value
            field = value
        }
    override val title: String
        @Composable
        get() = stringResource(R.string.enqueue)
    override val iconId: Int = R.drawable.enqueue

    override fun onShortClick() {
        binder?.player?.enqueue( items(), appContext() )
    }
}