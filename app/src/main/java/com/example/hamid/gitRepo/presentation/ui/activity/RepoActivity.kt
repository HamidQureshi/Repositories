package com.example.hamid.gitRepo.presentation.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hamid.gitRepo.R
import com.example.hamid.gitRepo.presentation.factory.ViewModelFactory
import com.example.hamid.gitRepo.presentation.ui.adaptar.RepoListAdapter
import com.example.hamid.gitRepo.presentation.ui.viewmodel.RepoViewModel
import com.hamid.domain.model.model.Status
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_repository.*
import javax.inject.Inject


class RepoActivity : AppCompatActivity() {

    lateinit var viewModel: RepoViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var itemListAdapter: RepoListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_repository)

        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(RepoViewModel::class.java)

        itemListAdapter =
            RepoListAdapter()

        val mLayoutManager = LinearLayoutManager(this)
        rv_list.layoutManager = mLayoutManager
        rv_list.itemAnimator = DefaultItemAnimator()
        rv_list.adapter = itemListAdapter

        viewModel.getData()

        viewModel.formattedList.observe(this, Observer { repositories ->

            if (repositories.status == Status.SUCCESS) {
                progress_bar.visibility = View.GONE
                itemListAdapter.setAdapterList(repositories!!.data)
            } else {
                progress_bar.visibility = View.VISIBLE
            }

        })

    }


    override fun onDestroy() {
        super.onDestroy()
        viewModel.compositeDisposable.clear()
    }
}
