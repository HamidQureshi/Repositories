package com.hamid.data.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hamid.data.utils.helper.MockApiResponse
import com.hamid.data.utils.helper.MockDBResponse
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ModelMapperImplTest {

    private val mapper = ModelMapperImpl()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun fromEntity() {
        val result = mapper.fromEntity(MockApiResponse.repoResponseList)

        assertEquals(MockDBResponse.repoDBList, result)
        assertEquals(MockDBResponse.repoDBList[0], result[0])
        assertEquals(MockDBResponse.repoDBList[1], result[1])
    }

}