package org.kbalazs.common.native_build_module.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.kbalazs.common.io_module.services.FileService;
import org.kbalazs.common.native_build_module.exceptions.RuntimeHintsReflection;
import org.kbalazs.common.native_build_module.value_objects.ReflectionClassList;
import org.kbalazs.common.templating_module.services.MustacheService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RuntimeHintsReflectionGenerator
{
    private final MustacheService mustacheService;
    private final FileService fileService;

    public void generate(
        @NonNull String destinationFile,
        @NonNull List<String> packageNames,
        @NonNull List<Class<?>> classes
    )
        throws IOException, RuntimeHintsReflection
    {
        List<String> classNames = getClassNamesInPackages(packageNames, classes);

        var newCode = mustacheService.execute(
            "templates/RuntimeHintsReflectionTemplate.mustache",
            new ReflectionClassList(classNames)
        );

        String oldCode = fileService.readString(destinationFile);
        fileService.saveString(destinationFile, newCode);

        if (!oldCode.equals(newCode))
        {
            throw new RuntimeHintsReflection("New ReflectionConfiguration generated, please restart the application!");
        }
    }

    private static List<String> getClassNamesInPackages(
        @NonNull List<String> packageNames,
        @NonNull List<Class<?>> classes
    )
    {
        List<String> fqns = new ArrayList<>();

        classes.stream().map(c -> c.getName() + ".class").forEach(fqns::add);

        packageNames.stream()
            .map(RuntimeHintsReflectionGenerator::getClassNamesInPackage)
            .flatMap(List::stream)
            .forEach(fqns::add);

        return fqns;
    }

    private static List<String> getClassNamesInPackage(@NonNull String packageName)
    {
        String path = "src/main/java/" + packageName.replace('.', '/');
        List<String> classNames = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(Paths.get(path)))
        {
            paths
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".java"))
                .forEach(p -> classNames.add(
                    packageName + "." + p.getFileName().toString().replace(".java", ".class")
                ));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return classNames;
    }
}
