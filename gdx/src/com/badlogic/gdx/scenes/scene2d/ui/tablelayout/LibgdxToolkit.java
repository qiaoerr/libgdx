/*******************************************************************************
 * Copyright (c) 2011, Nathan Sweet <nathan.sweet@gmail.com>
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/

package com.badlogic.gdx.scenes.scene2d.ui.tablelayout;

import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Layout;
import com.badlogic.gdx.scenes.scene2d.actors.Button;
import com.badlogic.gdx.scenes.scene2d.actors.Image;
import com.badlogic.gdx.scenes.scene2d.actors.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Array;

/** @author Nathan Sweet */
public class LibgdxToolkit extends Toolkit<Actor, Table, TableLayout> {
	static {
		addClassPrefix("com.badlogic.gdx.scenes.scene2d.");
		addClassPrefix("com.badlogic.gdx.scenes.scene2d.actors.");
	}

	static public LibgdxToolkit instance = new LibgdxToolkit();
	static public BitmapFont defaultFont;
	static private HashMap<String, BitmapFont> fonts = new HashMap();

	public Actor wrap (Object object) {
		if (object instanceof String) {
			if (defaultFont == null) throw new IllegalStateException("No default font has been set.");
			return new Label(null, defaultFont, (String)object);
		}
		if (object == null) return new Group();
		return super.wrap(object);
	}

	public Actor newWidget (TableLayout layout, String className) {
		if (layout.atlas != null) {
			AtlasRegion region = layout.atlas.findRegion(className);
			if (region != null) return new Image(className, region);
		}
		if (className.equals("button")) return new Button(null);
		return super.newWidget(layout, className);
	}

	public TableLayout getLayout (Table table) {
		return table.getTableLayout();
	}

	public Actor newStack () {
		return new Stack();
	}

	public void setProperty (TableLayout layout, Actor object, String name, List<String> values) {
		if (object instanceof Label) {
			Label label = ((Label)object);
			String value = values.get(0);
			if (name.equals("font")) {
				label.setFont(getFont(value));
				return;
			}
		}

		super.setProperty(layout, object, name, values);
	}

	public void addChild (Actor parent, Actor child, String layoutString) {
		if (child.parent != null) child.remove();
		((Group)parent).addActor(child);
	}

	public void removeChild (Actor parent, Actor child) {
		((Group)parent).removeActor(child);
	}

	public int getMinWidth (Actor actor) {
		if (actor instanceof Layout) return (int)((Layout)actor).getPrefWidth();
		return (int)actor.width;
	}

	public int getMinHeight (Actor actor) {
		if (actor instanceof Layout) return (int)((Layout)actor).getPrefHeight();
		return (int)actor.height;
	}

	public int getPrefWidth (Actor actor) {
		if (actor instanceof Layout) return (int)((Layout)actor).getPrefWidth();
		return (int)actor.width;
	}

	public int getPrefHeight (Actor actor) {
		if (actor instanceof Layout) return (int)((Layout)actor).getPrefHeight();
		return (int)actor.height;
	}

	public int getMaxWidth (Actor actor) {
		return 0;
	}

	public int getMaxHeight (Actor actor) {
		return 0;
	}

	public void clearDebugRectangles (TableLayout layout) {
		if (layout.debugRects != null) layout.debugRects.clear();
	}

	public void addDebugRectangle (TableLayout layout, int type, int x, int y, int w, int h) {
		if (layout.debugRects == null) layout.debugRects = new Array();
		layout.debugRects.add(new DebugRect(type, x, y, w, h));
	}

	/** Sets the name of a font. */
	static public void registerFont (String name, BitmapFont font) {
		fonts.put(name, font);
		if (defaultFont == null) defaultFont = font;
	}

	static public BitmapFont getFont (String name) {
		BitmapFont font = fonts.get(name);
		if (font == null) throw new IllegalArgumentException("Font not found: " + name);
		return font;
	}

	static class DebugRect extends Rectangle {
		final int type;

		public DebugRect (int type, int x, int y, int width, int height) {
			super(x, y, width, height);
			this.type = type;
		}
	}
}
