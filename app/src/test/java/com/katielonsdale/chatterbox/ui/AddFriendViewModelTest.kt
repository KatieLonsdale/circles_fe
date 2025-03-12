package com.katielonsdale.chatterbox.ui

import com.katielonsdale.chatterbox.SessionManager
import com.katielonsdale.chatterbox.api.ApiClient
import com.katielonsdale.chatterbox.api.ApiService
import com.katielonsdale.chatterbox.api.data.FriendshipRequest
import com.katielonsdale.chatterbox.api.data.FriendshipResponse
import com.katielonsdale.chatterbox.api.data.FriendshipUser
import com.katielonsdale.chatterbox.api.data.FriendshipAttributes
import com.katielonsdale.chatterbox.api.data.FriendshipData
import com.katielonsdale.chatterbox.api.data.UserAttributes
import com.katielonsdale.chatterbox.api.data.UserData
import com.katielonsdale.chatterbox.api.data.UsersResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.reflect.Field
import java.lang.reflect.Method

@ExperimentalCoroutinesApi
class AddFriendViewModelTest {

    private lateinit var viewModel: AddFriendViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val apiService = mock(ApiService::class.java)
    private val TEST_USER_ID = "123"
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        // Mock ApiClient
        ApiClient.apiService = apiService
        
        // Mock the SessionManager
        val sessionManagerClass = SessionManager::class.java
        
        // Use reflection to set isInitialized to true
        val isInitializedField = sessionManagerClass.getDeclaredField("isInitialized")
        isInitializedField.isAccessible = true
        isInitializedField.set(SessionManager, true)
        
        // Mock getUserId to return a test user ID
        val getUserIdMethod = sessionManagerClass.getDeclaredMethod("getUserId")
        val originalMethod = sessionManagerClass.getDeclaredMethod("getUserId")
        
        // Create a new instance of the view model
        viewModel = AddFriendViewModel()
        
        // Use reflection to set a mock implementation for getUserId
        val sharedPreferencesField = sessionManagerClass.getDeclaredField("sharedPreferences")
        sharedPreferencesField.isAccessible = true
        val mockSharedPreferences = mock(android.content.SharedPreferences::class.java)
        `when`(mockSharedPreferences.getString("userId", null)).thenReturn(TEST_USER_ID)
        sharedPreferencesField.set(SessionManager, mockSharedPreferences)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `updateSearchQuery updates searchQuery state`() = runTest {
        // Given
        val query = "test query"
        
        // When
        viewModel.updateSearchQuery(query)
        
        // Then
        assertEquals(query, viewModel.searchQuery.first())
    }

    @Test
    fun `searchUsers with empty query shows error message`() = runTest {
        // Given
        viewModel.updateSearchQuery("")
        
        // When
        viewModel.searchUsers()
        
        // Then
        assertEquals("Please enter a search term", viewModel.errorMessage.first())
        assertFalse(viewModel.hasSearched.first())
    }

    @Test
    fun `searchUsers with valid query calls API and updates results on success`() = runTest {
        // Given
        val query = "test query"
        viewModel.updateSearchQuery(query)
        
        val mockCall = mock(Call::class.java) as Call<UsersResponse>
        `when`(apiService.searchUsers(query)).thenReturn(mockCall)
        
        // Mock the enqueue to immediately call onResponse with success
        Mockito.doAnswer { invocation ->
            val callback = invocation.getArgument(0) as Callback<UsersResponse>
            
            val userData = UserData(
                id = "1",
                type = "user",
                attributes = UserAttributes(
                    id = 1,
                    email = "test@example.com",
                    displayName = "Test User"
                )
            )
            
            val usersResponse = UsersResponse(listOf(userData))
            callback.onResponse(mockCall, Response.success(usersResponse))
            null
        }.`when`(mockCall).enqueue(Mockito.any())
        
        // When
        viewModel.searchUsers()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        assertEquals(1, viewModel.searchResults.first().size)
        assertEquals("Test User", viewModel.searchResults.first()[0].attributes.displayName)
        assertTrue(viewModel.hasSearched.first())
    }

    @Test
    fun `searchUsers handles API error`() = runTest {
        // Given
        val query = "test query"
        viewModel.updateSearchQuery(query)
        
        val mockCall = mock(Call::class.java) as Call<UsersResponse>
        `when`(apiService.searchUsers(query)).thenReturn(mockCall)
        
        // Mock the enqueue to immediately call onResponse with error
        Mockito.doAnswer { invocation ->
            val callback = invocation.getArgument(0) as Callback<UsersResponse>
            // Use the updated ResponseBody creation method
            val responseBody = "".toResponseBody("application/json".toMediaTypeOrNull())
            callback.onResponse(mockCall, Response.error(404, responseBody))
            null
        }.`when`(mockCall).enqueue(Mockito.any())
        
        // When
        viewModel.searchUsers()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        assertTrue(viewModel.errorMessage.first()?.contains("Error searching for users") ?: false)
        assertTrue(viewModel.hasSearched.first())
    }

    @Test
    fun `searchUsers handles network failure`() = runTest {
        // Given
        val query = "test query"
        viewModel.updateSearchQuery(query)
        
        val mockCall = mock(Call::class.java) as Call<UsersResponse>
        `when`(apiService.searchUsers(query)).thenReturn(mockCall)
        
        // Mock the enqueue to immediately call onFailure
        Mockito.doAnswer { invocation ->
            val callback = invocation.getArgument(0) as Callback<UsersResponse>
            callback.onFailure(mockCall, IOException("Network error"))
            null
        }.`when`(mockCall).enqueue(Mockito.any())
        
        // When
        viewModel.searchUsers()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        assertTrue(viewModel.errorMessage.first()?.contains("Network error") ?: false)
        assertTrue(viewModel.hasSearched.first())
    }

    @Test
    fun `hasSearched is initially false`() = runTest {
        // Then
        assertFalse(viewModel.hasSearched.first())
    }

    @Test
    fun `sendFriendRequest with valid data calls API and shows success message`() = runTest {
        // Given
        val userId = 42
        val mockCall = mock(Call::class.java) as Call<FriendshipResponse>
        val friendshipRequest = FriendshipRequest(userId)
        
        `when`(apiService.createFriendship(TEST_USER_ID, friendshipRequest)).thenReturn(mockCall)
        
        // Mock the enqueue to immediately call onResponse with success
        Mockito.doAnswer { invocation ->
            val callback = invocation.getArgument(0) as Callback<FriendshipResponse>
            
            val friendshipUser = FriendshipUser(
                id = 42,
                email = "friend@example.com",
                displayName = "Friend User"
            )
            
            val friendshipAttributes = FriendshipAttributes(
                id = 1,
                status = "pending",
                createdAt = "2023-01-01T00:00:00Z",
                updatedAt = "2023-01-01T00:00:00Z",
                friend = friendshipUser,
                user = friendshipUser
            )
            
            val friendshipData = FriendshipData(
                id = "1",
                type = "friendship",
                attributes = friendshipAttributes
            )
            
            val friendshipResponse = FriendshipResponse(data = friendshipData)
            
            callback.onResponse(mockCall, Response.success(friendshipResponse))
            null
        }.`when`(mockCall).enqueue(Mockito.any())
        
        // When
        viewModel.sendFriendRequest(userId)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        assertEquals("Friend request sent successfully to this user!", viewModel.successMessage.first())
    }

    @Test
    fun `sendFriendRequest handles API error`() = runTest {
        // Given
        val userId = 42
        val mockCall = mock(Call::class.java) as Call<FriendshipResponse>
        val friendshipRequest = FriendshipRequest(userId)
        
        `when`(apiService.createFriendship(TEST_USER_ID, friendshipRequest)).thenReturn(mockCall)
        
        // Mock the enqueue to immediately call onResponse with error
        Mockito.doAnswer { invocation ->
            val callback = invocation.getArgument(0) as Callback<FriendshipResponse>
            val responseBody = "".toResponseBody("application/json".toMediaTypeOrNull())
            callback.onResponse(mockCall, Response.error(404, responseBody))
            null
        }.`when`(mockCall).enqueue(Mockito.any())
        
        // When
        viewModel.sendFriendRequest(userId)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        assertTrue(viewModel.errorMessage.first()?.contains("Error sending friend request") ?: false)
    }

    @Test
    fun `sendFriendRequest handles network failure`() = runTest {
        // Given
        val userId = 42
        val mockCall = mock(Call::class.java) as Call<FriendshipResponse>
        val friendshipRequest = FriendshipRequest(userId)
        
        `when`(apiService.createFriendship(TEST_USER_ID, friendshipRequest)).thenReturn(mockCall)
        
        // Mock the enqueue to immediately call onFailure
        Mockito.doAnswer { invocation ->
            val callback = invocation.getArgument(0) as Callback<FriendshipResponse>
            callback.onFailure(mockCall, IOException("Network error"))
            null
        }.`when`(mockCall).enqueue(Mockito.any())
        
        // When
        viewModel.sendFriendRequest(userId)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        assertTrue(viewModel.errorMessage.first()?.contains("Network error") ?: false)
    }

    @Test
    fun `sendFriendRequest handles friendship already exists error`() = runTest {
        // Given
        val userId = 42
        val displayName = "Test Friend"
        val mockCall = mock(Call::class.java) as Call<FriendshipResponse>
        val friendshipRequest = FriendshipRequest(userId)
        
        // Add the user to search results so we can get the display name
        val userData = UserData(
            id = "42",
            type = "user",
            attributes = UserAttributes(
                id = userId,
                email = "friend@example.com",
                displayName = displayName
            )
        )
        viewModel.updateSearchResults(listOf(userData))
        
        `when`(apiService.createFriendship(TEST_USER_ID, friendshipRequest)).thenReturn(mockCall)
        
        // Mock the enqueue to immediately call onResponse with friendship already exists error
        Mockito.doAnswer { invocation ->
            val callback = invocation.getArgument(0) as Callback<FriendshipResponse>
            val responseBody = "{\"error\":\"friendship already exists\"}".toResponseBody("application/json".toMediaTypeOrNull())
            callback.onResponse(mockCall, Response.error(400, responseBody))
            null
        }.`when`(mockCall).enqueue(Mockito.any())
        
        // When
        viewModel.sendFriendRequest(userId)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        assertEquals("You're already friends with $displayName", viewModel.errorMessage.first())
    }

    @Test
    fun `clearMessages clears error and success messages`() = runTest {
        // Given
        viewModel.updateSearchQuery("")
        viewModel.searchUsers() // This will set an error message
        
        // When
        viewModel.clearMessages()
        
        // Then
        assertEquals(null, viewModel.errorMessage.first())
        assertEquals(null, viewModel.successMessage.first())
    }
} 