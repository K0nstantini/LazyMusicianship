package com.grommade.lazymusicianship.domain.use_cases

import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.data.entity.Practice
import com.grommade.lazymusicianship.data.entity.Section
import com.grommade.lazymusicianship.data.entity.StateStudy
import com.grommade.lazymusicianship.domain.InputWorkUseCase
import com.grommade.lazymusicianship.domain.repos.RepoPiece
import com.grommade.lazymusicianship.domain.repos.RepoPractice
import com.grommade.lazymusicianship.domain.repos.RepoSection
import com.grommade.lazymusicianship.domain.repos.RepoStateStudy
import com.grommade.lazymusicianship.util.extentions.toTime
import java.time.LocalDate
import javax.inject.Inject

class PopulateDBWithPieces @Inject constructor(
    private val repoPiece: RepoPiece,
    private val repoSection: RepoSection,
    private val repoStates: RepoStateStudy,
    private val repoPractice: RepoPractice
) : InputWorkUseCase<Unit>() {

    override suspend fun doWork(params: Unit) {

        repoPractice.deleteAll()
        repoSection.deleteAll()
        repoPiece.deleteAll()
        repoStates.deleteAll()

        val states = populateStates()
        val pieces = populatePieces()
        populatePractices(pieces, states)
    }

    private suspend fun populateStates(): List<Long> {
        val states = mutableListOf<Long>()

        StateStudy(name = "Just practice").save()
            .apply { states.add(this) }
        StateStudy(name = "In tempo", considerTempo = true, countNumberOfTimes = true).save()
            .apply { states.add(this) }
        return states
    }

    private suspend fun populatePieces(): List<Long> {

        val pieces = mutableListOf<Long>()

        Piece(
            name = "Город, которого нет",
            author = "Игорь Корнелюк",
            arranger = "Колосов В. М."
        ).save().apply { pieces.add(this) }

        Piece(
            name = "Et si tu n’existais pas",
            author = "Joe Dassin",
            arranger = "Варфоломеев И."
        ).save().apply { pieces.add(this) }

        Piece(
            name = "Knockin' on Heaven's Door",
            author = "Bob Dylan",
            arranger = "Варфоломеев И."
        ).save().apply { pieces.add(this) }

        /** ============= Don't Cry ========================*/

        val idCry = Piece(
            name = "Don't Cry",
            author = "Guns N' Roses",
            arranger = "Варфоломеев И."
        ).save().apply { pieces.add(this) }

        val defaultSectionCry = Section(pieceId = idCry, tempo = 124, firstTime = false)
        defaultSectionCry.copy(name = "Intro").save()
        val idCryVerse1 = defaultSectionCry.copy(name = "Verse 1").save()
        defaultSectionCry.copy(name = "1", parentId = idCryVerse1).save()
        defaultSectionCry.copy(name = "2", parentId = idCryVerse1).save()
        defaultSectionCry.copy(name = "3", parentId = idCryVerse1).save()

        val idCryChorus1 = defaultSectionCry.copy(name = "Chorus 1").save()
        defaultSectionCry.copy(name = "1", parentId = idCryChorus1).save()
        defaultSectionCry.copy(name = "2", parentId = idCryChorus1).save()
        defaultSectionCry.copy(name = "3", parentId = idCryChorus1).save()

        val idCryVerse2 = defaultSectionCry.copy(name = "Verse 2").save()
        defaultSectionCry.copy(name = "1", parentId = idCryVerse2).save()
        defaultSectionCry.copy(name = "2", parentId = idCryVerse2).save()
        defaultSectionCry.copy(name = "3", parentId = idCryVerse2).save()
        defaultSectionCry.copy(name = "4", parentId = idCryVerse2).save()

        val idCryChorus2 = defaultSectionCry.copy(name = "Chorus 2").save()
        defaultSectionCry.copy(name = "1", parentId = idCryChorus2).save()
        defaultSectionCry.copy(name = "2", parentId = idCryChorus2).save()

        val idCryVerse3 = defaultSectionCry.copy(name = "Verse 3").save()
        defaultSectionCry.copy(name = "1", parentId = idCryVerse3).save()
        defaultSectionCry.copy(name = "2", parentId = idCryVerse3).save()
        defaultSectionCry.copy(name = "3", parentId = idCryVerse3).save()
        defaultSectionCry.copy(name = "4", parentId = idCryVerse3).save()

        val idCryChorus3 = defaultSectionCry.copy(name = "Chorus 3").save()
        defaultSectionCry.copy(name = "Part I", parentId = idCryChorus3).save()

        val idCryChorus3Part2 = defaultSectionCry.copy(name = "Part II", parentId = idCryChorus3).save()
        defaultSectionCry.copy(name = "1", parentId = idCryChorus3Part2).save()
        defaultSectionCry.copy(name = "2", parentId = idCryChorus3Part2).save()
        defaultSectionCry.copy(name = "3", parentId = idCryChorus3Part2).save()

        Piece(
            name = "I just want you",
            author = "Ozzy Osbourne",
            arranger = "Eiro Nareth"
        ).save().apply { pieces.add(this) }

        Piece(
            name = "Кончится лето",
            author = "Кино",
            arranger = "Eiro Nareth"
        ).save().apply { pieces.add(this) }

        Piece(
            name = "Mutter",
            author = "Rammstein",
            arranger = "Eiro Nareth"
        ).save().apply { pieces.add(this) }

        Piece(
            name = "О Любви",
            author = "Чиж & Co",
            arranger = "Eiro Nareth"
        ).save().apply { pieces.add(this) }

        Piece(
            name = "Ohne Dich",
            author = "Rammstein",
            arranger = "Eiro Nareth"
        ).save().apply { pieces.add(this) }

        Piece(
            name = "Rape Me",
            author = "Nirvana",
            arranger = "Eiro Nareth"
        ).save().apply { pieces.add(this) }

        Piece(
            name = "Серебро",
            author = "Би-2",
            arranger = "Eiro Nareth"
        ).save().apply { pieces.add(this) }

        Piece(
            name = "Sweet Harmony",
            author = "The Beloved",
            arranger = "Eiro Nareth"
        ).save().apply { pieces.add(this) }

        Piece(
            name = "Zombie",
            author = "The Cranberries",
            arranger = "Eiro Nareth"
        ).save().apply { pieces.add(this) }

        Piece(
            name = "Air OST - Natsukage",
            author = "Jun Maeda",
            arranger = "Eddie van der Meer"
        ).save().apply { pieces.add(this) }

        Piece(
            name = "Death Note - Opening",
            author = "Hideki Taniuchi",
            arranger = "Eddie van der Meer"
        ).save().apply { pieces.add(this) }

        return pieces
    }

    private suspend fun populatePractices(
        pieces: List<Long>,
        states: List<Long>
    ) {
        if (pieces.isEmpty() || states.isEmpty()) {
            return
        }
        for (i in 100 downTo 0) {
            repeat((0..10).random()) {
                val state = states.random()
                Practice(
                    date = LocalDate.now().minusDays(i.toLong()),
                    pieceId = pieces.random(),
                    stateId = state,
                    tempo = if (state == 2L) (60..120).random() else 0,
                    countTimes = if (state == 2L) (1..5).random() else 0,
                    elapsedTime = ("00:03".toTime().."01:00".toTime()).random()
                ).save()
            }
        }
    }

    private suspend fun Piece.save() = repoPiece.save(this)
    private suspend fun Section.save() = repoSection.save(this)
    private suspend fun StateStudy.save() = repoStates.save(this)
    private suspend fun Practice.save() = repoPractice.save(this)
}