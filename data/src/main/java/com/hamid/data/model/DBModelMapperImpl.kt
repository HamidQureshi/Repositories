package com.hamid.data.model

import com.hamid.domain.model.repository.RepoModelMapper

/**
 * Maps between Room database entity and Api.
 */
class DBModelMapperImpl : RepoModelMapper<List<RemoteApiResponse>, List<RepoDBModel>> {

    override fun fromEntity(from: List<RemoteApiResponse>) =
        convertResponseForDB(from)

    private fun convertResponseForDB(response: List<RemoteApiResponse>): List<RepoDBModel> {

        val formattedRepo = ArrayList<RepoDBModel>()

        for (repo in response) {
            formattedRepo.add(
                RepoDBModel(
                    repo.id,
                    repo.name,
                    repo.description,
                    repo.owner.avatar_url
                )
            )
        }

        return formattedRepo
    }

}