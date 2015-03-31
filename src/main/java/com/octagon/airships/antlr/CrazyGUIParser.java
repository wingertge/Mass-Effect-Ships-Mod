// Generated from E:/Dropbox/IdeaProjects/mods/MassEffectShips/src/main/antlr\CrazyGUI.g4 by ANTLR 4.5
package com.octagon.airships.antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CrazyGUIParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, NAME=6, WS=7, PREFIX=8, STRINGVALUE=9, 
		INTVALUE=10;
	public static final int
		RULE_tag = 0, RULE_selfContainedTag = 1, RULE_attribute = 2;
	public static final String[] ruleNames = {
		"tag", "selfContainedTag", "attribute"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'<'", "'>'", "'</'", "'/>'", "'='"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, "NAME", "WS", "PREFIX", "STRINGVALUE", 
		"INTVALUE"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override
	@NotNull
	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "CrazyGUI.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public CrazyGUIParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class TagContext extends ParserRuleContext {
		public List<TerminalNode> NAME() { return getTokens(CrazyGUIParser.NAME); }
		public TerminalNode NAME(int i) {
			return getToken(CrazyGUIParser.NAME, i);
		}
		public List<AttributeContext> attribute() {
			return getRuleContexts(AttributeContext.class);
		}
		public AttributeContext attribute(int i) {
			return getRuleContext(AttributeContext.class,i);
		}
		public List<TagContext> tag() {
			return getRuleContexts(TagContext.class);
		}
		public TagContext tag(int i) {
			return getRuleContext(TagContext.class,i);
		}
		public List<SelfContainedTagContext> selfContainedTag() {
			return getRuleContexts(SelfContainedTagContext.class);
		}
		public SelfContainedTagContext selfContainedTag(int i) {
			return getRuleContext(SelfContainedTagContext.class,i);
		}
		public TagContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tag; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CrazyGUIListener ) ((CrazyGUIListener)listener).enterTag(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CrazyGUIListener ) ((CrazyGUIListener)listener).exitTag(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CrazyGUIVisitor ) return ((CrazyGUIVisitor<? extends T>)visitor).visitTag(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TagContext tag() throws RecognitionException {
		TagContext _localctx = new TagContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_tag);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(6); 
			match(T__0);
			setState(7); 
			match(NAME);
			setState(11);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NAME || _la==PREFIX) {
				{
				{
				setState(8); 
				attribute();
				}
				}
				setState(13);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(14); 
			match(T__1);
			setState(19);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				setState(17);
				switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
				case 1:
					{
					setState(15); 
					tag();
					}
					break;
				case 2:
					{
					setState(16); 
					selfContainedTag();
					}
					break;
				}
				}
				setState(21);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(22); 
			match(T__2);
			setState(23); 
			match(NAME);
			setState(24); 
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SelfContainedTagContext extends ParserRuleContext {
		public TerminalNode NAME() { return getToken(CrazyGUIParser.NAME, 0); }
		public List<AttributeContext> attribute() {
			return getRuleContexts(AttributeContext.class);
		}
		public AttributeContext attribute(int i) {
			return getRuleContext(AttributeContext.class,i);
		}
		public SelfContainedTagContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selfContainedTag; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CrazyGUIListener ) ((CrazyGUIListener)listener).enterSelfContainedTag(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CrazyGUIListener ) ((CrazyGUIListener)listener).exitSelfContainedTag(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CrazyGUIVisitor ) return ((CrazyGUIVisitor<? extends T>)visitor).visitSelfContainedTag(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelfContainedTagContext selfContainedTag() throws RecognitionException {
		SelfContainedTagContext _localctx = new SelfContainedTagContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_selfContainedTag);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(26); 
			match(T__0);
			setState(27); 
			match(NAME);
			setState(31);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NAME || _la==PREFIX) {
				{
				{
				setState(28); 
				attribute();
				}
				}
				setState(33);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(34); 
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AttributeContext extends ParserRuleContext {
		public TerminalNode NAME() { return getToken(CrazyGUIParser.NAME, 0); }
		public TerminalNode PREFIX() { return getToken(CrazyGUIParser.PREFIX, 0); }
		public TerminalNode STRINGVALUE() { return getToken(CrazyGUIParser.STRINGVALUE, 0); }
		public TerminalNode INTVALUE() { return getToken(CrazyGUIParser.INTVALUE, 0); }
		public AttributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attribute; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CrazyGUIListener ) ((CrazyGUIListener)listener).enterAttribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CrazyGUIListener ) ((CrazyGUIListener)listener).exitAttribute(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CrazyGUIVisitor ) return ((CrazyGUIVisitor<? extends T>)visitor).visitAttribute(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeContext attribute() throws RecognitionException {
		AttributeContext _localctx = new AttributeContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_attribute);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(37);
			_la = _input.LA(1);
			if (_la==PREFIX) {
				{
				setState(36); 
				match(PREFIX);
				}
			}

			setState(43);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
				{
				setState(39); 
				match(NAME);
				setState(40); 
				match(T__4);
				setState(41);
				_la = _input.LA(1);
				if ( !(_la==STRINGVALUE || _la==INTVALUE) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				}
				}
				break;
			case 2:
				{
				setState(42); 
				match(NAME);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\f\60\4\2\t\2\4\3"+
		"\t\3\4\4\t\4\3\2\3\2\3\2\7\2\f\n\2\f\2\16\2\17\13\2\3\2\3\2\3\2\7\2\24"+
		"\n\2\f\2\16\2\27\13\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\7\3 \n\3\f\3\16\3#\13"+
		"\3\3\3\3\3\3\4\5\4(\n\4\3\4\3\4\3\4\3\4\5\4.\n\4\3\4\2\2\5\2\4\6\2\3\3"+
		"\2\13\f\62\2\b\3\2\2\2\4\34\3\2\2\2\6\'\3\2\2\2\b\t\7\3\2\2\t\r\7\b\2"+
		"\2\n\f\5\6\4\2\13\n\3\2\2\2\f\17\3\2\2\2\r\13\3\2\2\2\r\16\3\2\2\2\16"+
		"\20\3\2\2\2\17\r\3\2\2\2\20\25\7\4\2\2\21\24\5\2\2\2\22\24\5\4\3\2\23"+
		"\21\3\2\2\2\23\22\3\2\2\2\24\27\3\2\2\2\25\23\3\2\2\2\25\26\3\2\2\2\26"+
		"\30\3\2\2\2\27\25\3\2\2\2\30\31\7\5\2\2\31\32\7\b\2\2\32\33\7\4\2\2\33"+
		"\3\3\2\2\2\34\35\7\3\2\2\35!\7\b\2\2\36 \5\6\4\2\37\36\3\2\2\2 #\3\2\2"+
		"\2!\37\3\2\2\2!\"\3\2\2\2\"$\3\2\2\2#!\3\2\2\2$%\7\6\2\2%\5\3\2\2\2&("+
		"\7\n\2\2\'&\3\2\2\2\'(\3\2\2\2(-\3\2\2\2)*\7\b\2\2*+\7\7\2\2+.\t\2\2\2"+
		",.\7\b\2\2-)\3\2\2\2-,\3\2\2\2.\7\3\2\2\2\b\r\23\25!\'-";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}