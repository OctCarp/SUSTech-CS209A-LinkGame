package io.github.octcarp.sustech.cs209a.linkgame.client.utils

import io.github.octcarp.sustech.cs209a.linkgame.common.model.GridPos
import javafx.scene.image.Image
import javafx.scene.image.ImageView

object ImageLoader {
    fun addContent(content: Int): ImageView? {
        return when (content) {
            12 -> ImageView(imageCarambola)
            1 -> ImageView(imageApple)
            2 -> ImageView(imageMango)
            3 -> ImageView(imageBlueberry)
            4 -> ImageView(imageCherry)
            5 -> ImageView(imageGrape)
            6 -> ImageView(imageKiwi)
            7 -> ImageView(imageOrange)
            8 -> ImageView(imagePeach)
            9 -> ImageView(imagePear)
            10 -> ImageView(imagePineapple)
            11 -> ImageView(imageWatermelon)
            0 -> ImageView(imageEmpty)
            else -> null
        }
    }

    fun getDirectImgByPos(start: GridPos, mid: GridPos, end: GridPos): Image {
        val posList = listOf<GridPos>(start, end)

        var (left, right, up, down) = Array(4) { false }
        for (pos in posList) {
            when {
                pos.row == mid.row -> if (pos.col < mid.col) left = true else right = true
                pos.col == mid.col -> if (pos.row < mid.row) up = true else down = true
            }
        }

        return when {
            up -> {
                when {
                    left -> lineUpLeft
                    right -> lineUpRight
                    else -> lineVertical
                }
            }

            down -> {
                when {
                    left -> lineDownLeft
                    else -> lineDownRight
                }
            }

            else -> lineHorizontal
        }
    }

    private fun getImageByPath(path: String): Image =
        Image(ImageLoader::class.java.getResource(path)!!.toExternalForm())

    // Fruits
    private val imageApple = getImageByPath("/img/fruits/apple.png")
    private val imageMango = getImageByPath("/img/fruits/mango.png")
    private val imageBlueberry = getImageByPath("/img/fruits/blueberry.png")
    private val imageCherry = getImageByPath("/img/fruits/cherry.png")
    private val imageGrape = getImageByPath("/img/fruits/grape.png")
    private val imageCarambola = getImageByPath("/img/fruits/carambola.png")
    private val imageKiwi = getImageByPath("/img/fruits/kiwi.png")
    private val imageOrange = getImageByPath("/img/fruits/orange.png")
    private val imagePeach = getImageByPath("/img/fruits/peach.png")
    private val imagePear = getImageByPath("/img/fruits/pear.png")
    private val imagePineapple = getImageByPath("/img/fruits/pineapple.png")
    private val imageWatermelon = getImageByPath("/img/fruits/watermelon.png")
    private val imageEmpty = getImageByPath("/img/fruits/empty.png")

    // Lines
    private val lineUpLeft = getImageByPath("/img/lines/u-l.png")
    private val lineUpRight = getImageByPath("/img/lines/u-r.png")
    private val lineDownLeft = getImageByPath("/img/lines/d-l.png")
    private val lineDownRight = getImageByPath("/img/lines/d-r.png")
    private val lineHorizontal = getImageByPath("/img/lines/l-r.png")
    private val lineVertical = getImageByPath("/img/lines/u-d.png")
}