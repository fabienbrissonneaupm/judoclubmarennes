package biz.ei6.judo.domain


import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

enum class EventType { Tous, Competition, Stage, Grade }


data class JudoEvent @OptIn(ExperimentalUuidApi::class) constructor(
   val id: Uuid,
    val type: EventType,
    val title: String,
    val description: String,
    val dateLabel: String="",          // ex: "28 OCT"
    val timeRange: String = "",  // ex: "09:00 - 17:00"
    val location: String ="",
    val latitute: Double=0.0,
    val longitude: Double=0.0,
    val isFavorite: Boolean = false,
    val thumbImageUrl: List<String> = mutableListOf(),
    val featuredImageUrl: String? = null,


    ) {
    companion object {
        @OptIn(ExperimentalUuidApi::class)
        val DEFAULT = JudoEvent(
            id = Uuid.random(),
            type = EventType.Competition,
            title = "Tournoi International de Paris",
            description = "Super tournoi",
            dateLabel = "24-Oct",
            location = "Accor Arena, Paris",
            latitute = 48.8386,   // Paris (Accor Arena / Bercy)
            longitude = 2.3786,
            isFavorite = false,
            featuredImageUrl = "https://picsum.photos/300/200",
            thumbImageUrl = listOf(
                "https://picsum.photos/seed/1/300/200",
                "https://picsum.photos/seed/2/300/200",
                "https://picsum.photos/seed/3/300/200",
                "https://picsum.photos/seed/4/300/200",
                "https://picsum.photos/seed/5/300/200"
            )
        )

        @OptIn(ExperimentalUuidApi::class)
        val LIST = listOf(

            JudoEvent(
                id = Uuid.random(),
                type = EventType.Competition,
                title = "Open National de Lyon",
                description = "Compétition nationale regroupant les meilleurs judokas",
                dateLabel = "12-Mar",
                location = "Palais des Sports, Lyon",
                latitute = 45.7640,  // Lyon
                longitude = 4.8357,
                isFavorite = false,
                featuredImageUrl = "https://picsum.photos/300/200?random=10",
                thumbImageUrl = listOf(
                    "https://picsum.photos/seed/10/300/200",
                    "https://picsum.photos/seed/11/300/200",
                    "https://picsum.photos/seed/12/300/200"
                )
            ),

            JudoEvent(
                id = Uuid.random(),
                type = EventType.Stage,
                title = "Stage Technique Régional",
                description = "Stage intensif axé sur le ne-waza et le kumikata",
                dateLabel = "05-Apr",
                location = "Dojo Régional, Toulouse",
                latitute = 43.6047,  // Toulouse
                longitude = 1.4442,
                isFavorite = true,
                featuredImageUrl = "https://picsum.photos/300/200?random=20",
                thumbImageUrl = listOf(
                    "https://picsum.photos/seed/20/300/200",
                    "https://picsum.photos/seed/21/300/200",
                    "https://picsum.photos/seed/22/300/200",
                    "https://picsum.photos/seed/23/300/200"
                )
            ),

            JudoEvent(
                id = Uuid.random(),
                type = EventType.Competition,
                title = "Championnat Régional Île-de-France",
                description = "Qualifications pour les championnats nationaux",
                dateLabel = "18-May",
                location = "Gymnase Pierre de Coubertin, Créteil",
                latitute = 48.7904,  // Créteil
                longitude = 2.4556,
                isFavorite = false,
                featuredImageUrl = "https://picsum.photos/300/200?random=30",
                thumbImageUrl = listOf(
                    "https://picsum.photos/seed/30/300/200",
                    "https://picsum.photos/seed/31/300/200",
                    "https://picsum.photos/seed/32/300/200"
                )
            ),

            JudoEvent(
                id = Uuid.random(),
                type = EventType.Stage,
                title = "Gala du Judo Français",
                description = "Soirée de démonstrations et remise de récompenses",
                dateLabel = "30-Jun",
                location = "Maison du Judo, Paris",
                latitute = 43.2965,  // Marseille
                longitude = 5.3698,
                isFavorite = true,
                featuredImageUrl = "https://picsum.photos/300/200?random=40",
                thumbImageUrl = listOf(
                    "https://picsum.photos/seed/40/300/200",
                    "https://picsum.photos/seed/41/300/200"
                )
            ),

            JudoEvent(
                id = Uuid.random(),
                type = EventType.Grade,
                title = "Gala du Judo Français",
                description = "Soirée de démonstrations et remise de récompenses",
                dateLabel = "30-Jun",
                location = "Maison du Judo, Paris",
                latitute = 50.6292,  // Lille
                longitude = 3.0573,
                isFavorite = true,
                featuredImageUrl = "https://picsum.photos/300/200?random=40",
                thumbImageUrl = listOf(
                    "https://picsum.photos/seed/40/300/200",
                    "https://picsum.photos/seed/41/300/200"
                )
            ),

            JudoEvent(
                id = Uuid.random(),
                type = EventType.Stage,
                title = "Gala du Judo Français",
                description = "Soirée de démonstrations et remise de récompenses",
                dateLabel = "30-Jun",
                location = "Maison du Judo, Paris",
                latitute = 44.8378,  // Bordeaux
                longitude = -0.5792,
                isFavorite = true,
                featuredImageUrl = "https://picsum.photos/300/200?random=40",
                thumbImageUrl = listOf(
                    "https://picsum.photos/seed/40/300/200",
                    "https://picsum.photos/seed/41/300/200"
                )
            ),

            JudoEvent(
                id = Uuid.random(),
                type = EventType.Stage,
                title = "Gala du Judo Français",
                description = "Soirée de démonstrations et remise de récompenses",
                dateLabel = "30-Jun",
                location = "Maison du Judo, Paris",
                latitute = 47.2184,  // Nantes
                longitude = -1.5536,
                isFavorite = true,
                featuredImageUrl = "https://picsum.photos/300/200?random=40",
                thumbImageUrl = listOf(
                    "https://picsum.photos/seed/40/300/200",
                    "https://picsum.photos/seed/41/300/200"
                )
            )
        )
    }

}
/* {
    companion object {
        @OptIn(ExperimentalUuidApi::class)
        val DEFAULT = JudoEvent(
       id = Uuid.random(),
        type = EventType.Competition,
        title = "Tournoi International de Paris",
        description = "Super tournoi",
        dateLabel = "24-Oct",
        location = "Accor Arena, Paris",
        isFavorite = false,
        featuredImageUrl = "https://picsum.photos/300/200",
            thumbImageUrl=listOf("https://picsum.photos/seed/1/300/200","https://picsum.photos/seed/2/300/200","https://picsum.photos/seed/3/300/200","https://picsum.photos/seed/4/300/200","https://picsum.photos/seed/5/300/200"))

        @OptIn(ExperimentalUuidApi::class)
        val LIST = listOf(

                JudoEvent(
                    id = Uuid.random(),
                    type = EventType.Competition,
                    title = "Open National de Lyon",
                    description = "Compétition nationale regroupant les meilleurs judokas",
                    dateLabel = "12-Mar",
                    location = "Palais des Sports, Lyon",
                    isFavorite = false,
                    featuredImageUrl = "https://picsum.photos/300/200?random=10",
                    thumbImageUrl = listOf(
                        "https://picsum.photos/seed/10/300/200",
                        "https://picsum.photos/seed/11/300/200",
                        "https://picsum.photos/seed/12/300/200"
                    )
                ),

        JudoEvent(
        id = Uuid.random(),
        type = EventType.Stage,
        title = "Stage Technique Régional",
        description = "Stage intensif axé sur le ne-waza et le kumikata",
        dateLabel = "05-Apr",
        location = "Dojo Régional, Toulouse",
        isFavorite = true,
        featuredImageUrl = "https://picsum.photos/300/200?random=20",
        thumbImageUrl = listOf(
        "https://picsum.photos/seed/20/300/200",
        "https://picsum.photos/seed/21/300/200",
        "https://picsum.photos/seed/22/300/200",
        "https://picsum.photos/seed/23/300/200"
        )
        ),

        JudoEvent(
        id = Uuid.random(),
        type = EventType.Competition,
        title = "Championnat Régional Île-de-France",
        description = "Qualifications pour les championnats nationaux",
        dateLabel = "18-May",
        location = "Gymnase Pierre de Coubertin, Créteil",
        isFavorite = false,
        featuredImageUrl = "https://picsum.photos/300/200?random=30",
        thumbImageUrl = listOf(
        "https://picsum.photos/seed/30/300/200",
        "https://picsum.photos/seed/31/300/200",
        "https://picsum.photos/seed/32/300/200"
        )
        ),

        JudoEvent(
        id = Uuid.random(),
        type = EventType.Stage,
        title = "Gala du Judo Français",
        description = "Soirée de démonstrations et remise de récompenses",
        dateLabel = "30-Jun",
        location = "Maison du Judo, Paris",
        isFavorite = true,
        featuredImageUrl = "https://picsum.photos/300/200?random=40",
        thumbImageUrl = listOf(
        "https://picsum.photos/seed/40/300/200",
        "https://picsum.photos/seed/41/300/200"
        )
        ),
            JudoEvent(
                id = Uuid.random(),
                type = EventType.Grade,
                title = "Gala du Judo Français",
                description = "Soirée de démonstrations et remise de récompenses",
                dateLabel = "30-Jun",
                location = "Maison du Judo, Paris",
                isFavorite = true,
                featuredImageUrl = "https://picsum.photos/300/200?random=40",
                thumbImageUrl = listOf(
                    "https://picsum.photos/seed/40/300/200",
                    "https://picsum.photos/seed/41/300/200"
                )
            ),
            JudoEvent(
                id = Uuid.random(),
                type = EventType.Stage,
                title = "Gala du Judo Français",
                description = "Soirée de démonstrations et remise de récompenses",
                dateLabel = "30-Jun",
                location = "Maison du Judo, Paris",
                isFavorite = true,
                featuredImageUrl = "https://picsum.photos/300/200?random=40",
                thumbImageUrl = listOf(
                    "https://picsum.photos/seed/40/300/200",
                    "https://picsum.photos/seed/41/300/200"
                )
            ),
            JudoEvent(
                id = Uuid.random(),
                type = EventType.Stage,
                title = "Gala du Judo Français",
                description = "Soirée de démonstrations et remise de récompenses",
                dateLabel = "30-Jun",
                location = "Maison du Judo, Paris",
                isFavorite = true,
                featuredImageUrl = "https://picsum.photos/300/200?random=40",
                thumbImageUrl = listOf(
                    "https://picsum.photos/seed/40/300/200",
                    "https://picsum.photos/seed/41/300/200"
                )
            )
        )

    }*/




