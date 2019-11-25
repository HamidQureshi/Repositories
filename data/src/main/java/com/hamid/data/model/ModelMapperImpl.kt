package com.hamid.data.model

import com.hamid.domain.model.model.RemoteApiResponse
import com.hamid.domain.model.repository.ModelMapper

/**
 * Maps between Room database entity and Api.
 */
class ModelMapperImpl : ModelMapper<List<RemoteApiResponse>, List<RepoDBModel>> {

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