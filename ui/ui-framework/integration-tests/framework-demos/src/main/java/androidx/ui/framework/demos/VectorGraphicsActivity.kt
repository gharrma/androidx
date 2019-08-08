/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.ui.framework.demos

import android.app.Activity
import android.graphics.Shader
import android.os.Bundle
import androidx.compose.Children
import androidx.compose.Composable
import androidx.compose.composer
import androidx.compose.unaryPlus
import androidx.ui.core.Dp
import androidx.ui.core.IntPx
import androidx.ui.core.Layout
import androidx.ui.core.Px
import androidx.ui.core.WithDensity
import androidx.ui.core.dp
import androidx.ui.core.px
import androidx.ui.core.round
import androidx.ui.core.vectorgraphics.DrawVector
import androidx.ui.core.vectorgraphics.Group
import androidx.ui.core.vectorgraphics.Path
import androidx.ui.graphics.Color
import androidx.ui.graphics.vectorgraphics.HorizontalGradient
import androidx.ui.graphics.vectorgraphics.PathBuilder
import androidx.ui.core.setContent
import androidx.ui.core.vectorgraphics.DrawVector
import androidx.ui.core.vectorgraphics.Group
import androidx.ui.core.vectorgraphics.Path
import androidx.ui.graphics.vectorgraphics.RadialGradient
import androidx.ui.graphics.vectorgraphics.VerticalGradient
import androidx.ui.layout.Center
import androidx.ui.layout.Column
import androidx.ui.layout.Container
import androidx.ui.painting.TileMode
import androidx.ui.core.setContent
import androidx.ui.core.vectorgraphics.compat.vectorResource
import androidx.ui.graphics.vectorgraphics.PathDelegate
import androidx.ui.vector.VectorScope

class VectorGraphicsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Column {
                val vectorAsset = +vectorResource(R.drawable.ic_crane)
                WithDensity {
                    val width = vectorAsset.defaultWidth.toDp()
                    val height = vectorAsset.defaultHeight.toDp()
                    Container(width = width, height = height) {
                        DrawVector(vectorAsset)
                    }
                }

                Center {
                    val width = 300.px
                    val height = 300.px
                    FixedLayout(width.round(), height.round()) {
                        vectorShape(width, height)
                    }
                }
            }
        }
    }

    @Composable
    fun FixedLayout(width: IntPx, height: IntPx, child: @Composable() () -> Unit) {
        Layout(children = { child() },
            layoutBlock = { _, _ ->
                layout(width, height) {}
            })
    }

    @Composable
    fun vectorShape(width: Px, height: Px) {
        val viewportWidth = width.value
        val viewportHeight = height.value
        DrawVector(
            name = "vectorShape",
            defaultWidth = width,
            defaultHeight = height,
            viewportWidth = viewportWidth,
            viewportHeight = viewportHeight
        ) {
            Group(
                scaleX = 0.75f,
                scaleY = 0.75f,
                rotation = 45.0f,
                pivotX = (viewportWidth / 2),
                pivotY = (viewportHeight / 2)
            ) {
                backgroundPath(vectorWidth = viewportWidth, vectorHeight = viewportHeight)
                stripePath(vectorWidth = viewportWidth, vectorHeight = viewportHeight)
                Group(
                    translationX = 50.0f,
                    translationY = 50.0f,
                    pivotX = (viewportWidth / 2),
                    pivotY = (viewportHeight / 2),
                    rotation = 25.0f
                ) {
                    val pathData = PathDelegate {
                        moveTo(viewportWidth / 2 - 100, viewportHeight / 2 - 100)
                        horizontalLineToRelative(200.0f)
                        verticalLineToRelative(200.0f)
                        horizontalLineToRelative(-200.0f)
                        close()
                    }
                    Path(
                        fill = HorizontalGradient(
                            Color.Red,
                            Color.Blue,
                            startX = Px.Zero,
                            endX = Px(viewportWidth / 2 + 100)
                        ),
                        pathData = pathData
                    )
                }
                triangle()
                triangleWithOffsets()
            }
        }
    }

    @Composable
    fun VectorScope.backgroundPath(vectorWidth: Float, vectorHeight: Float) {
        val background = PathDelegate {
            horizontalLineTo(vectorWidth)
            verticalLineTo(vectorHeight)
            horizontalLineTo(0.0f)
            close()
        }

        Path(
            fill = VerticalGradient(
                0.0f to Color.Aqua,
                0.3f to Color.Lime,
                1.0f to Color.Fuchsia,
                startY = Px.Zero,
                endY = Px(vectorHeight)
            ),
            pathData = background
        )
    }

    @Composable
    fun VectorScope.triangle() {
        val length = 150.0f
        Path(
            fill = RadialGradient(
                Color.Navy,
                Color.Olive,
                Color.Teal,
                centerX = length / 2.0f,
                centerY = length / 2.0f,
                radius = length / 2.0f,
                tileMode = TileMode.repeated
            ),
            pathData = PathDelegate {
                verticalLineTo(length)
                horizontalLineTo(length)
                close()
            }
        )
    }

    @Composable
    fun VectorScope.triangleWithOffsets() {

        val side1 = 150.0f
        val side2 = 150.0f
        Path(
            fill = RadialGradient(
                0.0f to Color.Maroon,
                0.3f to Color.Cyan,
                0.8f to Color.Yellow,
                centerX = side1 / 2.0f,
                centerY = side2 / 2.0f,
                radius = side1 / 2.0f
            ),
            pathData = PathDelegate {
                horizontalLineToRelative(side1)
                verticalLineToRelative(side2)
                close()
            }
        )
    }

    @Composable
    fun VectorScope.stripePath(vectorWidth: Float, vectorHeight: Float) {
        val stripeDelegate = PathDelegate {
            stripe(vectorWidth, vectorHeight, 10)
        }

        Path(stroke = Color.Blue, pathData = stripeDelegate)
    }

    private fun PathBuilder.stripe(vectorWidth: Float, vectorHeight: Float, numLines: Int) {
        val stepSize = vectorWidth / numLines
        var currentStep = stepSize
        for (i in 1..numLines) {
            moveTo(currentStep, 0.0f)
            verticalLineTo(vectorHeight)
            currentStep += stepSize
        }
    }
}
