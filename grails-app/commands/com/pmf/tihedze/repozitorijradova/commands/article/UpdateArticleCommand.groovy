package com.pmf.tihedze.repozitorijradova.commands.article

import grails.validation.Validateable

class UpdateArticleCommand implements Validateable{
    String summary
    String title
    ArrayList<UUID> authorIds
    UUID volumeId
}
