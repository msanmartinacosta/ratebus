package ratebus

import groovy.time.TimeCategory

class Transport {

    protected Long id

    protected String name

    protected String ownerCompany

    protected String contactNumber

    protected Address address

    protected String country

    protected String city

    protected Set<GeneralRating> allRatings

    static constraints = {
    }

    def getGeneralRating() {
        allRatings.collect { it.calculateGeneralRating() }.sum() / allRatings.size()
    }

    def addGeneralRating(User user, GeneralRating generalRating) {
        if (!allRatings)
            allRatings = new ArrayList<GeneralRating>()

        def existingRatingForUser = allRatings.find { it.user == user }

        if (existingRatingForUser)
            throw new Exception("Existing rating in last 30 days, cannot ")

        generalRating.user = user
        allRatings.add(generalRating)
    }

    def editRating(User user, GeneralRating generalRating) {
        def existingRatingIn15Days = allRatings.find {
            TimeCategory.minus(new Date(), it.creationDate).days <= 30 && it.user == user
        }

        if (existingRatingIn15Days)
            throw new Exception("Existing rating in last 15 days, cannot edit")

        generalRating.user = user
        allRatings.add(generalRating)
    }
}
