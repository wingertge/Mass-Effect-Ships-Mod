// Generated from E:/Dropbox/IdeaProjects/mods/MassEffectShips/src/main/antlr\CrazyGUI.g4 by ANTLR 4.5
package com.octagon.airships.antlr;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link CrazyGUIParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface CrazyGUIVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link CrazyGUIParser#tag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTag(@NotNull CrazyGUIParser.TagContext ctx);
	/**
	 * Visit a parse tree produced by {@link CrazyGUIParser#selfContainedTag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelfContainedTag(@NotNull CrazyGUIParser.SelfContainedTagContext ctx);
	/**
	 * Visit a parse tree produced by {@link CrazyGUIParser#attribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute(@NotNull CrazyGUIParser.AttributeContext ctx);
}