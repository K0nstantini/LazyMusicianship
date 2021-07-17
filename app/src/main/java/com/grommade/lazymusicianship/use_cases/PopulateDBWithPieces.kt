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
            name = "Город, которого нет",
            author = "Игорь Корнелюк",
            arranger = "Колосов В. М."
        ).save()

        Piece(
            name = "Et si tu n’existais pas",
            author = "Joe Dassin",
            arranger = "Варфоломеев И."
        ).save()

        Piece(
            name = "Knockin' on Heaven's Door",
            author = "Bob Dylan",
            arranger = "Варфоломеев И."
        ).save()

        Piece(
            name = "Don't Cry",
            author = "Guns N' Roses",
            arranger = "Варфоломеев И."
        ).save()

        Piece(
            name = "I just want you",
            author = "Ozzy Osbourne",
            arranger = "Eiro Nareth"
        ).save()

        Piece(
            name = "Кончится лето",
            author = "Кино",
            arranger = "Eiro Nareth"
        ).save()

        Piece(
            name = "Mutter",
            author = "Rammstein",
            arranger = "Eiro Nareth"
        ).save()

        Piece(
            name = "О Любви",
            author = "Чиж & Co",
            arranger = "Eiro Nareth"
        ).save()

        Piece(
            name = "Ohne Dich",
            author = "Rammstein",
            arranger = "Eiro Nareth"
        ).save()

        Piece(
            name = "Rape Me",
            author = "Nirvana",
            arranger = "Eiro Nareth"
        ).save()

        Piece(
            name = "Серебро",
            author = "Би-2",
            arranger = "Eiro Nareth"
        ).save()

        Piece(
            name = "Sweet Harmony",
            author = "The Beloved",
            arranger = "Eiro Nareth"
        ).save()

        Piece(
            name = "Zombie",
            author = "The Cranberries",
            arranger = "Eiro Nareth"
        ).save()

        Piece(
            name = "Air OST - Natsukage",
            author = "Jun Maeda",
            arranger = "Eddie van der Meer"
        ).save()

        Piece(
            name = "Death Note - Opening",
            author = "Hideki Taniuchi",
            arranger = "Eddie van der Meer"
        ).save()
    }
    
    private suspend fun Piece.save() = repoPiece.save(this)
}