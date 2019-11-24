package com.hamid.data.model

import com.hamid.domain.model.model.RepoPresentationModel
import com.hamid.domain.model.model.Response
import com.hamid.domain.model.model.Status
import com.hamid.domain.model.repository.RepoModelMapper

/**
 * Maps between Room database entity and presentation model.
 */
class PresentationModelMapperImpl : RepoModelMapper<List<RepoDBModel>, Response> {
    override fun fromEntity(from: List<RepoDBModel>) =
        convertResponseForUI(convertModel(from))

    private fun convertModel(repoList: List<RepoDBModel>): List<RepoPresentationModel> {

        val formattedRepo = ArrayList<RepoPresentationModel>()

        for (repo in repoList) {
            formattedRepo.add(
                RepoPresentationModel(
                    repo.id,
                    repo.name,
                    repo.description,
                    repo.avatar
                )
            )
        }

        return formattedRepo
    }

    private fun convertResponseForUI(repoList: List<RepoPresentationModel>): Response {

        val response =
            Response(Status.SUCCESS, repoList)

        if (repoList.isEmpty()) {
            response.status = Status.ERROR
        }

        return response
    }
}