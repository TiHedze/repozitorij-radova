package com.pmf.tihedze.repozitorijradova.services

import com.pmf.tihedze.repozitorijradova.Article
import com.pmf.tihedze.repozitorijradova.Publication
import com.pmf.tihedze.repozitorijradova.Volume
import com.pmf.tihedze.repozitorijradova.commands.volume.AddArticlesCommand
import com.pmf.tihedze.repozitorijradova.commands.volume.CreateVolumeCommand
import com.pmf.tihedze.repozitorijradova.exceptions.PublicationNotFoundException
import com.pmf.tihedze.repozitorijradova.exceptions.VolumeNotFoundException
import grails.gorm.transactions.Transactional

@Transactional
class VolumeService {

    def getAllByPublicationId(UUID publicationId) {
        final def publication = Publication.findById(publicationId)
        publication.volumes
    }

    def getById(UUID volumeId) {
        Volume.findById(volumeId)
    }

    def create(CreateVolumeCommand command) {
        final def publicationUuid = UUID.fromString(command.publicationId)
        final def publication = Publication.findById(publicationUuid)
        final def articlesUuids = command.articleIds.collect {UUID.fromString(it)}
        final def articles = Article.findAllByIdInList(articlesUuids)



        if (publication == null) {
            throw new PublicationNotFoundException("No publication found with that id")
        }

        final def newVolume = new Volume(volume: command.volume, issue: command.issue, publication: publication)

        if (!articles.isEmpty()) {
            newVolume.addToArticles(articles: articles)
        }

        newVolume.save(flush: true)
    }

    def delete(UUID volumeId) {
        def volume = Volume.findById(volumeId)
        volume.delete()
    }

    def getByPublicationNameAndIssue(String publicationName, String issue) {
        final def publication = Publication.findByName(publicationName);
        if (publication != null) {
            return publication.volumes.find { Volume volume ->
                volume.issue == issue
            }
        }
        Volume.findByIssue(issue)
    }

    def addArticles(AddArticlesCommand command) {
        final def articleUuids = command.articleIds.collect {UUID.fromString(it)}
        final def articlesToAdd = Article.findAllByIdInList(articleUuids)
        final def volume = Volume.get(UUID.fromString(command.volumeId))

        if (volume == null) {
            throw new VolumeNotFoundException('Volume with the provided id not found')
        }

        if (!articlesToAdd.isEmpty()) {
            volume.addToArticles(articles: articlesToAdd)
        }


    }
}
