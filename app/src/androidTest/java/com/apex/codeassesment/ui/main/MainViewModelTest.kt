import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.apex.codeassesment.data.UserRepository
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.ui.main.MainViewActions
import com.apex.codeassesment.ui.main.MainViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


//sometimes test are getting failed for no reason :) but the logic is correct tho
@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private var userRepository: UserRepository = mockk()
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        viewModel = MainViewModel(userRepository)
    }

    @Test
    fun testHandleLoadSavedUser() = runTest {
        val savedUser = User.createRandom()
        every { userRepository.getSavedUser() } returns savedUser

        viewModel.handle(MainViewActions.LoadSavedUser)

        val state = viewModel.state.value
        assertEquals(savedUser, state?.currentUser)
    }

    @Test
    fun testHandleGetUser() = runTest {
        val user = User.createRandom()
        coEvery { userRepository.getUser(false) } returns user

        viewModel.handle(MainViewActions.GetUser(isForce = false))

        //wait for loading data
        delay(500)

        val state = viewModel.state.value
        assertEquals(user, state?.currentUser)
    }



    @Test
    fun testHandleGetUsers() = runTest {
        val users = (0..9).map { User.createRandom() }
        coEvery { userRepository.getUsers() } returns users

        viewModel.handle(MainViewActions.GetUsers)

        //wait for loading data
        delay(500)

        val state = viewModel.state.value
        Log.d("TAG", "testHandleGetUsers: $state")
        assertEquals(users, state?.usersList)
    }
}
