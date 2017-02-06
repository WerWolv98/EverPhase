package com.deltabase.everphase.engine.font;

import com.deltabase.everphase.gui.GuiText;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TextMeshCreator {

	public static final double LINE_HEIGHT = 0.03f;
	public static final int SPACE_ASCII = 32;

	private MetaFile metaData;

	public TextMeshCreator(File metaFile) {
		metaData = new MetaFile(metaFile);
	}

    private static void addVertices(List<Float> vertices, double x, double y, double maxX, double maxY) {
        vertices.add((float) x);
        vertices.add((float) y);
        vertices.add((float) x);
        vertices.add((float) maxY);
        vertices.add((float) maxX);
        vertices.add((float) maxY);
        vertices.add((float) maxX);
        vertices.add((float) maxY);
        vertices.add((float) maxX);
        vertices.add((float) y);
        vertices.add((float) x);
        vertices.add((float) y);
    }

    private static void addTexCoords(List<Float> texCoords, double x, double y, double maxX, double maxY) {
        texCoords.add((float) x);
        texCoords.add((float) y);
        texCoords.add((float) x);
        texCoords.add((float) maxY);
        texCoords.add((float) maxX);
        texCoords.add((float) maxY);
        texCoords.add((float) maxX);
        texCoords.add((float) maxY);
        texCoords.add((float) maxX);
        texCoords.add((float) y);
        texCoords.add((float) x);
        texCoords.add((float) y);
    }

    private static float[] listToArray(List<Float> listOfFloats) {
        float[] array = new float[listOfFloats.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = listOfFloats.get(i);
        }
        return array;
    }

	public TextMeshData createTextMesh(GuiText text) {
		List<Line> lines = createStructure(text);

        return createQuadVertices(text, lines);
    }

	private List<Line> createStructure(GuiText text) {
		char[] chars = text.getTextString().toCharArray();
		List<Line> lines = new ArrayList<>();
		Line currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
		Word currentWord = new Word(text.getFontSize());
		for (char c : chars) {
			int ascii = (int) c;
			if (ascii == SPACE_ASCII) {
				boolean added = currentLine.attemptToAddWord(currentWord);
				if (!added) {
					lines.add(currentLine);
					currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
					currentLine.attemptToAddWord(currentWord);
				}
				currentWord = new Word(text.getFontSize());
				continue;
			}
			Character character = metaData.getCharacter(ascii);
			currentWord.addCharacter(character);
		}
		completeStructure(lines, currentLine, currentWord, text);
		return lines;
	}

	private void completeStructure(List<Line> lines, Line currentLine, Word currentWord, GuiText text) {
		boolean added = currentLine.attemptToAddWord(currentWord);
		if (!added) {
			lines.add(currentLine);
			currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
			currentLine.attemptToAddWord(currentWord);
		}
		lines.add(currentLine);
	}

	private TextMeshData createQuadVertices(GuiText text, List<Line> lines) {
		text.setNumberOfLines(lines.size());
        double cursorX = 0f;
        double cursorY = 0f;
        List<Float> vertices = new ArrayList<>();
		List<Float> textureCoords = new ArrayList<>();
		for (Line line : lines) {
			if (text.isCentered()) {
                cursorX = (line.getMaxLength() - line.getLineLength()) / 2;
            }
			for (Word word : line.getWords()) {
				for (Character letter : word.getCharacters()) {
                    addVerticesForCharacter(cursorX, cursorY, letter, text.getFontSize(), vertices);
                    addTexCoords(textureCoords, letter.getxTextureCoord(), letter.getyTextureCoord(),
							letter.getXMaxTextureCoord(), letter.getYMaxTextureCoord());
                    cursorX += letter.getxAdvance() * text.getFontSize();
                }
                cursorX += metaData.getSpaceWidth() * text.getFontSize();
            }
            cursorX = 0;
            cursorY += LINE_HEIGHT * text.getFontSize();
        }
        return new TextMeshData(listToArray(vertices), listToArray(textureCoords));
	}

	private void addVerticesForCharacter(double curserX, double curserY, Character character, double fontSize,
			List<Float> vertices) {
		double x = curserX + (character.getxOffset() * fontSize);
		double y = curserY + (character.getyOffset() * fontSize);
		double maxX = x + (character.getSizeX() * fontSize);
		double maxY = y + (character.getSizeY() * fontSize);
		double properX = (2 * x) - 1;
		double properY = (-2 * y) + 1;
		double properMaxX = (2 * maxX) - 1;
		double properMaxY = (-2 * maxY) + 1;
		addVertices(vertices, properX, properY, properMaxX, properMaxY);
	}

}
