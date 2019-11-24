package com.hamid.data.utils.helper

import com.hamid.data.model.RepoDBModel

class MockDBResponse {
    companion object {

        private val dbRepo1 = RepoDBModel(
            274562,
            "yajl-objc",
            "Objective-C bindings for YAJL (Yet Another JSON Library) C library",
            "https://avatars0.githubusercontent.com/u/82592?v=4"
        )

        private val dbRepo2 = RepoDBModel(
            298912,
            "simplerrd",
            "SimpleRRD provides a simple Ruby interface for creating graphs with RRD",
            "https://avatars0.githubusercontent.com/u/82592?v=4"
        )

        val repoDBList = listOf(dbRepo1, dbRepo2)

    }
}