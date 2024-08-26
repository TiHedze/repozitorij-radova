package com.pmf.tihedze.repozitorijradova.services

import com.pmf.tihedze.repozitorijradova.Article
import com.pmf.tihedze.repozitorijradova.Publication
import com.pmf.tihedze.repozitorijradova.Volume
import com.pmf.tihedze.repozitorijradova.commands.volume.AddArticlesCommand
import com.pmf.tihedze.repozitorijradova.commands.volume.CreateVolumeCommand
import com.pmf.tihedze.repozitorijradova.commands.volume.RemoveArticlesCommand
import com.pmf.tihedze.repozitorijradova.exceptions.ArticleNotFoundException
import com.pmf.tihedze.repozitorijradova.exceptions.PublicationNotFoundException
import com.pmf.tihedze.repozitorijradova.exceptions.VolumeNotFoundException
import grails.gorm.transactions.Transactional

@Transactional
class VolumeService {

    List<Volume> getAllByPublicationId(UUID publicationId) {
        final def publication = Publication.findById(publicationId)
        publication.volumes
    }

    Volume getById(UUID volumeId) {
        Volume.findById(volumeId)
    }

    List<Volume> getAllByPublicationName(String query) {
        final List<Publication> publications = Publication.findAllByNameLike(query)
        publications.collectMany { it.volumes}
    }

    Volume create(CreateVolumeCommand command) {
        final def publicationUuid = UUID.fromString(command.publicationId)
        final def publication = Publication.findById(publicationUuid)
        final def articlesUuids = command.articleIds.collect {UUID.fromString(it)}
        final def articles = Article.getAll(articlesUuids)

        if (publication == null) {
            throw new PublicationNotFoundException("No publication found with that id")
        }

        final def newVolume = new Volume(volume: command.volume, issue: command.issue, publication: publication)

        if (!articles.isEmpty()) {
            newVolume.addToArticles(articles: articles)
        }

        newVolume.save(flush: true)
    }

    void delete(UUID volumeId) {
        final def volume = Volume.findById(volumeId)
        if (volume  == null) {
            throw new VolumeNotFoundException("No volume found for the provided id")
        }
        volume.delete()
    }

    Volume getByPublicationNameAndIssue(String publicationName, String issue) {
        final def publication = Publication.findByName(publicationName)
        if (publication != null) {
            return publication.volumes.find { Volume volume ->
                volume.issue == issue
            }
        }
        Volume.findByIssue(issue)
    }

    Volume addArticles(AddArticlesCommand command, UUID id) {
        addOrRemoveArticles(command.articleIds, id)
    }

    Volume removeArticles(RemoveArticlesCommand command, UUID id) {
        addOrRemoveArticles(command.articleIds, id, true)
    }

    private static Volume addOrRemoveArticles(List<String> articleIds, UUID volumeId, boolean delete = false) {
        final def articleUuids = articleIds.collect {UUID.fromString(it)}
        final def articles = Article.getAll(articleUuids)
        final def volume = Volume.get(volumeId)

        if (volume == null) {
            throw new VolumeNotFoundException('Volume with the provided id not found')
        }

        if (articles.isEmpty()) {
            throw new ArticleNotFoundException('No articles found for the provided ids')
        }

        if (delete)   {
            articles.each { volume.removeFromArticles(it)}
        } else {
            articles.each { volume.addToArticles(it) }
        }

        volume.save(flush: true)
    }
}
