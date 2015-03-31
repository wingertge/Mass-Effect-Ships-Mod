package com.octagon.airships.antlr;

import com.octagon.airships.util.LogHelper;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.HashMap;
import java.util.Map;

public class GuiVisitor extends CrazyGUIBaseVisitor<String> {
    private final String name;
    private final String componentPackage;
    private final String guiPackage;
    private final String generatedClassesPackage;
    private final String tileEntityPackage;
    private final String guiSlotsPackage;

    private String guiClass;
    private String containerClass;

    private int nameCounter = 0;

    public GuiVisitor(String name, String guiPackage, String generatedClassesPackage, String tileEntityPackage) {
        this.name = name;
        this.componentPackage = guiPackage + ".components";
        this.guiSlotsPackage = guiPackage + ".slots";
        this.guiPackage = guiPackage;
        this.generatedClassesPackage = generatedClassesPackage;
        this.tileEntityPackage = tileEntityPackage;
    }

    @Override
    public String visitAttribute(@NotNull CrazyGUIParser.AttributeContext ctx) {
        if(ctx.INTVALUE() != null)
            return "i" + (ctx.PREFIX() != null ? ctx.PREFIX().getText().toLowerCase() : "") + ctx.NAME().getText() + "=" + ctx.INTVALUE().getText();
        else {
            if(ctx.STRINGVALUE().getText().replace("\"", "").startsWith("{") && ctx.STRINGVALUE().getText().replace("\"", "").endsWith("}"))
                return "x" + (ctx.PREFIX() != null ? ctx.PREFIX().getText().toLowerCase() : "") + ctx.NAME().getText() + "=" + ctx.STRINGVALUE().getText().replace("\"", "");
            else if(ctx.STRINGVALUE().getText().toLowerCase().contains("true") || ctx.STRINGVALUE().getText().toLowerCase().contains("false"))
                return "b" + (ctx.PREFIX() != null ? ctx.PREFIX().getText().toLowerCase() : "") + ctx.NAME().getText() + "=" + ctx.STRINGVALUE().getText().replace("\"", "");
            else
                return "s" + (ctx.PREFIX() != null ? ctx.PREFIX().getText().toLowerCase() : "") + ctx.NAME().getText() + "=" + ctx.STRINGVALUE().getText();
        }
    }

    @Override
    public String visitSelfContainedTag(@NotNull CrazyGUIParser.SelfContainedTagContext ctx) {
        String attributesString = super.visitSelfContainedTag(ctx);
        String[] attributesStrings = attributesString.split("~");
        Map<String, String> stringAttributes = new HashMap<>();
        Map<String, Integer> intAttributes = new HashMap<>();
        Map<String, Boolean> booleanAttributes = new HashMap<>();
        Map<String, String> executableCode = new HashMap<>();
        for(String s : attributesStrings) {
            if(s.isEmpty() || (!s.startsWith("s") && !s.startsWith("i") && !s.startsWith("b") && !s.startsWith("x"))) continue;
            if(s.startsWith("i")) intAttributes.put(decapitalize(s.split("=")[0].substring(1)), Integer.parseInt(s.split("=")[1]));
            else if(s.startsWith("s")) stringAttributes.put(decapitalize(s.split("=")[0].substring(1)), s.split("=")[1]);
            else if(s.startsWith("b")) booleanAttributes.put(decapitalize(s.split("=")[0].substring(1)), Boolean.parseBoolean(s.split("=")[1]));
            else executableCode.put(decapitalize(s.split("=")[0].substring(1)), s.split("=")[1].replace("{", "").replace("}", ""));
        }

        if(ctx.NAME().getText().equalsIgnoreCase("partial")) {
            if(!stringAttributes.containsKey("name")) {
                LogHelper.error("Syntax error in GUI " + name + ", can't use partial without a name attribute!");
                return "";
            }
            String fieldName;
            if(stringAttributes.containsKey("x:name")) fieldName = stringAttributes.get("x:name").replace("\"", "");
            else {
                fieldName = "partial" + nameCounter;
                nameCounter++;
            }

            String className = "Gui" + capitalize(stringAttributes.get("name").replace("\"", ""));
            String result = "fprotected " + className + " " + fieldName + ";\n~" +
                    "c" + fieldName + " = new " + className + "(new " + className + ".Container(container.getPlayerInventory(), container.getOwner()));\n";
            result += stringAttributes.entrySet().stream().filter(a -> !a.getKey().startsWith("x:") && !a.getKey().equalsIgnoreCase("name"))
                    .map(a -> fieldName + ".set" + capitalize(a.getKey()) + "(" + a.getValue() + ");\n").reduce((a, b) -> a + b).orElse("");
            result += intAttributes.entrySet().stream().filter(a -> !a.getKey().startsWith("x:") && !a.getKey().equalsIgnoreCase("name"))
                    .map(a -> fieldName + ".set" + capitalize(a.getKey()) + "(" + a.getValue() + ");\n").reduce((a, b) -> a + b).orElse("");
            result += booleanAttributes.entrySet().stream().filter(a -> !a.getKey().startsWith("x:") && !a.getKey().equalsIgnoreCase("name"))
                    .map(a -> fieldName + ".set" + capitalize(a.getKey()) + "(" + a.getValue() + ");\n").reduce((a, b) -> a + b).orElse("");
            result += executableCode.entrySet().stream().filter(a -> !a.getKey().startsWith("x:") && !a.getKey().equalsIgnoreCase("name"))
                    .map(a -> fieldName + ".set" + capitalize(a.getKey()) + "(" + a.getValue() + ");\n").reduce((a, b) -> a + b).orElse("");
            result += fieldName + ".init(root);\n\n";

            return result;
        } else if(ctx.NAME().getText().equalsIgnoreCase("slot")) {
            if(!intAttributes.containsKey("id")) {
                LogHelper.error("Syntax error in GUI " + name + ", can't use slot without an id attribute!");
                return "";
            }
            int xPos = 0;
            int yPos = 0;
            if(intAttributes.containsKey("xPos")) xPos = intAttributes.get("xPos");
            if(intAttributes.containsKey("yPos")) yPos = intAttributes.get("yPos");
            int id = intAttributes.get("id");
            String className = "Slot";
            if(stringAttributes.containsKey("x:class")) className += stringAttributes.get("x:class").replace("\"", "");

            return String.format("laddSlotToContainer(new %s(owner.getInventory(), %d, %d, %d));\n", className, id, xPos, yPos);
        } else if(ctx.NAME().getText().equalsIgnoreCase("playerInventory")) {
            int xPos = 8;
            int yPos = 84;
            if(intAttributes.containsKey("xPos")) xPos = intAttributes.get("xPos");
            if(intAttributes.containsKey("yPos")) yPos = intAttributes.get("yPos");
            return String.format("laddPlayerInventorySlots(%d, %d);\n", xPos, yPos);
        } else {
            String className = "GuiComponent" + ctx.NAME().getText();
            String fieldName;
            if(stringAttributes.containsKey("x:name"))
                fieldName = stringAttributes.get("x:name").replace("\"", "");
            else {
                fieldName = "element" + nameCounter;
                nameCounter++;
            }

            int xPos = 0;
            int yPos = 0;
            if(intAttributes.containsKey("xPos")) xPos = intAttributes.get("xPos");
            if(intAttributes.containsKey("yPos")) yPos = intAttributes.get("yPos");

            String result = "fprotected " + className + " " + fieldName + ";\n~" +
                            "c" + fieldName + " = new " + className + "(" + xPos + ", " + yPos + ");\n";
            result += stringAttributes.entrySet().stream().filter(a -> !a.getKey().startsWith("x:") && !a.getKey().equalsIgnoreCase("xPos") && !a.getKey().equalsIgnoreCase("yPos"))
                    .map(a -> fieldName + ".set" + capitalize(a.getKey()) + "(" + a.getValue() + ");\n").reduce((a, b) -> a + b).orElse("");
            result += intAttributes.entrySet().stream().filter(a -> !a.getKey().startsWith("x:") && !a.getKey().equalsIgnoreCase("xPos") && !a.getKey().equalsIgnoreCase("yPos"))
                    .map(a -> fieldName + ".set" + capitalize(a.getKey()) + "(" + a.getValue() + ");\n").reduce((a, b) -> a + b).orElse("");
            result += booleanAttributes.entrySet().stream().filter(a -> !a.getKey().startsWith("x:") && !a.getKey().equalsIgnoreCase("xPos") && !a.getKey().equalsIgnoreCase("yPos"))
                    .map(a -> fieldName + ".set" + capitalize(a.getKey()) + "(" + a.getValue() + ");\n").reduce((a, b) -> a + b).orElse("");
            result += executableCode.entrySet().stream().filter(a -> !a.getKey().startsWith("x:") && !a.getKey().equalsIgnoreCase("xPos") && !a.getKey().equalsIgnoreCase("yPos"))
                    .map(a -> fieldName + ".set" + capitalize(a.getKey()) + "(" + a.getValue() + ");\n").reduce((a, b) -> a + b).orElse("");
            return result + "root.addComponent(" + fieldName + ");\n\n";
        }
    }

    @Override
    public String visitTag(@NotNull CrazyGUIParser.TagContext ctx) {
        if(ctx.NAME(0).getText().equalsIgnoreCase("root")) {
            String className = capitalize(name);
            String attributesString = super.visitTag(ctx);
            String[] attributesStrings = attributesString.split("~");
            Map<String, String> stringAttributes = new HashMap<>();
            Map<String, Integer> intAttributes = new HashMap<>();
            Map<String, Boolean> booleanAttributes = new HashMap<>();
            Map<String, String> executableCode = new HashMap<>();
            for(String s : attributesStrings) {
                if(s.isEmpty() || (!s.startsWith("s") && !s.startsWith("i"))) continue;
                if(s.startsWith("i")) intAttributes.put(decapitalize(s.split("=")[0].substring(1)), Integer.parseInt(s.split("=")[1]));
                else if(s.startsWith("s")) stringAttributes.put(decapitalize(s.split("=")[0].substring(1)), s.split("=")[1]);
                else if(s.startsWith("b")) booleanAttributes.put(decapitalize(s.split("=")[0].substring(1)), Boolean.parseBoolean(s.split("=")[1]));
                else executableCode.put(decapitalize(s.split("=")[0].substring(1)), s.split("=")[1].replace("{", "").replace("}", ""));
            }

            int width = intAttributes.containsKey("width") ? intAttributes.get("width") : 176;
            int height = intAttributes.containsKey("height") ? intAttributes.get("height") : 166;

            String tileEntityClass = "TileEntity" + className;

            String init = "public void init(Container container, BaseComposite root) {\n" +
                    tileEntityClass + " tileEntity = container.getOwner();\n";
            for(String s : attributesStrings) {
                if(s.isEmpty() || !s.startsWith("c")) continue;
                init += s.substring(1);
            }
            init += "postInit(root);\n";
            init += "}\n";
            if(stringAttributes.containsKey("tileEntity")) tileEntityClass = stringAttributes.get("tileEntity").replace("\"", "");

            containerClass = "public class Container extends CrazyContainer<" + tileEntityClass + "> {\n" +
                    "public Container(IInventory playerInventory, " + tileEntityClass + " owner) {\n" +
                    "super(playerInventory, owner.getInventory(), owner);\n";
            for(String s : attributesStrings) {
                if(s.startsWith("l")) containerClass += s.substring(1);
            }
            containerClass += "}\n}\n";

            guiClass = "package " + generatedClassesPackage + ";\n\n";
            guiClass +=
                    "import net.minecraft.inventory.IInventory;\n" +
                    "import net.minecraft.inventory.Slot;\n" +
                    "import " + guiPackage + ".*;\n" +
                    "import " + guiSlotsPackage + ".*;\n" +
                    "import " + componentPackage + ".*;\n" +
                    "import " + tileEntityPackage + ".*;\n" +
                    "import openmods.gui.component.BaseComposite;\n\n";
            guiClass += "public class AbstractGui" + className + " extends GuiAirshipsContainer<AbstractGui" + className + ".Container> {\n";
            for(String s : attributesStrings) {
                if(s.isEmpty() || !s.startsWith("f")) continue;
                guiClass += s.substring(1);
            }
            guiClass += "\npublic AbstractGui" + className + "(Container container) {\n";
            guiClass += String.format("super(container, %s, %s, \"%s\");\n", width, height, name);
            guiClass += "init(container, createRoot());\n";
            guiClass += "}\n\n";
            guiClass += init + "\n";
            guiClass += "public void postInit(BaseComposite root) {}\n\n";
            guiClass += containerClass;
            guiClass += "}\n";
        }
        return guiClass;
    }

    @Override
    protected String aggregateResult(String aggregate, String nextResult) {
        return aggregate + '~' + nextResult;
    }

    private String capitalize(String source) {
        return Character.toUpperCase(source.charAt(0)) + source.substring(1);
    }

    private String decapitalize(String source) {
        return Character.toLowerCase(source.charAt(0)) + source.substring(1);
    }
}
