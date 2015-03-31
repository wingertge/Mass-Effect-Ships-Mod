grammar CrazyGUI;

tag: '<' NAME attribute* '>' (tag | selfContainedTag)* '</' NAME '>';
NAME: [A-Za-z][A-Za-z0-9]*;
WS : [ \t\r\n]+ -> skip ;

selfContainedTag: '<' NAME attribute* '/>';

attribute: PREFIX? ((NAME '=' (STRINGVALUE | INTVALUE)) | NAME);
PREFIX: [xX] ':';

STRINGVALUE: '"' (.)+? '"';
INTVALUE: [0-9]+;