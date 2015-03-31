// Generated from E:/Dropbox/IdeaProjects/mods/MassEffectShips/src/main/antlr\CrazyGUI.g4 by ANTLR 4.5
package com.octagon.airships.antlr;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CrazyGUIParser}.
 */
public interface CrazyGUIListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CrazyGUIParser#tag}.
	 * @param ctx the parse tree
	 */
	void enterTag(@NotNull CrazyGUIParser.TagContext ctx);
	/**
	 * Exit a parse tree produced by {@link CrazyGUIParser#tag}.
	 * @param ctx the parse tree
	 */
	void exitTag(@NotNull CrazyGUIParser.TagContext ctx);
	/**
	 * Enter a parse tree produced by {@link CrazyGUIParser#selfContainedTag}.
	 * @param ctx the parse tree
	 */
	void enterSelfContainedTag(@NotNull CrazyGUIParser.SelfContainedTagContext ctx);
	/**
	 * Exit a parse tree produced by {@link CrazyGUIParser#selfContainedTag}.
	 * @param ctx the parse tree
	 */
	void exitSelfContainedTag(@NotNull CrazyGUIParser.SelfContainedTagContext ctx);
	/**
	 * Enter a parse tree produced by {@link CrazyGUIParser#attribute}.
	 * @param ctx the parse tree
	 */
	void enterAttribute(@NotNull CrazyGUIParser.AttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link CrazyGUIParser#attribute}.
	 * @param ctx the parse tree
	 */
	void exitAttribute(@NotNull CrazyGUIParser.AttributeContext ctx);
}