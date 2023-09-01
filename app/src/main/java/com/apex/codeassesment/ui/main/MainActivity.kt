package com.apex.codeassesment.ui.main

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.apex.codeassesment.R
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.databinding.ActivityMainBinding
import com.apex.codeassesment.di.MainComponent
import com.apex.codeassesment.ui.main.MainViewEvents.*
import com.apex.codeassesment.utils.navigateDetails
import com.bumptech.glide.RequestManager
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO (5 points): Move calls to repository to Presenter or ViewModel.
// TODO (5 points): Use combination of sealed/Dataclasses for exposing the data required by the view from viewModel .
// TODO (3 points): Add tests for viewmodel or presenter.
// TODO (1 point): Add content description to images
// TODO (3 points): Add tests
// TODO (Optional Bonus 10 points): Make a copy of this activity with different name and convert the current layout it is using in
//  Jetpack Compose.
class MainActivity : AppCompatActivity() {


    // TODO (2 points): Convert to view binding
    private lateinit var views: ActivityMainBinding
    @Inject
    lateinit var viewModel: MainViewModel
    @Inject
    lateinit var glide: RequestManager
    private var arrayAdapter :ArrayAdapter<User>?=null



    private var randomUser: User = User()
        set(value) {
            // TODO (1 point): Use Glide to load images after getting the data from endpoints mentioned in RemoteDataSource
            // userImageView.setImageBitmap(refreshedUser.image)
            views.apply {
                glide.load(value.picture?.large).into(mainImage)
                mainName.text = value.name!!.first
                mainEmail.text = value.email
            }
            field = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        views = ActivityMainBinding.inflate(layoutInflater)
        setContentView(views.root)
        (applicationContext as MainComponent.Injector).mainComponent.inject(this)
        subscribeObservers()
        init()
    }

    private fun init() {
        viewModel.handle(MainViewActions.LoadSavedUser)
        setUpRecyclerView()
        views.apply {
            mainSeeDetailsButton.setOnClickListener {
                navigateDetails(randomUser)
            }
            mainRefreshButton.setOnClickListener {
                viewModel.handle(MainViewActions.GetUser(true))
            }
        }

    }

    private fun subscribeObservers() {
        viewModel.state.observe(this@MainActivity) { state ->
            state?.currentUser?.let {
                randomUser = it
            }
            state?.usersList?.let {
                arrayAdapter?.clear()
                arrayAdapter?.addAll(it)
            }
        }

        lifecycleScope.launch {
            viewModel.viewEvents.collect{ event ->
                handleEachEvent(event)
            }
        }
    }

    private fun handleEachEvent(mainViewEvents: MainViewEvents){
        when(mainViewEvents){
            SavedUserLoaded -> {
                Toast.makeText(this@MainActivity,
                    getString(R.string.finished_loading_saved_user), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setUpRecyclerView() {
        views.apply {
            arrayAdapter = ArrayAdapter<User>(this@MainActivity, android.R.layout.simple_list_item_1)
            mainUserList.adapter = arrayAdapter
            mainUserList.setOnItemClickListener { parent, _, position, _ ->
                navigateDetails(
                    parent.getItemAtPosition(
                        position
                    ) as User
                )
            }

            mainUserListButton.setOnClickListener {
                viewModel.handle(MainViewActions.GetUsers)
            }
        }
    }

    // TODO (2 points): Convert to extenstion function.
//    private fun navigateDetails(user: User) {
//        val putExtra = Intent(this, DetailsActivityKt::class.java).putExtra("saved-user-key", user)
//        startActivity(putExtra)
//    }

    //big mistake to store context as companion object,use application context instead
//    companion object {
//        var sharedContext: Context? = null
//    }
}
