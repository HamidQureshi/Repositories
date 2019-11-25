package com.hamid.domain.model.usecases

import com.hamid.domain.model.repository.GitRepoRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.only
import com.nhaarman.mockitokotlin2.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class GitRepoUseCaseTest {

    private val repo: GitRepoRepository = mock()

    private lateinit var useCase: GitRepoUseCase

    @Before
    fun setUp() {

        useCase = GitRepoUseCase(repo)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getRepoFromDb() {

        useCase.getRepoFromDb()

        verify(repo, only())
            .getRepoFromDb()
    }

    @Test
    fun getRepositoriesFromServer() {
        useCase.getRepoFromServer()

        verify(repo, only())
            .getRepoFromServer()
    }

    @Test
    fun nukeDB() {
        useCase.nukeDB()

        verify(repo, only())
            .nukeDB()
    }

}