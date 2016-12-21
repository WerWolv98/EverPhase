package com.werwolv.shader;

import com.werwolv.entity.EntityCamera;
import com.werwolv.entity.EntityLight;
import com.werwolv.toolbox.Maths;
import org.joml.Matrix4f;

public class ShaderStatic extends Shader {

    private int loc_transMatrix, loc_projMatrix, loc_viewMatrix;
    private int loc_lightPos, loc_lightColor;
    private int loc_shineDamper, loc_reflectivity;

    public ShaderStatic() {
        super("vertexShader", "fragmentShader");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
        loc_transMatrix = super.getUniformLocation("transformationMatrix");
        loc_projMatrix = super.getUniformLocation("projectionMatrix");
        loc_viewMatrix = super.getUniformLocation("viewMatrix");

        loc_lightPos = super.getUniformLocation("lightPos");
        loc_lightColor = super.getUniformLocation("lightColor");

        loc_shineDamper = super.getUniformLocation("shineDamper");
        loc_reflectivity = super.getUniformLocation("reflectivity");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(loc_transMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(loc_projMatrix, matrix);
    }

    public void loadViewMatrix(EntityCamera camera) {
        super.loadMatrix(loc_viewMatrix, Maths.createViewMatrix(camera));
    }

    public void loadLight(EntityLight light) {
        super.loadVector(loc_lightPos, light.getPosition());
        super.loadVector(loc_lightColor, light.getColor());
    }

    public void loadShineVars(float damper, float reflectivity) {
        super.loadFloat(loc_shineDamper, damper);
        super.loadFloat(loc_reflectivity, reflectivity);
    }
}
