package com.pmf.tihedze.repozitorijradova.services

import com.pmf.tihedze.repozitorijradova.Publication
import com.pmf.tihedze.repozitorijradova.Volume
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

    def create(String volume, String issue, UUID publicationId) {
        final def publication = Publication.findById(publicationId)

        if (publication == null) {
            throw new PublicationNotFoundException("No publication found with that id")
        }

        final def newVolume = new Volume(volume: volume, issue: issue, publication: publication)
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
}
