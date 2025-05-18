import com.palantir.javapoet.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.AnnotatedConstruct
import javax.lang.model.SourceVersion
import javax.lang.model.element.*
import javax.lang.model.type.TypeMirror

class GenerateInterfaceProcessor : AbstractProcessor() {

    override fun getSupportedAnnotationTypes(): Set<String> =
        setOf(GenerateInterface::class.qualifiedName!!)

    override fun getSupportedSourceVersion(): SourceVersion =
        SourceVersion.latestSupported()

    override fun process(
        annotations: Set<TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        generateInterfaces(roundEnv)
        return true
    }

    private fun generateInterfaces(roundEnv: RoundEnvironment) {
        roundEnv
            .getElementsAnnotatedWith(
                GenerateInterface::class.java
            )
            .filterIsInstance<TypeElement>()
            .forEach(::generateInterface)
    }

    private fun generateInterface(annotatedClass: TypeElement) {
        val interfaceName = annotatedClass
            .getAnnotation(GenerateInterface::class.java)
            .name
        val interfacePackage = processingEnv
            .elementUtils
            .getPackageOf(annotatedClass)
            .qualifiedName
            .toString()

        val publicMethods = annotatedClass.enclosedElements
            .filter { it.kind == ElementKind.METHOD }
            .filter { Modifier.PUBLIC in it.modifiers }
            .filterIsInstance<ExecutableElement>()

        buildInterfaceFile(interfacePackage, interfaceName, publicMethods)
            .writeTo(processingEnv.filer)
    }

    private fun buildInterfaceFile(
        interfacePackage: String,
        interfaceName: String,
        publicMethods: List<ExecutableElement>
    ): JavaFile = JavaFile.builder(
        interfacePackage,
        buildInterface(interfaceName, publicMethods)
    ).build()

    private fun buildInterface(
        interfaceName: String,
        publicMethods: List<ExecutableElement>
    ): TypeSpec = TypeSpec
        .interfaceBuilder(interfaceName)
        .addMethods(publicMethods.map(::buildInterfaceMethod))
        .build()

    private fun buildInterfaceMethod(
        method: ExecutableElement
    ): MethodSpec = MethodSpec
        .methodBuilder(method.simpleName.toString())
        .addModifiers(method.modifiers)
        .addModifiers(Modifier.ABSTRACT)
        .addParameters(
            method.parameters
                .map(::buildInterfaceMethodParameter)
        )
        .returns(method.returnType.toTypeSpec())
        .addAnnotations(method.getAnnotationSpecs())
        .build()

    private fun buildInterfaceMethodParameter(
        variableElement: VariableElement
    ): ParameterSpec = ParameterSpec
        .builder(
            variableElement.asType().toTypeSpec(),
            variableElement.simpleName.toString()
        )
        .addAnnotations(variableElement.getAnnotationSpecs())
        .build()
}

private fun TypeMirror.toTypeSpec() = TypeName.get(this)
    .annotated(this.getAnnotationSpecs())

private fun AnnotatedConstruct.getAnnotationSpecs() =
    annotationMirrors.map(AnnotationSpec::get)