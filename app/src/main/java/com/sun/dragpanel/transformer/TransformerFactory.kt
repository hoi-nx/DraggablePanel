/*
 * Copyright (C) 2014 Pedro Vicente Gómez Sánchez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sun.dragpanel.transformer
import android.view.View

/**
 * Factory created to provide Transformer implementations like ResizeTransformer o
 * ScaleTransformer.
 *
 * @author Pedro Vicente Gómez Sánchez
 */
class TransformerFactory {

    fun getTransformer(resize: Boolean, view: View, parent: View): Transformer {
        val transformer: Transformer
        if (resize) {
            transformer = ResizeTransformer(view, parent)
        } else {
            transformer = ScaleTransformer(view, parent)
        }
        return transformer
    }
}
