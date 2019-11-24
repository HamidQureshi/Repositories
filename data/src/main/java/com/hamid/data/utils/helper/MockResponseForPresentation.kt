package com.hamid.data.utils.helper

import com.hamid.domain.model.model.RepoPresentationModel
import com.hamid.domain.model.model.Response
import com.hamid.domain.model.model.Status

class MockResponseForPresentation {
    companion object {

        private val presentationRepo1 = RepoPresentationModel(
            274562,
            "yajl-objc",
            "Objective-C bindings for YAJL (Yet Another JSON Library) C library",
            "https://avatars0.githubusercontent.com/u/82592?v=4"
        )

        private val presentationRepo2 = RepoPresentationModel(
            298912,
            "simplerrd",
            "SimpleRRD provides a simple Ruby interface for creating graphs with RRD",
            "https://avatars0.githubusercontent.com/u/82592?v=4"
        )

        val repoDBList = listOf(presentationRepo1, presentationRepo2)

        val responseSuccess = Response(Status.SUCCESS, repoDBList)
        val responseFailure = Response(Status.ERROR, emptyList())

    }
}