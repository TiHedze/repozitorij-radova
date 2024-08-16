package com.pmf.tihedze.repozitorijradova.services

import com.pmf.tihedze.repozitorijradova.Publication
import com.pmf.tihedze.repozitorijradova.Volume
import grails.gorm.transactions.Transactional


@Transactional
class VolumeService {

    def getAllByPublicationId(UUID publicationId) {
        def publication = Publication.findById(publicationId)
        publication.volumes
    }

    def getById(UUID volumeId) {
        Volume.findById(volumeId)
    }

    def create(String volume, String issue, UUID publicationId) {
        def publication = Publication.findById(publicationId)
        def newVolume = new Volume(volume: volume, issue: issue, publication: publication)
        newVolume.save(flush: true)
    }

    def delete(UUID volumeId) {
        def volume = Volume.findById(volumeId)
        volume.delete()
    }
}
