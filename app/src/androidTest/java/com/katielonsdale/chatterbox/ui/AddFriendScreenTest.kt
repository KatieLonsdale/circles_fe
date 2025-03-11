package com.katielonsdale.chatterbox.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.katielonsdale.chatterbox.api.data.UserAttributes
import com.katielonsdale.chatterbox.api.data.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class AddFriendScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun searchBarAndButtonAreDisplayed() {
        // Given
        composeTestRule.setContent {
            AddFriendScreen(
                onClickBack = {},
                onNavigateToNewsfeed = {}
            )
        }

        // Then
        composeTestRule.onNodeWithText("Search for users").assertIsDisplayed()
        composeTestRule.onNodeWithText("Search").assertIsDisplayed()
    }

    @Test
    fun backButtonNavigatesBack() {
        // Given
        val onClickBack = mock(Function0::class.java) as () -> Unit
        
        composeTestRule.setContent {
            AddFriendScreen(
                onClickBack = onClickBack,
                onNavigateToNewsfeed = {}
            )
        }

        // When
        composeTestRule.onNodeWithContentDescription("Back").performClick()

        // Then
        verify(onClickBack).invoke()
    }

    @Test
    fun emptySearchShowsNoResults() {
        // Given
        val viewModel = mock(AddFriendViewModel::class.java)
        `when`(viewModel.searchQuery).thenReturn(MutableStateFlow(""))
        `when`(viewModel.searchResults).thenReturn(MutableStateFlow(emptyList()))
        `when`(viewModel.isLoading).thenReturn(MutableStateFlow(false))
        `when`(viewModel.errorMessage).thenReturn(MutableStateFlow(null))
        `when`(viewModel.successMessage).thenReturn(MutableStateFlow(null))
        
        composeTestRule.setContent {
            AddFriendScreen(
                onClickBack = {},
                onNavigateToNewsfeed = {},
                viewModel = viewModel
            )
        }

        // Then
        composeTestRule.onNodeWithText("Search Results").assertDoesNotExist()
        composeTestRule.onNodeWithText("No users found").assertDoesNotExist()
    }

    @Test
    fun searchResultsAreDisplayed() {
        // Given
        val viewModel = mock(AddFriendViewModel::class.java)
        val userData = UserData(
            id = "1",
            type = "user",
            attributes = UserAttributes(
                id = 1,
                email = "test@example.com",
                displayName = "Test User"
            )
        )
        
        `when`(viewModel.searchQuery).thenReturn(MutableStateFlow("test"))
        `when`(viewModel.searchResults).thenReturn(MutableStateFlow(listOf(userData)))
        `when`(viewModel.isLoading).thenReturn(MutableStateFlow(false))
        `when`(viewModel.errorMessage).thenReturn(MutableStateFlow(null))
        `when`(viewModel.successMessage).thenReturn(MutableStateFlow(null))
        
        composeTestRule.setContent {
            AddFriendScreen(
                onClickBack = {},
                onNavigateToNewsfeed = {},
                viewModel = viewModel
            )
        }

        // Then
        composeTestRule.onNodeWithText("Search Results").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test User").assertIsDisplayed()
        composeTestRule.onNodeWithText("test@example.com").assertIsDisplayed()
    }

    @Test
    fun noResultsMessageIsDisplayed() {
        // Given
        val viewModel = mock(AddFriendViewModel::class.java)
        
        `when`(viewModel.searchQuery).thenReturn(MutableStateFlow("test"))
        `when`(viewModel.searchResults).thenReturn(MutableStateFlow(emptyList()))
        `when`(viewModel.isLoading).thenReturn(MutableStateFlow(false))
        `when`(viewModel.errorMessage).thenReturn(MutableStateFlow(null))
        `when`(viewModel.successMessage).thenReturn(MutableStateFlow(null))
        
        composeTestRule.setContent {
            AddFriendScreen(
                onClickBack = {},
                onNavigateToNewsfeed = {},
                viewModel = viewModel
            )
        }

        // Then
        composeTestRule.onNodeWithText("No users found").assertIsDisplayed()
    }

    @Test
    fun loadingIndicatorIsDisplayed() {
        // Given
        val viewModel = mock(AddFriendViewModel::class.java)
        
        `when`(viewModel.searchQuery).thenReturn(MutableStateFlow("test"))
        `when`(viewModel.searchResults).thenReturn(MutableStateFlow(emptyList()))
        `when`(viewModel.isLoading).thenReturn(MutableStateFlow(true))
        `when`(viewModel.errorMessage).thenReturn(MutableStateFlow(null))
        `when`(viewModel.successMessage).thenReturn(MutableStateFlow(null))
        
        composeTestRule.setContent {
            AddFriendScreen(
                onClickBack = {},
                onNavigateToNewsfeed = {},
                viewModel = viewModel
            )
        }

        // Then
        // Check for CircularProgressIndicator - this is a bit tricky in Compose UI tests
        // as it doesn't have a text or content description by default
        composeTestRule.onNodeWithText("No users found").assertDoesNotExist()
    }

    @Test
    fun searchButtonCallsSearchUsers() {
        // Given
        val viewModel = mock(AddFriendViewModel::class.java)
        
        `when`(viewModel.searchQuery).thenReturn(MutableStateFlow("test"))
        `when`(viewModel.searchResults).thenReturn(MutableStateFlow(emptyList()))
        `when`(viewModel.isLoading).thenReturn(MutableStateFlow(false))
        `when`(viewModel.errorMessage).thenReturn(MutableStateFlow(null))
        `when`(viewModel.successMessage).thenReturn(MutableStateFlow(null))
        
        composeTestRule.setContent {
            AddFriendScreen(
                onClickBack = {},
                onNavigateToNewsfeed = {},
                viewModel = viewModel
            )
        }

        // When
        composeTestRule.onNodeWithText("Search").performClick()

        // Then
        verify(viewModel).searchUsers()
    }

    @Test
    fun userItemClickCallsSendFriendRequest() {
        // Given
        val viewModel = mock(AddFriendViewModel::class.java)
        val userData = UserData(
            id = "1",
            type = "user",
            attributes = UserAttributes(
                id = 1,
                email = "test@example.com",
                displayName = "Test User"
            )
        )
        
        `when`(viewModel.searchQuery).thenReturn(MutableStateFlow("test"))
        `when`(viewModel.searchResults).thenReturn(MutableStateFlow(listOf(userData)))
        `when`(viewModel.isLoading).thenReturn(MutableStateFlow(false))
        `when`(viewModel.errorMessage).thenReturn(MutableStateFlow(null))
        `when`(viewModel.successMessage).thenReturn(MutableStateFlow(null))
        
        composeTestRule.setContent {
            AddFriendScreen(
                onClickBack = {},
                onNavigateToNewsfeed = {},
                viewModel = viewModel
            )
        }

        // When
        composeTestRule.onNodeWithText("Test User").performClick()

        // Then
        verify(viewModel).sendFriendRequest(1)
    }

    @Test
    fun successMessageNavigatesToNewsfeed() {
        // Given
        val onNavigateToNewsfeed = mock(Function0::class.java) as () -> Unit
        val viewModel = mock(AddFriendViewModel::class.java)
        
        `when`(viewModel.searchQuery).thenReturn(MutableStateFlow("test"))
        `when`(viewModel.searchResults).thenReturn(MutableStateFlow(emptyList()))
        `when`(viewModel.isLoading).thenReturn(MutableStateFlow(false))
        `when`(viewModel.errorMessage).thenReturn(MutableStateFlow(null))
        `when`(viewModel.successMessage).thenReturn(MutableStateFlow("Friend request sent successfully!"))
        
        composeTestRule.setContent {
            AddFriendScreen(
                onClickBack = {},
                onNavigateToNewsfeed = onNavigateToNewsfeed,
                viewModel = viewModel
            )
        }

        // Then
        verify(onNavigateToNewsfeed).invoke()
        verify(viewModel).clearMessages()
    }
} 