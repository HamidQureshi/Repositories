package com.hamid.data.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hamid.data.utils.helper.MockDBResponse
import com.hamid.data.utils.helper.MockResponseForPresentation
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class PresentationModelMapperImplTest {

    private val mapper = PresentationModelMapperImpl()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun fromEntity() {

        val result = mapper.fromEntity(MockDBResponse.repoDBList)

        assertEquals(MockResponseForPresentation.responseSuccess.status, result.status)
        assertEquals(MockResponseForPresentation.responseSuccess.data, result.data)

        val resultEmpty = mapper.fromEntity(emptyList())

        assertEquals(MockResponseForPresentation.responseFailure.status, resultEmpty.status)
        assertEquals(MockResponseForPresentation.responseFailure.data, resultEmpty.data)

    }

}