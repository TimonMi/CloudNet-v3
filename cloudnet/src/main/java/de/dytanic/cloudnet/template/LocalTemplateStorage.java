package de.dytanic.cloudnet.template;

import com.google.common.base.Preconditions;
import de.dytanic.cloudnet.common.io.FileUtils;
import de.dytanic.cloudnet.driver.service.ServiceTemplate;
import de.dytanic.cloudnet.driver.template.DefaultSyncTemplateStorage;
import de.dytanic.cloudnet.driver.template.FileInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.function.Predicate;
import java.util.zip.ZipInputStream;

public final class LocalTemplateStorage extends DefaultSyncTemplateStorage {

    private final File storageDirectory;

    public LocalTemplateStorage(File storageDirectory) {
        this.storageDirectory = storageDirectory;
        this.storageDirectory.mkdirs();
    }

    @Override
    @Deprecated
    public boolean deploy(@NotNull byte[] zipInput, @NotNull ServiceTemplate target) {
        Preconditions.checkNotNull(target);

        try {
            FileUtils.extract(zipInput, new File(this.storageDirectory, target.getTemplatePath()).toPath());
            return true;
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deploy(@NotNull File directory, @NotNull ServiceTemplate target, @Nullable Predicate<File> fileFilter) {
        Preconditions.checkNotNull(directory);
        Preconditions.checkNotNull(target);

        if (!directory.isDirectory()) {
            return false;
        }

        try {
            FileUtils.copyFilesToDirectory(directory, new File(this.storageDirectory, target.getTemplatePath()), fileFilter);
            return true;
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deploy(@NotNull ZipInputStream inputStream, @NotNull ServiceTemplate target) {
        Preconditions.checkNotNull(inputStream);
        Preconditions.checkNotNull(target);

        try {
            FileUtils.extract0(inputStream, new File(this.storageDirectory, target.getTemplatePath()).toPath());
            return true;
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deploy(@NotNull Path[] paths, @NotNull ServiceTemplate target) {
        Preconditions.checkNotNull(paths);
        Preconditions.checkNotNull(target);

        return this.deploy(Arrays.stream(paths).map(Path::toFile).toArray(File[]::new), target);
    }

    @Override
    public boolean deploy(@NotNull File[] files, @NotNull ServiceTemplate target) {
        Preconditions.checkNotNull(files);
        Preconditions.checkNotNull(target);

        byte[] buffer = new byte[32768];

        File templateDirectory = new File(this.storageDirectory, target.getTemplatePath());

        boolean value = true;

        for (File entry : files) {
            try {
                if (entry.isDirectory()) {
                    FileUtils.copyFilesToDirectory(entry, new File(templateDirectory, entry.getName()), buffer);
                } else {
                    FileUtils.copy(entry, new File(templateDirectory, entry.getName()), buffer);
                }

            } catch (Exception ex) {
                ex.printStackTrace();

                value = false;
            }
        }

        return value;
    }

    @Override
    public boolean copy(@NotNull ServiceTemplate template, @NotNull File directory) {
        Preconditions.checkNotNull(template);
        Preconditions.checkNotNull(directory);

        byte[] buffer = new byte[32768];
        File templateDirectory = new File(this.storageDirectory, template.getTemplatePath());
        boolean value = true;

        try {
            FileUtils.copyFilesToDirectory(templateDirectory, directory, buffer);
        } catch (IOException e) {
            e.printStackTrace();
            value = false;
        }

        return value;
    }

    @Override
    public boolean copy(@NotNull ServiceTemplate template, @NotNull Path directory) {
        Preconditions.checkNotNull(template);
        Preconditions.checkNotNull(directory);

        return this.copy(template, directory.toFile());
    }

    @Override
    public boolean copy(@NotNull ServiceTemplate template, @NotNull File[] directories) {
        Preconditions.checkNotNull(directories);
        boolean value = true;

        for (File directory : directories) {
            if (!this.copy(template, directory)) {
                value = false;
            }
        }

        return value;
    }

    @Override
    public boolean copy(@NotNull ServiceTemplate template, @NotNull Path[] directories) {
        Preconditions.checkNotNull(directories);
        boolean value = true;

        for (Path path : directories) {
            if (!this.copy(template, path)) {
                value = false;
            }
        }

        return value;
    }

    @Override
    @Deprecated
    public byte[] toZipByteArray(@NotNull ServiceTemplate template) {
        File directory = new File(this.storageDirectory, template.getTemplatePath());
        return directory.exists() ? FileUtils.convert(new Path[]{directory.toPath()}) : null;
    }

    @Override
    @Nullable
    public InputStream zipTemplate(@NotNull ServiceTemplate template) throws IOException {
        if (!this.has(template)) {
            return null;
        }

        Path directory = new File(this.storageDirectory, template.getTemplatePath()).toPath();
        Path tempFile = Paths.get(System.getProperty("cloudnet.tempDir", "temp"), UUID.randomUUID().toString());

        Path file = FileUtils.zipToFile(directory, tempFile);
        if (file == null) {
            return null;
        }

        return Files.newInputStream(file, StandardOpenOption.DELETE_ON_CLOSE, LinkOption.NOFOLLOW_LINKS);
    }

    @Override
    public boolean delete(@NotNull ServiceTemplate template) {
        Preconditions.checkNotNull(template);

        FileUtils.delete(new File(this.storageDirectory, template.getTemplatePath()));
        return true;
    }

    @Override
    public boolean create(@NotNull ServiceTemplate template) {
        File directory = new File(this.storageDirectory, template.getTemplatePath());
        if (directory.exists()) {
            return false;
        }
        return directory.mkdirs();
    }

    @Override
    public boolean has(@NotNull ServiceTemplate template) {
        Preconditions.checkNotNull(template);

        return new File(this.storageDirectory, template.getTemplatePath()).exists();
    }

    @Nullable
    @Override
    public OutputStream appendOutputStream(@NotNull ServiceTemplate template, @NotNull String path) throws IOException {
        Path file = this.storageDirectory.toPath().resolve(template.getTemplatePath()).resolve(path);
        if (!Files.exists(file)) {
            Files.createDirectories(file.getParent());
            Files.createFile(file);
        }

        return Files.newOutputStream(file, StandardOpenOption.APPEND);
    }

    @Nullable
    @Override
    public OutputStream newOutputStream(@NotNull ServiceTemplate template, @NotNull String path) throws IOException {
        Path file = this.storageDirectory.toPath().resolve(template.getTemplatePath()).resolve(path);
        if (Files.exists(file)) {
            Files.delete(file);
        } else {
            Files.createDirectories(file.getParent());
        }
        return Files.newOutputStream(file, StandardOpenOption.CREATE);
    }

    @Override
    public boolean createFile(@NotNull ServiceTemplate template, @NotNull String path) throws IOException {
        Path file = this.storageDirectory.toPath().resolve(template.getTemplatePath()).resolve(path);
        if (Files.exists(file)) {
            return false;
        }
        Files.createDirectories(file.getParent());
        Files.createFile(file);
        return true;
    }

    @Override
    public boolean createDirectory(@NotNull ServiceTemplate template, @NotNull String path) throws IOException {
        Path dir = this.storageDirectory.toPath().resolve(template.getTemplatePath()).resolve(path);
        if (Files.exists(dir)) {
            return false;
        }
        Files.createDirectories(dir);
        return true;
    }

    @Override
    public boolean hasFile(@NotNull ServiceTemplate template, @NotNull String path) {
        Path file = this.storageDirectory.toPath().resolve(template.getTemplatePath()).resolve(path);
        return Files.exists(file);
    }

    @Override
    public boolean deleteFile(@NotNull ServiceTemplate template, @NotNull String path) throws IOException {
        Path file = this.storageDirectory.toPath().resolve(template.getTemplatePath()).resolve(path);
        if (!Files.exists(file)) {
            return false;
        }
        if (Files.isDirectory(file)) {
            Files.walkFileTree(file, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        Files.delete(file);
        return true;
    }

    @Override
    public @Nullable InputStream newInputStream(@NotNull ServiceTemplate template, @NotNull String path) throws IOException {
        Path file = this.storageDirectory.toPath().resolve(template.getTemplatePath()).resolve(path);
        return Files.exists(file) ? Files.newInputStream(file) : null;
    }

    @Override
    public @Nullable FileInfo getFileInfo(@NotNull ServiceTemplate template, @NotNull String path) throws IOException {
        Path file = this.storageDirectory.toPath().resolve(template.getTemplatePath()).resolve(path);
        return Files.exists(file) ? FileInfo.of(file) : null;
    }

    @Override
    public FileInfo[] listFiles(@NotNull ServiceTemplate template, @NotNull String dir, boolean deep) throws IOException {
        List<FileInfo> files = new ArrayList<>();
        Path directory = this.storageDirectory.toPath().resolve(template.getTemplatePath()).resolve(dir);
        if (!Files.exists(directory)) {
            return null;
        }

        if (deep) {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    files.add(FileInfo.of(file, directory.relativize(file), attrs));
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    files.add(FileInfo.of(dir, directory.relativize(dir), attrs));
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
                for (Path path : stream) {
                    files.add(FileInfo.of(path, directory.relativize(path)));
                }
            }
        }
        return files.toArray(new FileInfo[0]);
    }

    @Override
    public @NotNull Collection<ServiceTemplate> getTemplates() {
        Collection<ServiceTemplate> templates = new ArrayList<>();

        File[] files = this.storageDirectory.listFiles();

        if (files != null) {
            for (File entry : files) {
                if (entry.isDirectory()) {
                    File[] subPathEntries = entry.listFiles();

                    if (subPathEntries != null) {
                        for (File subEntry : subPathEntries) {
                            if (subEntry.isDirectory()) {
                                templates.add(ServiceTemplate.local(entry.getName(), subEntry.getName()));
                            }
                        }
                    }
                }
            }
        }

        return templates;
    }

    @Override
    public boolean shouldSyncInCluster() {
        return true;
    }

    @Override
    public void close() {
    }

    public File getStorageDirectory() {
        return this.storageDirectory;
    }

    @Override
    public String getName() {
        return ServiceTemplate.LOCAL_STORAGE;
    }
}