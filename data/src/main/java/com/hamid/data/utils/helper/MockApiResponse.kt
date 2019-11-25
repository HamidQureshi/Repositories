package com.hamid.data.utils.helper

import com.hamid.domain.model.model.Owner
import com.hamid.domain.model.model.RemoteApiResponse

class MockApiResponse {
    companion object {

        private val owner1 = Owner(
            "https://avatars0.githubusercontent.com/u/82592?v=4"
        )

        private val responseRepo1 = RemoteApiResponse(
            274562,
            "yajl-objc",
            "Objective-C bindings for YAJL (Yet Another JSON Library) C library",
            owner1
        )

        private val owner2 = Owner(
            "https://avatars0.githubusercontent.com/u/82592?v=4"
        )

        private val responseRepo2 = RemoteApiResponse(
            298912,
            "simplerrd",
            "SimpleRRD provides a simple Ruby interface for creating graphs with RRD",
            owner2
        )

        val repoResponseList = listOf(responseRepo1, responseRepo2)

    }
}