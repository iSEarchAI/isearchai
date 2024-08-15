package br.otimizes.isearchai.parser;

import br.otimizes.isearchai.learning.MLElement;
import com.github.javaparser.ParseResult;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.SourceRoot;

import java.io.IOException;
import java.util.List;

public class MLElementParser extends FrameworkParser {

    private String projectPath;
    private String clazzPath;
    private SourceRoot sourceRoot;
    private CompilationUnit parse;
    private ClassOrInterfaceDeclaration clazz;

    public static void main(String[] args) throws IOException {


        // Save the modified file
//            FileWriter writer = new FileWriter("path/to/your/Element.java");
//            writer.write(cu.toString());
//            writer.close();
        String project = "/home/wmfsystem/Documents/Doutorado/0_framework/code/OPLA-Tool/modules/architecture-representation";
        String clazz = "src/main/java/br/otimizes/oplatool/architecture/representation/Element.java";
        MLElementParser ps = new MLElementParser().projectClazz(project, clazz)
            .implement();
        ps.listAllClasses();
        System.out.println(ps);
        System.out.println();
    }

    public MLElementParser() {
        super(MLElement.class);
    }

    public MLElementParser projectClazz(String projectPath, String clazzPath) {
        this.projectPath = projectPath;
        this.clazzPath = clazzPath;
        sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(MLElementParser.class).resolve(projectPath));

        parse = sourceRoot.parse("", clazzPath);

        clazz = (ClassOrInterfaceDeclaration) parse.getTypes().stream().findFirst().get();
        return this;
    }

    public MLElementParser implement() {
        if (!hasImplementationClazz()) {
            ClassOrInterfaceType implementation = new ClassOrInterfaceType(null, implementationClazz);
            clazz.addImplementedType(implementation);

            parse.addImport(new ImportDeclaration(new Name(implementationPackage + "." + implementationClazz), false, false));
        }

        for (MethodDeclaration method : getClassOrInterface().getMethods()) {
            MethodDeclaration methodStub = getClonedMethod(method);
            boolean hasMethod = hasImplementationMethod(methodStub);
            if (!hasMethod) {
                methodStub.setBody(StaticJavaParser.parseBlock("{ throw new UnsupportedOperationException(\"Not implemented yet.\"); }"));
                clazz.addMember(methodStub);
            }
        }
        return this;
    }

    MethodDeclaration getClonedMethod(MethodDeclaration method) {
        MethodDeclaration methodStub = method.clone();

        NodeList<TypeParameter> typeParameters = methodStub.getTypeParameters();
        if (!typeParameters.isEmpty()) {
            TypeParameter typeParameter = typeParameters.get(0);
            ClassOrInterfaceType classOrInterfaceType = typeParameter.getTypeBound().get(0);
            methodStub.setTypeParameters(new NodeList<>());
            methodStub.setParameter(0, new Parameter(classOrInterfaceType, "element"));
        }
        return methodStub;
    }

    public boolean hasImplementationClazz() {
        return clazz.getImplementedTypes().stream().anyMatch(type -> type.getName().getId().equals(implementationClazz));
    }

    public boolean hasImplementationMethod(MethodDeclaration method) {
        return clazz.getMethodsByName(method.getNameAsString()).stream().anyMatch(impMethod -> impMethod.getSignature().asString().equals(method.getSignature().asString()));
    }

    public void listAllClasses() throws IOException {
        // Recursively parse all Java files in the modules directory
        List<ParseResult<CompilationUnit>> parseResults = sourceRoot.tryToParse("");

        // Iterate over the parsed results
        for (ParseResult<CompilationUnit> parseResult : parseResults) {
            parseResult.ifSuccessful(compilationUnit -> {
                // Get all types (classes, interfaces, enums) declared in the compilation unit
                for (TypeDeclaration<?> type : compilationUnit.getTypes()) {
                    // Print the fully qualified name of the type
                    System.out.println(type.getFullyQualifiedName().orElse("[Anonymous]"));
                }
            });
        }
    }

    @Override
    public String toString() {
        return this.parse.toString();
    }
}
