package com.werwolv.game.model;

import com.werwolv.game.resource.TextureModel;

public class ModelTextured {

    private ModelRaw modelRaw;          //The model of this model
    private TextureModel texture;       //The texture of this model

    public ModelTextured(ModelRaw model, TextureModel texture) {
        this.modelRaw = model;
        this.texture = texture;
    }

    /* Getters */

    public ModelRaw getModelRaw() {
        return modelRaw;
    }

    public TextureModel getTexture() {
        return texture;
    }

}