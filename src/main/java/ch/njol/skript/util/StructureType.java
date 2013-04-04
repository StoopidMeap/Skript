/*
 *   This file is part of Skript.
 *
 *  Skript is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Skript is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 * Copyright 2011-2013 Peter Güttinger
 * 
 */

package ch.njol.skript.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.block.Block;

import ch.njol.skript.localization.Language;
import ch.njol.skript.localization.LanguageChangeListener;
import ch.njol.skript.localization.Noun;

/**
 * @author Peter Güttinger
 */
public enum StructureType {
	TREE(TreeType.TREE, TreeType.BIG_TREE, TreeType.REDWOOD, TreeType.TALL_REDWOOD, TreeType.SMALL_JUNGLE, TreeType.JUNGLE, TreeType.SWAMP),
	
	REGULAR(TreeType.TREE, TreeType.BIG_TREE), SMALL_REGULAR(TreeType.TREE), BIG_REGULAR(TreeType.BIG_TREE),
	REDWOOD(TreeType.REDWOOD, TreeType.TALL_REDWOOD), SMALL_REDWOOD(TreeType.REDWOOD), BIG_REDWOOD(TreeType.TALL_REDWOOD),
	JUNGLE(TreeType.SMALL_JUNGLE, TreeType.JUNGLE), SMALL_JUNGLE(TreeType.SMALL_JUNGLE), BIG_JUNGLE(TreeType.JUNGLE),
	JUNGLE_BUSH(TreeType.JUNGLE_BUSH),
	SWAMP(TreeType.SWAMP),
	
	MUSHROOM(TreeType.RED_MUSHROOM, TreeType.BROWN_MUSHROOM),
	RED_MUSHROOM(TreeType.RED_MUSHROOM), BROWN_MUSHROOM(TreeType.BROWN_MUSHROOM), ;
	
	private Noun name;
	private final TreeType[] types;
	
	private StructureType(final TreeType... types) {
		this.types = types;
		name = new Noun("tree types." + name() + ".name");
	}
	
	public void grow(final Location loc) {
		loc.getWorld().generateTree(loc, Utils.random(types));
	}
	
	public void grow(final Block b) {
		b.getWorld().generateTree(b.getLocation(), Utils.random(types));
	}
	
	public TreeType[] getTypes() {
		return types;
	}
	
	@Override
	public String toString() {
		return name.toString();
	}
	
	public Noun getName() {
		return name;
	}
	
	public boolean is(final TreeType type) {
		return Utils.contains(types, type);
	}
	
	/**
	 * lazy
	 */
	private final static Map<Pattern, StructureType> parseMap = new HashMap<Pattern, StructureType>();
	
	static {
		Language.addListener(new LanguageChangeListener() {
			@Override
			public void onLanguageChange() {
				parseMap.clear();
			}
		});
	}
	
	public static StructureType fromName(String s) {
		if (parseMap.isEmpty()) {
			for (final StructureType t : values()) {
				final String pattern = Language.get("tree types." + t.name() + ".pattern");
				parseMap.put(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE), t);
			}
		}
		s = s.toLowerCase();
		for (final Entry<Pattern, StructureType> e : parseMap.entrySet()) {
			if (e.getKey().matcher(s).matches())
				return e.getValue();
		}
		return null;
	}
	
}