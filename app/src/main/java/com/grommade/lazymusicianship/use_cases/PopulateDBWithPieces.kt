package com.grommade.lazymusicianship.use_cases

import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.data.repos.RepoPiece
import javax.inject.Inject

interface PopulateDBWithPieces {
    suspend operator fun invoke()
}

class PopulateDBWithPiecesImpl @Inject constructor(
    private val repoPiece: RepoPiece
) : PopulateDBWithPieces {
    override suspend fun invoke() {

        repoPiece.deleteAll()

        Piece(
            title = "Город, которого нет",
            author = "Игорь Корнелюк",
            arranger = "Колосов В. М."
        ).save()

        Piece(
            title = "Et si tu n’existais pas",
            author = "Joe Dassin",
            arranger = "Варфоломеев И."
        ).save()

        Piece(
            title = "Knockin' on Heaven's Door",
            author = "Bob Dylan",
            arranger = "Варфоломеев И."
        ).save()

        Piece(
            title = "Don't Cry",
            author = "Guns N' Roses",
            arranger = "Варфоломеев И."
        ).save()

        Piece(
            title = "I just want you",
            author = "Ozzy Osbourne",
            arranger = "Eiro Nareth"
        ).save()

        Piece(
            title = "Кончится лето",
            author = "Кино",
            arranger = "Eiro Nareth"
        ).save()

        Piece(
            title = "Mutter",
            author = "Rammstein",
            arranger = "Eiro Nareth"
        ).save()

        Piece(
            title = "О Любви",
            author = "Чиж & Co",
            arranger = "Eiro Nareth"
        ).save()

        Piece(
            title = "Ohne Dich",
            author = "Rammstein",
            arranger = "Eiro Nareth"
        ).save()

        Piece(
            title = "Rape Me",
            author = "Nirvana",
            arranger = "Eiro Nareth"
        ).save()

        Piece(
            title = "Серебро",
            author = "Би-2",
            arranger = "Eiro Nareth"
        ).save()

        Piece(
            title = "Sweet Harmony",
            author = "The Beloved",
            arranger = "Eiro Nareth"
        ).save()

        Piece(
            title = "Zombie",
            author = "The Cranberries",
            arranger = "Eiro Nareth"
        ).save()

        Piece(
            title = "Air OST - Natsukage",
            author = "Jun Maeda",
            arranger = "Eddie van der Meer"
        ).save()

        Piece(
            title = "Death Note - Opening",
            author = "Hideki Taniuchi",
            arranger = "Eddie van der Meer"
        ).save()
    }
    
    private suspend fun Piece.save() = repoPiece.save(this)
}