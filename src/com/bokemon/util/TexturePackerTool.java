package com.bokemon.util;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class TexturePackerTool {
	public static void main(String[] args) {
		TexturePacker.process(
				"res/graphics_unpacked/tiles",
				"res/graphics_packed/tiles/",
				"tile_textures");
		TexturePacker.process(
				"res/graphics_unpacked/battle",
				"res/graphics_packed/battle/",
				"battle_textures");
		TexturePacker.process(
				"res/graphics_unpacked/ui",
				"res/graphics_packed/ui/",
				"ui_textures");
//		TexturePacker.process("res/graphics_unpacked/battle/pokemon_sprites", 
//				"res/graphics_packed/battle/pokemon_sprites", 
//				"front_textures");
//		TexturePacker.process("res/graphics_unpacked/battle/pokemon_back_sprites", 
//				"res/graphics_packed/battle/pokemon_back_sprites", 
//				"back_textures");
	}
}
