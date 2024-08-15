package br.otimizes.isearchai.parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.SourceRoot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

public class Adapter {
    public static void main(String[] args) throws IOException {
        String url = "/home/wmfsystem/Documents/Doutorado/0_framework/code/OPLA-Tool";

        SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(Adapter.class).resolve(url));

        CompilationUnit parse = sourceRoot.parse("", "modules/architecture-representation/src/main/java/br/otimizes/oplatool/architecture/representation/Element.java");

        ClassOrInterfaceDeclaration clazz = (ClassOrInterfaceDeclaration) parse.getTypes().stream().findFirst().get();
        ClassOrInterfaceType serializable = new ClassOrInterfaceType(null, "Serializable");
        clazz.addImplementedType(serializable);

        parse.addImport(new ImportDeclaration(new Name("Serializable"), false, false));
        System.out.println();

        // Save the modified file
//            FileWriter writer = new FileWriter("path/to/your/Element.java");
//            writer.write(cu.toString());
//            writer.close();
    }
}
