package it.fast4x.rimusic.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import it.fast4x.compose.persist.persist
import it.fast4x.rimusic.Database
import it.fast4x.rimusic.LocalPlayerServiceBinder
import it.fast4x.rimusic.MODIFIED_PREFIX
import it.fast4x.rimusic.R
import it.fast4x.rimusic.enums.AlbumSortBy
import it.fast4x.rimusic.enums.LibraryItemSize
import it.fast4x.rimusic.enums.NavigationBarPosition
import it.fast4x.rimusic.enums.SortOrder
import it.fast4x.rimusic.enums.ThumbnailRoundness
import it.fast4x.rimusic.enums.UiType
import it.fast4x.rimusic.models.Album
import it.fast4x.rimusic.models.Song
import it.fast4x.rimusic.models.SongPlaylistMap
import it.fast4x.rimusic.query
import it.fast4x.rimusic.transaction
import it.fast4x.rimusic.ui.components.LocalMenuState
import it.fast4x.rimusic.ui.components.themed.AlbumsItemMenu
import it.fast4x.rimusic.ui.components.themed.FloatingActionsContainerWithScrollToTop
import it.fast4x.rimusic.ui.components.themed.HeaderInfo
import it.fast4x.rimusic.ui.components.themed.IconButton
import it.fast4x.rimusic.ui.components.themed.InputTextDialog
import it.fast4x.rimusic.ui.components.themed.Menu
import it.fast4x.rimusic.ui.components.themed.MenuEntry
import it.fast4x.rimusic.ui.components.themed.MultiFloatingActionsContainer
import it.fast4x.rimusic.ui.components.themed.SmartMessage
import it.fast4x.rimusic.ui.components.themed.SortMenu
import it.fast4x.rimusic.ui.items.AlbumItem
import it.fast4x.rimusic.ui.styling.Dimensions
import it.fast4x.rimusic.ui.styling.favoritesIcon
import it.fast4x.rimusic.ui.styling.px
import it.fast4x.rimusic.utils.PlayShuffledSongs
import it.fast4x.rimusic.utils.addNext
import it.fast4x.rimusic.utils.albumSortByKey
import it.fast4x.rimusic.utils.albumSortOrderKey
import it.fast4x.rimusic.utils.albumsItemSizeKey
import it.fast4x.rimusic.utils.asMediaItem
import it.fast4x.rimusic.utils.enqueue
import it.fast4x.rimusic.utils.navigationBarPositionKey
import it.fast4x.rimusic.utils.rememberPreference
import it.fast4x.rimusic.utils.secondary
import it.fast4x.rimusic.utils.semiBold
import it.fast4x.rimusic.utils.showFloatingIconKey
import it.fast4x.rimusic.utils.showSearchTabKey
import it.fast4x.rimusic.utils.thumbnailRoundnessKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.knighthat.colorPalette
import me.knighthat.component.header.TabToolBar
import me.knighthat.component.tab.TabHeader
import me.knighthat.thumbnailShape
import me.knighthat.typography
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalTextApi
@UnstableApi
@SuppressLint("SuspiciousIndentation")
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun HomeAlbumsModern(
    onAlbumClick: (Album) -> Unit,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val menuState = LocalMenuState.current
    val binder = LocalPlayerServiceBinder.current
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var sortBy by rememberPreference(albumSortByKey, AlbumSortBy.DateAdded)
    var sortOrder by rememberPreference(albumSortOrderKey, SortOrder.Descending)

    var items by persist<List<Album>>(tag = "home/albums", emptyList())

    var searching by rememberSaveable { mutableStateOf(false) }
    var isSearchInputFocused by rememberSaveable { mutableStateOf( false ) }
    var filter by rememberSaveable { mutableStateOf( "" ) }

    LaunchedEffect(sortBy, sortOrder, filter) {
        Database.albums(sortBy, sortOrder).collect { items = it }
    }

    if ( filter.isNotBlank() )
        items = items.filter {
            it.title?.contains( filter, true) ?: false
                    || it.year?.contains( filter, true) ?: false
                    || it.authorsText?.contains( filter, true) ?: false
        }

    var itemSize by rememberPreference(albumsItemSizeKey, LibraryItemSize.Small.size)
    val thumbnailSizeDp = itemSize.dp + 24.dp
    val thumbnailSizePx = thumbnailSizeDp.px

    val sortOrderIconRotation by animateFloatAsState(
        targetValue = if (sortOrder == SortOrder.Ascending) 0f else 180f,
        animationSpec = tween(durationMillis = 400, easing = LinearEasing), label = ""
    )

    val navigationBarPosition by rememberPreference(
        navigationBarPositionKey,
        NavigationBarPosition.Bottom
    )
    /*
        var showSortTypeSelectDialog by remember {
            mutableStateOf(false)
        }

     */

    val lazyListState = rememberLazyListState()

    val showSearchTab by rememberPreference(showSearchTabKey, false)
    //val effectRotationEnabled by rememberPreference(effectRotationKey, true)
    var isRotated by rememberSaveable { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (isRotated) 360F else 0f,
        animationSpec = tween(durationMillis = 300), label = ""
    )

    val lazyGridState = rememberLazyGridState()

    val thumbnailRoundness by rememberPreference(
        thumbnailRoundnessKey,
        ThumbnailRoundness.Heavy
    )

    Box(
        modifier = Modifier
            .background(colorPalette().background0)
            //.fillMaxSize()
            .fillMaxHeight()
            .fillMaxWidth(
                if (navigationBarPosition == NavigationBarPosition.Left ||
                    navigationBarPosition == NavigationBarPosition.Top ||
                    navigationBarPosition == NavigationBarPosition.Bottom
                ) 1f
                else Dimensions.contentWidthRightBar
            )
    ) {
        Column( Modifier.fillMaxSize() ) {
            // Sticky tab's title
            TabHeader( R.string.albums ) {
                HeaderInfo( items.size.toString(), R.drawable.album )
            }

            // Sticky tab's tool bar
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(vertical = 4.dp)
                    .fillMaxWidth()
            ) {
                TabToolBar.Icon(
                    iconId = R.drawable.arrow_up,
                    onShortClick = { sortOrder = !sortOrder },
                    onLongClick = {
                        menuState.display {
                            SortMenu(
                                title = stringResource(R.string.sorting_order),
                                onDismiss = menuState::hide,
                                onTitle = { sortBy = AlbumSortBy.Title },
                                onYear = { sortBy = AlbumSortBy.Year },
                                onDateAdded = { sortBy = AlbumSortBy.DateAdded },
                                onArtist = { sortBy = AlbumSortBy.Artist },
                                onSongNumber = { sortBy = AlbumSortBy.Songs },
                                onDuration = { sortBy = AlbumSortBy.Duration }
                            )
                        }
                    },
                    modifier = Modifier.graphicsLayer { rotationZ = sortOrderIconRotation }
                )

                TabToolBar.Icon( R.drawable.search_circle ) {
                    searching = !searching
                    isSearchInputFocused = searching
                }

                TabToolBar.Icon(
                    iconId = R.drawable.dice,
                    enabled = items.isNotEmpty(),
                    modifier = Modifier.rotate( rotationAngle )
                ) {
                    isRotated = !isRotated

                    val randIndex = Random( System.currentTimeMillis() ).nextInt( items.size )
                    onAlbumClick( items[randIndex] )
                }

                TabToolBar.Icon(
                    iconId = R.drawable.shuffle,
                    onShortClick = {
                        coroutineScope.launch {
                            withContext(Dispatchers.IO) {
                                Database.songsInAllBookmarkedAlbums()
                                    .collect { PlayShuffledSongs(songsList = it, binder = binder, context = context) }
                            }
                        }

                    },
                    onLongClick = {
                        SmartMessage(
                            context.resources.getString(R.string.shuffle),
                            context = context
                        )
                    }
                )

                TabToolBar.Icon( R.drawable.resize ) {
                    menuState.display {
                        Menu {
                            MenuEntry(
                                icon = R.drawable.arrow_forward,
                                text = stringResource(R.string.small),
                                onClick = {
                                    itemSize = LibraryItemSize.Small.size
                                    menuState.hide()
                                }
                            )
                            MenuEntry(
                                icon = R.drawable.arrow_forward,
                                text = stringResource(R.string.medium),
                                onClick = {
                                    itemSize = LibraryItemSize.Medium.size
                                    menuState.hide()
                                }
                            )
                            MenuEntry(
                                icon = R.drawable.arrow_forward,
                                text = stringResource(R.string.big),
                                onClick = {
                                    itemSize = LibraryItemSize.Big.size
                                    menuState.hide()
                                }
                            )
                        }
                    }
                }
            }

            // Sticky search bar
            AnimatedVisibility(
                visible = searching,
                modifier = Modifier.padding( all = 10.dp )
                                  .fillMaxWidth()
            ) {
                val focusRequester = remember { FocusRequester() }
                val focusManager = LocalFocusManager.current
                val keyboardController = LocalSoftwareKeyboardController.current

                LaunchedEffect(searching, isSearchInputFocused) {
                    if( !searching ) return@LaunchedEffect

                    if( isSearchInputFocused )
                        focusRequester.requestFocus()
                    else {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                }

                var searchInput by remember { mutableStateOf(TextFieldValue(filter)) }
                BasicTextField(
                    value = searchInput,
                    onValueChange = {
                        searchInput = it.copy(
                            selection = TextRange( it.text.length )
                        )
                        filter = it.text
                    },
                    textStyle = typography().xs.semiBold,
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        searching = filter.isNotBlank()
                        isSearchInputFocused = false
                    }),
                    cursorBrush = SolidColor(colorPalette().text),
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.CenterStart,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 10.dp)
                        ) {
                            IconButton(
                                onClick = {},
                                icon = R.drawable.search,
                                color = colorPalette().favoritesIcon,
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .size(16.dp)
                            )
                        }
                        Box(
                            contentAlignment = Alignment.CenterStart,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 30.dp)
                        ) {
                            androidx.compose.animation.AnimatedVisibility(
                                visible = filter.isBlank(),
                                enter = fadeIn(tween(100)),
                                exit = fadeOut(tween(100)),
                            ) {
                                BasicText(
                                    text = stringResource(R.string.search),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = typography().xs.semiBold.secondary.copy(color = colorPalette().textDisabled)
                                )
                            }

                            innerTextField()
                        }
                    },
                    modifier = Modifier
                        .height(30.dp)
                        .fillMaxWidth()
                        .background(
                            colorPalette().background4,
                            shape = thumbnailRoundness.shape()
                        )
                        .focusRequester(focusRequester)
                )
            }

            LazyVerticalGrid(
                state = lazyGridState,
                columns = GridCells.Adaptive(itemSize.dp + 24.dp),
                //contentPadding = LocalPlayerAwareWindowInsets.current.asPaddingValues(),
                modifier = Modifier
                    .background(colorPalette().background0)
                    .fillMaxSize()
            ) {
                items(
                    items = items,
                    key = Album::id
                ) { album ->
                    var songs = remember { listOf<Song>() }
                    query {
                        songs = Database.albumSongsList(album.id)
                    }

                    var showDialogChangeAlbumTitle by remember {
                        mutableStateOf(false)
                    }
                    var showDialogChangeAlbumAuthors by remember {
                        mutableStateOf(false)
                    }
                    var showDialogChangeAlbumCover by remember {
                        mutableStateOf(false)
                    }

                    if (showDialogChangeAlbumTitle)
                        InputTextDialog(
                            onDismiss = { showDialogChangeAlbumTitle = false },
                            title = stringResource(R.string.update_title),
                            value = album.title.toString(),
                            placeholder = stringResource(R.string.title),
                            setValue = {
                                if (it.isNotEmpty()) {
                                    query {
                                        Database.updateAlbumTitle(album.id, it)
                                    }
                                    //context.toast("Album Saved $it")
                                }
                            },
                            prefix = MODIFIED_PREFIX
                        )
                    if (showDialogChangeAlbumAuthors)
                        InputTextDialog(
                            onDismiss = { showDialogChangeAlbumAuthors = false },
                            title = stringResource(R.string.update_authors),
                            value = album?.authorsText.toString(),
                            placeholder = stringResource(R.string.authors),
                            setValue = {
                                if (it.isNotEmpty()) {
                                    query {
                                        Database.updateAlbumAuthors(album.id, it)
                                    }
                                    //context.toast("Album Saved $it")
                                }
                            },
                            prefix = MODIFIED_PREFIX
                        )

                    if (showDialogChangeAlbumCover)
                        InputTextDialog(
                            onDismiss = { showDialogChangeAlbumCover = false },
                            title = stringResource(R.string.update_cover),
                            value = album?.thumbnailUrl.toString(),
                            placeholder = stringResource(R.string.cover),
                            setValue = {
                                if (it.isNotEmpty()) {
                                    query {
                                        Database.updateAlbumCover(album.id, it)
                                    }
                                    //context.toast("Album Saved $it")
                                }
                            },
                            prefix = MODIFIED_PREFIX
                        )

                    var position by remember {
                        mutableIntStateOf(0)
                    }
                    val context = LocalContext.current

                    AlbumItem(
                        alternative = true,
                        showAuthors = true,
                        album = album,
                        thumbnailSizePx = thumbnailSizePx,
                        thumbnailSizeDp = thumbnailSizeDp,
                        modifier = Modifier
                            .combinedClickable(

                                onLongClick = {
                                    menuState.display {
                                        AlbumsItemMenu(
                                            onDismiss = menuState::hide,
                                            album = album,
                                            onChangeAlbumTitle = {
                                                showDialogChangeAlbumTitle = true
                                            },
                                            onChangeAlbumAuthors = {
                                                showDialogChangeAlbumAuthors = true
                                            },
                                            onChangeAlbumCover = {
                                                showDialogChangeAlbumCover = true
                                            },
                                            onPlayNext = {
                                                println("mediaItem ${songs}")
                                                binder?.player?.addNext(
                                                    songs.map(Song::asMediaItem), context
                                                )

                                            },
                                            onEnqueue = {
                                                println("mediaItem ${songs}")
                                                binder?.player?.enqueue(
                                                    songs.map(Song::asMediaItem), context
                                                )

                                            },
                                            onAddToPlaylist = { playlistPreview ->
                                                position =
                                                    playlistPreview.songCount.minus(1) ?: 0
                                                //Log.d("mediaItem", " maxPos in Playlist $it ${position}")
                                                if (position > 0) position++ else position =
                                                    0
                                                //Log.d("mediaItem", "next initial pos ${position}")
                                                //if (listMediaItems.isEmpty()) {
                                                songs.forEachIndexed { index, song ->
                                                    transaction {
                                                        Database.insert(song.asMediaItem)
                                                        Database.insert(
                                                            SongPlaylistMap(
                                                                songId = song.asMediaItem.mediaId,
                                                                playlistId = playlistPreview.playlist.id,
                                                                position = position + index
                                                            )
                                                        )
                                                    }
                                                    //Log.d("mediaItemPos", "added position ${position + index}")
                                                }
                                                //}
                                            }
                                        )
                                    }
                                },
                                onClick = {
                                    onAlbumClick(album)
                                }
                            )
                            .clip(thumbnailShape())
                    )
                }

                item(
                    key = "footer",
                    contentType = 0,
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    Spacer(modifier = Modifier.height(Dimensions.bottomSpacer))
                }
            }
        }

        FloatingActionsContainerWithScrollToTop(lazyListState = lazyListState)

        val showFloatingIcon by rememberPreference(showFloatingIconKey, false)
        if ( UiType.ViMusic.isCurrent() && showFloatingIcon )
            MultiFloatingActionsContainer(
                iconId = R.drawable.search,
                onClick = onSearchClick,
                onClickSettings = onSettingsClick,
                onClickSearch = onSearchClick
            )
    }
}
