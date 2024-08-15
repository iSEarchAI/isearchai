package br.otimizes.isearchai.parser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.SourceRoot;

import java.io.IOException;
import java.util.Optional;

public class FrameworkParser {

    protected String implementationClazz;
    protected String implementationPackage;
    protected Class clazz;

    public FrameworkParser(String implementationPackage, String implementationClazz) {
        this.implementationClazz = implementationClazz;
        this.implementationPackage = implementationPackage;
    }

    public FrameworkParser(Class clazz) {
        this.clazz = clazz;
        this.implementationClazz = clazz.getSimpleName();
        this.implementationPackage = clazz.getPackage().getName();
    }

    public ClassOrInterfaceDeclaration getClassOrInterface() {
        SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(MLElementParser.class).resolve("./"));
        Optional<CompilationUnit> externalParseResult;
        try {
            externalParseResult = sourceRoot.tryToParse("",
                "src/main/java/" + implementationPackage.replace('.', '/') + "/" + implementationClazz + ".java").getResult();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (externalParseResult.isPresent()) {
            CompilationUnit externalCU = externalParseResult.get();
            ClassOrInterfaceDeclaration externalInterface = (ClassOrInterfaceDeclaration) externalCU.getTypes().stream().findFirst().get();
            return externalInterface;
        }
        return null;
    }


    public String getImplementationClazz() {
        return implementationClazz;
    }

    public void setImplementationClazz(String implementationClazz) {
        this.implementationClazz = implementationClazz;
    }

    public String getImplementationPackage() {
        return implementationPackage;
    }

    public void setImplementationPackage(String implementationPackage) {
        this.implementationPackage = implementationPackage;
    }
}
